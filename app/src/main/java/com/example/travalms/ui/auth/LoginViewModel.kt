package com.example.travalms.ui.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travalms.data.remote.XMPPManager
import com.example.travalms.data.remote.XMPPService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jivesoftware.smack.tcp.XMPPTCPConnection

// 定义登录界面的不同状态
sealed interface LoginUiState {
    object Idle : LoginUiState // 初始状态
    object Loading : LoginUiState // 正在登录
    object Success : LoginUiState // 登录成功 - 修改为不需要Connection参数
    data class Error(val message: String) : LoginUiState // 登录失败
}

class LoginViewModel : ViewModel() {

    // 使用单例模式
    private val xmppManager = XMPPManager.getInstance()
    
    // 保存上下文引用
    private var applicationContext: Context? = null

    // 使用 StateFlow 暴露 UI 状态
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * 设置应用上下文
     * 通常在构造ViewModel时从主Activity或应用类获得
     */
    fun setContext(context: Context) {
        this.applicationContext = context.applicationContext
    }

    /**
     * 执行登录操作
     * 强制重新登录，即使已经登录了相同账号
     */
    fun performLogin(username: String, password: String) {
        // 防止重复登录请求
        if (_uiState.value == LoginUiState.Loading) return

        _uiState.value = LoginUiState.Loading // 更新状态为正在登录

        viewModelScope.launch {
            // 强制重新登录，即使用户已经登录
            val result = xmppManager.login(username, password, forceLogin = true)
            if (result.isSuccess) {
                // 保存登录凭据，用于后续自动重连
                applicationContext?.let { context ->
                    xmppManager.saveCredentials(context, username, password)
                    
                    // 确保XMPPService正在运行
                    XMPPService.startService(context)
                    
                    // 主动加入用户所在的群聊
                    try {
                        // 给一些时间让连接完全建立
                        delay(1000)
                        // 主动触发加入群聊
                        context.startService(Intent(context, XMPPService::class.java).apply {
                            action = XMPPService.ACTION_JOIN_GROUP_CHATS
                        })
                    } catch (e: Exception) {
                        // 即使加入群聊操作失败，也不影响登录成功
                        Log.e("LoginViewModel", "触发加入群聊失败: ${e.message}", e)
                    }
                }
                
                _uiState.value = LoginUiState.Success // 修改为使用不带参数的Success
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
    
    /**
     * 退出登录
     */
    fun logout() {
        viewModelScope.launch {
            // 断开XMPP连接
            xmppManager.disconnect()
            
            // 清除保存的凭据
            applicationContext?.let { context ->
                xmppManager.clearCredentials(context)
                
                // 停止XMPP服务
                XMPPService.stopService(context)
            }
            
            // 重置状态
            _uiState.value = LoginUiState.Idle
        }
    }
} 