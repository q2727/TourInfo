package com.example.travalms.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travalms.data.api.NetworkModule
import com.example.travalms.data.api.UserApiService
import com.example.travalms.ui.screens.TailOrder
import com.example.travalms.ui.viewmodels.MyPublishedTailsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * 尾单详情视图模型状态
 */
data class TailOrderDetailState(
    val isLoading: Boolean = true,
    val tailOrder: TailOrder? = null,
    val error: String? = null,
    val publisherInfo: Map<String, Any>? = null
)

/**
 * 尾单详情视图模型
 */
class TailOrderDetailViewModel(
    private val tailOrderId: String,
    private val application: Application
) : AndroidViewModel(application) {
    private val TAG = "TailOrderDetailVM"
    
    // API服务
    private val userApiService = NetworkModule.userApiService
    
    // 状态流
    private val _state = MutableStateFlow(TailOrderDetailState())
    val state: StateFlow<TailOrderDetailState> = _state.asStateFlow()
    
    init {
        // 初始化时加载尾单详情和发布者信息
        loadTailOrderDetail()
    }
    
    /**
     * 加载尾单详情
     */
    private fun loadTailOrderDetail() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                
                // 首先尝试从MyPublishedTailsViewModel获取尾单详情
                var tailOrder: TailOrder? = null
                try {
                    val myPublishedTailsViewModel = MyPublishedTailsViewModel.getInstance(application)
                    val publishedTails = myPublishedTailsViewModel.uiState.value.publishedTails
                    
                    Log.d(TAG, "尝试从MyPublishedTailsViewModel获取尾单，ID: $tailOrderId, 可用尾单数量: ${publishedTails.size}")
                    
                    // 查找匹配ID的尾单
                    val publishedTail = publishedTails.find { it.id == tailOrderId.toInt() }
                    
                    if (publishedTail != null) {
                        Log.d(TAG, "从MyPublishedTailsViewModel找到尾单: ${publishedTail.title}, 发布者: ${publishedTail.publisher}")
                        
                        // 将PostItem转换为TailOrder
                        tailOrder = TailOrder(
                            id = publishedTail.id,
                            title = publishedTail.title,
                            company = "我的发布",
                            companyId = "my_publish",
                            contactPerson = publishedTail.publisher,
                            contactPersonId = publishedTail.publisher,
                            contactPhone = "",  // 从用户信息中获取
                            price = "¥${publishedTail.price}",
                            remainingDays = publishedTail.daysExpired.toString(),
                            remainingHours = "0:00",
                            content = listOf(publishedTail.feature),
                            summary = publishedTail.feature,
                            isFavorite = false,
                            publisherJid = publishedTail.publisher, // 使用发布者用户名
                            productId = publishedTail.productId?.toLong(),  // 添加产品ID
                            productTitle = publishedTail.productTitle  // 添加产品标题
                        )
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "获取MyPublishedTailsViewModel实例失败", e)
                }
                
                // 如果在MyPublishedTailsViewModel中找不到，尝试从TailListViewModel获取
                if (tailOrder == null) {
                    try {
                        val tailListViewModel = TailListViewModel.getInstance()
                        val tailOrders = tailListViewModel.state.value.tailOrders
                        
                        Log.d(TAG, "尝试从TailListViewModel获取尾单，ID: $tailOrderId, 可用尾单数量: ${tailOrders.size}")
                        
                        // 查找匹配ID的尾单
                        tailOrder = tailOrders.find { it.id == tailOrderId.toInt() }
                        
                        if (tailOrder != null) {
                            Log.d(TAG, "从TailListViewModel找到尾单: ${tailOrder.title}, 发布者JID: ${tailOrder.publisherJid}")
                            Log.d(TAG, "尾单产品信息: ID=${tailOrder.id}, 产品ID=${tailOrder.productId}, 产品标题=${tailOrder.productTitle}")
                        } else {
                            Log.w(TAG, "在TailListViewModel中找不到ID为 $tailOrderId 的尾单，尝试回退到模拟数据")
                            // 如果在TailListViewModel中找不到，使用模拟数据作为备用
                            tailOrder = getBackupTailOrderData(tailOrderId.toInt())
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "获取TailListViewModel实例失败，使用备用数据", e)
                        // 如果获取TailListViewModel实例失败，使用模拟数据作为备用
                        tailOrder = getBackupTailOrderData(tailOrderId.toInt())
                    }
                }
                
                if (tailOrder != null) {
                    _state.update { it.copy(tailOrder = tailOrder) }
                    
                    // 如果有发布者JID，加载发布者信息
                    if (tailOrder.publisherJid.isNotEmpty()) {
                        loadPublisherInfo(tailOrder.publisherJid)
                    } else {
                        // 没有发布者JID，结束加载
                        _state.update { it.copy(isLoading = false) }
                    }
                } else {
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            error = "找不到尾单信息"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "加载尾单详情失败", e)
                _state.update { 
                    it.copy(
                        isLoading = false,
                        error = "加载尾单详情失败: ${e.message}"
                    )
                }
            }
        }
    }
    
    /**
     * 备用数据 - 当从TailListViewModel无法获取数据时使用
     */
    private fun getBackupTailOrderData(id: Int): TailOrder {
        return TailOrder(
            id = id,
            title = "备用数据: 尾单详情",
            company = "系统提供的备用数据",
            companyId = "backup_company",
            contactPerson = "系统",
            contactPersonId = "system",
            contactPhone = "无可用联系方式",
            price = "¥0",
            remainingDays = "0",
            remainingHours = "0:00",
            content = listOf(
                "此为备用数据，无法从尾单列表中获取实际数据。",
                "可能是由于尾单ID不匹配或尾单列表尚未加载完成。"
            ),
            summary = "备用数据",
            isFavorite = false,
            publisherJid = "system@localhost",
            productId = null,  // 备用数据无产品ID
            productTitle = null  // 备用数据无产品标题
        )
    }
    
    /**
     * 加载发布者信息
     */
    private fun loadPublisherInfo(publisherJid: String) {
        viewModelScope.launch {
            try {
                // 从JID中提取用户名部分
                val username = if (publisherJid.contains("@")) {
                    publisherJid.substringBefore("@")
                } else {
                    publisherJid
                }
                
                Log.d(TAG, "从JID提取的用户名: $username, 原始JID: $publisherJid")
                
                // 调用API获取用户信息
                val response = userApiService.getUserInfo(username)
                
                if (response.isSuccessful && response.body() != null) {
                    val publisherInfo = response.body()!!
                    _state.update { 
                        it.copy(
                            publisherInfo = publisherInfo,
                            isLoading = false
                        )
                    }
                    Log.d(TAG, "成功获取发布者信息: $username")
                } else {
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            error = "获取发布者信息失败: ${response.code()}"
                        )
                    }
                    Log.e(TAG, "获取发布者信息失败: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "获取发布者信息异常", e)
                _state.update { 
                    it.copy(
                        isLoading = false,
                        error = "获取发布者信息异常: ${e.message}"
                    )
                }
            }
        }
    }
    
    /**
     * 删除尾单
     */
    fun deleteTailOrder() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                
                // 模拟删除操作
                delay(500)
                
                // 删除成功
                _state.update { 
                    it.copy(
                        isLoading = false,
                        tailOrder = null
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "删除尾单失败", e)
                _state.update { 
                    it.copy(
                        isLoading = false,
                        error = "删除尾单失败: ${e.message}"
                    )
                }
            }
        }
    }
    
    /**
     * 举报尾单
     */
    fun reportTailOrder(reason: String) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                
                // 模拟举报操作
                delay(500)
                
                // 举报成功
                _state.update { it.copy(isLoading = false) }
                
                Log.d(TAG, "尾单举报成功: $tailOrderId, 原因: $reason")
            } catch (e: Exception) {
                Log.e(TAG, "举报尾单失败", e)
                _state.update { 
                    it.copy(
                        isLoading = false,
                        error = "举报尾单失败: ${e.message}"
                    )
                }
            }
        }
    }
    
    /**
     * ViewModel Factory
     */
    class Factory(private val tailOrderId: String, private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TailOrderDetailViewModel::class.java)) {
                return TailOrderDetailViewModel(tailOrderId, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 