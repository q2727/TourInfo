package com.example.travalms.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travalms.data.remote.XMPPManager
import com.example.travalms.ui.screens.SubscriptionNodeItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 订阅视图模型状态数据类
 */
data class SubscriptionState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val selectedNodeIds: List<String> = emptyList(),
    val subscribedNodeIds: List<String> = emptyList(),
    val errorMessage: String? = null
)

/**
 * 订阅视图模型，简化版，专注于订阅功能
 */
class SubscriptionViewModel : ViewModel() {
    private val xmppManager = XMPPManager.getInstance()
    
    private val _uiState = MutableStateFlow(SubscriptionState())
    val uiState: StateFlow<SubscriptionState> = _uiState.asStateFlow()
    
    private val TAG = "SubscriptionViewModel"
    
    init {
        // 视图模型初始化时，加载已订阅的节点列表
        loadSubscribedNodes()
    }
    
    /**
     * 加载已订阅的节点列表
     */
    private fun loadSubscribedNodes() {
        _uiState.update { it.copy(isLoading = true) }
        
        viewModelScope.launch {
            try {
                val result = xmppManager.getUserSubscriptions()
                if (result.isSuccess) {
                    val subscriptions = result.getOrThrow()
                    val subscribedNodeIds = subscriptions.map { it.node }
                    
                    _uiState.update { it.copy(
                        subscribedNodeIds = subscribedNodeIds,
                        isLoading = false
                    ) }
                    
                    Log.d(TAG, "已加载订阅节点: ${subscribedNodeIds.size}个")
                } else {
                    _uiState.update { it.copy(
                        errorMessage = "加载订阅失败: ${result.exceptionOrNull()?.message}",
                        isLoading = false
                    ) }
                }
            } catch (e: Exception) {
                Log.e(TAG, "加载订阅节点失败", e)
                _uiState.update { it.copy(
                    errorMessage = "加载订阅失败: ${e.message}",
                    isLoading = false
                ) }
            }
        }
    }
    
    /**
     * 订阅选中的节点
     */
    fun subscribeToNodes(selectedNodes: List<String>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            try {
                // 获取当前已订阅的节点
                val currentSubscribed = _uiState.value.subscribedNodeIds.toSet()
                
                // 需要新订阅的节点
                val nodesToSubscribe = selectedNodes.filter { it !in currentSubscribed }
                
                // 需要取消订阅的节点
                val nodesToUnsubscribe = currentSubscribed.filter { it !in selectedNodes }
                
                var hasError = false
                var errorMessage = ""
                
                // 执行订阅操作
                if (nodesToSubscribe.isNotEmpty()) {
                    Log.d(TAG, "开始订阅节点: ${nodesToSubscribe.size}个")
                    val subscribeResult = xmppManager.batchSubscribe(nodesToSubscribe)
                    if (subscribeResult.isSuccess) {
                        val successSubscribed = subscribeResult.getOrNull() ?: emptyList()
                        if (successSubscribed.size < nodesToSubscribe.size) {
                            hasError = true
                            val unsuccessfulCount = nodesToSubscribe.size - successSubscribed.size
                            errorMessage = "订阅部分失败: $unsuccessfulCount 个节点可能不存在或订阅失败"
                            Log.w(TAG, "部分节点订阅失败: 成功=${successSubscribed.size}/${nodesToSubscribe.size}")
                        } else {
                            Log.d(TAG, "成功订阅节点: ${successSubscribed.size}个")
                        }
                    } else {
                        hasError = true
                        errorMessage = "订阅失败: ${subscribeResult.exceptionOrNull()?.message ?: "未知错误"}"
                        Log.e(TAG, errorMessage)
                    }
                }
                
                // 执行取消订阅操作（即使订阅有错误也尝试取消订阅）
                if (nodesToUnsubscribe.isNotEmpty()) {
                    Log.d(TAG, "开始取消订阅节点: ${nodesToUnsubscribe.size}个")
                    val unsubscribeResult = xmppManager.batchUnsubscribe(nodesToUnsubscribe)
                    if (unsubscribeResult.isSuccess) {
                        val successUnsubscribed = unsubscribeResult.getOrNull() ?: emptyList()
                        if (successUnsubscribed.size < nodesToUnsubscribe.size) {
                            val unsubscribeErrorMsg = "部分节点取消订阅失败"
                            if (hasError) {
                                errorMessage += ", $unsubscribeErrorMsg"
                            } else {
                                hasError = true
                                errorMessage = unsubscribeErrorMsg
                            }
                            Log.w(TAG, "部分节点取消订阅失败: 成功=${successUnsubscribed.size}/${nodesToUnsubscribe.size}")
                        } else {
                            Log.d(TAG, "成功取消订阅节点: ${successUnsubscribed.size}个")
                        }
                    } else {
                        val unsubscribeErrorMsg = "取消订阅失败: ${unsubscribeResult.exceptionOrNull()?.message ?: "未知错误"}"
                        if (hasError) {
                            errorMessage += ", $unsubscribeErrorMsg"
                        } else {
                            hasError = true
                            errorMessage = unsubscribeErrorMsg
                        }
                        Log.e(TAG, unsubscribeErrorMsg)
                    }
                }
                
                // 无论是否有错误，都重新加载当前订阅状态，确保UI显示准确的订阅情况
                loadSubscribedNodes()
                
                // 如果有错误，显示错误消息
                if (hasError) {
                    _uiState.update { it.copy(
                        errorMessage = errorMessage,
                        isLoading = false
                    ) }
                } else {
                    // 一切正常，更新本地状态
                    _uiState.update { it.copy(
                        isLoading = false
                    ) }
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "订阅/取消订阅操作失败", e)
                _uiState.update { it.copy(
                    errorMessage = "操作失败: ${e.message}",
                    isLoading = false
                ) }
                
                // 发生异常后，重新加载当前订阅状态
                loadSubscribedNodes()
            }
        }
    }
    
    /**
     * 构建节点ID到名称的映射
     */
    fun buildNodeNameMap(nodes: List<SubscriptionNodeItem>) {
        // 保留此方法签名但不实现细节，因为不再需要处理通知
        Log.d(TAG, "构建节点ID到名称的映射")
    }
    
    /**
     * 设置选中的节点ID列表
     */
    fun setSelectedNodeIds(nodeIds: List<String>) {
        _uiState.update { it.copy(selectedNodeIds = nodeIds) }
        Log.d(TAG, "已选择节点: ${nodeIds.size}个")
    }
    
    /**
     * 清除错误消息
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
    
    override fun onCleared() {
        super.onCleared()
    }
} 