package com.example.travalms.data.remote

import android.content.Context
import android.util.Log
import com.example.travalms.data.model.ChatInvitation
import com.example.travalms.data.model.ChatMessage
import com.example.travalms.data.model.ContactItem
import com.example.travalms.data.model.Message
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.jivesoftware.smack.*
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.packet.ExtensionElement
import org.jivesoftware.smack.packet.IQ
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.packet.StandardExtensionElement
import org.jivesoftware.smack.packet.Stanza
import org.jivesoftware.smack.packet.StanzaError
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager
import org.jivesoftware.smackx.disco.packet.DiscoverItems
import org.jivesoftware.smackx.pubsub.*
import org.jivesoftware.smackx.pubsub.listener.ItemEventListener
import org.jivesoftware.smackx.pubsub.packet.PubSub
import org.jivesoftware.smackx.search.UserSearchManager
import org.jivesoftware.smackx.vcardtemp.VCardManager
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jivesoftware.smackx.ping.PingManager
import org.jivesoftware.smackx.ping.PingFailedListener
import org.jivesoftware.smackx.pubsub.form.FillableConfigureForm
import java.time.LocalDateTime
import java.util.*
import org.jxmpp.jid.*
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.jid.parts.Localpart
import org.jxmpp.stringprep.XmppStringprepException

// 自定义 IQ 类，用于发送 XEP-0055 User Search 请求
class UserSearchIQ : IQ("query", "jabber:iq:search") {
    override fun getIQChildElementBuilder(childElementBuilder: IQ.IQChildElementXmlStringBuilder): IQ.IQChildElementXmlStringBuilder {
        childElementBuilder.rightAngleBracket()
        return childElementBuilder
    }
}

// 搜索请求 IQ 类，用于发送包含搜索条件的请求
class UserSearchRequestIQ(searchXML: String) : IQ("query", "jabber:iq:search") {
    private val innerXml = searchXML

    override fun getIQChildElementBuilder(childElementBuilder: IQ.IQChildElementXmlStringBuilder): IQ.IQChildElementXmlStringBuilder {
        childElementBuilder.rightAngleBracket()
        childElementBuilder.append(innerXml)
        return childElementBuilder
    }
}

// Enum to represent connection states
enum class ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    AUTHENTICATING,
    AUTHENTICATED,
    ERROR,
    CONNECTION_CLOSED,
    RECONNECTING
}

/**
 * XMPP连接和认证的管理类
 * 负责处理与XMPP服务器的所有交互
 */
class XMPPManager private constructor() {
    // 常量配置
    companion object {
        private const val TAG = "XMPPManager"
        const val SERVER_DOMAIN = "localhost"
        private const val SERVER_HOST = "120.46.26.49"
        private const val SERVER_PORT = 5222
        private const val RESOURCE = "AndroidClient"
        private const val PUBSUB_SERVICE = SERVER_DOMAIN
        private const val PING_INTERVAL = 30
        private const val INACTIVITY_TIMEOUT = 600
        private const val CONNECTION_TIMEOUT = 60000

        // 单例实例
        @Volatile
        private var INSTANCE: XMPPManager? = null

        // 获取单例实例的方法
        fun getInstance(): XMPPManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: XMPPManager().also { INSTANCE = it }
            }
        }
    }

    // 当前连接
    var currentConnection: XMPPTCPConnection? = null
        private set

    // PubSub管理器
    private var pubSubManager: PubSubManager? = null

    // 添加群聊管理器
    private val _groupChatManager by lazy { XMPPGroupChatManager(this) }

    // 对外提供群聊管理器访问
    val groupChatManager: XMPPGroupChatManager
        get() = _groupChatManager

    // 项目通知流
    private val _pubsubItemsFlow = MutableSharedFlow<PubSubNotification>(replay = 100)
    val pubsubItemsFlow: SharedFlow<PubSubNotification> = _pubsubItemsFlow

    // 添加消息流
    private val _messageFlow = MutableSharedFlow<ChatMessage>(replay = 10)
    val messageFlow = _messageFlow.asSharedFlow()

    // 添加登录结果流
    private val _loginResultFlow = MutableSharedFlow<Result<Unit>>(replay = 1)
    val loginResultFlow: SharedFlow<Result<Unit>> = _loginResultFlow

    // 添加 Presence 更新流
    private val _presenceUpdateFlow =
        MutableSharedFlow<Pair<String, String>>(extraBufferCapacity = 64) // JID String to Status String
    val presenceUpdateFlow: SharedFlow<Pair<String, String>> = _presenceUpdateFlow.asSharedFlow()

    // 内部维护的在线状态映射
    private val presenceMap = Collections.synchronizedMap(mutableMapOf<String, String>())

    // 添加协程作用域
    private val scope = CoroutineScope(Dispatchers.IO)

    // Connection State Flow
    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    // 用于初始化操作的协程作用域
    private val initScope = CoroutineScope(Dispatchers.IO)

    // 订阅节点管理
    private val subscribedNodes = mutableSetOf<String>()
    private val nodeListeners =
        mutableMapOf<String, ItemEventListener<PayloadItem<SimplePayload>>>()
    private val messageCache = mutableMapOf<String, List<PubSubNotification>>()

    private val connectionListener = object : ConnectionListener {
        override fun connected(connection: XMPPConnection?) {
            Log.d(TAG, "XMPP 已连接")
            _connectionState.value = ConnectionState.CONNECTED
        }

        override fun authenticated(connection: XMPPConnection?, resumed: Boolean) {
            Log.d(TAG, "XMPP 已认证 (resumed: $resumed)")

            // 立即将回调提供的connection赋值给实例变量
            if (connection is XMPPTCPConnection) { // 添加类型检查和null检查
                // 使用限定的 this 访问外部类的属性
                this@XMPPManager.currentConnection = connection
                Log.d(TAG, "Assigned currentConnection in authenticated callback.")
            } else {
                Log.e(TAG, "Authenticated callback received null or non-TCP connection!")
                // 如果 connection 为 null 或类型不对，则无法继续初始化
                _connectionState.value = ConnectionState.ERROR // 设置错误状态
                disconnect() // 尝试清理
                return // 提前退出回调
            }

            // 在赋值后，更新状态并调用初始化方法
            _connectionState.value = ConnectionState.AUTHENTICATED

            Log.d(TAG, "Authenticated callback: Directly calling initializers...")

            // 初始化PubSub管理器，因为认证成功
            initPubSubManager()
            // 配置保活
            setupPingMechanism()
            // 设置消息监听器
            setupMessageListeners()
            // 设置Roster监听器
            setupRosterListener()
            // 请求所有联系人的在线状态
            requestAllContactsPresence()
            // 设置Presence直接监听器
            setupPresenceListener()
            // 发送可用状态
            sendInitialPresence()

            // 初始化群聊管理器
            _groupChatManager.initMucManager()
            // 设置群聊消息监听器
            _groupChatManager.setupGroupChatMessageListener()
        }

        override fun connectionClosed() {
            Log.d(TAG, "XMPP 连接已关闭")
            _connectionState.value = ConnectionState.CONNECTION_CLOSED
            currentConnection = null
            pubSubManager = null
        }

        override fun connectionClosedOnError(e: Exception?) {
            Log.e(TAG, "XMPP 连接因错误关闭", e)
            _connectionState.value = ConnectionState.ERROR

            // 尝试重新连接
            if (e is java.net.SocketException) {
                Log.d(TAG, "检测到网络连接问题，系统将尝试自动重连")
                // 确保ReconnectionManager会处理重连
                // 不要在这里手动断开连接，让重连机制处理
            } else {
                currentConnection = null
                pubSubManager = null
            }
        }
    }

    // 获取配置好的XMPP连接
    private fun getConnectionConfig(): XMPPTCPConnectionConfiguration {
        // 通过系统属性禁用DNS SRV查询
        System.setProperty("smack.dnssrv.enabled", "false")

        // 设置SASL超时参数，默认是5000ms(5秒)
        SmackConfiguration.setDefaultReplyTimeout(3000000) // 增加到30秒

        // 生成唯一的资源标识符，避免冲突
        val uniqueResource = "${RESOURCE}_${UUID.randomUUID().toString().substring(0, 8)}"
        Log.d(TAG, "Using unique resource: $uniqueResource")

        return XMPPTCPConnectionConfiguration.builder()
            .setXmppDomain(SERVER_DOMAIN) // 使用SERVER_DOMAIN作为域名，而不是IP地址
            .setHost(SERVER_HOST) // 服务器地址
            .setPort(SERVER_PORT) // XMPP标准端口
            .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled) // 开发环境禁用安全模式
            .setResource(uniqueResource) // 使用唯一的资源标识
            .setConnectTimeout(3000000) // 连接超时30秒
            .setSendPresence(true)
            .setCompressionEnabled(false) // 禁用压缩
            .enableDefaultDebugger() // 开启调试器以便检查连接问题
            .setSendPresence(true) // 登录后立即发送在线状态
            .build()
    }

    /**
     * 登录到XMPP服务器
     * @param username 用户名
     * @param password 密码
     * @return 登录结果，包含成功/失败信息和连接对象（如果成功）
     */
    suspend fun login(username: String, password: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            if (currentConnection?.isAuthenticated == true) {
                // 检查是否是同一个用户
                val currentJid = currentConnection?.user?.asEntityBareJidString()
                val newJid = "$username@$SERVER_DOMAIN"

                if (currentJid == newJid) {
                    Log.d(TAG, "已登录同一账号，无需重复操作")
                    _connectionState.value =
                        ConnectionState.AUTHENTICATED // Ensure state is correct
                    return@withContext Result.success(Unit)
                } else {
                    Log.d(TAG, "检测到账号切换，先断开旧连接: $currentJid -> $newJid")
                    // 不同账号，必须先清理旧连接
                    disconnect()
                }
            } else if (currentConnection != null) {
                // 有连接但未认证，也需要断开
                Log.d(TAG, "有未认证的连接，先断开它")
                disconnect()
            }

            _connectionState.value = ConnectionState.CONNECTING
            try {
                // 设置系统属性
                System.setProperty("smack.dnssec.disabled", "true")
                System.setProperty("smack.debugEnabled", "true")
                System.setProperty(
                    "smack.dnsResolver",
                    "org.jivesoftware.smack.util.dns.minidns.MiniDnsResolver"
                )
                System.setProperty("smack.dnssrv.enabled", "false")
                System.setProperty(
                    "smack.debuggerClass",
                    "org.jivesoftware.smack.debugger.ConsoleDebugger"
                )

                // 创建连接配置
                val config = getConnectionConfig()

                // 创建XMPP连接
                val connection = XMPPTCPConnection(config)

                // 添加监听器
                connection.addConnectionListener(connectionListener)

                // 配置自动重连 - 使用更保守的设置
                ReconnectionManager.getInstanceFor(connection).apply {
                    enableAutomaticReconnection()
                    setReconnectionPolicy(ReconnectionManager.ReconnectionPolicy.FIXED_DELAY)
                    setFixedDelay(30) // 重试间隔增加到30秒，减少频繁重连
                }

                // 连接到服务器
                Log.d(TAG, "开始连接...")
                try {
                    connection.connect()
                } catch (e: Exception) {
                    Log.e(TAG, "连接到XMPP服务器失败", e)
                    return@withContext Result.failure(e)
                }
                // connectionListener会处理状态变为 CONNECTED

                // 登录
                _connectionState.value = ConnectionState.AUTHENTICATING
                Log.d(TAG, "开始登录: $username")
                try {
                    connection.login(username, password)
                } catch (e: Exception) {
                    Log.e(TAG, "XMPP认证失败", e)
                    try {
                        connection.disconnect()
                    } catch (e2: Exception) {
                        Log.e(TAG, "断开失败的连接时发生错误", e2)
                    }
                    return@withContext Result.failure(e)
                }
                // connectionListener会处理状态变为 AUTHENTICATED

                Log.d(TAG, "XMPP登录成功: 用户名=$username")
                Log.d(
                    TAG,
                    "服务器连接信息: 服务器=${connection.host}, 端口=${connection.port}, 用户=${connection.user}"
                )

                // 保存当前连接
                currentConnection = connection
                // initPubSubManager 会在 authenticated 回调中调用

                // 调用登录成功处理函数
                onLoginSuccess()

                // 返回成功结果
                Result.success(Unit)
            } catch (e: Exception) {
                // 记录错误日志
                Log.e(TAG, "XMPP登录失败", e)
                _connectionState.value = ConnectionState.ERROR
                disconnect() // Ensure cleanup on error

                // 发送登录失败消息
                scope.launch {
                    _loginResultFlow.emit(Result.failure(e))
                }

                // 返回失败结果
                Result.failure(e)
            }
        }

    /**
     * 在XMPP服务器上注册新用户
     * @param username 用户名 (通常是邮箱或手机号)
     * @param password 密码
     * @param nickname 昵称 (可选)
     * @param email 电子邮箱 (可选, 如果username不是邮箱)
     * @param companyName 公司名称
     * @param mobileNumber 手机号
     * @param province 所在地 - 省份
     * @param city 所在地 - 城市
     * @param businessLicensePath 营业执照文件路径 (可选)
     * @param idCardFrontPath 负责人身份证正面文件路径 (可选)
     * @param idCardBackPath 负责人身份证反面文件路径 (可选)
     * @return 注册结果，表示成功或失败
     */
    suspend fun register(
        username: String,
        password: String,
        nickname: String? = null,
        email: String? = null,
        companyName: String,
        mobileNumber: String,
        province: String,
        city: String,
        businessLicensePath: String? = null,
        idCardFrontPath: String? = null,
        idCardBackPath: String? = null
    ): Result<Unit> = withContext(Dispatchers.IO) {
        var connection: XMPPTCPConnection? = null
        try {
            Log.d(TAG, "开始注册用户: $username, 公司: $companyName, 昵称: $nickname")
            // 设置系统属性 (应该在Application或初始化时做一次即可)
            // System.setProperty("smack.dnssrv.enabled", "false")

            val config = XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain(SERVER_DOMAIN)
                .setHost(SERVER_HOST)
                .setPort(SERVER_PORT)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setResource(RESOURCE) // Use a generic resource for registration
                .setConnectTimeout(30000) // 30 seconds timeout
                .setSendPresence(false)
                .build()

            connection = XMPPTCPConnection(config)
            Log.d(TAG, "正在连接到XMPP服务器进行注册...")
            connection.connect()
            Log.d(TAG, "已成功连接到XMPP服务器进行注册")

            val accountManager = AccountManager.getInstance(connection)
            accountManager.sensitiveOperationOverInsecureConnection(true)

            // 构建用户属性Map
            val attributes = mutableMapOf<String, String>()
            nickname?.let { attributes["name"] = it } // 使用标准字段 "name" 存储昵称
            email?.let { attributes["email"] = it }
            attributes["companyName"] = companyName
            attributes["mobileNumber"] = mobileNumber
            attributes["province"] = province
            attributes["city"] = city
            businessLicensePath?.let { attributes["businessLicensePath"] = it }
            idCardFrontPath?.let { attributes["idCardFrontPath"] = it }
            idCardBackPath?.let { attributes["idCardBackPath"] = it }

            Log.d(TAG, "开始创建账户，属性: $attributes")
            accountManager.createAccount(Localpart.from(username), password, attributes)

            Log.d(TAG, "XMPP注册成功: 用户名=$username")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "XMPP注册过程中出错: ${e.message}", e)
            // 可以根据错误类型提供更具体的失败原因
            Result.failure(e)
        } finally {
            try {
                connection?.disconnect()
                Log.d(TAG, "注册用XMPP连接已断开")
            } catch (e: Exception) {
                Log.e(TAG, "断开注册用连接时出错: ${e.message}", e)
            }
        }
    }

    /**
     * 初始化PubSub管理器
     * 确保使用正确的服务JID并配置IQ过滤器
     */
    private fun initPubSubManager() {
        Log.d(TAG, "确保 PubSubManager 初始化...")
        val connection = currentConnection
        if (connection == null) {
            Log.e(TAG, "Initialization failed: Connection is null.")
            pubSubManager = null
            return
        }

        try {
            // 1. 使用明确的pubsub服务JID
            val pubsubServiceJid = JidCreate.domainBareFrom("pubsub.$SERVER_DOMAIN")
            Log.d(TAG, "创建PubSubManager，使用服务JID: $pubsubServiceJid")

            // 2. 获取PubSubManager实例
            val manager = PubSubManager.getInstanceFor(connection, pubsubServiceJid)
            Log.d(TAG, "PubSubManager实例已获取")

            // 先清空旧的缓存和订阅
            messageCache.clear()
            subscribedNodes.clear()
            nodeListeners.clear()

            this.pubSubManager = manager

            Log.d(TAG, "PubSubManager初始化完成")
        } catch (e: Exception) {
            Log.e(TAG, "初始化PubSubManager失败: ${e.message}", e)
            this.pubSubManager = null
        }
    }

    /**
     * 创建发布-订阅节点
     * 确保使用正确的服务JID并处理冲突和无效名称
     */
    suspend fun createNode(nodeId: String, name: String?, description: String?): Result<LeafNode> =
        withContext(Dispatchers.IO) { // 允许 name 和 description 为 null
            if (connectionState.value != ConnectionState.AUTHENTICATED) {
                Log.e(TAG, "创建节点失败 $nodeId: 用户未认证")
                return@withContext Result.failure(IllegalStateException("用户未认证"))
            }

            val connection = currentConnection ?: return@withContext Result.failure(
                IllegalStateException("连接无效")
            )

            try {
                // 1. 使用明确的PubSub服务JID
                val pubsubServiceJid = JidCreate.domainBareFrom("pubsub.$SERVER_DOMAIN")
                Log.d(TAG, "为节点 $nodeId 获取PubSubManager，使用服务JID: $pubsubServiceJid")

                // 2. 获取PubSubManager实例 (确保过滤器已在init中设置好)
                val pubsub = PubSubManager.getInstanceFor(connection, pubsubServiceJid)

                // 3. 检查节点是否已存在 (使用 try-catch)
                try {
                    val existingNode = pubsub.getNode(nodeId)
                    Log.d(TAG, "节点已存在: $nodeId, 直接返回")
                    return@withContext Result.success(existingNode as LeafNode)
                } catch (e: Exception) {
                    // 预期 getNode 会失败，因为节点不存在
                    if (e is XMPPException.XMPPErrorException && e.stanzaError.condition == StanzaError.Condition.item_not_found) {
                        Log.d(TAG, "getNode确认节点不存在，将创建新节点: $nodeId")
                    } else {
                        // 其他getNode错误也认为是节点不存在，尝试创建
                        Log.w(TAG, "getNode时出现意外错误 (${e.message})，仍尝试创建节点: $nodeId")
                    }
                }

                // 4. 尝试创建节点
                try {
                    Log.d(TAG, "尝试创建节点 (目标: $pubsubServiceJid): $nodeId")
                    // 使用完整配置创建
                    val defaultConfigureForm = pubsub.defaultConfiguration
                    val configForm: FillableConfigureForm = defaultConfigureForm.fillableForm

                    // *** 添加对 name 的检查和处理 ***
                    val validTitle = if (name.isNullOrBlank()) {
                        Log.w(TAG, "节点 '$nodeId' 的名称无效 (null或空)，将使用 nodeId 作为标题。")
                        nodeId // 使用 nodeId 作为备用标题
                    } else {
                        name
                    }
                    Log.d(TAG, "准备设置节点标题: '$validTitle'")
                    // *** 结束检查 ***

                    configForm.setAccessModel(AccessModel.open)
                    configForm.setDeliverPayloads(true)
                    configForm.setPersistentItems(true) // 改为true，以便节点持久存在
                    configForm.setPublishModel(PublishModel.open)

                    // 使用try-catch包装setTitle调用，以便捕获可能的异常
                    try {
                        // 确保标题不为null，并且是一个符合要求的字符串
                        val safeTitle = validTitle.toString().trim()
                        configForm.setTitle(safeTitle)
                        Log.d(TAG, "已成功设置节点标题: '$safeTitle'")
                    } catch (e: Exception) {
                        Log.e(TAG, "设置节点标题失败: ${e.message}，尝试使用更安全的标题", e)
                        // 尝试使用一个非常安全的备用标题 (只包含字母、数字和下划线)
                        val fallbackTitle = "Node_${nodeId.replace(Regex("[^a-zA-Z0-9_]"), "_")}"
                        try {
                            Log.d(TAG, "尝试使用备用标题: '$fallbackTitle'")
                            configForm.setTitle(fallbackTitle)
                        } catch (e2: Exception) {
                            Log.e(TAG, "设置备用标题也失败了，将跳过标题设置: ${e2.message}")
                            // 跳过标题设置，继续其他配置
                        }
                    }

                    configForm.setMaxItems(50)

                    val node = pubsub.createNode(nodeId, configForm)
                    Log.d(TAG, "节点创建成功: $nodeId, name=$validTitle") // 使用 validTitle 记录日志
                    Result.success(node as LeafNode)
                } catch (e: Exception) {
                    // 5. 处理创建时的冲突错误
                    if (e is XMPPException.XMPPErrorException &&
                        e.stanzaError.condition == StanzaError.Condition.conflict
                    ) {
                        Log.d(TAG, "节点 $nodeId 已存在(创建时冲突409)，尝试获取现有节点")
                        try {
                            val existingNode = pubsub.getNode(nodeId)
                            Log.d(TAG, "成功获取到现有节点: $nodeId")
                            return@withContext Result.success(existingNode as LeafNode)
                        } catch (e2: Exception) {
                            Log.e(TAG, "获取现有节点失败 (冲突后): ${e2.message}", e2)
                            // 如果获取也失败，返回原始冲突错误
                            return@withContext Result.failure(e)
                        }
                    } else {
                        // 其他创建错误
                        Log.e(TAG, "创建节点失败: ${e.message}", e)
                        Result.failure(e)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "创建节点过程中发生顶级异常: ${e.message}", e)
                Result.failure(e)
            }
        }

    /**
     * 订阅节点
     * @param nodeId 要订阅的节点ID
     * @return 订阅结果
     */
    suspend fun subscribeToNode(nodeId: String): Result<Boolean> = withContext(Dispatchers.IO) {
        if (connectionState.value != ConnectionState.AUTHENTICATED) {
            return@withContext Result.failure(IllegalStateException("用户未认证"))
        }

        try {
            val manager = pubSubManager
                ?: return@withContext Result.failure(IllegalStateException("PubSub管理器未初始化"))

            // 检查是否已经订阅
            if (subscribedNodes.contains(nodeId)) {
                Log.d(TAG, "节点 $nodeId 已经订阅，跳过重复订阅")
                return@withContext Result.success(true)
            }

            // 订阅节点
            val node = manager.getNode(nodeId)
            val subscription = node.subscribe(currentConnection?.user?.asBareJid())

            // 添加节点监听器
            val nodeListener = object : ItemEventListener<PayloadItem<SimplePayload>> {
                override fun handlePublishedItems(items: ItemPublishEvent<PayloadItem<SimplePayload>>) {
                    val itemsList = items.items.mapNotNull { item ->
                        val payload = item.payload
                        val itemId = item.id
                        val payloadXml = payload?.toXML()?.toString()
                        if (payloadXml != null) {
                            PubSubNotification(
                                nodeId = nodeId,
                                itemId = itemId,
                                payload = payloadXml
                            )
                        } else {
                            null
                        }
                    }
                    messageCache[nodeId] = itemsList
                }
            }

            // 保存订阅和监听器
            subscribedNodes.add(nodeId)
            nodeListeners[nodeId] = nodeListener
            node.addItemEventListener(nodeListener)

            Log.d(TAG, "成功订阅节点: $nodeId")
            Result.success(true)
        } catch (e: Exception) {
            Log.e(TAG, "订阅节点 $nodeId 失败", e)
            Result.failure(e)
        }
    }

    /**
     * 取消订阅一个节点
     */
    suspend fun unsubscribeFromNode(nodeId: String): Result<Boolean> = withContext(Dispatchers.IO) {
        if (connectionState.value != ConnectionState.AUTHENTICATED) {
            return@withContext Result.failure(IllegalStateException("用户未认证"))
        }

        val pubsub = pubSubManager
            ?: return@withContext Result.failure(IllegalStateException("PubSub管理器未初始化"))

        try {
            Log.d(TAG, "尝试获取用户订阅信息")
            // 先获取用户在该节点上的所有订阅
            val allSubscriptions = pubsub.getSubscriptions()

            // 找到对应节点的订阅
            val nodeSubscriptions = allSubscriptions.filter { it.node == nodeId }

            if (nodeSubscriptions.isNotEmpty()) {
                // 获取节点对象
                val node = pubsub.getNode(nodeId)
                var allSuccess = true

                // 逐一取消每个订阅
                for (subscription in nodeSubscriptions) {
                    try {
                        Log.d(
                            TAG,
                            "正在取消订阅: 节点=${subscription.node}, JID=${subscription.jid}, 订阅ID=${subscription.id}"
                        )
                        if (subscription.id != null) {
                            // 如果有订阅ID，使用带订阅ID的方法
                            node.unsubscribe(subscription.jid.toString(), subscription.id)
                        } else {
                            // 没有订阅ID，使用不带订阅ID的方法
                            node.unsubscribe(subscription.jid.toString())
                        }
                        Log.d(TAG, "成功取消单个订阅")
                    } catch (e: Exception) {
                        Log.e(TAG, "取消单个订阅失败", e)
                        allSuccess = false
                    }
                }

                if (allSuccess) {
                    Log.d(TAG, "成功取消所有订阅: $nodeId")
                    return@withContext Result.success(true)
                } else {
                    Log.w(TAG, "部分订阅取消失败: $nodeId")
                    return@withContext Result.success(false)  // 返回部分成功
                }
            } else {
                Log.w(TAG, "未找到节点的订阅信息: $nodeId")
                // 如果没有找到订阅，也视为成功（因为最终状态是未订阅）
                return@withContext Result.success(true)
            }
        } catch (e: Exception) {
            Log.e(TAG, "取消订阅节点失败: $nodeId", e)
            return@withContext Result.failure(e)
        }
    }

    /**
     * 发布项目到节点
     * @param nodeId 目标节点ID
     * @param content 要发布的内容 (JSON格式，但内容不会直接发送到XMPP)
     * @param contentType 内容类型 (如 "application/json")
     * @return 操作结果，包含生成的itemId
     */
    suspend fun publishToNode(
        nodeId: String,
        content: String,
        contentType: String = "application/json"
    ): Result<String> = withContext(Dispatchers.IO) {
        if (connectionState.value != ConnectionState.AUTHENTICATED) {
            return@withContext Result.failure(IllegalStateException("用户未认证"))
        }
        val pubsub = pubSubManager
            ?: return@withContext Result.failure(IllegalStateException("PubSub管理器未初始化"))

        try {
            Log.d(TAG, "尝试获取节点: $nodeId")
            val node = pubsub.getNode(nodeId) as LeafNode
            val itemId = UUID.randomUUID().toString()

            try {
                // 对 JSON 内容进行 XML 转义，避免特殊字符导致的解析问题
                val escapedContent = content.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;")

                // 创建包含完整内容的 XML 负载
                val xmlContent = """
                    <taillist xmlns='urn:xmpp:taillist:0'>
                        <id>${itemId}</id>
                        <timestamp>${System.currentTimeMillis()}</timestamp>
                        <content type='application/json'>${escapedContent}</content>
                    </taillist>
                """.trimIndent()

                Log.d(TAG, "准备发布完整内容到节点: $nodeId")

                // 创建 SimplePayload，使用我们的自定义 XML
                val payload = SimplePayload(
                    "taillist",
                    "urn:xmpp:taillist:0",
                    xmlContent
                )

                val item = PayloadItem<SimplePayload>(itemId, payload)
                node.publish(item)

                Log.d(TAG, "成功发布完整内容到节点: $nodeId, itemId=$itemId")
                Result.success(itemId)
            } catch (e: Exception) {
                Log.e(TAG, "发布完整内容失败: ${e.message}", e)

                // 如果发布完整内容失败，尝试发布简化版本
                try {
                    Log.d(TAG, "尝试发布简化版本...")
                    // 从 JSON 中提取基本信息
                    val jsonObject = org.json.JSONObject(content)
                    val title = jsonObject.optString("title", "未知标题")
                    val price = jsonObject.optDouble("price", 0.0)

                    // 创建简化的 XML 负载，只包含基本信息
                    val simpleXmlContent = """
                        <taillist xmlns='urn:xmpp:taillist:0'>
                            <id>${itemId}</id>
                            <timestamp>${System.currentTimeMillis()}</timestamp>
                            <title>${title.replace("&", "&amp;").replace("<", "&lt;")}</title>
                            <price>${price}</price>
                        </taillist>
                    """.trimIndent()

                    val simplePayload = SimplePayload(
                        "taillist",
                        "urn:xmpp:taillist:0",
                        simpleXmlContent
                    )

                    val simpleItem = PayloadItem<SimplePayload>(itemId, simplePayload)
                    node.publish(simpleItem)

                    Log.d(TAG, "成功发布简化版本到节点: $nodeId, itemId=$itemId")
                    Result.success(itemId)
                } catch (e2: Exception) {
                    Log.e(TAG, "发布简化版本也失败了: ${e2.message}", e2)
                    Result.failure(e2)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "发布过程中发生顶级异常: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * 获取节点的历史项目
     * @param nodeId 节点ID
     * @return 项目列表结果
     */
    suspend fun getNodeItems(nodeId: String): Result<List<PubSubNotification>> =
        withContext(Dispatchers.IO) {
            if (connectionState.value != ConnectionState.AUTHENTICATED) {
                return@withContext Result.failure(IllegalStateException("用户未认证"))
            }

            try {
                val manager = pubSubManager ?: return@withContext Result.failure(
                    IllegalStateException("PubSub管理器未初始化")
                )

                // 检查缓存中是否有数据
                val cachedItems = messageCache[nodeId]
                if (cachedItems != null) {
                    Log.d(TAG, "从缓存中获取节点 $nodeId 的项目，数量: ${cachedItems.size}")
                    return@withContext Result.success(cachedItems)
                }

                // 如果没有缓存，从服务器获取
                val node = manager.getNode(nodeId) as LeafNode

                // 明确指定getItems返回的泛型类型
                val items: List<Item> = node.getItems()

                // 转换并缓存项目
                val convertedItems = mutableListOf<PubSubNotification>()

                for (item in items) {
                    if (item is PayloadItem<*>) {
                        val payload = item.payload
                        val itemId = item.id ?: UUID.randomUUID().toString()
                        val payloadXml = payload?.toXML()?.toString()
                        if (payloadXml != null) {
                            convertedItems.add(
                                PubSubNotification(
                                    nodeId = nodeId,
                                    itemId = itemId,
                                    payload = payloadXml
                                )
                            )
                        }
                    }
                }

                // 更新缓存
                messageCache[nodeId] = convertedItems

                Log.d(TAG, "成功获取节点 $nodeId 的项目，数量: ${convertedItems.size}")
                Result.success(convertedItems)
            } catch (e: Exception) {
                Log.e(TAG, "获取节点 $nodeId 的项目失败", e)
                Result.failure(e)
            }
        }

    /**
     * 获取用户已订阅的所有节点
     * @return 订阅节点列表结果
     */
    suspend fun getUserSubscriptions(): Result<List<Subscription>> = withContext(Dispatchers.IO) {
        if (connectionState.value != ConnectionState.AUTHENTICATED) {
            return@withContext Result.failure(IllegalStateException("用户未认证"))
        }
        val pubsub = pubSubManager
            ?: return@withContext Result.failure(IllegalStateException("PubSub管理器未初始化"))

        try {
            val subscriptions = pubsub.getSubscriptions()
            Log.d(TAG, "成功获取用户订阅: 数量=${subscriptions.size}")
            Result.success(subscriptions)
        } catch (e: Exception) {
            Log.e(TAG, "获取用户订阅失败: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * 获取已发布到XMPP服务器的所有节点 (需要认证)
     * 确保使用正确的PubSub服务JID
     */
    suspend fun getAllNodes(): Result<List<String>> = withContext(Dispatchers.IO) {
        if (connectionState.value != ConnectionState.AUTHENTICATED) {
            return@withContext Result.failure(IllegalStateException("用户未认证"))
        }
        val connection = currentConnection ?: return@withContext Result.failure(
            IllegalStateException("连接无效")
        )

        try {
            // 1. 使用明确的PubSub服务JID
            val pubsubServiceJid = JidCreate.domainBareFrom("pubsub.$SERVER_DOMAIN")
            Log.d(TAG, "获取所有节点，使用服务JID: $pubsubServiceJid")

            // 2. 获取PubSubManager实例 (确保过滤器已在init中设置好)
            val pubsub = PubSubManager.getInstanceFor(connection, pubsubServiceJid)

            // 3. 尝试使用ServiceDiscovery获取节点
            try {
                val discoveryManager = ServiceDiscoveryManager.getInstanceFor(connection)
                val discoveryResult = discoveryManager.discoverItems(pubsubServiceJid)
                val nodeIds = discoveryResult.items.mapNotNull { it.node }

                if (nodeIds.isNotEmpty()) {
                    Log.d(TAG, "通过服务发现获取节点成功: 数量=${nodeIds.size}")
                    return@withContext Result.success(nodeIds)
                } else {
                    Log.d(TAG, "服务发现未找到节点，尝试其他方法...")
                }
            } catch (e: Exception) {
                Log.w(TAG, "通过服务发现获取节点失败: ${e.message}，尝试其他方法...")
            }

            // 4. 尝试使用affiliations获取节点 (如果ServiceDiscovery失败)
            try {
                val affiliations = pubsub.affiliations
                if (affiliations.isNotEmpty()) {
                    val nodeIds = affiliations.map { it.node }
                    Log.d(TAG, "通过affiliations获取节点成功: 数量=${nodeIds.size}")
                    return@withContext Result.success(nodeIds)
                } else {
                    Log.d(TAG, "通过affiliations也未找到节点")
                }
            } catch (e: Exception) {
                Log.w(TAG, "通过affiliations获取节点失败: ${e.message}")
            }

            // 如果所有方法都失败，返回空列表
            Log.d(TAG, "所有方法都未找到节点，返回空列表")
            return@withContext Result.success(emptyList())
        } catch (e: Exception) {
            Log.e(TAG, "获取所有节点时出现顶级异常: ${e.message}", e)
            return@withContext Result.failure(e)
        }
    }

    /**
     * 批量订阅多个节点
     * @param nodeIds 要订阅的节点ID列表
     * @return 成功订阅的节点ID列表
     */
    suspend fun batchSubscribe(nodeIds: List<String>): Result<List<String>> =
        withContext(Dispatchers.IO) {
            if (connectionState.value != ConnectionState.AUTHENTICATED) {
                return@withContext Result.failure(IllegalStateException("用户未认证"))
            }

            try {
                // 首先获取所有可用节点
                val allNodesResult = getAllNodes()
                if (allNodesResult.isFailure) {
                    return@withContext Result.failure(IllegalStateException("无法获取可用节点列表"))
                }

                val availableNodes = allNodesResult.getOrThrow().toSet()

                val successfulSubscriptions = mutableListOf<String>()
                val invalidNodes = mutableListOf<String>()
                val failedNodes = mutableListOf<String>()

                // 筛选出有效节点和无效节点
                for (nodeId in nodeIds) {
                    if (!availableNodes.contains(nodeId)) {
                        Log.w(TAG, "节点 $nodeId 不存在，跳过订阅")
                        invalidNodes.add(nodeId)
                        continue
                    }

                    try {
                        val result = subscribeToNode(nodeId)
                        if (result.isSuccess) {
                            successfulSubscriptions.add(nodeId)
                            Log.d(TAG, "成功订阅节点: $nodeId")
                        } else {
                            failedNodes.add(nodeId)
                            Log.e(
                                TAG,
                                "订阅节点 $nodeId 失败: ${result.exceptionOrNull()?.message}"
                            )
                        }
                    } catch (e: Exception) {
                        failedNodes.add(nodeId)
                        Log.e(TAG, "订阅节点 $nodeId 失败: ${e.message}")
                    }
                }

                // 记录详细的结果日志
                if (invalidNodes.isNotEmpty()) {
                    Log.w(TAG, "以下节点不存在，已跳过: ${invalidNodes.joinToString()}")
                }

                if (failedNodes.isNotEmpty()) {
                    Log.e(TAG, "以下节点订阅失败: ${failedNodes.joinToString()}")
                }

                Log.d(
                    TAG,
                    "批量订阅完成: 成功=${successfulSubscriptions.size}, 无效=${invalidNodes.size}, 失败=${failedNodes.size}, 总请求=${nodeIds.size}"
                )

                // 如果有无效节点，在结果中包含提示信息
                if (invalidNodes.isNotEmpty() || failedNodes.isNotEmpty()) {
                    val message = buildString {
                        if (invalidNodes.isNotEmpty()) append("${invalidNodes.size}个节点不存在")
                        if (invalidNodes.isNotEmpty() && failedNodes.isNotEmpty()) append(", ")
                        if (failedNodes.isNotEmpty()) append("${failedNodes.size}个节点订阅失败")
                    }
                    Log.w(TAG, message)
                }

                Result.success(successfulSubscriptions)
            } catch (e: Exception) {
                Log.e(TAG, "批量订阅失败: ${e.message}", e)
                Result.failure(e)
            }
        }

    /**
     * 批量取消订阅节点
     */
    suspend fun batchUnsubscribe(nodeIds: List<String>): Result<List<String>> =
        withContext(Dispatchers.IO) {
            if (nodeIds.isEmpty()) {
                return@withContext Result.success(emptyList())
            }

            val successfulNodes = mutableListOf<String>()
            val failedNodes = mutableListOf<String>()

            for (nodeId in nodeIds) {
                try {
                    val result = unsubscribeFromNode(nodeId)
                    if (result.isSuccess && result.getOrDefault(false)) {
                        successfulNodes.add(nodeId)
                    } else {
                        failedNodes.add(nodeId)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "批量取消订阅时出错: $nodeId", e)
                    failedNodes.add(nodeId)
                }
            }

            Log.d(TAG, "批量取消订阅完成: 成功=${successfulNodes.size}/${nodeIds.size}")

            // 即使有些失败，也返回成功的节点列表
            return@withContext Result.success(successfulNodes)
        }

    /**
     * 断开与XMPP服务器的连接
     */
    fun disconnect() {
        try {
            val conn = currentConnection
            if (conn != null) {
                // 取消所有节点的订阅
                val nodesToUnsubscribe = subscribedNodes.toSet() // 创建副本避免并发修改
                subscribedNodes.clear() // 清空订阅集合

                // 不使用suspend函数，直接通过PubSubManager处理
                nodesToUnsubscribe.forEach { nodeId ->
                    try {
                        val manager = pubSubManager
                        if (manager != null) {
                            val node = manager.getNode(nodeId)
                            val user = conn.user.asBareJid()
                            if (user != null) {
                                // 尝试直接取消订阅
                                node.unsubscribe(user.toString())
                                Log.d(TAG, "已取消订阅节点: $nodeId")
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "取消订阅节点 $nodeId 失败", e)
                    }
                }

                // 移除所有节点监听器
                nodeListeners.forEach { (nodeId, listener) ->
                    try {
                        val node = pubSubManager?.getNode(nodeId)
                        node?.removeItemEventListener(listener)
                        Log.d(TAG, "已移除节点监听器: $nodeId")
                    } catch (e: Exception) {
                        Log.e(TAG, "移除节点监听器 $nodeId 失败", e)
                    }
                }
                nodeListeners.clear() // 清空监听器集合

                // 如果连接是活跃的，尝试正常断开
                if (conn.isConnected) {
                    try {
                        conn.disconnect()
                        Log.d(TAG, "已断开XMPP连接")
                    } catch (e: Exception) {
                        Log.e(TAG, "断开XMPP连接时发生错误", e)
                    }
                }

                // 移除所有连接监听器
                try {
                    conn.removeConnectionListener(connectionListener)
                } catch (e: Exception) {
                    Log.e(TAG, "移除连接监听器时发生错误", e)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "断开连接过程中发生错误", e)
        } finally {
            // 清理所有缓存和状态
            currentConnection = null
            pubSubManager = null
            messageCache.clear()
            _connectionState.value = ConnectionState.DISCONNECTED
            Log.d(TAG, "已清理所有缓存和状态")
        }
    }

    /**
     * 配置XMPP连接的保活机制
     */
    private fun setupPingMechanism() {
        val connection = currentConnection ?: return
        if (!connection.isAuthenticated) return

        try {
            // 获取PingManager实例
            val pingManager = PingManager.getInstanceFor(connection)

            // 启用自动ping
            pingManager.pingInterval = PING_INTERVAL
            pingManager.registerPingFailedListener {
                Log.w(TAG, "XMPP服务器Ping失败，可能连接已断开")
                // 检查当前连接状态，如果还是AUTHENTICATED但ping失败，更新状态
                if (_connectionState.value == ConnectionState.AUTHENTICATED) {
                    Log.d(TAG, "检测到连接可能中断，更新状态为RECONNECTING")
                    _connectionState.value = ConnectionState.RECONNECTING
                }
            }

            // 设置周期性发送Presence保持连接活跃
            initScope.launch {
                while (currentConnection?.isAuthenticated == true &&
                    _connectionState.value == ConnectionState.AUTHENTICATED
                ) {
                    try {
                        sendInitialPresence()
                        Log.d(TAG, "发送周期性Presence更新")
                    } catch (e: Exception) {
                        Log.e(TAG, "发送周期性Presence失败", e)
                    }
                    delay(PING_INTERVAL * 1000L) // 转换为毫秒
                }
            }

            Log.d(TAG, "已配置XMPP连接保活机制，ping间隔=${PING_INTERVAL}秒")
        } catch (e: Exception) {
            Log.e(TAG, "配置XMPP保活机制失败", e)
        }
    }

    /**
     * 发送初始在线状态
     */
    private fun sendInitialPresence() {
        val connection = currentConnection ?: return
        if (!connection.isAuthenticated) return

        try {
            // 发送可用状态
            val presence = Presence(Presence.Type.available)
            presence.mode = Presence.Mode.available
            presence.status = "在线"

            connection.sendStanza(presence)
            Log.d(TAG, "已发送初始在线状态")
        } catch (e: Exception) {
            Log.e(TAG, "发送在线状态失败", e)
        }
    }

    /**
     * 获取当前登录用户的个人资料信息
     * @return 用户资料信息Map，如果未登录或获取失败则返回错误
     */
    suspend fun getUserProfile(): Result<Map<String, String>> = withContext(Dispatchers.IO) {
        if (connectionState.value != ConnectionState.AUTHENTICATED) {
            return@withContext Result.failure(IllegalStateException("用户未认证"))
        }

        val connection = currentConnection ?: return@withContext Result.failure(
            IllegalStateException("连接无效")
        )

        try {
            val accountManager = AccountManager.getInstance(connection)
            val username = connection.user.localpart.toString()

            // 获取用户账户属性名
            val attributeNames = accountManager.getAccountAttributes()

            // 创建结果Map，添加用户名
            val profileData = mutableMapOf<String, String>()
            profileData["username"] = username

            // 添加其他属性
            // 注意：属性名与注册时使用的一致
            attributeNames.forEach { name ->
                val value = accountManager.getAccountAttribute(name)
                if (value != null) { // Check for null value just in case
                    profileData[name] = value
                }
            }

            Log.d(TAG, "成功获取用户资料: $profileData")
            Result.success(profileData)
        } catch (e: Exception) {
            Log.e(TAG, "获取用户资料失败: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * 获取特定好友的个人资料信息 (使用VCard和UserSearch)
     * @param friendJid 好友的BareJid
     * @return 好友资料信息Map，如果未登录或获取失败则返回错误
     */
    suspend fun getFriendProfile(friendJid: BareJid): Result<Map<String, String>> =
        withContext(Dispatchers.IO) {
            if (connectionState.value != ConnectionState.AUTHENTICATED) {
                Log.e(TAG, "getFriendProfile: 用户未认证")
                return@withContext Result.failure(IllegalStateException("用户未认证"))
            }

            val connection = currentConnection ?: run {
                Log.e(TAG, "getFriendProfile: 连接无效")
                return@withContext Result.failure(IllegalStateException("连接无效"))
            }

            try {
                Log.d(TAG, "尝试获取好友资料: $friendJid")
                val profileMap = mutableMapOf<String, String>()


                // 2. 如果缺少基本信息，尝试使用Roster获取
                if (!profileMap.containsKey("nickName") && !profileMap.containsKey("fullName")) {
                    try {
                        val roster = Roster.getInstanceFor(connection)
                        if (!roster.isLoaded) {
                            roster.reloadAndWait()
                        }

                        val rosterEntry = roster.getEntry(friendJid)
                        rosterEntry?.name?.let {
                            if (it.isNotBlank()) {
                                profileMap["rosterName"] = it
                                // 如果没有其他名称信息，使用花名册名称作为显示名称
                                if (!profileMap.containsKey("nickName") && !profileMap.containsKey("fullName")) {
                                    profileMap["displayName"] = it
                                }
                            }
                        }

                        // 添加在线状态
                        val presence = roster.getPresence(friendJid)
                        profileMap["presence"] = presence.type.toString()
                        presence.status?.let { if (it.isNotBlank()) profileMap["status"] = it }

                        Log.d(TAG, "补充了来自Roster的好友信息")
                    } catch (e: Exception) {
                        Log.d(TAG, "获取 $friendJid 的Roster信息失败: ${e.javaClass.simpleName}")
                    }
                }

                // 3. 添加JID相关信息
                profileMap["jid"] = friendJid.toString()
                profileMap["username"] =
                    friendJid.localpartOrNull?.toString() ?: friendJid.toString()

                // 如果没有设置显示名称，使用优先级顺序：nickname > fullName > rosterName > username
                if (!profileMap.containsKey("displayName")) {
                    val displayName = profileMap["nickName"]
                        ?: profileMap["fullName"]
                        ?: profileMap["rosterName"]
                        ?: profileMap["username"]
                    displayName?.let { profileMap["displayName"] = it }
                }

                if (profileMap.isEmpty()) {
                    return@withContext Result.failure(Exception("无法获取好友资料信息"))
                }

                Log.d(TAG, "成功获取好友 $friendJid 的资料信息")
                Result.success(profileMap)

            } catch (e: SmackException.NotConnectedException) {
                Log.e(TAG, "获取好友资料失败: 连接已断开", e)
                Result.failure(e)
            } catch (e: SmackException.NoResponseException) {
                Log.e(TAG, "获取好友资料失败: 服务器无响应", e)
                Result.failure(e)
            } catch (e: XMPPException.XMPPErrorException) {
                Log.e(TAG, "获取好友资料时发生XMPP错误: ${e.stanzaError}", e)
                Result.failure(e)
            } catch (e: Exception) {
                Log.e(TAG, "获取好友资料时发生未知错误", e)
                Result.failure(e)
            }
        }

    /**
     * 发送好友请求 (添加好友到花名册)
     * @param friendJidString 要添加的好友的完整JID (例如 "user@domain")
     * @return 操作结果 Result<Unit>
     */
    suspend fun sendFriendRequest(friendJidString: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            if (connectionState.value != ConnectionState.AUTHENTICATED) {
                Log.e(TAG, "sendFriendRequest: 用户未认证")
                return@withContext Result.failure(IllegalStateException("用户未认证"))
            }
            val connection = currentConnection ?: run {
                Log.e(TAG, "sendFriendRequest: 连接无效")
                return@withContext Result.failure(IllegalStateException("连接无效"))
            }

            try {
                Log.d(TAG, "尝试添加好友: $friendJidString")
                val friendJid: BareJid = JidCreate.bareFrom(friendJidString)
                val roster = Roster.getInstanceFor(connection)

                // 确保Roster已加载，虽然通常是自动的
                if (!roster.isLoaded) {
                    roster.reloadAndWait()
                }

                // 检查是否已经是好友
                if (roster.contains(friendJid)) {
                    Log.w(TAG, "用户 $friendJidString 已经是好友")
                    // 可以根据需要返回特定的成功或失败信息
                    return@withContext Result.failure(IllegalArgumentException("用户已经是好友"))
                }

                // 创建条目并发送订阅请求。使用对方 JID 的 localpart 作为默认昵称。
                // 第三个参数是组名，这里设为 null 表示不分组。
                val nickname = getLocalpartSafely(friendJid) // 使用安全方法获取localpart
                roster.createEntry(friendJid, nickname, null)

                Log.d(TAG, "已发送好友请求给: $friendJidString")
                Result.success(Unit)

            } catch (e: XmppStringprepException) {
                Log.e(TAG, "无效的好友 JID 格式: $friendJidString", e)
                Result.failure(IllegalArgumentException("无效的好友 JID 格式", e))
            } catch (e: SmackException.NotLoggedInException) {
                Log.e(TAG, "发送好友请求失败: 未登录", e)
                Result.failure(e)
            } catch (e: SmackException.NoResponseException) {
                Log.e(TAG, "发送好友请求失败: 服务器无响应", e)
                Result.failure(e)
            } catch (e: SmackException.NotConnectedException) {
                Log.e(TAG, "发送好友请求失败: 连接已断开", e)
                Result.failure(e)
            } catch (e: XMPPException.XMPPErrorException) {
                Log.e(TAG, "发送好友请求时发生 XMPP 错误: ${e.stanzaError}", e)
                Result.failure(e)
            } catch (e: InterruptedException) {
                Log.e(TAG, "发送好友请求操作被中断", e)
                Thread.currentThread().interrupt() // Restore interruption status
                Result.failure(e)
            } catch (e: Exception) {
                Log.e(TAG, "发送好友请求时发生未知错误", e)
                Result.failure(e)
            }
        }

    /**
     * 获取服务器上的所有用户 (使用User Search XEP-0055优先，失败则回退到服务发现和花名册)
     * @return 用户列表，包含 JID 和 昵称 (如果可用) Result<List<Pair<BareJid, String?>>>
     */
    suspend fun getServerUsers(): Result<List<Pair<BareJid, String?>>> =
        withContext(Dispatchers.IO) {
            if (connectionState.value != ConnectionState.AUTHENTICATED) {
                return@withContext Result.failure(IllegalStateException("用户未认证"))
            }
            val connection = currentConnection ?: return@withContext Result.failure(
                IllegalStateException("连接无效")
            )
            val currentUserJid = connection.user.asBareJid()
            val domain = connection.xmppServiceDomain.toString()

            try {
                val usersMap = mutableMapOf<BareJid, String?>() // Use a map to avoid duplicates
                var source = "Unknown"
                var lastError: Exception? = null

                // 尝试使用 XEP-0055 User Search 获取用户
                try {
                    Log.d(TAG, "尝试通过 User Search (XEP-0055) 获取用户")

                    // 常见的搜索服务名称: "search.$domain" 或 "vjud.$domain"
                    val searchServices = listOf(
                        "search.$domain",
                        "vjud.$domain",
                        domain // 最后尝试主域
                    )

                    var foundUsers = false

                    for (serviceName in searchServices) {
                        if (foundUsers && usersMap.isNotEmpty()) {
                            Log.d(TAG, "已通过先前的服务找到 ${usersMap.size} 个用户，跳过剩余服务")
                            break
                        }

                        try {
                            val serviceJid = JidCreate.domainBareFrom(serviceName)
                            Log.d(TAG, "尝试搜索服务: $serviceJid")

                            // 步骤1: 获取搜索表单
                            val searchFormIQ = UserSearchIQ()
                            searchFormIQ.to = serviceJid
                            searchFormIQ.type = IQ.Type.get

                            var formResult: IQ? = null
                            try {
                                // 使用 StanzaCollector 等待响应
                                val formCollector =
                                    connection.createStanzaCollectorAndSend(searchFormIQ)
                                formResult =
                                    formCollector.nextResult<IQ>(connection.replyTimeout) // 使用 replyTimeout
                                formCollector.cancel() // 及时取消收集器

                                if (formResult != null) {
                                    Log.d(TAG, "收到搜索表单响应")
                                } else {
                                    Log.w(TAG, "获取搜索表单响应超时或为空")
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "获取搜索表单失败: ${e.message}")
                                continue
                            }

                            if (formResult == null || formResult.type == IQ.Type.error) {
                                Log.e(TAG, "搜索服务 $serviceJid 返回错误或无响应")
                                continue
                            }

                            // 步骤2: 解析表单，准备搜索请求
                            val dataExtension: ExtensionElement? =
                                formResult?.getExtension("x", "jabber:x:data")
                            if (dataExtension == null) {
                                Log.e(TAG, "搜索表单不包含 Data Form 扩展")
                                continue
                            }

                            // 创建搜索请求XML
                            val searchRequestXml = StringBuilder()
                            searchRequestXml.append("<x xmlns='jabber:x:data' type='submit'>")

                            // 添加表单类型
                            searchRequestXml.append("<field var='FORM_TYPE'><value>jabber:iq:search</value></field>")

                            // 添加搜索条件: 使用通配符查找所有用户
                            searchRequestXml.append("<field var='search'><value>*</value></field>")

                            // 添加其他可能的字段，启用所有搜索选项
                            val fieldNames = listOf("Username", "Name", "Email")
                            for (fieldName in fieldNames) {
                                searchRequestXml.append("<field var='$fieldName'><value>1</value></field>")
                            }

                            searchRequestXml.append("</x>")

                            // 步骤3: 发送搜索请求
                            val searchRequestIQ = UserSearchRequestIQ(searchRequestXml.toString())
                            searchRequestIQ.to = serviceJid
                            searchRequestIQ.type = IQ.Type.set

                            var searchResult: IQ? = null
                            try {
                                // 使用 StanzaCollector 等待响应
                                val searchCollector =
                                    connection.createStanzaCollectorAndSend(searchRequestIQ)
                                searchResult =
                                    searchCollector.nextResult<IQ>(connection.replyTimeout) // 使用 replyTimeout
                                searchCollector.cancel() // 及时取消收集器

                                if (searchResult != null) {
                                    Log.d(TAG, "收到搜索结果响应")
                                } else {
                                    Log.w(TAG, "获取搜索结果响应超时或为空")
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "执行搜索请求失败: ${e.message}")
                                continue
                            }

                            if (searchResult == null || searchResult.type == IQ.Type.error) {
                                Log.e(TAG, "搜索请求返回错误或无响应")
                                continue
                            }

                            // 步骤4: 解析搜索结果
                            val resultExtension: ExtensionElement? =
                                searchResult?.getExtension("x", "jabber:x:data")
                            if (resultExtension == null) {
                                Log.d(TAG, "搜索结果不包含Data Form扩展")
                                continue
                            }

                            // 从结果中提取用户信息
                            val resultXml = resultExtension.toXML().toString()
                            Log.d(TAG, "收到搜索结果XML，长度: ${resultXml.length}")

                            // 提取用户数据
                            val userCountBefore = usersMap.size

                            // 使用多种解析方法，确保能提取出用户
                            var parsed = false

                            // 方法1: 使用基于item结构的解析
                            try {
                                val jidValuePattern =
                                    "<field\\s+var=\"jid\"[^>]*>.*?<value>\\s*(.*?)\\s*</value>".toRegex(
                                        RegexOption.DOT_MATCHES_ALL
                                    )
                                val usernameValuePattern =
                                    "<field\\s+var=\"Username\"[^>]*>.*?<value>\\s*(.*?)\\s*</value>".toRegex(
                                        RegexOption.DOT_MATCHES_ALL
                                    )
                                val nameValuePattern =
                                    "<field\\s+var=\"Name\"[^>]*>.*?<value>\\s*(.*?)\\s*</value>".toRegex(
                                        RegexOption.DOT_MATCHES_ALL
                                    )

                                // 分析出整个项目列表结构
                                val itemPattern =
                                    "<item>(.*?)</item>".toRegex(RegexOption.DOT_MATCHES_ALL)
                                val items = itemPattern.findAll(resultXml).map { it.groupValues[1] }
                                    .toList()

                                Log.d(TAG, "找到 ${items.size} 个用户项")

                                if (items.isNotEmpty()) {
                                    for (item in items) {
                                        // 在每个item内单独查找 jid, username 和 name
                                        val itemJidMatch = jidValuePattern.find(item)
                                        val itemUsernameMatch = usernameValuePattern.find(item)
                                        val itemNameMatch = nameValuePattern.find(item)

                                        if (itemJidMatch != null) {
                                            try {
                                                val jidStr = itemJidMatch.groupValues[1].trim()
                                                if (jidStr.isNotEmpty()) {
                                                    val userJid = JidCreate.bareFrom(jidStr)

                                                    // 排除当前用户和服务组件
                                                    if (userJid != currentUserJid && !isServiceComponent(
                                                            getLocalpartSafely(userJid)
                                                        )
                                                    ) {
                                                        // 默认用户名为JID的本地部分
                                                        var userName = getLocalpartSafely(userJid)

                                                        // 首先尝试从Name字段获取显示名
                                                        if (itemNameMatch != null) {
                                                            val name =
                                                                itemNameMatch.groupValues[1].trim()
                                                            if (name.isNotEmpty()) {
                                                                userName = name
                                                            }
                                                        }
                                                        // 如果没有Name，尝试Username字段
                                                        else if (itemUsernameMatch != null) {
                                                            val username =
                                                                itemUsernameMatch.groupValues[1].trim()
                                                            if (username.isNotEmpty()) {
                                                                userName = username
                                                            }
                                                        }

                                                        usersMap[userJid] = userName
                                                        Log.d(
                                                            TAG,
                                                            "添加用户: $userJid -> $userName"
                                                        )
                                                        foundUsers = true
                                                        parsed = true
                                                    } else {
                                                        Log.d(
                                                            TAG,
                                                            "跳过用户 $jidStr (当前用户或服务组件)"
                                                        )
                                                    }
                                                }
                                            } catch (e: Exception) {
                                                Log.e(TAG, "处理JID失败: ${e.message}")
                                                // 继续处理其他JID
                                            }
                                        }
                                    }
                                } else {
                                    Log.d(TAG, "未找到基于item的用户数据，尝试其他解析方法")
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "尝试提取基于item的用户数据时出错: ${e.message}")
                                lastError = e
                                // 继续尝试其他解析方法
                            }

                            // 方法4: 极简匹配，尝试最后的解析努力
                            if (!parsed) {
                                try {
                                    Log.d(TAG, "尝试极简解析方法4")
                                    // 匹配任何 xxx@yyy 形式的字符串
                                    val ultraSimplePattern = "\\b([\\w.-]+@[\\w.-]+)\\b".toRegex()
                                    val matches = ultraSimplePattern.findAll(resultXml)

                                    for (match in matches) {
                                        val jidStr = match.groupValues[1].trim()
                                        try {
                                            if (jidStr.contains("@") && !jidStr.contains("<") && !jidStr.contains(
                                                    ">"
                                                )
                                            ) {
                                                val userJid = JidCreate.bareFrom(jidStr)
                                                if (userJid != currentUserJid && !isServiceComponent(
                                                        getLocalpartSafely(userJid)
                                                    )
                                                ) {
                                                    val userName = getLocalpartSafely(userJid)
                                                    usersMap[userJid] = userName
                                                    Log.d(
                                                        TAG,
                                                        "添加用户(极简方法4): $userJid -> $userName"
                                                    )
                                                    foundUsers = true
                                                }
                                            }
                                        } catch (e: Exception) {
                                            // 忽略单个错误
                                        }
                                    }
                                } catch (e: Exception) {
                                    Log.e(TAG, "极简解析方法4失败: ${e.message}")
                                    lastError = e
                                }
                            }

                            // 检查是否成功添加了新用户
                            val addedUsers = usersMap.size - userCountBefore
                            if (addedUsers > 0) {
                                Log.d(TAG, "从服务 $serviceName 添加了 $addedUsers 个新用户")
                                foundUsers = true
                                source = "User Search (XEP-0055)"
                            } else {
                                // 打印更详细的错误信息，帮助调试
                                Log.w(TAG, "服务 $serviceName 的搜索结果中未找到有效用户")
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "搜索服务 $serviceName 处理失败: ${e.message}")
                            lastError = e
                        }
                    }

                    // 改变这里的逻辑 - 只要有用户数据，就返回成功
                    if (usersMap.isNotEmpty()) {
                        Log.d(TAG, "User Search 成功: 已获取 ${usersMap.size} 个用户")
                    } else {
                        Log.w(TAG, "所有搜索服务都未返回有效用户")
                        if (lastError != null) {
                            throw lastError
                        } else {
                            throw Exception("未能找到任何用户")
                        }
                    }
                } catch (e: Exception) {
                    // 即使User Search出错也继续处理，如果已有用户则忽略错误
                    Log.e(TAG, "使用User Search时发生错误: ${e.message}", e)
                    lastError = e
                    if (usersMap.isEmpty()) {
                        // 继续尝试其他获取用户的方法
                        Log.d(TAG, "User Search失败，未找到任何用户，将尝试其他方法")
                    } else {
                        Log.d(TAG, "尽管出现错误，但已找到 ${usersMap.size} 个用户，继续处理")
                    }
                }

                // 获取用户VCard信息以获取更好的显示名称
                val finalUsers = if (usersMap.isNotEmpty()) {
                    try {
                        Log.d(TAG, "尝试获取用户VCard信息 (来源: $source)")
                        val vCardManager = VCardManager.getInstanceFor(connection)
                        val usersList = mutableListOf<Pair<BareJid, String?>>()

                        usersMap.forEach { (bareJid, name) ->
                            try {
                                usersList.add(bareJid to name)
                            } catch (e: Exception) {
                                // 忽略VCard获取错误，继续使用原始名称
                                if (e !is SmackException.NotConnectedException && e !is SmackException.NoResponseException) {
                                    Log.d(
                                        TAG,
                                        "获取 $bareJid 的VCard失败: ${e.javaClass.simpleName}"
                                    )
                                }
                                usersList.add(bareJid to name)
                            }
                        }
                        usersList
                    } catch (e: Exception) {
                        Log.e(TAG, "获取VCard过程中出错", e)
                        // VCard管理器整体失败时，仍返回原始用户列表
                        usersMap.map { it.key to it.value }
                    }
                } else {
                    emptyList()
                }

                Log.d(TAG, "最终获取到 ${finalUsers.size} 个用户 (来源: $source)")

                if (finalUsers.isEmpty()) {
                    // 确保结果列表有数据
                    if (usersMap.isNotEmpty()) {
                        Log.w(
                            TAG,
                            "最终用户列表为空，但原始用户映射有 ${usersMap.size} 个条目，直接使用原始映射"
                        )
                        return@withContext Result.success(usersMap.map { it.key to it.value }
                            .sortedBy { it.second })
                    }
                    return@withContext Result.failure(lastError ?: Exception("未找到任何有效用户"))
                } else {
                    // 按名称排序后返回
                    return@withContext Result.success(finalUsers.sortedBy { it.second })
                }
            } catch (e: Exception) {
                Log.e(TAG, "获取用户过程中发生错误", e)
                return@withContext Result.failure(e)
            }
        }

    /**
     * 获取好友JID集合
     */
    suspend fun getFriendsJids(): Result<Set<BareJid>> = withContext(Dispatchers.IO) {
        if (connectionState.value != ConnectionState.AUTHENTICATED) {
            return@withContext Result.failure(IllegalStateException("用户未认证"))
        }
        val connection = currentConnection ?: return@withContext Result.failure(
            IllegalStateException("连接无效")
        )

        try {
            val roster = Roster.getInstanceFor(connection)
            if (!roster.isLoaded) {
                roster.reloadAndWait()
            }
            val friendsJids = roster.entries.mapNotNull { it.jid }.toSet()
            Log.d(TAG, "获取到好友列表 JIDs: 数量=${friendsJids.size}")
            Result.success(friendsJids)
        } catch (e: Exception) {
            Log.e(TAG, "获取好友列表失败", e)
            Result.failure(e)
        }
    }

    /**
     * 获取好友列表，包含JID和昵称
     */
    suspend fun getFriends(): Result<List<Pair<BareJid?, String?>>> = withContext(Dispatchers.IO) {
        if (connectionState.value != ConnectionState.AUTHENTICATED) {
            return@withContext Result.failure(IllegalStateException("用户未认证"))
        }
        val connection = currentConnection ?: return@withContext Result.failure(
            IllegalStateException("连接无效")
        )

        try {
            val roster = Roster.getInstanceFor(connection)
            if (!roster.isLoaded) {
                roster.reloadAndWait()
            }

            val friends = roster.entries.map { entry ->
                // 优先使用昵称，如果没有则使用JID的本地部分
                val name = entry.name ?: entry.jid?.let { getLocalpartSafely(it) } ?: "未知好友"
                Pair(entry.jid, name)
            }

            Log.d(TAG, "获取到好友列表，数量=${friends.size}")
            Result.success(friends)
        } catch (e: Exception) {
            Log.e(TAG, "获取好友列表失败", e)
            Result.failure(e)
        }
    }

    /**
     * 获取好友的在线状态
     * @param jid 好友的BareJid
     * @return 好友的在线状态描述
     */
    suspend fun getFriendPresence(jid: BareJid): Result<String> = withContext(Dispatchers.IO) {
        if (connectionState.value != ConnectionState.AUTHENTICATED) {
            return@withContext Result.failure(IllegalStateException("用户未认证"))
        }
        val connection = currentConnection ?: return@withContext Result.failure(
            IllegalStateException("连接无效")
        )

        try {
            val roster = Roster.getInstanceFor(connection)
            if (!roster.isLoaded) {
                roster.reloadAndWait()
            }

            // 确保是好友才获取状态
            if (!roster.contains(jid)) {
                return@withContext Result.failure(IllegalArgumentException("该用户不是好友"))
            }

            // 获取好友的所有Presence信息
            val presences = roster.getPresences(jid)
            if (presences.isEmpty()) {
                return@withContext Result.success("离线")
            }

            // 遍历所有Presence找出最高可用性
            var bestPresence: Presence? = null
            for (presence in presences) {
                if (presence.type == Presence.Type.available) {
                    if (bestPresence == null || presence.mode.ordinal < bestPresence.mode.ordinal) {
                        bestPresence = presence
                    }
                }
            }

            // 根据最高优先级的Presence返回状态
            val status = when {
                bestPresence == null -> "离线"
                bestPresence.mode == Presence.Mode.chat -> "在线-空闲"
                bestPresence.mode == Presence.Mode.available -> "在线"
                bestPresence.mode == Presence.Mode.away -> "离开"
                bestPresence.mode == Presence.Mode.xa -> "长时间离开"
                bestPresence.mode == Presence.Mode.dnd -> "忙碌"
                else -> bestPresence.status ?: "在线"
            }

            return@withContext Result.success(status)
        } catch (e: Exception) {
            Log.e(TAG, "获取好友在线状态失败: ${e.message}", e)
            return@withContext Result.failure(e)
        }
    }

    /**
     * 批量获取好友的在线状态
     * @return 成功返回包含好友JID和状态的映射，失败返回异常
     */
    suspend fun getFriendsPresence(): Result<Map<BareJid, String>> = withContext(Dispatchers.IO) {
        try {
            checkConnection()
            Log.d(TAG, "getFriendsPresence: 返回内部维护的状态映射")

            val rosterManager = Roster.getInstanceFor(currentConnection)
            if (!rosterManager.isLoaded) {
                Log.d(TAG, "花名册未加载，正在重新加载...")
                rosterManager.reloadAndWait()
            }

            // 创建结果映射
            val resultMap = mutableMapOf<BareJid, String>()
            val entries = rosterManager.entries

            // 如果presenceMap为空，先进行一次状态请求
            if (this@XMPPManager.presenceMap.isEmpty()) {
                Log.d(TAG, "状态映射为空，请求一次所有联系人状态")
                requestAllContactsPresence()
                // 给一点时间让服务器响应
                delay(500)
            }

            Log.d(TAG, "从内部映射中获取状态，映射大小: ${this@XMPPManager.presenceMap.size}")

            // 遍历所有roster条目，从presenceMap获取状态
            for (entry in entries) {
                val jid = entry.jid.asBareJid()
                val jidStr = jid.toString()

                // 从内部状态映射获取状态
                val status = this@XMPPManager.presenceMap[jidStr] ?: "离线"
                resultMap[jid] = status
            }

            Log.d(
                TAG, "已返回内部状态映射: 共 ${resultMap.size} 个, " +
                    "在线: ${resultMap.values.count { it.contains("在线") }}, " +
                    "离线: ${resultMap.values.count { it == "离线" }}"
            )

            Result.success(resultMap)
        } catch (e: Exception) {
            Log.e(TAG, "获取好友在线状态失败", e)
            Result.failure(e)
        }
    }

    /**
     * 请求所有联系人的在线状态
     */
    fun requestAllContactsPresence() {
        try {
            if (!isConnected()) {
                Log.e(TAG, "无法请求在线状态: 连接无效")
                return
            }

            val roster = Roster.getInstanceFor(currentConnection)

            // 确保roster已加载
            if (!roster.isLoaded) {
                roster.reloadAndWait()
            }

            Log.d(TAG, "正在请求 ${roster.entries.size} 个联系人的在线状态...")

            // 对每个联系人发送presence probe和subscribe
            roster.entries.forEach { entry ->
                try {
                    // 1. 发送probe请求 - 用于立即获取在线状态
                    val probe = Presence(Presence.Type.available)
                    probe.to = entry.jid
                    currentConnection?.sendStanza(probe)

                    // 2. 发送subscribe请求 - 确保获得持续的状态更新
                    val subscribe = Presence(Presence.Type.subscribe)
                    subscribe.to = entry.jid
                    currentConnection?.sendStanza(subscribe)

                    Log.d(TAG, "已向 ${entry.jid} 发送在线状态请求")
                } catch (e: Exception) {
                    Log.e(TAG, "向 ${entry.jid} 发送状态请求失败", e)
                }
            }

            Log.d(TAG, "已完成所有联系人状态请求")
        } catch (e: Exception) {
            Log.e(TAG, "请求联系人状态失败", e)
        }
    }

    // At the top of the file, let's add a helper method to safely get the localpart
    private fun getLocalpartSafely(jid: BareJid): String {
        // Rely only on string manipulation, avoid direct .localpart access
        val jidStr = jid.toString()
        return if (jidStr.contains("@")) {
            jidStr.split("@").firstOrNull() ?: ""
        } else {
            jidStr // Return the full string if no "@" is present (might be domain only)
        }
    }

    private fun isServiceComponent(localpart: String): Boolean {
        return localpart.equals("pubsub", ignoreCase = true) ||
                localpart.equals("conference", ignoreCase = true) ||
                localpart.equals("proxy", ignoreCase = true) ||
                localpart.equals("search", ignoreCase = true) ||
                localpart.equals("vjud", ignoreCase = true)
    }

    // 添加在connectionManager初始化相关代码之后
    // 在初始化后注册消息监听器
    private fun setupMessageListener() {
        if (connectionState.value != ConnectionState.AUTHENTICATED || currentConnection == null) {
            Log.e(TAG, "setupMessageListener: 未登录或连接无效")
            return
        }

        try {
            // 获取当前用户JID用于比较
            val currentUserJid = currentConnection?.user?.asEntityBareJidString()
            Log.d(TAG, "当前用户JID: $currentUserJid")

            // 添加消息监听器
            currentConnection?.addStanzaListener(
                { stanza ->
                    if (stanza is org.jivesoftware.smack.packet.Message && stanza.type == org.jivesoftware.smack.packet.Message.Type.chat) {
                        val fromJid = stanza.from.asBareJid().toString()
                        val body = stanza.body

                        Log.d(TAG, "==================== 收到消息 ====================")
                        Log.d(TAG, "收到聊天消息: $body 从 $fromJid (当前用户: $currentUserJid)")

                        if (body != null) {
                            // 使用协程异步处理
                            scope.launch {
                                try {
                                    // 获取对方的实际名称
                                    val senderName = getSenderName(JidCreate.bareFrom(fromJid))
                                    Log.d(TAG, "获取到发送者名称: $senderName")

                                    // 使用XMPP消息的stanzaId作为唯一标识，如果没有则生成新的
                                    val messageId = stanza.stanzaId ?: UUID.randomUUID().toString()

                                    val newMessage = ChatMessage(
                                        id = messageId,
                                        senderId = fromJid, // 使用完整JID
                                        senderName = senderName, // 使用发送者的实际名称
                                        content = body,
                                        timestamp = LocalDateTime.now(),
                                        isRead = false,
                                        recipientId = currentUserJid // 接收者是当前用户
                                    )

                                    Log.d(
                                        TAG,
                                        "创建接收消息: ID=$messageId, senderId=$fromJid, recipientId=$currentUserJid, senderName=$senderName"
                                    )

                                    // 发布到Flow
                                    _messageFlow.emit(newMessage)
                                } catch (e: Exception) {
                                    Log.e(TAG, "处理收到的消息时出错", e)
                                }
                            }
                        }
                    }
                },
                org.jivesoftware.smack.filter.StanzaTypeFilter(org.jivesoftware.smack.packet.Message::class.java)
            )

            Log.d(TAG, "成功设置消息监听器")
        } catch (e: Exception) {
            Log.e(TAG, "设置消息监听器失败: ${e.message}", e)
        }
    }

    // 在XMPPManager类中添加以下函数

    /**
     * 发送即时消息
     * @param recipientJid 接收者的JID
     * @param messageContent 消息内容
     * @return 发送结果
     */
    suspend fun sendMessage(recipientJid: String, messageContent: String): Result<ChatMessage> =
        withContext(Dispatchers.IO) {
            if (connectionState.value != ConnectionState.AUTHENTICATED) {
                Log.e(TAG, "sendMessage: 用户未认证")
                return@withContext Result.failure(IllegalStateException("用户未认证"))
            }

            val connection = currentConnection ?: run {
                Log.e(TAG, "sendMessage: 连接无效")
                return@withContext Result.failure(IllegalStateException("连接无效"))
            }

            try {
                // 获取当前用户的完整JID
                val currentUserJid = connection.user.asEntityBareJidString()
                val currentUserName = currentConnection?.user?.localpart?.toString() ?: "我"

                // 确保recipientJid格式正确
                val fullRecipientJid = if (!recipientJid.contains('@')) {
                    "$recipientJid@$SERVER_DOMAIN"
                } else {
                    recipientJid
                }

                Log.d(TAG, "==================== 发送消息 ====================")
                Log.d(TAG, "当前用户JID: $currentUserJid")
                Log.d(TAG, "接收者JID: $fullRecipientJid")
                Log.d(TAG, "消息内容: $messageContent")

                // 创建消息
                val message = org.jivesoftware.smack.packet.Message()
                message.type = org.jivesoftware.smack.packet.Message.Type.chat
                message.to = JidCreate.entityBareFrom(fullRecipientJid)
                message.body = messageContent
                message.from = connection.user // 确保from字段正确设置

                // 生成唯一的消息ID
                val messageId = UUID.randomUUID().toString()
                message.stanzaId = messageId

                // 发送消息
                connection.sendStanza(message)

                // 创建本地消息对象 - 使用当前用户的实际名称
                val chatMessage = ChatMessage(
                    id = messageId, // 使用相同的ID
                    senderId = currentUserJid, // 确保使用完整的JID
                    senderName = currentUserName, // 使用当前用户的实际名称，而不是硬编码的"我"
                    content = messageContent,
                    timestamp = LocalDateTime.now(),
                    isRead = true, // 自己发的消息默认已读
                    recipientId = fullRecipientJid // 使用完整接收者JID
                )

                Log.d(
                    TAG,
                    "创建本地消息: ID=$messageId, senderId=$currentUserJid, recipientId=$fullRecipientJid, senderName=$currentUserName"
                )

                // 发布到Flow
                _messageFlow.emit(chatMessage)

                Log.d(TAG, "消息已发送，ID: $messageId")
                Result.success(chatMessage)
            } catch (e: Exception) {
                Log.e(TAG, "发送消息失败: ${e.message}", e)
                Result.failure(e)
            }
        }

    /**
     * 获取最近的聊天历史记录
     * @param otherJid 聊天对象的JID
     * @param limit 限制返回的消息数量
     * @return 聊天历史记录列表
     */
    suspend fun getChatHistory(otherJid: String, limit: Int = 20): Result<List<ChatMessage>> =
        withContext(Dispatchers.IO) {
            if (connectionState.value != ConnectionState.AUTHENTICATED) {
                Log.e(TAG, "getChatHistory: 用户未认证")
                return@withContext Result.failure(IllegalStateException("用户未认证"))
            }

            try {
                Log.d(TAG, "获取与 $otherJid 的聊天历史")

                // 这里应该是从服务器或本地数据库获取聊天记录
                // 目前XMPP服务器可能没有默认的消息归档功能，需要使用XEP-0136 (Message Archiving) 扩展
                // 简化起见，这里先返回空列表，然后通过 messageFlow 接收新的消息

                // 如果要实现完整功能，需要添加数据库存储逻辑

                Result.success(emptyList())
            } catch (e: Exception) {
                Log.e(TAG, "获取聊天历史失败: ${e.message}", e)
                Result.failure(e)
            }
        }

    // 辅助方法，获取发送者名称
    private suspend fun getSenderName(jid: BareJid): String {
        return try {
            val profile = getFriendProfile(jid)
            if (profile.isSuccess) {
                val profileData = profile.getOrDefault(emptyMap())
                profileData["displayName"] ?: profileData["rosterName"] ?: jid.toString()
            } else {
                jid.toString()
            }
        } catch (e: Exception) {
            Log.e(TAG, "获取发送者名称失败: ${e.message}", e)
            jid.toString()
        }
    }

    // 在登录完成后调用
    private fun onLoginSuccess() {
        // 设置消息监听器
        setupMessageListener()

        // 更新连接状态
        _connectionState.value = ConnectionState.AUTHENTICATED

        // 通知登录成功
        scope.launch {
            _loginResultFlow.emit(Result.success(Unit))
        }

        Log.i(TAG, "登录成功")
    }

    /**
     * 发送XMPP ping以保持连接活跃
     * 返回ping是否成功
     */
    suspend fun sendPing(): Result<Boolean> = withContext(Dispatchers.IO) {
        if (connectionState.value != ConnectionState.AUTHENTICATED || currentConnection == null) {
            Log.e(TAG, "sendPing: 未登录或连接无效")
            return@withContext Result.failure(IllegalStateException("未登录或连接无效"))
        }

        try {
            val pingManager = PingManager.getInstanceFor(currentConnection)
            val pingSuccess = pingManager.ping(currentConnection?.user?.asDomainBareJid())

            if (pingSuccess) {
                Log.d(TAG, "XMPP ping成功")
            } else {
                Log.w(TAG, "XMPP ping失败")
            }

            Result.success(pingSuccess)
        } catch (e: Exception) {
            Log.e(TAG, "发送ping时出错: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * 保存登录凭据到安全存储
     * 注意：这是一个简单的实现，生产环境中应使用更安全的存储方式
     */
    fun saveCredentials(context: Context, username: String, password: String) {
        try {
            val prefs = context.getSharedPreferences("xmpp_prefs", Context.MODE_PRIVATE)
            prefs.edit()
                .putString("username", username)
                .putString("password", password)
                .apply()

            Log.d(TAG, "已保存登录凭据")
        } catch (e: Exception) {
            Log.e(TAG, "保存登录凭据时出错: ${e.message}", e)
        }
    }

    /**
     * 清除保存的登录凭据
     */
    fun clearCredentials(context: Context) {
        try {
            val prefs = context.getSharedPreferences("xmpp_prefs", Context.MODE_PRIVATE)
            prefs.edit()
                .remove("username")
                .remove("password")
                .apply()

            Log.d(TAG, "已清除登录凭据")
        } catch (e: Exception) {
            Log.e(TAG, "清除登录凭据时出错: ${e.message}", e)
        }
    }

    /**
     * 获取最近联系的用户列表
     *
     * @return 最近联系人的BareJid列表
     */
    suspend fun getRecentContacts(): Result<List<BareJid>> = withContext(Dispatchers.IO) {
        if (connectionState.value != ConnectionState.AUTHENTICATED) {
            Log.e(TAG, "getRecentContacts: 未登录")
            return@withContext Result.failure(IllegalStateException("未登录"))
        }

        try {
            // 获取所有好友
            val friendsResult = getFriendsJids()

            if (friendsResult.isSuccess) {
                // 返回所有好友JID（按字母顺序）
                return@withContext Result.success(friendsResult.getOrDefault(emptySet()).toList())
            } else {
                Log.e(TAG, "获取好友JID失败", friendsResult.exceptionOrNull())
                return@withContext Result.failure(
                    friendsResult.exceptionOrNull() ?: Exception("获取好友JID失败")
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "获取最近联系人失败", e)
            return@withContext Result.failure(e)
        }
    }

    /**
     * 检查当前连接状态，如果未认证或连接无效则抛出异常
     * 此方法在需要确保连接有效的情况下使用
     */
    private fun checkConnection() {
        if (connectionState.value != ConnectionState.AUTHENTICATED) {
            throw IllegalStateException("用户未认证")
        }

        if (currentConnection == null) {
            throw IllegalStateException("连接无效")
        }
    }

    /**
     * 初始化Roster（联系人列表）并配置监听器
     */
    private fun setupRosterListener() {
        if (currentConnection == null) {
            Log.e(TAG, "无法设置Roster监听器：连接无效")
            return
        }

        try {
            val roster = Roster.getInstanceFor(currentConnection)

            // 配置Roster选项
            roster.subscriptionMode = Roster.SubscriptionMode.accept_all

            // 添加Presence监听器，监听联系人状态变化
            roster.addPresenceEventListener(object :
                org.jivesoftware.smack.roster.PresenceEventListener {
                override fun presenceAvailable(jid: FullJid?, presence: Presence?) {
                    val bareJid = jid?.asBareJid()
                    Log.d(TAG, "Presence可用事件: jid=$bareJid, presence=$presence")
                    if (presence != null) {
                        val status = when (presence.mode) {
                            Presence.Mode.away -> "离开"
                            Presence.Mode.xa -> "长时间离开"
                            Presence.Mode.dnd -> "忙碌"
                            Presence.Mode.chat -> "想聊天"
                            else -> "在线"
                        }
                        updatePresenceInViewModel(bareJid, status)
                    }
                }

                override fun presenceUnavailable(jid: FullJid?, presence: Presence?) {
                    val bareJid = jid?.asBareJid()
                    Log.d(TAG, "Presence不可用事件: jid=$bareJid, presence=$presence")
                    updatePresenceInViewModel(bareJid, "离线")
                }

                // 修正签名以匹配接口
                override fun presenceError(jid: Jid?, errorPresence: Presence?) {
                    Log.e(TAG, "Presence错误事件: jid=$jid, errorPresence=$errorPresence")
                }

                override fun presenceSubscribed(jid: BareJid?, subscribedPresence: Presence?) {
                    Log.d(TAG, "Presence订阅事件: jid=$jid")
                }

                override fun presenceUnsubscribed(jid: BareJid?, unsubscribedPresence: Presence?) {
                    Log.d(TAG, "Presence取消订阅事件: jid=$jid")
                }
            })

            // 设置Roster加载监听器 - 修正类型
            roster.addRosterLoadedListener(object :
                org.jivesoftware.smack.roster.RosterLoadedListener {
                override fun onRosterLoaded(roster: Roster?) {
                    Log.d(TAG, "Roster加载完成，共 ${roster?.entries?.size ?: 0} 个条目")
                }

                override fun onRosterLoadingFailed(exception: Exception?) {
                    Log.e(TAG, "Roster加载失败", exception)
                }
            })

            // 加载Roster
            if (!roster.isLoaded) {
                Log.d(TAG, "开始加载Roster...")
                roster.reloadAndWait()
            }

            Log.d(TAG, "Roster监听器设置完成")
        } catch (e: Exception) {
            Log.e(TAG, "设置Roster监听器失败", e)
        }
    }

    /**
     * 更新单个联系人的在线状态到ViewModel (改为通过Flow广播)
     * @param jid 联系人的BareJid
     * @param status 新的状态字符串 ("在线", "离线", "离开" 等)
     */
    private fun updatePresenceInViewModel(jid: BareJid?, status: String) {
        if (jid == null) return
        val jidString = jid.toString()
        // 使用IO作用域更新ViewModel的状态流
        scope.launch {
            try {
                Log.d(TAG, "广播 Presence 更新: $jidString -> $status")
                _presenceUpdateFlow.emit(jidString to status)
            } catch (e: Exception) {
                Log.e(TAG, "广播 Presence 更新失败 for $jidString: ${e.message}")
            }
        }
    }

    /**
     * 设置直接的Presence监听器，绕过Roster直接监听所有Presence包
     */
    private fun setupPresenceListener() {
        val connection = currentConnection ?: return

        // 添加Presence StanzaListener
        connection.addSyncStanzaListener({ stanza ->
            if (stanza is Presence) {
                try {
                    val from = stanza.from?.asBareJid()?.toString() ?: return@addSyncStanzaListener
                    val currentUserJid =
                        connection.user?.asBareJid()?.toString() ?: return@addSyncStanzaListener

                    // 忽略自己发送的Presence
                    if (from == currentUserJid) return@addSyncStanzaListener

                    // 确定状态
                    val status = when {
                        stanza.type == Presence.Type.unavailable -> "离线"
                        stanza.mode == Presence.Mode.away -> "离开"
                        stanza.mode == Presence.Mode.xa -> "长时间离开"
                        stanza.mode == Presence.Mode.dnd -> "忙碌"
                        stanza.mode == Presence.Mode.chat -> "想聊天"
                        else -> { // 默认在线状态
                            if (!stanza.status.isNullOrBlank()) {
                                "在线 - ${stanza.status}"
                            } else {
                                "在线"
                            }
                        }
                    }

                    // 更新内部映射
                    val oldStatus = presenceMap[from]
                    if (oldStatus != status) {
                        presenceMap[from] = status

                        // 广播更新
                        scope.launch {
                            _presenceUpdateFlow.emit(from to status)
                            Log.d(TAG, "Presence更新: $from -> $status (来自直接监听)")
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "处理Presence包出错", e)
                }
            }
        }, { stanza -> stanza is Presence })

        Log.d(TAG, "已设置Presence直接监听器")

        // 登录后主动请求一次所有联系人的状态
        scope.launch {
            delay(2000) // 给系统一些时间处理初始化
            requestAllContactsPresence()
        }
    }

    /**
     * 添加消息监听器，处理各类消息
     */
    private fun setupMessageListeners() {
        val connection = currentConnection ?: return
        Log.d(TAG, "Setting up message listeners...")

        // 常规消息监听器 - 使用已有的消息处理机制
        connection.addAsyncStanzaListener({ stanza ->
            if (stanza is org.jivesoftware.smack.packet.Message) {
                // 继续使用已有的消息处理流程
                val body = stanza.body
                if (body != null) {
                    Log.d(TAG, "Received regular message: ${stanza.from} -> ${stanza.to}: $body")
                }
            }
        }, null)

        // 群聊消息已经在XMPPGroupChatManager中处理
        
        // 添加一个专门用于直接消息邀请的监听器
        connection.addAsyncStanzaListener({ stanza ->
            if (stanza is org.jivesoftware.smack.packet.Message && 
                stanza.type == org.jivesoftware.smack.packet.Message.Type.chat) {
                
                // 检查是否包含群聊邀请扩展
                val extensions = stanza.extensions
                val invitationExt = extensions.find { 
                    it.elementName == "group-invitation" && it.namespace == "travalms:invitation" 
                }
                
                if (invitationExt != null) {
                    Log.d(TAG, "Received direct message stanza with invitation extension")
                    try {
                        // 尝试解析XML内容来提取属性
                        val xmlString = invitationExt.toXML().toString()
                        Log.d(TAG, "Invitation Extension XML: $xmlString")
                        
                        // 简单的XML解析来提取属性
                        val roomJid = xmlString.substringAfter("roomJid='", "").substringBefore("'", "")
                        val inviter = xmlString.substringAfter("inviter='", "").substringBefore("'", "")
                        val reason = xmlString.substringAfter("reason='", "").substringBefore("'", "")
                        
                        if (roomJid.isNotEmpty() && inviter.isNotEmpty()) {
                            Log.d(TAG, "Received direct message invitation: room=$roomJid, inviter=$inviter, reason=$reason")
                            
                            // 尝试获取房间名称
                            val roomName = roomJid.substringBefore('@')
                            
                            // 创建邀请对象
                            val chatInvitation = com.example.travalms.data.model.ChatInvitation(
                                roomJid = roomJid,
                                roomName = roomName,
                                senderJid = inviter,
                                reason = if (reason.isBlank()) "邀请你加入群聊" else reason
                            )
                            
                            // 添加到邀请列表
                            _groupChatManager.addInvitation(chatInvitation)
                            
                            Log.d(TAG, "Successfully processed direct message invitation: ${chatInvitation.shortDescription}")
                        } else {
                            Log.w(TAG, "Failed to parse invitation attributes from XML: $xmlString")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error processing direct message invitation extension", e)
                    }
                }
            }
        }, null)
        
        Log.d(TAG, "Message listeners setup complete.")
    }

    /**
     * 检查连接是否有效
     * @return 如果连接有效且已认证则返回true
     */
    private fun isConnected(): Boolean {
        return currentConnection != null && 
               currentConnection?.isConnected == true && 
               currentConnection?.isAuthenticated == true
    }
}

/**
 * PubSub通知数据类
 * 用于表示从XMPP PubSub系统接收到的通知
 */
data class PubSubNotification(
    val nodeId: String,   // 发布节点ID
    val itemId: String,   // 项目ID
    val payload: String   // 内容负载
)