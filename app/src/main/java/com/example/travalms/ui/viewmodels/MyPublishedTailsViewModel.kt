package com.example.travalms.ui.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travalms.api.dto.TailOrderResponse
import com.example.travalms.data.api.NetworkModule
import com.example.travalms.data.remote.ConnectionState
import com.example.travalms.data.remote.PubSubNotification
import com.example.travalms.data.remote.XMPPManager
import com.example.travalms.ui.model.PostItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import kotlinx.coroutines.delay

private const val TAG = "MyPublishedTailsVM"

/**
 * 我的发布界面数据状态
 */
data class MyPublishedTailsState(
    val isLoading: Boolean = false,
    val publishedTails: List<PostItem> = emptyList(),
    val errorMessage: String? = null
)

/**
 * 管理"我的发布"界面数据的ViewModel
 */
class MyPublishedTailsViewModel(application: Application) : AndroidViewModel(application) {
    
    // XMPP管理器
    private val xmppManager = XMPPManager.getInstance()
    
    // API服务
    private val tailOrderApiService = NetworkModule.tailOrderApiService
    
    // UI状态
    private val _uiState = MutableStateFlow(MyPublishedTailsState())
    val uiState: StateFlow<MyPublishedTailsState> = _uiState.asStateFlow()

    // 保存原始的后端响应数据，用于详情页面
    private val _originalTailOrders = MutableStateFlow<Map<Long, TailOrderResponse>>(emptyMap())
    val originalTailOrders: StateFlow<Map<Long, TailOrderResponse>> = _originalTailOrders.asStateFlow()

    // 获取特定ID的尾单原始数据
    fun getOriginalTailOrderById(id: Long): TailOrderResponse? {
        return _originalTailOrders.value[id]
    }

    // 更新原始尾单数据
    fun updateOriginalTailOrders(newData: Map<Long, TailOrderResponse>) {
        _originalTailOrders.value = newData
        Log.d(TAG, "更新原始尾单数据，当前数量: ${newData.size}")
    }

    // 静态实例，用于其他ViewModel访问
    companion object {
        private var instance: MyPublishedTailsViewModel? = null
        
        fun getInstance(app: Application): MyPublishedTailsViewModel {
            if (instance == null) {
                instance = MyPublishedTailsViewModel(app)
            }
            return instance!!
        }
    }

    // 加载用户发布的尾单
    fun loadUserPublishedTails() {
        // 检查登录状态
        if (xmppManager.connectionState.value != ConnectionState.AUTHENTICATED) {
            _uiState.update { state -> state.copy(errorMessage = "未登录到XMPP服务器") }
            return
        }

        // 获取当前用户的JID
        val currentUserJid = xmppManager.currentConnection?.user?.asEntityBareJidString()
        if (currentUserJid == null) {
            _uiState.update { state -> state.copy(errorMessage = "无法获取当前用户JID") }
            Log.e(TAG, "无法获取JID，无法加载用户发布历史")

            return
        }
        
        // 开始加载数据
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        
        viewModelScope.launch {
            try {


                // 调用API获取用户发布的尾单
                val response = tailOrderApiService.getUserTailOrders(currentUserJid)
                
                if (response.isSuccessful && response.body() != null) {
                    val tailOrders = response.body()!!
                    val postItems = convertToPostItems(tailOrders)

                    // 保存原始数据
                    val originalDataMap = tailOrders.associateBy { it.id }
                    _originalTailOrders.value = originalDataMap
                    
                    _uiState.update { state ->
                        state.copy(publishedTails = postItems, isLoading = false, errorMessage = null)
                    }
                    Log.d(TAG, "成功加载用户发布历史: ${tailOrders.size} 条记录")
                } else {
                    val errorBody = response.errorBody()?.string() ?: "未知错误"
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = "获取发布历史失败: ${response.code()} - $errorBody"
                        )
                    }
                    Log.e(TAG, "获取用户发布历史失败: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = "加载数据时出错: ${e.message}"
                    )
                }
                Log.e(TAG, "加载用户发布历史时出现异常", e)
            }
        }
    }
    
    /**
     * 将API返回的TailOrderResponse转换为UI使用的PostItem
     */
    private fun convertToPostItems(tailOrders: List<TailOrderResponse>): List<PostItem> {
        // 获取当前用户名，从JID中提取用户名部分
        val currentUserJid = xmppManager.currentConnection?.user?.asEntityBareJidString() ?: ""
        val currentUsername = if (currentUserJid.contains("@")) {
            currentUserJid.substringBefore("@")
        } else {
            currentUserJid
        }
        
        Log.d(TAG, "当前用户名: $currentUsername, JID: $currentUserJid")
        
        return tailOrders.map { tailOrder ->
            // 解析productDetails JSON字符串
            val productDetails = try {
                JSONObject(tailOrder.productDetails)
            } catch (e: Exception) {
                JSONObject()
            }
            
            // 提取数据
            val price = productDetails.optInt("price", 0)
            val startDateStr = productDetails.optString("startDate", "")
            val location = productDetails.optString("location", "")
            
            // 使用当前用户名作为发布者
            val contactPerson = currentUsername
            
            // 计算过期天数
            val daysExpired = calculateDaysExpired(tailOrder.expiryDate)
            
            // 提取节点信息
            val publishLocations = tailOrder.publishingNodes?.map { it.nodeName } ?: emptyList()

            // 记录产品信息日志
            Log.d(TAG, "尾单产品信息: ID=${tailOrder.id}, 产品ID=${tailOrder.productId}, 产品标题=${tailOrder.productTitle}")
            
            // 创建UI对象
            PostItem(
                id = tailOrder.id.toInt(),
                title = tailOrder.title,
                feature = tailOrder.itinerary,
                publisher = contactPerson,
                publishTime = System.currentTimeMillis(),
                dates = startDateStr,
                remainingSlots = 10, // 假设值，后端没有提供
                price = price.toString(),
                daysExpired = daysExpired,
                publishLocations = publishLocations,
                productId = tailOrder.productId?.toInt(),
                productTitle = tailOrder.productTitle
            )
        }
    }
    
    /**
     * 计算尾单过期天数
     */
    private fun calculateDaysExpired(expiryDateStr: String): Int {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val expiryDate = dateFormat.parse(expiryDateStr)
            val currentDate = Date()
            
            val diffInMillis = expiryDate.time - currentDate.time
            val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)
            
            // 返回天数，最小为0
            diffInDays.toInt().coerceAtLeast(0)
        } catch (e: Exception) {
            Log.e(TAG, "解析过期日期时出错: $expiryDateStr", e)
            0 // 默认值
        }
    }

    /**
     * 删除尾单
     * 从后端数据库删除尾单，并从所有发布节点中删除对应的XMPP消息
     * @param tailOrderId 要删除的尾单ID
     */
    fun deleteTailOrder(tailOrderId: Long) {
        // 检查登录状态
        if (xmppManager.connectionState.value != ConnectionState.AUTHENTICATED) {
            _uiState.update { state -> state.copy(errorMessage = "未登录到XMPP服务器，无法删除") }
            return
        }

        // 开始删除操作
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                Log.d(TAG, "开始删除尾单: ID=$tailOrderId")

                // 1. 获取尾单信息和发布节点
                val originalTailOrder = _originalTailOrders.value[tailOrderId]
                if (originalTailOrder == null) {
                    Log.e(TAG, "找不到要删除的尾单: ID=$tailOrderId")
                    _uiState.update { it.copy(isLoading = false, errorMessage = "找不到要删除的尾单") }
                    return@launch
                }

                // 获取发布节点信息
                val publishingNodes = originalTailOrder.publishingNodes?.map { it.nodeName } ?: emptyList()
                Log.d(TAG, "尾单发布节点: ${publishingNodes.joinToString()}")

                // 2. 从XMPP节点中删除消息
                var xmppDeleteSuccess = false
                for (nodeId in publishingNodes) {
                    try {
                        // 获取节点中的所有项目
                        val nodeItemsResult = xmppManager.getNodeItems(nodeId)
                        if (nodeItemsResult.isSuccess) {
                            val notifications = nodeItemsResult.getOrThrow()
                            Log.d(TAG, "节点 $nodeId 中找到 ${notifications.size} 个项目")

                            // 查找匹配的项目
                            for (notification in notifications) {
                                // 检查是否是匹配的尾单
                                if (isMatchingTailOrder(notification, tailOrderId, originalTailOrder.title)) {
                                    // 删除找到的匹配项
                                    val result = xmppManager.retractItemFromNode(nodeId, notification.itemId)
                                    if (result.isSuccess) {
                                        xmppDeleteSuccess = true
                                        Log.d(TAG, "从节点 $nodeId 删除了项目: itemId=${notification.itemId}")
                                        // 添加延迟，避免过快发送请求
                                        delay(100)
                                    } else {
                                        Log.e(TAG, "从节点 $nodeId 删除项目失败: ${result.exceptionOrNull()?.message}")
                                    }
                                }
                            }
                        } else {
                            Log.e(TAG, "获取节点 $nodeId 项目失败: ${nodeItemsResult.exceptionOrNull()?.message}")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "处理节点 $nodeId 时出错", e)
                    }
                }

                // 3. 从后端API删除
                val response = tailOrderApiService.deleteTailOrder(tailOrderId)

                if (response.isSuccessful) {
                    Log.d(TAG, "成功从后端API删除尾单: ID=$tailOrderId")

                    // 4. 更新本地数据 - 使用单次更新避免多次触发UI更新
                    val updatedOriginals = _originalTailOrders.value.toMutableMap()
                    updatedOriginals.remove(tailOrderId)

                    // 在状态更新后更新原始数据
                    _originalTailOrders.value = updatedOriginals
                    val updatedList = _uiState.value.publishedTails.filterNot { it.id == tailOrderId.toInt() }

                    // 使用单次更新，避免多次触发UI更新
                    _uiState.update { it.copy(
                        publishedTails = updatedList,
                        isLoading = false,
                        errorMessage = null
                    ) }

                    // 记录XMPP删除结果
                    if (!xmppDeleteSuccess) {
                        Log.w(TAG, "尾单从后端删除成功，但XMPP消息删除失败")
                    }

                    // 在成功删除所有内容并更新UI后，重置XMPP连接
                    resetXmppConnectionAfterDelete()

                } else {
                    // 处理API错误
                    val errorMsg = "删除失败: ${response.code()} - ${response.message()}"
                    Log.e(TAG, errorMsg)
                    _uiState.update { it.copy(isLoading = false, errorMessage = errorMsg) }
                }
            } catch (e: Exception) {
                // 处理异常
                val errorMsg = "删除出错: ${e.message}"
                Log.e(TAG, errorMsg, e)
                _uiState.update { it.copy(isLoading = false, errorMessage = errorMsg) }
            }
        }
    }

    /**
     * 重置XMPP连接
     */
    private suspend fun resetXmppConnectionAfterDelete() {
        try {
            Log.d(TAG, "开始重置XMPP连接...")

            // 断开当前连接
            xmppManager.disconnect()

            // 等待1秒确保连接完全关闭
            delay(1000)

            // 从SharedPreferences获取保存的凭据
            val app = getApplication<Application>()
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

            Log.d(TAG, "XMPP连接重置完成")
        } catch (e: Exception) {
            Log.e(TAG, "重置XMPP连接时出错", e)
        }
    }

    /**
     * 检查通知是否匹配指定的尾单ID和标题
     */
    private fun isMatchingTailOrder(notification: PubSubNotification, tailOrderId: Long, title: String): Boolean {
        try {
            val payload = notification.payload
            // 使用JSoup解析XML
            val document = Jsoup.parse(payload, "", Parser.xmlParser())

            // 尝试获取content元素
            val contentElement = document.select("content").firstOrNull()
             if (contentElement != null) {
                val contentText = contentElement.text()
                // 解析内容，检查是否包含指定的尾单ID或标题
                try {
                    val jsonObject = JSONObject(contentText)

                    // 1. 检查productId字段
                    if (jsonObject.has("productId") && jsonObject.optLong("productId", 0) == tailOrderId) {
                        Log.d(TAG, "通过productId匹配到尾单: $tailOrderId")
                        return true
                    }

                    // 2. 检查标题是否完全匹配
                    if (jsonObject.has("title") && jsonObject.optString("title") == title) {
                        Log.d(TAG, "通过title匹配到尾单: $title")
                        return true
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "解析通知内容JSON失败", e)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "解析通知失败", e)
        }
        return false
    }

    // 初始加载数据
    init {
        loadUserPublishedTails()
    }

} 