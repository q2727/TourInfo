package com.example.travalms.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travalms.data.api.NetworkModule.userApiService
import com.example.travalms.data.remote.XMPPManager
import com.example.travalms.data.remote.ConnectionState
import com.example.travalms.data.remote.PubSubNotification
import com.example.travalms.data.repository.TailOrderRepository
import com.example.travalms.data.repository.TailOrderRepositoryImpl
import com.example.travalms.ui.screens.TailOrder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.random.Random
import android.app.Application
import android.content.Context
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.CancellationException

/**
 * 尾单列表视图模型状态
 */
data class TailListState(
    val isLoading: Boolean = false,
    val tailOrders: List<TailOrder> = emptyList(),
    val error: String? = null,
    val favoriteOrderIds: List<String> = emptyList()
)

/**
 * 尾单列表视图模型，负责获取和管理用户订阅的节点中发布的尾单
 */
class TailListViewModel(
    private val repository: TailOrderRepository
) : ViewModel() {
    private val xmppManager = XMPPManager.getInstance()

    private val _state = MutableStateFlow(TailListState())
    val state: StateFlow<TailListState> = _state.asStateFlow()

    private val TAG = "TailListViewModel"

    // 尾单内容缓存，避免重复解析
    private val tailOrderCache = mutableMapOf<String, TailOrder>()
    
    // 默认的节点列表
    private val defaultNodes = listOf("tails", "tailOrders")
    
    // 添加Application引用
    private var application: Application? = null
    
    // Job to control the PubSub monitoring coroutine
    private var pubSubMonitoringJob: Job? = null

    // 设置Application的方法
    fun setApplication(app: Application) {
        application = app
        Log.d(TAG, "已设置Application上下文")
    }

    init {
        // Observe XMPP connection state to manage PubSub monitoring
        viewModelScope.launch {
            xmppManager.connectionState
                // Only react to actual state changes
                .collect { connState ->
                    when (connState) {
                        ConnectionState.AUTHENTICATED -> {
                            Log.d(TAG, "XMPP Authenticated. Ensuring PubSub monitoring is active and refreshing data.")
                            // Cancel previous monitoring job if it's active
                            pubSubMonitoringJob?.cancel() // Simple cancel
                            // Start new monitoring
                            pubSubMonitoringJob = monitorNewTailLists()
                            // Refresh tail orders as connection is now authenticated
                            fetchTailOrders()
                        }
                        ConnectionState.DISCONNECTED, ConnectionState.ERROR, ConnectionState.CONNECTION_CLOSED -> {
                            Log.d(TAG, "XMPP Disconnected or Error. Cancelling PubSub items monitoring.")
                            pubSubMonitoringJob?.cancel()
                            // Update UI to reflect disconnected state, avoid clearing orders immediately
                            // unless that's the desired behavior on any disconnection.
                            // Consider if loading should also be false here.
                            _state.update { it.copy(isLoading = false, error = "消息服务器连接已断开") }
                        }
                        else -> {
                            Log.d(TAG, "XMPP Connection State: $connState")
                        }
                    }
                }
        }
        // Initial fetch and monitor are now driven by the connection state collector.
    }
    
    /**
     * 确保XMPP连接并订阅默认节点
     */
    private fun ensureXmppConnectionAndSubscriptions() {
        viewModelScope.launch {
            // 检查XMPP连接状态
            if (xmppManager.connectionState.value != ConnectionState.AUTHENTICATED) {
                Log.d(TAG, "XMPP未连接，等待连接后再继续...")
                _state.update { it.copy(isLoading = true, error = "正在连接到消息服务器...") }
                
                // 尝试使用保存的凭据重新登录
                val app = application
                if (app != null) {
                    val prefs = app.getSharedPreferences("xmpp_prefs", Context.MODE_PRIVATE)
                    val username = prefs.getString("username", "") ?: ""
                    val password = prefs.getString("password", "") ?: ""
                    
                    if (username.isNotEmpty() && password.isNotEmpty()) {
                        // 使用保存的凭据重新登录
                        val result = xmppManager.login(username, password)
                        if (result.isSuccess) {
                            Log.d(TAG, "XMPP重新连接成功")
                        } else {
                            Log.e(TAG, "XMPP重新连接失败: ${result.exceptionOrNull()?.message}")
                        }
                    } else {
                        Log.e(TAG, "无法重新连接：未找到保存的凭据")
                    }
                } else {
                    Log.e(TAG, "无法重新连接：Application context 未设置")
                }
                
                // 等待10秒，看是否能自动连接上
                for (i in 1..10) {
                    if (xmppManager.connectionState.value == ConnectionState.AUTHENTICATED) {
                        Log.d(TAG, "XMPP已连接，继续操作")
                        break
                    }
                    delay(1000) // 等待1秒
                    Log.d(TAG, "等待XMPP连接... ${i}秒")
                }
            }
            
            // 不管是否已连接，直接尝试订阅默认节点
            subscribeToDefaultNodes()
            
            // 加载尾单数据
            fetchTailOrders()
        }
    }
    
    /**
     * 订阅默认节点
     */
    private suspend fun subscribeToDefaultNodes() {
        Log.d(TAG, "尝试订阅默认节点...")
        
        // 确保至少订阅了默认节点
        for (nodeId in defaultNodes) {
            try {
                val result = xmppManager.subscribeToNode(nodeId)
                if (result.isSuccess) {
                    Log.d(TAG, "成功订阅节点: $nodeId")
                } else {
                    Log.e(TAG, "订阅节点失败: $nodeId, 错误: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "订阅节点时出错: $nodeId", e)
            }
            
            // 短暂延迟，避免服务器压力
            delay(500)
        }
    }

    /**
     * 加载所有已订阅节点的尾单列表
     */
    fun fetchTailOrders() {
        viewModelScope.launch {
            // Check connection state before proceeding
            if (xmppManager.connectionState.value != ConnectionState.AUTHENTICATED) {
                Log.w(TAG, "fetchTailOrders called but XMPP not authenticated. Current state: ${xmppManager.connectionState.value}")
                _state.update { it.copy(isLoading = false, error = "消息服务器未连接，请稍后再试") }
                return@launch
            }

            _state.update { it.copy(isLoading = true, error = null) }

            Log.d(TAG, "开始获取尾单列表数据...")

            try {
                val subscriptionsResult = xmppManager.getUserSubscriptions()
                if (subscriptionsResult.isSuccess) {
                    val subscriptions = subscriptionsResult.getOrThrow()

                    Log.d(TAG, "获取到 ${subscriptions.size} 个已订阅节点")

                    if (subscriptions.isEmpty()) {
                        // 如果没有订阅，显示默认数据
                        val fallbackOrders = repository.getTailOrders()
                        _state.update { it.copy(tailOrders = fallbackOrders, isLoading = false) }
                        Log.d(TAG, "没有订阅节点，显示默认数据 ${fallbackOrders.size} 条")
                        return@launch
                    }

                    // 临时存储所有尾单
                    val allTailOrders = mutableListOf<TailOrder>()

                    // 获取每个订阅节点的项目
                    for (subscription in subscriptions) {
                        val nodeId = subscription.node
                        Log.d(TAG, "正在从节点获取项目: $nodeId")

                        try {
                            val nodeItemsResult = xmppManager.getNodeItems(nodeId)
                            if (nodeItemsResult.isSuccess) {
                                val notifications = nodeItemsResult.getOrThrow()
                                Log.d(TAG, "节点 $nodeId 找到 ${notifications.size} 个通知项目")

                                for (notification in notifications) {
                                    // 强制重新解析，忽略缓存
                                    tailOrderCache.remove(notification.itemId)

                                    val tailOrder = parseTailListNotification(notification)
                                    if (tailOrder != null) {
                                        allTailOrders.add(tailOrder)
                                        Log.d(TAG, "添加尾单: ${tailOrder.title}, ID=${tailOrder.id}")
                                    }
                                }
                            } else {
                                Log.e(TAG, "获取节点 $nodeId 的项目失败: ${nodeItemsResult.exceptionOrNull()?.message}")
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "处理节点 $nodeId 时出错", e)
                        }
                    }

                    // 按新旧排序（假设ID编号越大越新）
                    allTailOrders.sortByDescending { it.id }

                    if (allTailOrders.isEmpty()) {
                        // 如果从XMPP获取不到数据，使用仓库中的备用数据
                        val fallbackOrders = repository.getTailOrders()
                        _state.update { it.copy(tailOrders = fallbackOrders, isLoading = false) }
                        Log.d(TAG, "XMPP未获取到数据，使用备用数据 ${fallbackOrders.size} 条")
                    } else {
                        _state.update { it.copy(tailOrders = allTailOrders, isLoading = false) }
                        Log.d(TAG, "成功从XMPP加载 ${allTailOrders.size} 条尾单数据")
                    }
                } else {
                    val error = subscriptionsResult.exceptionOrNull()?.message ?: "获取订阅失败"
                    Log.e(TAG, "获取订阅失败: $error")

                    // 使用仓库中的备用数据
                    val fallbackOrders = repository.getTailOrders()
                    _state.update { it.copy(
                        tailOrders = fallbackOrders,
                        isLoading = false,
                        error = "从XMPP获取数据失败，显示本地数据: $error"
                    ) }
                }
            } catch (e: Exception) {
                Log.e(TAG, "加载尾单时出错", e)

                // 使用仓库中的备用数据
                val fallbackOrders = repository.getTailOrders()
                _state.update { it.copy(
                    tailOrders = fallbackOrders,
                    isLoading = false,
                    error = "连接出错，显示本地数据: ${e.message}"
                ) }
            }
        }
    }

    /**
     * 监听新的尾单通知
     */
    private fun monitorNewTailLists(): Job {
        return viewModelScope.launch {
            Log.d(TAG, "Starting to monitor new tail lists from PubSub.")
            try {
                xmppManager.pubsubItemsFlow.collect { notification ->
                    try {
                        Log.d(TAG, "收到新的尾单通知: ${notification.itemId}")

                        // 强制从通知中解析尾单，而不是从缓存中获取
                        tailOrderCache.remove(notification.itemId) // 移除缓存以确保重新解析

                        val tailOrder = parseTailListNotification(notification)
                        if (tailOrder != null) {
                            // 添加新的尾单到列表首位
                            _state.update { currentState ->
                                val updatedList = currentState.tailOrders.toMutableList()
                                // 检查是否已存在，避免重复
                                val existingIndex = updatedList.indexOfFirst { it.id == tailOrder.id }
                                if (existingIndex >= 0) {
                                    updatedList[existingIndex] = tailOrder
                                } else {
                                    updatedList.add(0, tailOrder)
                                }
                                // Sort by ID descending to keep newest on top, assuming ID reflects newness
                                currentState.copy(tailOrders = updatedList.sortedByDescending { it.id })
                            }
                            Log.d(TAG, "成功添加/更新尾单: ${tailOrder.title}")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "处理尾单通知时出错 (within collect)", e)
                    }
                }
            } catch (e: CancellationException) {
                Log.d(TAG, "PubSub monitoring Job was cancelled.")
                // Cancellation is normal when the job is explicitly cancelled.
                // Rethrow to ensure the coroutine respects cancellation.
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Error in pubsubItemsFlow collection loop", e)
                // Potentially update UI or log more specifically
            }
        }
    }

    /**
     * 解析尾单通知
     */
    private fun parseTailListNotification(notification: PubSubNotification): TailOrder? {
        val itemId = notification.itemId

        // 检查缓存中是否已存在
        tailOrderCache[itemId]?.let { return it }

        try {
            val payload = notification.payload
            Log.d(TAG, "解析尾单通知，payload: $payload")

            // 使用JSoup解析XML
            val document = Jsoup.parse(payload, "", Parser.xmlParser())

            // 尝试获取<taillist>元素
            val taillistElement = document.select("taillist").firstOrNull()
            if (taillistElement == null) {
                Log.e(TAG, "找不到taillist元素")
                return null
            }

            // 获取发布者JID
            val publisherJid = taillistElement.select("publisher").firstOrNull()?.text() ?: ""
            Log.d(TAG, "获取到发布者JID: $publisherJid")

            // 尝试获取content元素
            val contentElement = taillistElement.select("content").firstOrNull()

            // 根据找到的元素决定如何解析
            if (contentElement != null && contentElement.hasAttr("type") && contentElement.attr("type") == "application/json") {
                // 如果有content且类型为JSON，解析JSON内容
                val jsonText = contentElement.text()

                try {
                    val json = JSONObject(jsonText)

                    // 检查是否是后端API格式（包含itinerary和productDetails）
                    if (json.has("itinerary") && json.has("productDetails")) {
                        return parseAPIFormatTailOrder(itemId, notification.nodeId, json, publisherJid)
                    } else {
                        // 常规格式
                        return parseFullTailOrder(itemId, notification.nodeId, jsonText, publisherJid)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "解析JSON内容失败", e)
                    // 如果JSON解析失败，尝试常规解析
                    return parseFullTailOrder(itemId, notification.nodeId, jsonText, publisherJid)
                }
            } else {
                // 否则，尝试解析简化版本
                return parseSimpleTailOrder(itemId, notification.nodeId, taillistElement, publisherJid)
            }
        } catch (e: Exception) {
            Log.e(TAG, "解析尾单通知失败", e)
            return null
        }
    }

    /**
     * 解析完整版尾单（从JSON）
     */
    private fun parseFullTailOrder(itemId: String, nodeId: String, jsonText: String, publisherJid: String = ""): TailOrder? {
        try {
            val json = JSONObject(jsonText)

            // 提取基本信息
            val title = json.optString("title", "未知标题")
            val description = json.optString("description", "")
            val price = json.optDouble("price", 0.0)
            val originalPrice = json.optDouble("originalPrice", price)
            val contactPerson = json.optString("contactPerson", "未知联系人")
            val contactPhone = json.optString("contactPhone", "")
            val tourGuide = json.optString("tourGuide", "")
            val location = json.optString("location", "")
            val company = json.optString("company", "未知公司")

            // 提取产品信息 - 直接从JSON中提取
            val productId = json.optLong("productId", 0L)
            val productTitle = json.optString("productTitle", "")

            // 如果JSON中有publisherJid，优先使用JSON中的值
            val finalPublisherJid = json.optString("publisherJid", publisherJid)

            // 解析日期
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val startDateStr = json.optString("startDate", "")
            val endDateStr = json.optString("endDate", "")
            val publishTimeStr = json.optString("publishTime", "")

            val startDate = try {
                if (startDateStr.isNotEmpty()) dateFormat.parse(startDateStr) else Date()
            } catch (e: Exception) {
                Date()
            }

            val endDate = try {
                if (endDateStr.isNotEmpty()) dateFormat.parse(endDateStr) else Date()
            } catch (e: Exception) {
                Date()
            }

            // 计算剩余有效期
            val currentTime = System.currentTimeMillis()
            val endTime = endDate.time
            val diffMillis = endTime - currentTime
            val diffDays = if (diffMillis > 0) TimeUnit.MILLISECONDS.toDays(diffMillis) else 0

            // 提取行程内容
            val contentJsonStr = json.optString("content", "")
            val contentList = if (contentJsonStr.isNotEmpty()) {
                try {
                    val contentJson = JSONObject(contentJsonStr)
                    val list = mutableListOf<String>()
                    for (i in 1..10) {
                        val key = "item$i"
                        if (contentJson.has(key)) {
                            list.add(contentJson.getString(key))
                        }
                    }
                    list
                } catch (e: Exception) {
                    listOf(description)
                }
            } else {
                // 如果没有详细内容，使用描述作为内容
                if (description.isNotEmpty()) {
                    description.split("\n").filter { it.isNotEmpty() }
                } else {
                    emptyList()
                }
            }

            // 生成随机ID，确保唯一性
            val uniqueId = UUID.fromString(itemId).hashCode()

            // 获取发布者JID的用户名部分
            val publisherUsername = if (finalPublisherJid.contains("@")) {
                finalPublisherJid.substringBefore("@")
            } else {
                finalPublisherJid
            }

            // 创建尾单对象
            val tailOrder = TailOrder(
                id = uniqueId,
                title = title,
                company = company,
                companyId = "company_${abs(company.hashCode())}",
                contactPerson = contactPerson.ifEmpty { publisherUsername },
                contactPersonId = finalPublisherJid,  // 使用完整JID作为联系人ID
                contactPhone = contactPhone,
                price = "¥${price.toInt()}",
                remainingDays = diffDays.toString(),
                remainingHours = "0:00",
                content = if (contentList.isNotEmpty()) contentList else listOf(description),
                summary = description,
                isFavorite = _state.value.favoriteOrderIds.contains(itemId),
                publisherJid = finalPublisherJid,  // 添加发布者JID
                productId = if (productId > 0) productId else null,  // 只有当productId大于0时才设置
                productTitle = if (productTitle.isNotEmpty()) productTitle else null  // 只有当productTitle不为空时才设置
            )

            // 添加到缓存
            tailOrderCache[itemId] = tailOrder

            // 记录产品信息到日志
            Log.d(TAG, "成功解析尾单: ID=${tailOrder.id}, 标题=${tailOrder.title}, 产品ID=${tailOrder.productId}, 产品标题=${tailOrder.productTitle}")

            return tailOrder
        } catch (e: Exception) {
            Log.e(TAG, "解析完整尾单失败", e)
            return null
        }
    }

    /**
     * 解析简化版尾单（从XML）
     */
    private fun parseSimpleTailOrder(itemId: String, nodeId: String, element: org.jsoup.nodes.Element, publisherJid: String = ""): TailOrder? {
        try {
            // 提取基本信息
            val title = element.select("title").firstOrNull()?.text() ?: "未知标题"
            val price = element.select("price").firstOrNull()?.text()?.toDoubleOrNull() ?: 0.0

            // 尝试提取产品信息
            val productId = element.select("productId").firstOrNull()?.text()?.toLongOrNull()
            val productTitle = element.select("productTitle").firstOrNull()?.text()

            // 生成随机ID
            val uniqueId = UUID.fromString(itemId).hashCode()

            // 获取发布者JID的用户名部分
            val publisherUsername = if (publisherJid.contains("@")) {
                publisherJid.substringBefore("@")
            } else {
                publisherJid
            }

            // 创建尾单对象，使用默认值
            val tailOrder = TailOrder(
                id = uniqueId,
                title = title,
                company = "来自节点: $nodeId",
                companyId = "node_$nodeId",
                contactPerson = publisherUsername.ifEmpty { "未知联系人" },
                contactPersonId = publisherJid,  // 使用完整JID作为联系人ID
                contactPhone = "",
                price = "¥${price.toInt()}",
                remainingDays = "3", // 默认3天
                remainingHours = "0:00",
                content = listOf("此尾单没有详细描述"),
                summary = "此尾单没有详细描述",
                isFavorite = _state.value.favoriteOrderIds.contains(itemId),
                publisherJid = publisherJid,  // 添加发布者JID
                productId = productId,  // 添加产品ID
                productTitle = productTitle  // 添加产品标题
            )

            // 添加到缓存
            tailOrderCache[itemId] = tailOrder

            return tailOrder
        } catch (e: Exception) {
            Log.e(TAG, "解析简化尾单失败", e)
            return null
        }
    }

    /**
     * 解析后端API格式的尾单（包含itinerary和嵌套的productDetails）
     */
    private fun parseAPIFormatTailOrder(itemId: String, nodeId: String, json: JSONObject, publisherJid: String = ""): TailOrder? {
        try {
            // 提取基本信息
            val title = json.optString("title", "未知标题")
            val itinerary = json.optString("itinerary", "")
            val expiryDateStr = json.optString("expiryDate", "")
            val productDetailsStr = json.optString("productDetails", "")

            // 如果JSON中有publisherJid，优先使用JSON中的值
            val finalPublisherJid = json.optString("publisherJid", publisherJid)

            // 提取产品信息 - 直接从JSON中提取
            var productId = json.optLong("productId", 0L)
            var productTitle = json.optString("productTitle", "")

            // 解析嵌套的productDetails JSON
            if (productDetailsStr.isNotEmpty() && (productId == 0L || productTitle.isEmpty())) {
                try {
                    val productDetailsJson = JSONObject(productDetailsStr)

                    // 从嵌套JSON中提取产品信息
                    if (productId == 0L) {
                        productId = productDetailsJson.optLong("productId", 0L)
                    }

                    if (productTitle.isEmpty()) {
                        productTitle = productDetailsJson.optString("productTitle", "")
                    }

                    Log.d(TAG, "从嵌套productDetails中提取了产品信息: ID=$productId, 标题=$productTitle")
                } catch (e: Exception) {
                    Log.e(TAG, "解析嵌套productDetails JSON失败", e)
                }
            }

            // 生成随机ID，确保唯一性
            val uniqueId = UUID.fromString(itemId).hashCode()

            // 获取发布者JID的用户名部分
            val publisherUsername = if (finalPublisherJid.contains("@")) {
                finalPublisherJid.substringBefore("@")
            } else {
                finalPublisherJid
            }

            // 计算剩余天数
            val diffDays = try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val expiryDate = dateFormat.parse(expiryDateStr)
                val currentDate = Date()
                val diffInMillis = expiryDate.time - currentDate.time
                if (diffInMillis > 0) {
                    TimeUnit.MILLISECONDS.toDays(diffInMillis).toString()
                } else {
                    "0" // 如果已经过期，显示为0天
                }
            } catch (e: Exception) {
                Log.e(TAG, "解析尾单过期时间失败: $expiryDateStr", e)
                "0" // 默认值
            }

            // 创建尾单对象
            val tailOrder = TailOrder(
                id = uniqueId,
                title = title,
                company = "我的发布",
                companyId = "my_publish",
                contactPerson = publisherUsername,
                contactPersonId = finalPublisherJid,
                contactPhone = "",
                price = "¥0", // 价格通常在productDetails中，这里设置默认值
                remainingDays = diffDays,
                remainingHours = "0:00",
                content = listOf(itinerary),
                summary = itinerary,
                isFavorite = _state.value.favoriteOrderIds.contains(itemId),
                publisherJid = finalPublisherJid,
                productId = if (productId > 0) productId else null,
                productTitle = if (productTitle.isNotEmpty()) productTitle else null
            )

            // 添加到缓存
            tailOrderCache[itemId] = tailOrder

            Log.d(TAG, "成功解析后端API格式尾单: ID=${tailOrder.id}, 标题=${tailOrder.title}, 产品ID=${tailOrder.productId}, 产品标题=${tailOrder.productTitle}")

            return tailOrder
        } catch (e: Exception) {
            Log.e(TAG, "解析后端API格式尾单失败", e)
            return null
        }
    }

    /**
     * 刷新数据
     */
    fun refreshTailLists() {
        // 清除缓存，确保获取最新数据
        tailOrderCache.clear()

        // 确保XMPP连接并订阅节点
        ensureXmppConnectionAndSubscriptions()
    }

    /**
     * 标记或取消标记尾单为收藏
     */
    fun toggleFavorite(id: Int) {
        val currentList = _state.value.tailOrders.toMutableList()
        val index = currentList.indexOfFirst { it.id == id }

        if (index != -1) {
            val tailOrder = currentList[index]
            val updatedTailOrder = tailOrder.copy(isFavorite = !tailOrder.isFavorite)
            currentList[index] = updatedTailOrder

            _state.update { it.copy(tailOrders = currentList) }

            // 在后台更新收藏状态
            viewModelScope.launch {
                try {
                    repository.updateFavoriteStatus(id.toString(), updatedTailOrder.isFavorite)
                } catch (e: Exception) {
                    // 如果更新失败，恢复原始状态
                    val revertedList = _state.value.tailOrders.toMutableList()
                    val revertIndex = revertedList.indexOfFirst { it.id == id }
                    if (revertIndex != -1) {
                        revertedList[revertIndex] = tailOrder
                        _state.update { it.copy(tailOrders = revertedList) }
                    }
                }
            }
        }
    }

    /**
     * 获取已收藏的尾单
     */
    fun getFavoriteTailOrders(): List<TailOrder> {
        return _state.value.tailOrders.filter { it.isFavorite }
    }

    /**
     * 获取用户信息
     */
    suspend fun getUserInfo(username: String): Map<String, Any>? {
        return try {
            val response = userApiService.getUserInfo(username)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * ViewModel工厂类，用于创建ViewModel的实例
     */
    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TailListViewModel::class.java)) {
                return TailListViewModel(
                    repository = TailOrderRepositoryImpl.getInstance()
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
    
    companion object {
        @Volatile
        private var instance: TailListViewModel? = null
        
        fun getInstance(): TailListViewModel {
            return instance ?: throw IllegalStateException("TailListViewModel尚未初始化")
        }

    }
} 