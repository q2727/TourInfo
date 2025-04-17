package com.example.travalms.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travalms.data.remote.XMPPManager
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
    
    init {
        fetchTailOrders()
        
        // 监听新的尾单通知
        monitorNewTailLists()
    }
    
    /**
     * 加载所有已订阅节点的尾单列表
     */
    fun fetchTailOrders() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            try {
                val subscriptionsResult = xmppManager.getUserSubscriptions()
                if (subscriptionsResult.isSuccess) {
                    val subscriptions = subscriptionsResult.getOrThrow()
                    
                    Log.d(TAG, "获取到 ${subscriptions.size} 个已订阅节点")
                    
                    if (subscriptions.isEmpty()) {
                        // 如果没有订阅，显示默认数据
                        val fallbackOrders = repository.getTailOrders()
                        _state.update { it.copy(tailOrders = fallbackOrders, isLoading = false) }
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
                                for (notification in notifications) {
                                    val tailOrder = parseTailListNotification(notification)
                                    if (tailOrder != null) {
                                        allTailOrders.add(tailOrder)
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
                    } else {
                        _state.update { it.copy(tailOrders = allTailOrders, isLoading = false) }
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
    private fun monitorNewTailLists() {
        viewModelScope.launch {
            xmppManager.pubsubItemsFlow.collect { notification ->
                try {
                    val tailOrder = parseTailListNotification(notification)
                    if (tailOrder != null) {
                        // 添加新的尾单到列表首位
                        _state.update { state ->
                            val updatedList = state.tailOrders.toMutableList()
                            // 检查是否已存在，避免重复
                            val existingIndex = updatedList.indexOfFirst { it.id == tailOrder.id }
                            if (existingIndex >= 0) {
                                updatedList[existingIndex] = tailOrder
                            } else {
                                updatedList.add(0, tailOrder)
                            }
                            state.copy(tailOrders = updatedList)
                        }
                        Log.d(TAG, "收到新的尾单通知: ${tailOrder.title}")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "处理尾单通知时出错", e)
                }
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
            
            // 尝试获取content元素
            val contentElement = taillistElement.select("content").firstOrNull()
            
            // 根据找到的元素决定如何解析
            if (contentElement != null && contentElement.hasAttr("type") && contentElement.attr("type") == "application/json") {
                // 如果有content且类型为JSON，解析完整内容
                val jsonText = contentElement.text()
                return parseFullTailOrder(itemId, notification.nodeId, jsonText)
            } else {
                // 否则，尝试解析简化版本
                return parseSimpleTailOrder(itemId, notification.nodeId, taillistElement)
            }
        } catch (e: Exception) {
            Log.e(TAG, "解析尾单通知失败", e)
            return null
        }
    }
    
    /**
     * 解析完整版尾单（从JSON）
     */
    private fun parseFullTailOrder(itemId: String, nodeId: String, jsonText: String): TailOrder? {
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
            val diffMillis = abs(endTime - currentTime)
            val diffDays = TimeUnit.MILLISECONDS.toDays(diffMillis)
            val remainingHours = TimeUnit.MILLISECONDS.toHours(diffMillis) % 24
            
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
            
            // 创建尾单对象
            val tailOrder = TailOrder(
                id = uniqueId,
                title = title,
                company = company,
                companyId = "company_${abs(company.hashCode())}",
                contactPerson = contactPerson,
                contactPersonId = "person_${abs(contactPerson.hashCode())}",
                contactPhone = contactPhone,
                price = "¥${price.toInt()}",
                remainingDays = diffDays.toString(),
                remainingHours = String.format("%d:%02d", remainingHours, Random.nextInt(60)),
                content = if (contentList.isNotEmpty()) contentList else listOf(description),
                summary = description,
                isFavorite = _state.value.favoriteOrderIds.contains(itemId)
            )
            
            // 添加到缓存
            tailOrderCache[itemId] = tailOrder
            
            return tailOrder
        } catch (e: Exception) {
            Log.e(TAG, "解析完整尾单失败", e)
            return null
        }
    }
    
    /**
     * 解析简化版尾单（从XML）
     */
    private fun parseSimpleTailOrder(itemId: String, nodeId: String, element: org.jsoup.nodes.Element): TailOrder? {
        try {
            // 提取基本信息
            val title = element.select("title").firstOrNull()?.text() ?: "未知标题"
            val price = element.select("price").firstOrNull()?.text()?.toDoubleOrNull() ?: 0.0
            
            // 生成随机ID
            val uniqueId = UUID.fromString(itemId).hashCode()
            
            // 创建尾单对象，使用默认值
            val tailOrder = TailOrder(
                id = uniqueId,
                title = title,
                company = "来自节点: $nodeId",
                companyId = "node_$nodeId",
                contactPerson = "未知联系人",
                contactPersonId = "unknown",
                contactPhone = "",
                price = "¥${price.toInt()}",
                remainingDays = "3", // 默认3天
                remainingHours = "0:00",
                content = listOf("此尾单没有详细描述"),
                summary = "此尾单没有详细描述",
                isFavorite = _state.value.favoriteOrderIds.contains(itemId)
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
     * 刷新数据
     */
    fun refreshTailLists() {
        fetchTailOrders()
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
} 