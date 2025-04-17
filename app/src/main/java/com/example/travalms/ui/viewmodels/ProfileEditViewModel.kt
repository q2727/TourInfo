package com.example.travalms.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travalms.data.remote.XMPPManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 定义个人资料界面的不同状态
sealed interface ProfileEditUiState {
    object Loading : ProfileEditUiState // 正在加载
    data class Success(
        val userId: String = "",
        val username: String = "",
        val nickname: String = "",
        val email: String = "",
        val phone: String = "",
        val qq: String = "",
        val wechat: String = "",
        val companyName: String = "",
        val province: String? = null,
        val city: String? = null,
        val introduction: String = "",
        val isVerified: Boolean = false
    ) : ProfileEditUiState // 加载成功
    data class Error(val message: String) : ProfileEditUiState // 加载失败
}

class ProfileEditViewModel : ViewModel() {
    private val xmppManager = XMPPManager.getInstance()
    
    // 使用 StateFlow 暴露 UI 状态
    private val _uiState = MutableStateFlow<ProfileEditUiState>(ProfileEditUiState.Loading)
    val uiState: StateFlow<ProfileEditUiState> = _uiState.asStateFlow()

    init {
        // 初始化时加载用户资料
        loadUserProfile()
    }
    
    /**
     * 从XMPP服务器加载用户资料
     */
    fun loadUserProfile() {
        _uiState.value = ProfileEditUiState.Loading
        
        viewModelScope.launch {
            try {
                val profileResult = xmppManager.getUserProfile()
                
                if (profileResult.isSuccess) {
                    val profileData = profileResult.getOrThrow()
                    Log.d("ProfileEditViewModel", "获取到用户资料：$profileData")
                    
                    // 转换为UI状态
                    val userId = profileData["userId"] ?: ""
                    val username = profileData["username"] ?: ""
                    val nickname = profileData["name"] ?: "" // 使用标准字段"name"作为昵称
                    val email = profileData["email"] ?: ""
                    val phone = profileData["mobileNumber"] ?: ""
                    val qq = profileData["qq"] ?: ""
                    val wechat = profileData["wechat"] ?: ""
                    val companyName = profileData["companyName"] ?: ""
                    val province = profileData["province"]
                    val city = profileData["city"]
                    val introduction = profileData["introduction"] ?: ""
                    // 检查是否有实名认证标志
                    val isVerified = profileData["verified"]?.toBoolean() ?: false
                    
                    _uiState.value = ProfileEditUiState.Success(
                        userId = userId,
                        username = username,
                        nickname = nickname,
                        email = email,
                        phone = phone,
                        qq = qq,
                        wechat = wechat,
                        companyName = companyName,
                        province = province,
                        city = city,
                        introduction = introduction,
                        isVerified = isVerified
                    )
                } else {
                    val exception = profileResult.exceptionOrNull()
                    val errorMessage = exception?.message ?: "未知错误"
                    _uiState.value = ProfileEditUiState.Error("获取用户资料失败: $errorMessage")
                }
            } catch (e: Exception) {
                _uiState.value = ProfileEditUiState.Error("获取用户资料出错: ${e.message}")
            }
        }
    }
    
    /**
     * 更新用户资料
     * @param updatedInfo 更新的字段信息Map
     */
    fun updateUserProfile(updatedInfo: Map<String, String?>) {
        viewModelScope.launch {
            // TODO: 实现调用XMPPManager更新用户资料的方法
            // 现在只是简单地将成功状态与更新后的字段合并
            when (val currentState = _uiState.value) {
                is ProfileEditUiState.Success -> {
                    // 创建当前状态的副本，更新需要修改的字段
                    val updatedState = currentState.copy(
                        nickname = updatedInfo["nickname"] ?: currentState.nickname,
                        email = updatedInfo["email"] ?: currentState.email,
                        phone = updatedInfo["phone"] ?: currentState.phone,
                        qq = updatedInfo["qq"] ?: currentState.qq,
                        wechat = updatedInfo["wechat"] ?: currentState.wechat,
                        companyName = updatedInfo["companyName"] ?: currentState.companyName,
                        province = updatedInfo["province"] ?: currentState.province,
                        city = updatedInfo["city"] ?: currentState.city,
                        introduction = updatedInfo["introduction"] ?: currentState.introduction
                    )
                    _uiState.value = updatedState
                }
                else -> {
                    // 如果当前不是成功状态，则不做任何操作
                    Log.w("ProfileEditViewModel", "尝试更新资料时，UI不在成功状态")
                }
            }
        }
    }
} 