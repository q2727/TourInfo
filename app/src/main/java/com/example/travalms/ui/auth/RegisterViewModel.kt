package com.example.travalms.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travalms.data.remote.XMPPManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 定义注册界面的不同状态
sealed interface RegisterUiState {
    object Idle : RegisterUiState // 初始状态
    object Loading : RegisterUiState // 正在注册
    object Success : RegisterUiState // 注册成功
    data class Error(val message: String) : RegisterUiState // 注册失败
}

class RegisterViewModel : ViewModel() {

    // 使用单例模式
    private val xmppManager = XMPPManager.getInstance()

    // 使用 StateFlow 暴露 UI 状态
    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    /**
     * 执行注册操作
     */
    fun performRegister(
        username: String, 
        password: String, 
        nickname: String?, 
        email: String?,
        companyName: String,
        mobileNumber: String,
        province: String,
        city: String,
        businessLicensePath: String? = null,
        idCardFrontPath: String? = null,
        idCardBackPath: String? = null
    ) {
        // 防止重复注册请求
        if (_uiState.value == RegisterUiState.Loading) return

        _uiState.value = RegisterUiState.Loading // 更新状态为正在注册

        viewModelScope.launch {
            // 简单验证 (可以添加更复杂的验证逻辑)
            if (username.isBlank() || password.isBlank()) {
                 _uiState.value = RegisterUiState.Error("用户名和密码不能为空")
                 return@launch
            }
            if (companyName.isBlank()) {
                _uiState.value = RegisterUiState.Error("公司名称不能为空")
                return@launch
            }
            if (mobileNumber.isBlank()) {
                _uiState.value = RegisterUiState.Error("手机号不能为空")
                return@launch
            }
            if (province.isBlank() || city.isBlank()) {
                _uiState.value = RegisterUiState.Error("所在地不能为空")
                return@launch
            }
            // 可以在这里添加更多验证逻辑

            val result = xmppManager.register(
                username = username, 
                password = password, 
                nickname = nickname, 
                email = email,
                companyName = companyName,
                mobileNumber = mobileNumber,
                province = province,
                city = city,
                businessLicensePath = businessLicensePath,
                idCardFrontPath = idCardFrontPath,
                idCardBackPath = idCardBackPath
            )
            
            if (result.isSuccess) {
                _uiState.value = RegisterUiState.Success
            } else {
                val exception = result.exceptionOrNull()
                val errorMessage = exception?.message ?: "未知错误"
                // 根据异常类型定制更友好的错误信息
                _uiState.value = RegisterUiState.Error(exception?.javaClass?.simpleName ?: errorMessage)
            }
        }
    }

     /**
     * 重置UI状态到初始状态
     */
    fun resetState() {
         if (_uiState.value != RegisterUiState.Loading) {
             _uiState.value = RegisterUiState.Idle
         }
    }
} 