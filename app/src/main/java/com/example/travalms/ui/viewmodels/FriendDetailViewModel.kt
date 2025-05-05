package com.example.travalms.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travalms.data.api.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 好友详情页面状态
 */
data class FriendDetailState(
    val isLoading: Boolean = true,
    val userInfo: Map<String, Any>? = null,
    val error: String? = null
)

/**
 * 好友详情页面ViewModel
 */
class FriendDetailViewModel(
    private val username: String,
    private val application: Application
) : AndroidViewModel(application) {
    private val TAG = "FriendDetailVM"
    
    // API服务
    private val userApiService = NetworkModule.userApiService
    
    // 状态流
    private val _state = MutableStateFlow(FriendDetailState())
    val state: StateFlow<FriendDetailState> = _state.asStateFlow()
    
    init {
        loadUserInfo()
    }
    
    /**
     * 加载用户信息
     */
    private fun loadUserInfo() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                
                // 确保使用纯用户名（去掉可能存在的 @domain 后缀）
                val cleanUsername = username.substringBefore("@")
                Log.d(TAG, "开始获取用户信息: $cleanUsername")
                val response = userApiService.getUserInfo(cleanUsername)
                
                if (response.isSuccessful && response.body() != null) {
                    val userInfo = response.body()!!
                    Log.d(TAG, "成功获取用户信息: $userInfo")
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            userInfo = userInfo,
                            error = null
                        )
                    }
                } else {
                    Log.e(TAG, "获取用户信息失败: ${response.code()}")
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            error = "获取用户信息失败: ${response.code()}"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "获取用户信息异常", e)
                _state.update { 
                    it.copy(
                        isLoading = false,
                        error = "获取用户信息异常: ${e.message}"
                    )
                }
            }
        }
    }
    
    /**
     * ViewModel工厂类
     */
    class Factory(
        private val username: String,
        private val application: Application
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FriendDetailViewModel::class.java)) {
                return FriendDetailViewModel(username, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 