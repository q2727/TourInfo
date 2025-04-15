package com.example.travalms.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travalms.data.remote.XMPPManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jivesoftware.smack.tcp.XMPPTCPConnection

// 定义登录界面的不同状态
sealed interface LoginUiState {
    object Idle : LoginUiState // 初始状态
    object Loading : LoginUiState // 正在登录
    data class Success(val connection: XMPPTCPConnection) : LoginUiState // 登录成功
    data class Error(val message: String) : LoginUiState // 登录失败
}

class LoginViewModel : ViewModel() {

    // 在ViewModel内部创建XMPPManager实例 (同样，更好的方式是依赖注入)
    private val xmppManager = XMPPManager()

    // 使用 StateFlow 暴露 UI 状态
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * 执行登录操作
     */
    fun performLogin(username: String, password: String) {
        // 防止重复登录请求
        if (_uiState.value == LoginUiState.Loading) return

        _uiState.value = LoginUiState.Loading // 更新状态为正在登录

        viewModelScope.launch {
            val result = xmppManager.login(username, password)
            if (result.isSuccess) {
                _uiState.value = LoginUiState.Success(result.getOrThrow())
            } else {
                val exception = result.exceptionOrNull()
                val errorMessage = exception?.message ?: "未知错误"
                 // 可以根据异常类型定制更友好的错误信息
                _uiState.value = LoginUiState.Error(exception?.javaClass?.simpleName ?: errorMessage)
            }
        }
    }

    /**
     * 重置UI状态到初始状态 (例如，在错误提示后或成功导航后)
     */
    fun resetState() {
         if (_uiState.value != LoginUiState.Loading) { // 不要在加载时重置
             _uiState.value = LoginUiState.Idle
         }
    }
} 