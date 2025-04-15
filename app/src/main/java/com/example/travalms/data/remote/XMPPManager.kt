package com.example.travalms.data.remote

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.SmackConfiguration
import org.jivesoftware.smack.StanzaListener
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
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager
import com.google.gson.Gson
import org.jivesoftware.smackx.pubsub.form.ConfigureForm as SmackConfigureForm
import org.jivesoftware.smackx.pubsub.form.FillableConfigureForm
import org.jivesoftware.smack.packet.ExtensionElement

/**
 * XMPP连接和认证的管理类
 * 负责处理与XMPP服务器的所有交互
 */
class XMPPManager {
    // 常量配置
    companion object {
        private const val TAG = "XMPPManager"
        private const val SERVER_DOMAIN = "120.46.26.49"
        private const val SERVER_HOST = "120.46.26.49"
        private const val SERVER_PORT = 5222
        private const val RESOURCE = "AndroidClient"
        private const val PUBSUB_SERVICE = "pubsub.$SERVER_DOMAIN"
    }

    // 当前连接
    private var currentConnection: XMPPTCPConnection? = null
    
    // PubSub管理器
    private var pubSubManager: PubSubManager? = null
    
    // 项目通知流
    private val _pubsubItemsFlow = MutableSharedFlow<PubSubNotification>(replay = 100)
    val pubsubItemsFlow: SharedFlow<PubSubNotification> = _pubsubItemsFlow
    
    // 获取配置好的XMPP连接
    private fun getConnectionConfig(): XMPPTCPConnectionConfiguration {
        return XMPPTCPConnectionConfiguration.builder()
            .setXmppDomain(SERVER_DOMAIN) // 服务器域名
            .setHost(SERVER_HOST) // 服务器地址
            .setPort(SERVER_PORT) // XMPP标准端口
            .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled) // 开发环境禁用安全模式
            .setResource(RESOURCE) // 设置资源标识
            .setConnectTimeout(300000) // 连接超时30秒
            .setSendPresence(true)
            .build()
    }

    /**
     * 登录到XMPP服务器
     * @param username 用户名
     * @param password 密码
     * @return 登录结果，包含成功/失败信息和连接对象（如果成功）
     */
    suspend fun login(username: String, password: String): Result<XMPPTCPConnection> = withContext(Dispatchers.IO) {
        try {
            // 创建连接配置
            val config = getConnectionConfig()
            
            // 创建XMPP连接
            val connection = XMPPTCPConnection(config)
            
            // 连接到服务器
            connection.connect()
            
            // 登录
            connection.login(username, password)
            
            // 记录成功日志
            Log.d(TAG, "XMPP登录成功: 用户名=$username")
            Log.d(TAG, "服务器连接信息: 服务器=${connection.host}, 端口=${connection.port}, 用户=${connection.user}")
            
            // 保存当前连接
            currentConnection = connection
            
            // 初始化PubSub管理器
            initPubSubManager()
            
            // 返回成功结果
            Result.success(connection)
        } catch (e: Exception) {
            // 记录错误日志
            Log.e(TAG, "XMPP登录失败", e)
            
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
            // 详细记录开始注册流程
            Log.d(TAG, "开始注册用户: $username")
            
            // 创建连接配置，确保最适合注册流程的设置
            val config = XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain(SERVER_DOMAIN)
                .setHost(SERVER_HOST)
                .setPort(SERVER_PORT)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setResource(RESOURCE)
                .setConnectTimeout(300000) // 5分钟超时
                .setSendPresence(false) // 注册时不发送状态
                .build()
            
            // 手动启用调试
            SmackConfiguration.DEBUG = true
            
            // 创建连接
            connection = XMPPTCPConnection(config)
            
            // 详细记录连接过程
            Log.d(TAG, "正在连接到XMPP服务器...")
            connection.connect()
            Log.d(TAG, "已成功连接到XMPP服务器")
            
            // 先进行匿名连接
            connection.login("qinchong", "qc@BIT")  // 使用空用户名和密码进行匿名连接
            Log.d(TAG, "匿名登录成功")
            // 获取账户管理器并执行注册
            val accountManager = AccountManager.getInstance(connection)
            
            // 允许在不安全连接上执行敏感操作
            accountManager.sensitiveOperationOverInsecureConnection(true)
            
            // 准备注册属性
            val attributes = HashMap<String, String>()
            if (nickname != null) attributes["name"] = nickname
            if (email != null) attributes["email"] = email
            
            // 尝试创建账户
            try {
                Log.d(TAG, "开始创建账户...")
                if (attributes.isEmpty()) {
                    accountManager.createAccount(Localpart.from(username), password)
                } else {
                    accountManager.createAccount(Localpart.from(username), password, attributes)
                }
                Log.d(TAG, "XMPP注册成功: 用户名=$username")
                return@withContext Result.success(Unit)
            } catch (e: Exception) {
                Log.e(TAG, "创建账户失败: ${e.message}", e)
                throw e
            }
        } catch (e: Exception) {
            Log.e(TAG, "XMPP注册过程中出错: ${e.message}", e)
            return@withContext Result.failure(e)
        } finally {
            // 确保关闭连接
            try {
                connection?.disconnect()
                Log.d(TAG, "XMPP连接已断开")
            } catch (e: Exception) {
                Log.e(TAG, "断开连接时出错: ${e.message}", e)
            }
        }
    }

    /**
     * 初始化PubSub管理器
     */
    private fun initPubSubManager() {
        currentConnection?.let { connection ->
            // 创建PubSub服务JID
            val pubsubService = JidCreate.domainBareFrom(PUBSUB_SERVICE)
            
            // 创建PubSub管理器
            pubSubManager = PubSubManager.getInstance(connection, pubsubService)
            
            Log.d(TAG, "PubSub管理器初始化成功: service=$pubsubService")
        } ?: run {
            Log.e(TAG, "初始化PubSub管理器失败: 未登录或连接无效")
        }
    }

    /**
     * 创建发布-订阅节点
     * @param nodeId 节点ID (应该是唯一的)
     * @param name 节点名称
     * @param description 节点描述
     * @return 创建结果
     */
    suspend fun createNode(nodeId: String, name: String, description: String): Result<LeafNode> = withContext(Dispatchers.IO) {
        try {
            val pubsub = pubSubManager ?: throw Exception("PubSub管理器未初始化")
            
            // 检查节点是否已存在
            try {
                val existingNode = pubsub.getNode(nodeId)
                Log.d(TAG, "节点已存在: $nodeId, 直接返回")
                return@withContext Result.success(existingNode as LeafNode)
            } catch (e: Exception) {
                // 节点不存在，继续创建
                Log.d(TAG, "节点不存在，将创建新节点: $nodeId")
            }
            
            // 获取默认配置并创建可填充表单
            val defaultConfigureForm = pubsub.defaultConfiguration
            val configForm: FillableConfigureForm = defaultConfigureForm.fillableForm // Explicit type
            
            // 设置配置选项
            configForm.setAccessModel(AccessModel.open) // 允许任何人访问
            configForm.setDeliverPayloads(true) // 传递内容负载
            configForm.setPublishModel(PublishModel.open) // 允许任何人发布
            configForm.setTitle(name) // 设置标题
            configForm.setMaxItems(50) // 最多保存50个项目
            
            // 创建节点
            val node = pubsub.createNode(nodeId, configForm) as LeafNode
            
            Log.d(TAG, "节点创建成功: $nodeId, name=$name")
            Result.success(node)
        } catch (e: Exception) {
            Log.e(TAG, "创建节点失败: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * 订阅节点
     * @param nodeId 要订阅的节点ID
     * @return 订阅结果
     */
    suspend fun subscribeToNode(nodeId: String): Result<Subscription> = withContext(Dispatchers.IO) {
        try {
            val pubsub = pubSubManager ?: throw Exception("PubSub管理器未初始化")
            val connection = currentConnection ?: throw Exception("未连接到XMPP服务器")
            
            // 获取节点
            val node = pubsub.getNode(nodeId) as LeafNode
            
            // 订阅节点
            val subscription = node.subscribe(connection.user.asEntityBareJidString())
            
            // 添加项目事件监听器
            node.addItemEventListener(object : ItemEventListener<PayloadItem<SimplePayload>> {
                override fun handlePublishedItems(items: org.jivesoftware.smackx.pubsub.ItemPublishEvent<PayloadItem<SimplePayload>>) {
                    Log.d(TAG, "收到节点 $nodeId 的新项目，数量: ${items.items.size}")
                    
                    // 处理项目
                    items.items.forEach { item ->
                        val payload = item.payload
                        val itemId = item.id
                        try {
                            _pubsubItemsFlow.tryEmit(
                                PubSubNotification(
                                    nodeId = nodeId,
                                    itemId = itemId,
                                    payload = payload.toString()
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
        try {
            val pubsub = pubSubManager ?: throw Exception("PubSub管理器未初始化")
            val connection = currentConnection ?: throw Exception("未连接到XMPP服务器")
            
            // 获取节点
            val node = pubsub.getNode(nodeId) as LeafNode
            
            // 取消订阅
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
     * @param content 要发布的内容
     * @param contentType 内容类型 (如 "application/json")
     * @return 操作结果
     */
    suspend fun publishToNode(nodeId: String, content: String, contentType: String = "application/json"): Result<String> = withContext(Dispatchers.IO) {
        try {
            val pubsub = pubSubManager ?: throw Exception("PubSub管理器未初始化")
            
            // 获取节点
            val node = pubsub.getNode(nodeId) as LeafNode
            
            // 创建唯一项目ID
            val itemId = UUID.randomUUID().toString()
            
            // 创建简单负载
            val payload = SimplePayload(
                contentType,
                "item",
                content
            )
            
            // 创建有负载的项目
            val item = PayloadItem<SimplePayload>(itemId, payload)
            
            // 发布项目
            node.publish(item)
            
            Log.d(TAG, "成功发布项目到节点: $nodeId, itemId=$itemId")
            Result.success(itemId)
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
        try {
            val pubsub = pubSubManager ?: throw Exception("PubSub管理器未初始化")
            
            // 获取节点
            val node = pubsub.getNode(nodeId) as LeafNode
            
            // 获取项目
            val items: List<Item> = node.getItems(maxItems) // Explicit type for items
            
            // 转换为通知对象
            val notifications = items.mapNotNull { item ->
                if (item is PayloadItem<*>) {
                    val payload: ExtensionElement? = item.payload // Explicit type for payload
                    val payloadXml = payload?.toXML()?.toString() // Get XML string
                    if (payloadXml != null) {
                        PubSubNotification(
                            nodeId = nodeId,
                            itemId = item.id ?: UUID.randomUUID().toString(), // Ensure itemId is not null
                            payload = payloadXml
                        )
                    } else {
                        null // Skip if payload is null or has no XML representation
                    }
                } else {
                    null // Skip non-PayloadItems
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
        try {
            val pubsub = pubSubManager ?: throw Exception("PubSub管理器未初始化")
            
            // 获取所有订阅
            val subscriptions = pubsub.getSubscriptions()
            
            Log.d(TAG, "成功获取用户订阅: 数量=${subscriptions.size}")
            Result.success(subscriptions)
        } catch (e: Exception) {
            Log.e(TAG, "获取用户订阅失败: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    /**
     * 获取已发布到XMPP服务器的所有节点
     * @return 节点ID列表结果
     */
    suspend fun getAllNodes(): Result<List<String>> = withContext(Dispatchers.IO) {
        try {
            val pubsub = pubSubManager ?: throw Exception("PubSub管理器未初始化")
            
            // 获取服务发现管理器
            val discoveryManager = ServiceDiscoveryManager.getInstanceFor(currentConnection)
            
            // 获取pubsub服务上可用的节点
            val discoveryResult = discoveryManager.discoverItems(JidCreate.domainBareFrom(PUBSUB_SERVICE))
            
            // 提取节点ID
            val nodeIds = discoveryResult.items.map { it.entityID.toString() }
            
            Log.d(TAG, "成功获取所有节点: 数量=${nodeIds.size}")
            Result.success(nodeIds)
        } catch (e: Exception) {
            Log.e(TAG, "获取所有节点失败: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    /**
     * 批量订阅多个节点
     * @param nodeIds 要订阅的节点ID列表
     * @return 成功订阅的节点ID列表
     */
    suspend fun batchSubscribe(nodeIds: List<String>): Result<List<String>> = withContext(Dispatchers.IO) {
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
     * 断开XMPP连接
     */
    fun disconnect() {
        try {
            currentConnection?.disconnect()
            currentConnection = null
            pubSubManager = null
            Log.d(TAG, "XMPP连接已断开")
        } catch (e: Exception) {
            Log.e(TAG, "断开连接时出错: ${e.message}", e)
        }
    }

    private fun createNode(nodeName: String, parentNode: String? = null): Boolean {
        try {
            val pubSubManager = PubSubManager.getInstanceFor(currentConnection ?: return false)
            val node = if (parentNode != null) {
                pubSubManager.getNode(parentNode)
            } else {
                null
            }

            val defaultConfigureForm = pubSubManager.defaultConfiguration
            val config: FillableConfigureForm = defaultConfigureForm.fillableForm // Explicit type

            val newNode = pubSubManager.createNode(nodeName, config)
            return newNode != null
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    private fun publishItem(nodeName: String, item: Any): Boolean {
        try {
            val pubSubManager = PubSubManager.getInstanceFor(currentConnection ?: return false)
            val node = pubSubManager.getNode(nodeName) as? LeafNode ?: return false // Explicit cast to LeafNode
            
            val jsonString = when (item) {
                is String -> item
                else -> Gson().toJson(item)
            }
            
            val payload = SimplePayload("application/json", "payload", jsonString)
            val payloadItem = PayloadItem<SimplePayload>(UUID.randomUUID().toString(), payload)
            node.publish(payloadItem) // Now 'publish' should resolve on LeafNode
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
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