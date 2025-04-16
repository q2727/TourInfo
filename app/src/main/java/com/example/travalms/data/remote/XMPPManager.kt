package com.example.travalms.data.remote

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.SmackConfiguration
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jivesoftware.smackx.pubsub.AccessModel
import org.jivesoftware.smackx.pubsub.Item
import org.jivesoftware.smackx.pubsub.LeafNode
import org.jivesoftware.smackx.pubsub.PayloadItem
import org.jivesoftware.smackx.pubsub.PubSubManager
import org.jivesoftware.smackx.pubsub.PublishModel
import org.jivesoftware.smackx.pubsub.SimplePayload
import org.jivesoftware.smackx.pubsub.Subscription
import org.jivesoftware.smackx.pubsub.listener.ItemEventListener
import org.jxmpp.jid.BareJid
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.jid.parts.Localpart
import java.util.UUID
import org.jivesoftware.smackx.pubsub.form.ConfigureForm
import org.jivesoftware.smackx.pubsub.form.FillableConfigureForm
import org.jivesoftware.smack.packet.ExtensionElement
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager
import org.jivesoftware.smack.util.dns.DNSResolver
import org.jivesoftware.smack.util.dns.minidns.MiniDnsResolver
import com.google.gson.Gson
import org.jivesoftware.smack.ReconnectionManager
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smackx.ping.PingManager
import org.jivesoftware.smackx.ping.PingFailedListener
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smack.packet.StanzaError

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
        private const val SERVER_DOMAIN = "localhost"
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
    var currentConnection: XMPPTCPConnection? = null // Made public for isAuthenticated check, consider better encapsulation
        private set

    // PubSub管理器
    private var pubSubManager: PubSubManager? = null

    // 项目通知流
    private val _pubsubItemsFlow = MutableSharedFlow<PubSubNotification>(replay = 100)
    val pubsubItemsFlow: SharedFlow<PubSubNotification> = _pubsubItemsFlow

    // Connection State Flow
    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    // 用于初始化操作的协程作用域
    private val initScope = CoroutineScope(Dispatchers.IO)

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
            // 发送可用状态
            sendInitialPresence()
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
    suspend fun login(username: String, password: String): Result<Unit> = withContext(Dispatchers.IO) {
        if (currentConnection?.isAuthenticated == true) {
            Log.d(TAG, "已登录，无需重复操作")
            _connectionState.value = ConnectionState.AUTHENTICATED // Ensure state is correct
            return@withContext Result.success(Unit)
        }

        // 断开旧连接（如果存在）
        disconnect()

        _connectionState.value = ConnectionState.CONNECTING
        try {
            // 设置系统属性
            System.setProperty("smack.dnssec.disabled", "true")
            System.setProperty("smack.debugEnabled", "true")
            System.setProperty("smack.dnsResolver", "org.jivesoftware.smack.util.dns.minidns.MiniDnsResolver")
            System.setProperty("smack.dnssrv.enabled", "false")
            System.setProperty("smack.debuggerClass", "org.jivesoftware.smack.debugger.ConsoleDebugger")

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
            Log.d(TAG, "开始登录...")
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
            Log.d(TAG, "服务器连接信息: 服务器=${connection.host}, 端口=${connection.port}, 用户=${connection.user}")

            // 保存当前连接
            currentConnection = connection
            // initPubSubManager 会在 authenticated 回调中调用

            // 返回成功结果
            Result.success(Unit)
        } catch (e: Exception) {
            // 记录错误日志
            Log.e(TAG, "XMPP登录失败", e)
            _connectionState.value = ConnectionState.ERROR
            disconnect() // Ensure cleanup on error
            // 返回失败结果
            Result.failure(e)
        }
    }

    /**
     * 在XMPP服务器上注册新用户
     * @param username 用户名
     * @param password 密码
     * @param nickname 昵称（可选）
     * @param email 电子邮箱（可选）
     * @return 注册结果，表示成功或失败
     */
    suspend fun register(username: String, password: String, nickname: String? = null, email: String? = null): Result<Unit> = withContext(Dispatchers.IO) {
        var connection: XMPPTCPConnection? = null
        try {
            Log.d(TAG, "开始注册用户: $username")
            // 设置系统属性 (应该在Application或初始化时做一次即可)
            // System.setProperty("smack.dnssrv.enabled", "false")

            val config = XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain(SERVER_DOMAIN)
                .setHost(SERVER_HOST)
                .setPort(SERVER_PORT)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setResource(RESOURCE)
                .setConnectTimeout(300000)
                .setSendPresence(false)
                .build()

            connection = XMPPTCPConnection(config)
            Log.d(TAG, "正在连接到XMPP服务器进行注册...")
            connection.connect()
            Log.d(TAG, "已成功连接到XMPP服务器进行注册")

            val accountManager = AccountManager.getInstance(connection)
            accountManager.sensitiveOperationOverInsecureConnection(true)

            val attributes = HashMap<String, String>()
            if (nickname != null) attributes["name"] = nickname
            if (email != null) attributes["email"] = email

            Log.d(TAG, "开始创建账户...")
            if (attributes.isEmpty()) {
                accountManager.createAccount(Localpart.from(username), password)
            } else {
                accountManager.createAccount(Localpart.from(username), password, attributes)
            }
            Log.d(TAG, "XMPP注册成功: 用户名=$username")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "XMPP注册过程中出错: ${e.message}", e)
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
    suspend fun createNode(nodeId: String, name: String?, description: String?): Result<LeafNode> = withContext(Dispatchers.IO) { // 允许 name 和 description 为 null
        if (connectionState.value != ConnectionState.AUTHENTICATED) {
            Log.e(TAG, "创建节点失败 $nodeId: 用户未认证")
            return@withContext Result.failure(IllegalStateException("用户未认证"))
        }

        val connection = currentConnection ?: return@withContext Result.failure(IllegalStateException("连接无效"))

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
                    e.stanzaError.condition == StanzaError.Condition.conflict) {
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
    suspend fun subscribeToNode(nodeId: String): Result<Subscription> = withContext(Dispatchers.IO) {
        if (connectionState.value != ConnectionState.AUTHENTICATED) {
            return@withContext Result.failure(IllegalStateException("用户未认证"))
        }
        val pubsub = pubSubManager ?: return@withContext Result.failure(IllegalStateException("PubSub管理器未初始化"))
        val connection = currentConnection ?: return@withContext Result.failure(IllegalStateException("连接无效"))

        try {
            val node = pubsub.getNode(nodeId) as LeafNode
            val subscription = node.subscribe(connection.user.asEntityBareJidString())

            node.addItemEventListener(object : ItemEventListener<PayloadItem<SimplePayload>> {
                override fun handlePublishedItems(items: org.jivesoftware.smackx.pubsub.ItemPublishEvent<PayloadItem<SimplePayload>>) {
                    Log.d(TAG, "收到节点 $nodeId 的新项目，数量: ${items.items.size}")
                    items.items.forEach { item ->
                        val payload = item.payload
                        val itemId = item.id
                        try {
                            _pubsubItemsFlow.tryEmit(
                                PubSubNotification(
                                    nodeId = nodeId,
                                    itemId = itemId,
                                    payload = payload.toXML()?.toString() ?: ""
                                )
                            )
                        } catch (e: Exception) {
                            Log.e(TAG, "处理项目消息时出错: ${e.message}", e)
                        }
                    }
                }
            })

            Log.d(TAG, "成功订阅节点: $nodeId, jid=${connection.user.asBareJid()}")
            Result.success(subscription)
        } catch (e: Exception) {
            Log.e(TAG, "订阅节点失败: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * 取消订阅节点
     * @param nodeId 要取消订阅的节点ID
     * @return 操作结果
     */
    suspend fun unsubscribeFromNode(nodeId: String): Result<Unit> = withContext(Dispatchers.IO) {
        if (connectionState.value != ConnectionState.AUTHENTICATED) {
            return@withContext Result.failure(IllegalStateException("用户未认证"))
        }
        val pubsub = pubSubManager ?: return@withContext Result.failure(IllegalStateException("PubSub管理器未初始化"))
        val connection = currentConnection ?: return@withContext Result.failure(IllegalStateException("连接无效"))

        try {
            val node = pubsub.getNode(nodeId) as LeafNode
            node.unsubscribe(connection.user.asEntityBareJidString())
            Log.d(TAG, "成功取消订阅节点: $nodeId")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "取消订阅节点失败: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * 发布项目到节点
     * @param nodeId 目标节点ID
     * @param content 要发布的内容 (JSON格式，但内容不会直接发送到XMPP)
     * @param contentType 内容类型 (如 "application/json")
     * @return 操作结果，包含生成的itemId
     */
    suspend fun publishToNode(nodeId: String, content: String, contentType: String = "application/json"): Result<String> = withContext(Dispatchers.IO) {
        if (connectionState.value != ConnectionState.AUTHENTICATED) {
            return@withContext Result.failure(IllegalStateException("用户未认证"))
        }
        val pubsub = pubSubManager ?: return@withContext Result.failure(IllegalStateException("PubSub管理器未初始化"))

        try {
            Log.d(TAG, "尝试获取节点: $nodeId")
            val node = pubsub.getNode(nodeId) as LeafNode
            val itemId = UUID.randomUUID().toString()
            
            Log.d(TAG, "准备发布到节点: $nodeId，包含简单负载，内容长度: ${content.length}字节")
            
            try {
                // 创建一个非常简单的XML负载
                // 只发送一个标识符和时间戳，不包含实际JSON内容
                val timestamp = System.currentTimeMillis()
                val simplePayload = SimplePayload(
                    "notification", 
                    "urn:xmpp:taillist:notification",
                    "<notification xmlns='urn:xmpp:taillist:notification'><id>${itemId}</id><timestamp>${timestamp}</timestamp></notification>"
                )
                
                val item = PayloadItem<SimplePayload>(itemId, simplePayload)
                node.publish(item)
                
                Log.d(TAG, "成功发布项目到节点: $nodeId, itemId=$itemId")
                
                // 返回生成的 itemId，应用可以将实际内容存储在本地数据库中
                Result.success(itemId)
            } catch (e: Exception) {
                Log.e(TAG, "发布带有简单负载的项目失败: ${e.message}", e)
                
                // 如果简单负载也失败，尝试最小化的负载
                try {
                    Log.d(TAG, "尝试使用最小化的负载...")
                    val minimalPayload = SimplePayload(
                        "data", 
                        "urn:xmpp:data",
                        "<data xmlns='urn:xmpp:data'>minimal</data>"
                    )
                    
                    val item = PayloadItem<SimplePayload>(itemId, minimalPayload)
                    node.publish(item)
                    
                    Log.d(TAG, "使用最小化负载成功发布: $nodeId, itemId=$itemId")
                    Result.success(itemId)
                } catch (e2: Exception) {
                    Log.e(TAG, "使用最小化负载也失败了: ${e2.message}", e2)
                    Result.failure(e2)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "发布项目到节点失败: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * 获取节点的历史项目
     * @param nodeId 节点ID
     * @param maxItems 最大项目数
     * @return 项目列表结果
     */
    suspend fun getNodeItems(nodeId: String, maxItems: Int = 20): Result<List<PubSubNotification>> = withContext(Dispatchers.IO) {
        if (connectionState.value != ConnectionState.AUTHENTICATED) {
            return@withContext Result.failure(IllegalStateException("用户未认证"))
        }
        val pubsub = pubSubManager ?: return@withContext Result.failure(IllegalStateException("PubSub管理器未初始化"))

        try {
            val node = pubsub.getNode(nodeId) as LeafNode
            val items: List<Item> = node.getItems(maxItems)
            val notifications = items.mapNotNull { item ->
                if (item is PayloadItem<*>) {
                    val payload: ExtensionElement? = item.payload
                    val payloadXml = payload?.toXML()?.toString()
                    if (payloadXml != null) {
                        PubSubNotification(
                            nodeId = nodeId,
                            itemId = item.id ?: UUID.randomUUID().toString(),
                            payload = payloadXml
                        )
                    } else {
                        null
                    }
                } else {
                    null
                }
            }
            Log.d(TAG, "成功获取节点项目: $nodeId, 数量=${notifications.size}")
            Result.success(notifications)
        } catch (e: Exception) {
            Log.e(TAG, "获取节点项目失败: ${e.message}", e)
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
        val pubsub = pubSubManager ?: return@withContext Result.failure(IllegalStateException("PubSub管理器未初始化"))

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
        val connection = currentConnection ?: return@withContext Result.failure(IllegalStateException("连接无效"))

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
    suspend fun batchSubscribe(nodeIds: List<String>): Result<List<String>> = withContext(Dispatchers.IO) {
        if (connectionState.value != ConnectionState.AUTHENTICATED) {
            return@withContext Result.failure(IllegalStateException("用户未认证"))
        }
        try {
            val successfulSubscriptions = mutableListOf<String>()

            for (nodeId in nodeIds) {
                try {
                    val result = subscribeToNode(nodeId)
                    if (result.isSuccess) {
                        successfulSubscriptions.add(nodeId)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "订阅节点 $nodeId 失败: ${e.message}")
                    // 继续处理其他节点
                }
            }

            Log.d(TAG, "批量订阅完成: 成功=${successfulSubscriptions.size}/${nodeIds.size}")
            Result.success(successfulSubscriptions)
        } catch (e: Exception) {
            Log.e(TAG, "批量订阅失败: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * 批量取消订阅多个节点
     * @param nodeIds 要取消订阅的节点ID列表
     * @return 成功取消订阅的节点ID列表
     */
    suspend fun batchUnsubscribe(nodeIds: List<String>): Result<List<String>> = withContext(Dispatchers.IO) {
        if (connectionState.value != ConnectionState.AUTHENTICATED) {
            return@withContext Result.failure(IllegalStateException("用户未认证"))
        }
        try {
            val successfulUnsubscriptions = mutableListOf<String>()

            for (nodeId in nodeIds) {
                try {
                    val result = unsubscribeFromNode(nodeId)
                    if (result.isSuccess) {
                        successfulUnsubscriptions.add(nodeId)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "取消订阅节点 $nodeId 失败: ${e.message}")
                    // 继续处理其他节点
                }
            }

            Log.d(TAG, "批量取消订阅完成: 成功=${successfulUnsubscriptions.size}/${nodeIds.size}")
            Result.success(successfulUnsubscriptions)
        } catch (e: Exception) {
            Log.e(TAG, "批量取消订阅失败: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * 断开与XMPP服务器的连接
     */
    fun disconnect() {
        try {
            val conn = currentConnection
            if (conn != null) {
                // 如果连接是活跃的，尝试正常断开
                if (conn.isConnected) {
                    try {
                        conn.disconnect()
                        Log.d(TAG, "已断开XMPP连接")
                    } catch (e: Exception) {
                        Log.e(TAG, "断开XMPP连接时发生错误", e)
                    }
                }

                // 移除所有监听器
                try {
                    conn.removeConnectionListener(connectionListener)
                } catch (e: Exception) {
                    Log.e(TAG, "移除连接监听器时发生错误", e)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "断开连接过程中发生错误", e)
        } finally {
            currentConnection = null
            pubSubManager = null
            _connectionState.value = ConnectionState.DISCONNECTED
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
            pingManager.registerPingFailedListener(object : PingFailedListener {
                override fun pingFailed() {
                    Log.w(TAG, "XMPP服务器Ping失败，可能连接已断开")
                    // 检查当前连接状态，如果还是AUTHENTICATED但ping失败，更新状态
                    if (_connectionState.value == ConnectionState.AUTHENTICATED) {
                        Log.d(TAG, "检测到连接可能中断，更新状态为RECONNECTING")
                        _connectionState.value = ConnectionState.RECONNECTING
                    }
                }
            })

            // 设置周期性发送Presence保持连接活跃
            initScope.launch {
                while (currentConnection?.isAuthenticated == true && 
                       _connectionState.value == ConnectionState.AUTHENTICATED) {
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
}

/**
 * PubSub通知数据类
 */
data class PubSubNotification(
    val nodeId: String,
    val itemId: String,
    val payload: String
) 