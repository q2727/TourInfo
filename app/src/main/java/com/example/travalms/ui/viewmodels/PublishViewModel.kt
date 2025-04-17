package com.example.travalms.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travalms.data.remote.XMPPManager
import com.example.travalms.data.remote.PubSubNotification
import com.example.travalms.ui.screens.SubscriptionNodeItem
import com.example.travalms.ui.screens.SubscriptionNodeType
import com.example.travalms.data.remote.ConnectionState
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
    val selectedNodeNames: List<String> = emptyList()
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
    val tags: List<String> = emptyList()
)

/**
 * 发布视图模型，处理尾单的发布
 */
class PublishViewModel : ViewModel() {
    // 使用XMPPManager的单例实例
    private val xmppManager = XMPPManager.getInstance()
    
    private val _uiState = MutableStateFlow(PublishState())
    val uiState: StateFlow<PublishState> = _uiState.asStateFlow()
    
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()
    
    val connectionState: StateFlow<ConnectionState> = xmppManager.connectionState
    
    private val TAG = "PublishViewModel"
    
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
                    _uiState.update { it.copy(errorMessage = null) }
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
            _uiState.update { state -> state.copy(errorMessage = "未登录到XMPP服务器") }
            return
        }
        
        if (_uiState.value.selectedNodeIds.isEmpty()) {
            _uiState.update { state -> state.copy(errorMessage = "未选择发布节点") }
            return
        }
        
        _uiState.update { state -> state.copy(isPublishing = true, publishSuccess = false, errorMessage = null) }
        
        viewModelScope.launch {
            try {
                // 创建尾单内容的JSON
                val content = createTailListJson(tailListItem)
                
                // 计算发布成功的节点数
                var successCount = 0
                
                // 发布到所有选择的节点
                for (nodeId in _uiState.value.selectedNodeIds) {
                    try {
                        val result = xmppManager.publishToNode(nodeId, content)
                        if (result.isSuccess) {
                            successCount++
                            Log.d(TAG, "尾单发布成功到节点: $nodeId, itemId=${result.getOrNull()}")
                        } else {
                            Log.e(TAG, "尾单发布到节点 $nodeId 失败: ${result.exceptionOrNull()?.message}")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "发布到节点 $nodeId 时出错", e)
                    }
                }
                
                // 更新UI状态
                _uiState.update { state -> 
                    state.copy(
                        isPublishing = false,
                        publishSuccess = successCount > 0,
                        errorMessage = if (successCount == 0) "发布失败，请重试" else null
                    )
                }
                
                Log.d(TAG, "尾单发布完成: 成功=${successCount}/${_uiState.value.selectedNodeIds.size}")
            } catch (e: Exception) {
                _uiState.update { state -> 
                    state.copy(
                        isPublishing = false,
                        publishSuccess = false,
                        errorMessage = "发布出错: ${e.message}"
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
                errorMessage = null
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
            _uiState.update { it.copy(errorMessage = null) }
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        // 不在这里断开XMPP连接，连接由XMPPManager单例管理
        // 移除之前的 xmppManager.disconnect() 调用
    }
} 