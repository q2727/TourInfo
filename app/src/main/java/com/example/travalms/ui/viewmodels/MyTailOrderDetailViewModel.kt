package com.example.travalms.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travalms.api.dto.TailOrderResponse
import com.example.travalms.data.api.NetworkModule
import com.example.travalms.data.api.TailOrderApiService
import com.example.travalms.data.remote.XMPPManager
import com.example.travalms.ui.screens.TailOrder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "MyTailOrderDetailVM"

/**
 * 我的尾单详情视图模型状态
 */
data class MyTailOrderDetailState(
    val isLoading: Boolean = true,
    val tailOrder: TailOrder? = null,
    val error: String? = null
)

/**
 * 我的尾单详情视图模型
 * 专门用于显示当前用户发布的尾单详情
 */
class MyTailOrderDetailViewModel(
    private val tailOrderId: String,
    private val application: Application
) : AndroidViewModel(application) {
    
    // API服务
    private val tailOrderApiService = NetworkModule.tailOrderApiService
    
    // 状态流
    private val _state = MutableStateFlow(MyTailOrderDetailState())
    val state: StateFlow<MyTailOrderDetailState> = _state.asStateFlow()
    
    init {
        // 初始化时加载尾单详情
        loadMyTailOrderDetail()
    }
    
    /**
     * 加载我的尾单详情
     */
    private fun loadMyTailOrderDetail() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                
                // 从MyPublishedTailsViewModel获取原始尾单数据
                val myPublishedTailsViewModel = MyPublishedTailsViewModel.getInstance()
                val origTailOrderId = tailOrderId.toLong()
                var origTailOrder = myPublishedTailsViewModel.getOriginalTailOrderById(origTailOrderId)
                
                if (origTailOrder != null) {
                    Log.d(TAG, "从原始数据中找到尾单: ID=${origTailOrder.id}, 标题=${origTailOrder.title}")
                    
                    // 记录产品信息
                    Log.d(TAG, "原始尾单产品信息: 产品ID=${origTailOrder.productId}, 产品标题=${origTailOrder.productTitle}")
                    
                    // 解析productDetails JSON字符串
                    val productDetails = try {
                        JSONObject(origTailOrder.productDetails)
                    } catch (e: Exception) {
                        JSONObject()
                    }
                    
                    // 提取数据
                    val price = productDetails.optInt("price", 0)
                    val startDateStr = productDetails.optString("startDate", "")
                    val contactPerson = productDetails.optString("contactPerson", "")
                    val contactPhone = productDetails.optString("contactPhone", "")
                    
                    // 从JID中提取用户名
                    val publisherJid = origTailOrder.jid
                    val publisherName = if (publisherJid.contains("@")) {
                        publisherJid.substringBefore("@")
                    } else {
                        publisherJid
                    }
                    
                    // 计算剩余天数
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    val remainingDays = try {
                        val expiryDate = dateFormat.parse(origTailOrder.expiryDate)
                        val currentDate = Date()
                        val diffInMillis = expiryDate.time - currentDate.time
                        val diffInDays = if (diffInMillis > 0) {
                            java.util.concurrent.TimeUnit.MILLISECONDS.toDays(diffInMillis)
                        } else {
                            0
                        }
                        diffInDays.toString()
                    } catch (e: Exception) {
                        Log.e(TAG, "解析截止日期失败: ${origTailOrder.expiryDate}", e)
                        "0"
                    }
                    
                    // 将原始数据转换为TailOrder
                    val tailOrder = TailOrder(
                        id = origTailOrder.id.toInt(),
                        title = origTailOrder.title,
                        company = "我的发布", 
                        companyId = "my_publish",
                        contactPerson = contactPerson,
                        contactPersonId = publisherName,
                        contactPhone = contactPhone,
                        price = "¥$price",
                        remainingDays = remainingDays,
                        remainingHours = "0:00",
                        content = listOf(origTailOrder.itinerary),
                        summary = origTailOrder.itinerary,
                        isFavorite = false,
                        publisherJid = origTailOrder.jid,
                        productId = origTailOrder.productId ?: 0L,
                        productTitle = origTailOrder.productTitle ?: ""
                    )
                    
                    _state.update { it.copy(tailOrder = tailOrder, isLoading = false) }
                } else {
                    // 如果没有找到原始数据，尝试从UI列表查找
                    val publishedTails = myPublishedTailsViewModel.uiState.value.publishedTails
                    val publishedTail = publishedTails.find { it.id == tailOrderId.toInt() }
                    
                    if (publishedTail != null) {
                        Log.d(TAG, "从MyPublishedTailsViewModel UI列表找到尾单: ${publishedTail.title}")
                        
                        // 记录产品信息日志
                        Log.d(TAG, "尾单产品信息: ID=${publishedTail.id}, 产品ID=${publishedTail.productId}, 产品标题=${publishedTail.productTitle}")
                        
                        // 将PostItem转换为TailOrder
                        val tailOrder = TailOrder(
                            id = publishedTail.id,
                            title = publishedTail.title,
                            company = "我的发布",
                            companyId = "my_publish",
                            contactPerson = publishedTail.publisher,
                            contactPersonId = publishedTail.publisher,
                            contactPhone = "",
                            price = publishedTail.price,
                            remainingDays = publishedTail.daysExpired.toString(),
                            remainingHours = "0:00",
                            content = listOf(publishedTail.feature),
                            summary = publishedTail.feature,
                            isFavorite = false,
                            publisherJid = publishedTail.publisher,
                            productId = publishedTail.productId?.toLong() ?: 0L,
                            productTitle = publishedTail.productTitle ?: ""
                        )
                        
                        _state.update { it.copy(tailOrder = tailOrder, isLoading = false) }
                    } else {
                        // 在缓存和UI列表中都找不到尾单，尝试通过API直接获取
                        Log.d(TAG, "在缓存中未找到尾单，尝试直接从API获取：ID=$tailOrderId")
                        
                        try {
                            // 获取当前用户的JID
                            val currentUserJid = XMPPManager.getInstance().currentConnection?.user?.asEntityBareJidString()
                            
                            if (currentUserJid != null) {
                                // 调用API获取尾单
                                val userTailOrders = tailOrderApiService.getUserTailOrders(currentUserJid)
                                
                                if (userTailOrders.isSuccessful && userTailOrders.body() != null) {
                                    // 查找指定ID的尾单
                                    origTailOrder = userTailOrders.body()!!.find { it.id == origTailOrderId }
                                    
                                    if (origTailOrder != null) {
                                        Log.d(TAG, "从API中找到尾单: ID=${origTailOrder.id}, 标题=${origTailOrder.title}")
                                        
                                        // 更新MyPublishedTailsViewModel中的原始数据
                                        val existingData = myPublishedTailsViewModel.originalTailOrders.value.toMutableMap()
                                        existingData[origTailOrderId] = origTailOrder
                                        myPublishedTailsViewModel.updateOriginalTailOrders(existingData)
                                        
                                        // 解析productDetails JSON字符串
                                        val productDetails = try {
                                            JSONObject(origTailOrder.productDetails)
                                        } catch (e: Exception) {
                                            JSONObject()
                                        }
                                        
                                        // 提取数据
                                        val price = productDetails.optInt("price", 0)
                                        val startDateStr = productDetails.optString("startDate", "")
                                        val contactPerson = productDetails.optString("contactPerson", "")
                                        val contactPhone = productDetails.optString("contactPhone", "")
                                        
                                        // 从JID中提取用户名
                                        val publisherJid = origTailOrder.jid
                                        val publisherName = if (publisherJid.contains("@")) {
                                            publisherJid.substringBefore("@")
                                        } else {
                                            publisherJid
                                        }
                                        
                                        // 计算剩余天数
                                        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                                        val remainingDays = try {
                                            val expiryDate = dateFormat.parse(origTailOrder.expiryDate)
                                            val currentDate = Date()
                                            val diffInMillis = expiryDate.time - currentDate.time
                                            val diffInDays = if (diffInMillis > 0) {
                                                java.util.concurrent.TimeUnit.MILLISECONDS.toDays(diffInMillis)
                                            } else {
                                                0
                                            }
                                            diffInDays.toString()
                                        } catch (e: Exception) {
                                            Log.e(TAG, "解析截止日期失败: ${origTailOrder.expiryDate}", e)
                                            "0"
                                        }
                                        
                                        // 将原始数据转换为TailOrder
                                        val tailOrder = TailOrder(
                                            id = origTailOrder.id.toInt(),
                                            title = origTailOrder.title,
                                            company = "我的发布", 
                                            companyId = "my_publish",
                                            contactPerson = contactPerson,
                                            contactPersonId = publisherName,
                                            contactPhone = contactPhone,
                                            price = "¥$price",
                                            remainingDays = remainingDays,
                                            remainingHours = "0:00",
                                            content = listOf(origTailOrder.itinerary),
                                            summary = origTailOrder.itinerary,
                                            isFavorite = false,
                                            publisherJid = origTailOrder.jid,
                                            productId = origTailOrder.productId ?: 0L,
                                            productTitle = origTailOrder.productTitle ?: ""
                                        )
                                        
                                        _state.update { it.copy(tailOrder = tailOrder, isLoading = false) }
                                    } else {
                                        // API中也找不到尾单
                                        _state.update { 
                                            it.copy(
                                                isLoading = false,
                                                error = "找不到尾单信息（API查询无结果）"
                                            )
                                        }
                                        return@launch
                                    }
                                } else {
                                    // API调用失败
                                    _state.update { 
                                        it.copy(
                                            isLoading = false,
                                            error = "获取尾单信息失败: ${userTailOrders.code()}"
                                        )
                                    }
                                    return@launch
                                }
                            } else {
                                // 无法获取用户JID
                                _state.update { 
                                    it.copy(
                                        isLoading = false,
                                        error = "无法获取用户信息，请重新登录"
                                    )
                                }
                                return@launch
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "从API获取尾单详情失败", e)
                            _state.update { 
                                it.copy(
                                    isLoading = false,
                                    error = "获取尾单详情失败: ${e.message}"
                                )
                            }
                            return@launch
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "加载我的尾单详情失败", e)
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
     * 删除尾单
     */
    fun deleteTailOrder() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                
                // 执行删除操作
                val response = tailOrderApiService.deleteTailOrder(tailOrderId.toLong())
                
                if (response.isSuccessful) {
                    // 删除成功，更新UI状态
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            tailOrder = null
                        )
                    }
                    Log.d(TAG, "尾单删除成功: $tailOrderId")
                    
                    // 通知MyPublishedTailsViewModel刷新数据
                    refreshMyPublishedTails()
                } else {
                    // 删除失败
                    val errorBody = response.errorBody()?.string() ?: "未知错误"
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            error = "删除失败: ${response.code()} - $errorBody"
                        )
                    }
                    Log.e(TAG, "尾单删除失败: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                // 处理异常
                Log.e(TAG, "删除尾单时出现异常", e)
                _state.update { 
                    it.copy(
                        isLoading = false,
                        error = "删除尾单时出错: ${e.message}"
                    )
                }
            }
        }
    }
    
    /**
     * 刷新我的发布列表
     */
    private fun refreshMyPublishedTails() {
        try {
            val myPublishedTailsViewModel = MyPublishedTailsViewModel.getInstance()
            myPublishedTailsViewModel.loadUserPublishedTails()
        } catch (e: Exception) {
            Log.e(TAG, "刷新我的发布列表失败", e)
        }
    }
    
    /**
     * ViewModel Factory
     */
    class Factory(private val tailOrderId: String, private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MyTailOrderDetailViewModel::class.java)) {
                return MyTailOrderDetailViewModel(tailOrderId, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 