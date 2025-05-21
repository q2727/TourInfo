package com.example.travalms.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travalms.data.remote.XMPPManager
import com.example.travalms.data.remote.PubSubNotification
import com.example.travalms.ui.screens.SubscriptionNodeItem
import com.example.travalms.ui.screens.SubscriptionNodeType
import com.example.travalms.data.remote.ConnectionState
import com.example.travalms.api.dto.TailOrderRequest
import com.example.travalms.api.dto.TailOrderResponse
import com.example.travalms.data.api.NetworkModule
import com.example.travalms.data.api.TailOrderApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 发布视图模型状态
 */
data class PublishState(
    val isPublishing: Boolean = false,
    val selectedNodeIds: List<String> = emptyList(),
    val publishSuccess: Boolean = false,
    val errorMessage: String? = null,
    // 为了方便UI显示，保存节点名称
    val selectedNodeNames: List<String> = emptyList(),
    // 新增API调用状态
    val apiSuccess: Boolean? = null, // null表示未尝试，true表示成功，false表示失败
    val apiErrorMessage: String? = null // API调用的错误信息
)

/**
 * 尾单数据类，包含所有需要发布的信息
 */
data class TailListItem(
    val title: String,
    val description: String,
    val price: Double,
    val originalPrice: Double,
    val startDate: Date,
    val endDate: Date,
    val contactPerson: String,
    val contactPhone: String,
    val tourGuide: String? = null,
    val location: String? = null,
    val tags: List<String> = emptyList(),
    val productId: Long? = null,
    val productTitle: String? = null
)

/**
 * 发布视图模型，处理尾单的发布
 */
class PublishViewModel : ViewModel() {
    // 使用XMPPManager的单例实例
    private val xmppManager = XMPPManager.getInstance()
    // 使用TailOrderApiService实例
    private val tailOrderApiService: TailOrderApiService = NetworkModule.tailOrderApiService
    
    private val _uiState = MutableStateFlow(PublishState())
    val uiState: StateFlow<PublishState> = _uiState.asStateFlow()
    
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()
    
    val connectionState: StateFlow<ConnectionState> = xmppManager.connectionState
    
    private val TAG = "PublishViewModel"
    
    // 用于API请求的ISO 8601日期格式
    private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
        timeZone = java.util.TimeZone.getTimeZone("UTC")
    }
    
    // 用于显示的日期格式
    private val displayDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    
    // 初始化代码块，检查XMPPManager的当前状态
    init {
        // 在ViewModel初始化时同步登录状态
        _isLoggedIn.value = (connectionState.value == ConnectionState.AUTHENTICATED)
        
        // 监听连接状态变化，自动更新isLoggedIn
        viewModelScope.launch {
            connectionState.collect { state ->
                _isLoggedIn.value = (state == ConnectionState.AUTHENTICATED)
                
                // 如果连接断开，清除错误信息，避免显示旧的错误
                if (state != ConnectionState.AUTHENTICATED) {
                    _uiState.update { it.copy(errorMessage = null, apiErrorMessage = null) }
                }
                
                // 日志状态变化，帮助调试
                Log.d(TAG, "XMPP连接状态变为: $state, 登录状态: ${_isLoggedIn.value}")
            }
        }
    }
    
    /**
     * 设置选定的发布节点
     */
    fun setSelectedNodes(nodeIds: List<String>, nodeNames: List<String>) {
        _uiState.update { state ->
            state.copy(
                selectedNodeIds = nodeIds,
                selectedNodeNames = nodeNames
            )
        }
        Log.d(TAG, "已选择的发布节点: ${nodeIds.size}个")
    }
    
    /**
     * 登录XMPP服务器
     * @return 登录是否成功
     */
    suspend fun loginToXMPP(username: String, password: String): Boolean {
        try {
            val result = xmppManager.login(username, password)
            if (result.isSuccess) {
                _isLoggedIn.value = true
                Log.d(TAG, "XMPP登录成功")
                return true
            } else {
                _isLoggedIn.value = false
                val error = result.exceptionOrNull()?.message ?: "登录失败"
                _uiState.update { state -> state.copy(errorMessage = error) }
                Log.e(TAG, "XMPP登录失败: $error")
                return false
            }
        } catch (e: Exception) {
            _isLoggedIn.value = false
            _uiState.update { state -> state.copy(errorMessage = "登录错误: ${e.message}") }
            Log.e(TAG, "XMPP登录异常", e)
            return false
        }
    }
    
    /**
     * 创建节点映射
     * 自动为"选择发布节点"界面中的每个节点创建对应的XMPP节点
     */
    fun createNodesForHierarchy(nodes: List<SubscriptionNodeItem>) {
        if (xmppManager.connectionState.value != ConnectionState.AUTHENTICATED) {
            _uiState.update { state -> state.copy(errorMessage = "XMPP未认证，无法创建节点") }
            Log.w(TAG, "尝试创建节点但XMPP未认证")
            return
        }
        
        viewModelScope.launch {
            try {
                createNodesRecursively(nodes)
                Log.d(TAG, "所有节点创建完成")
            } catch (e: Exception) {
                _uiState.update { state -> state.copy(errorMessage = "创建节点时出错: ${e.message}") }
                Log.e(TAG, "创建节点时出现异常", e)
            }
        }
    }
    
    /**
     * 递归创建节点
     */
    private suspend fun createNodesRecursively(nodes: List<SubscriptionNodeItem>) {
        for (node in nodes) {
            try {
                // 为每个节点创建一个XMPP节点
                val nodeId = node.id
                val name = node.name
                val description = when (node.type) {
                    SubscriptionNodeType.PROVINCE -> "省份: ${node.name}"
                    SubscriptionNodeType.CITY -> "城市: ${node.name}"
                    SubscriptionNodeType.ATTRACTION -> "景点: ${node.name}"
                    SubscriptionNodeType.COMPANY -> "公司: ${node.name}"
                    else -> node.name
                }
                
                val result = xmppManager.createNode(nodeId, name, description)
                if (result.isSuccess) {
                    Log.d(TAG, "节点创建成功: $nodeId")
                } else {
                    Log.e(TAG, "节点创建失败: $nodeId, ${result.exceptionOrNull()?.message}")
                }
                
                // 递归处理子节点
                if (node.children.isNotEmpty()) {
                    createNodesRecursively(node.children)
                }
            } catch (e: Exception) {
                Log.e(TAG, "处理节点 ${node.id} 时出错", e)
                // 继续处理其他节点
            }
        }
    }
    
    /**
     * 发布尾单到选定的节点
     */
    fun publishTailList(tailListItem: TailListItem) {
        if (xmppManager.connectionState.value != ConnectionState.AUTHENTICATED) {
            _uiState.update { state -> state.copy(errorMessage = "未登录到XMPP服务器", apiSuccess = null, apiErrorMessage = null) }
            return
        }
        
        // 获取当前用户的JID
        val currentUserJid = xmppManager.currentConnection?.user?.asEntityBareJidString()
        if (currentUserJid == null) {
            _uiState.update { state -> state.copy(errorMessage = "无法获取当前用户JID", apiSuccess = null, apiErrorMessage = null) }
            Log.e(TAG, "无法获取JID，无法发布数据到后端")
            return
        }
        
        if (_uiState.value.selectedNodeIds.isEmpty()) {
            _uiState.update { state -> state.copy(errorMessage = "未选择发布节点", apiSuccess = null, apiErrorMessage = null) }
            return
        }
        
        _uiState.update { state -> state.copy(isPublishing = true, publishSuccess = false, errorMessage = null, apiSuccess = null, apiErrorMessage = null) }
        
        viewModelScope.launch {
            // XMPP发布状态追踪
            var xmppSuccessCount = 0
            var xmppPublishAttempted = false
            
            try {
                // 1. 先进行XMPP发布
                val content = createTailListJson(tailListItem)
                
                // 发布到所有选择的节点
                for (nodeId in _uiState.value.selectedNodeIds) {
                    try {
                        xmppPublishAttempted = true
                        val result = xmppManager.publishToNode(nodeId, content)
                        if (result.isSuccess) {
                            xmppSuccessCount++
                            Log.d(TAG, "尾单发布成功到节点: $nodeId, itemId=${result.getOrNull()}")
                        } else {
                            Log.e(TAG, "尾单发布到节点 $nodeId 失败: ${result.exceptionOrNull()?.message}")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "发布到节点 $nodeId 时出错", e)
                    }
                }
                
                // 判断XMPP发布是否整体成功
                val xmppOverallSuccess = xmppSuccessCount > 0 && xmppPublishAttempted
                
                // 更新XMPP发布状态
                _uiState.update { state -> 
                    state.copy(
                        publishSuccess = xmppOverallSuccess,
                        errorMessage = if (!xmppOverallSuccess && xmppPublishAttempted) "XMPP发布失败，请重试" else null
                    )
                }
                
                Log.d(TAG, "尾单XMPP发布完成: 成功=${xmppSuccessCount}/${_uiState.value.selectedNodeIds.size}")
                
                // 2. 如果XMPP发布至少部分成功，则继续调用后端API存储数据
                if (xmppOverallSuccess) {
                    try {
                        Log.d(TAG, "XMPP发布成功，开始调用后端API存储数据...")
                        
                        // 创建产品详情JSON
                        val productDetailsJson = JSONObject().apply {
                            put("price", tailListItem.price)
                            put("originalPrice", tailListItem.originalPrice)
                            put("startDate", displayDateFormat.format(tailListItem.startDate))
                            put("contactPerson", tailListItem.contactPerson)
                            put("contactPhone", tailListItem.contactPhone)
                            put("publishTime", displayDateFormat.format(Date()))
                            
                            tailListItem.tourGuide?.let { put("tourGuide", it) }
                            tailListItem.location?.let { put("location", it) }
                            tailListItem.productId?.let { put("productId", it) }
                            tailListItem.productTitle?.let { put("productTitle", it) }
                            
                            if (tailListItem.tags.isNotEmpty()) {
                                put("tags", JSONObject().apply {
                                    tailListItem.tags.forEachIndexed { index, tag ->
                                        put(index.toString(), tag)
                                    }
                                })
                            }
                        }.toString()
                        
                        // 创建API请求对象
                        val request = TailOrderRequest(
                            jid = currentUserJid,
                            title = tailListItem.title,
                            itinerary = tailListItem.description, // 将description映射到后端的itinerary字段
                            expiryDate = apiDateFormat.format(tailListItem.endDate), // 将endDate映射到后端的expiryDate字段
                            publishingNodes = _uiState.value.selectedNodeIds,
                            productDetails = productDetailsJson,
                            productId = tailListItem.productId,
                            productTitle = tailListItem.productTitle
                        )
                        
                        // 调用后端API
                        val response = tailOrderApiService.publishTailOrder(request)
                        
                        // 处理API响应
                        if (response.isSuccessful && response.body() != null) {
                            _uiState.update { it.copy(
                                apiSuccess = true,
                                apiErrorMessage = null,
                                isPublishing = false
                            )}
                            Log.d(TAG, "尾单成功存储到后端数据库: ID=${response.body()?.id}")
                        } else {
                            val errorBody = response.errorBody()?.string() ?: "未知错误"
                            _uiState.update { it.copy(
                                apiSuccess = false,
                                apiErrorMessage = "存储到数据库失败: ${response.code()} - $errorBody",
                                isPublishing = false
                            )}
                            Log.e(TAG, "存储尾单到后端数据库失败: ${response.code()} - $errorBody")
                        }
                    } catch (e: Exception) {
                        _uiState.update { it.copy(
                            apiSuccess = false,
                            apiErrorMessage = "调用API时出错: ${e.message}",
                            isPublishing = false
                        )}
                        Log.e(TAG, "调用后端API时出现异常", e)
                    }
                } else {
                    // XMPP发布完全失败，结束处理
                    _uiState.update { it.copy(isPublishing = false) }
                }
                
            } catch (e: Exception) {
                _uiState.update { state -> 
                    state.copy(
                        isPublishing = false,
                        publishSuccess = false,
                        errorMessage = "发布出错: ${e.message}",
                        apiSuccess = null,
                        apiErrorMessage = null
                    )
                }
                Log.e(TAG, "发布尾单时出现异常", e)
            }
        }
    }
    
    /**
     * 创建尾单的JSON内容
     */
    private fun createTailListJson(item: TailListItem): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val jsonObj = JSONObject().apply {
            put("type", "taillist")
            put("title", item.title)
            put("description", item.description)
            put("price", item.price)
            put("originalPrice", item.originalPrice)
            put("startDate", dateFormat.format(item.startDate))
            put("endDate", dateFormat.format(item.endDate))
            put("contactPerson", item.contactPerson)
            put("contactPhone", item.contactPhone)
            put("publishTime", dateFormat.format(Date()))
            
            item.tourGuide?.let { put("tourGuide", it) }
            item.location?.let { put("location", it) }
            item.productId?.let { put("productId", it) }
            item.productTitle?.let { put("productTitle", it) }
            
            if (item.tags.isNotEmpty()) {
                put("tags", JSONObject().apply {
                    item.tags.forEachIndexed { index, tag ->
                        put(index.toString(), tag)
                    }
                })
            }
        }
        
        return jsonObj.toString()
    }
    
    /**
     * 清除发布状态
     */
    fun resetPublishState() {
        _uiState.update { state -> 
            state.copy(
                isPublishing = false,
                publishSuccess = false,
                errorMessage = null,
                apiSuccess = null,
                apiErrorMessage = null
            )
        }
    }
    
    /**
     * 刷新登录状态
     * 强制检查并同步XMPPManager的连接状态
     */
    fun refreshLoginState() {
        val currentConnectionState = xmppManager.connectionState.value
        _isLoggedIn.value = (currentConnectionState == ConnectionState.AUTHENTICATED)
        
        Log.d(TAG, "刷新登录状态: connectionState=$currentConnectionState, isLoggedIn=${_isLoggedIn.value}")
        
        // 如果已认证，确保没有错误消息
        if (_isLoggedIn.value) {
            _uiState.update { it.copy(errorMessage = null, apiErrorMessage = null) }
        }
    }

    
    override fun onCleared() {
        super.onCleared()
        // 不在这里断开XMPP连接，连接由XMPPManager单例管理
        // 移除之前的 xmppManager.disconnect() 调用
    }
} 