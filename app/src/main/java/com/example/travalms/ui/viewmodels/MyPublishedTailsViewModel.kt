package com.example.travalms.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travalms.api.dto.TailOrderResponse
import com.example.travalms.data.api.NetworkModule
import com.example.travalms.data.remote.ConnectionState
import com.example.travalms.data.remote.XMPPManager
import com.example.travalms.ui.screens.PostItem
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
class MyPublishedTailsViewModel : ViewModel() {
    
    // XMPP管理器
    private val xmppManager = XMPPManager.getInstance()
    
    // API服务
    private val tailOrderApiService = NetworkModule.tailOrderApiService
    
    // UI状态
    private val _uiState = MutableStateFlow(MyPublishedTailsState())
    val uiState: StateFlow<MyPublishedTailsState> = _uiState.asStateFlow()
    
    // 静态实例，用于其他ViewModel访问
    companion object {
        private var instance: MyPublishedTailsViewModel? = null
        
        fun getInstance(): MyPublishedTailsViewModel {
            if (instance == null) {
                instance = MyPublishedTailsViewModel()
                instance?.loadUserPublishedTails()
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
                    // 解析响应数据并转换为UI模型
                    val tailOrders = response.body()!!
                    val postItems = convertToPostItems(tailOrders)
                    
                    _uiState.update { state -> 
                        state.copy(publishedTails = postItems, isLoading = false, errorMessage = null) 
                    }
                    Log.d(TAG, "成功加载用户发布历史: ${tailOrders.size} 条记录")
                } else {
                    // 处理API错误
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
                // 处理异常
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
        
        return tailOrders.mapIndexed { index, tailOrder ->
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
            
            // 创建UI对象
            PostItem(
                id = tailOrder.id.toInt(),
                title = tailOrder.title,
                dates = startDateStr,
                feature = tailOrder.itinerary,
                remainingSlots = 10, // 假设值，后端没有提供
                price = price,
                daysExpired = daysExpired,
                publisher = contactPerson,
                publishLocations = publishLocations
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
    
    // 初始加载数据
    init {
        loadUserPublishedTails()
    }
} 