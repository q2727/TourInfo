package com.example.travalms.ui.auth

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travalms.data.remote.XMPPManager
import com.example.travalms.data.api.UserApiService
import com.example.travalms.data.api.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

// 定义注册界面的不同状态
sealed interface RegisterUiState {
    object Idle : RegisterUiState // 初始状态
    object Loading : RegisterUiState // 正在注册
    object Success : RegisterUiState // 注册成功
    data class Error(val message: String) : RegisterUiState // 注册失败
}

class RegisterViewModel : ViewModel() {
    private val TAG = "RegisterViewModel"

    // 使用单例模式
    private val xmppManager = XMPPManager.getInstance()
    
    // 创建API服务
    private val userApiService = NetworkModule.provideUserApiService()

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
        idCardBackPath: String? = null,
        avatarPath: String? = null
    ) {
        // 防止重复注册请求
        if (_uiState.value == RegisterUiState.Loading) return

        _uiState.value = RegisterUiState.Loading // 更新状态为正在注册
        Log.d(TAG, "开始注册流程: username=$username, email=$email, companyName=$companyName")
        
        // 记录文件路径信息
        Log.d(TAG, "注册文件路径信息: 营业执照=${businessLicensePath ?: "无"}, 身份证正面=${idCardFrontPath ?: "无"}, 身份证背面=${idCardBackPath ?: "无"}")

        viewModelScope.launch {
            // 简单验证 (可以添加更复杂的验证逻辑)
            if (username.isBlank() || password.isBlank()) {
                 _uiState.value = RegisterUiState.Error("用户名和密码不能为空")
                 Log.e(TAG, "注册验证失败: 用户名和密码不能为空")
                 return@launch
            }
            if (companyName.isBlank()) {
                _uiState.value = RegisterUiState.Error("公司名称不能为空")
                Log.e(TAG, "注册验证失败: 公司名称不能为空")
                return@launch
            }
            if (mobileNumber.isBlank()) {
                _uiState.value = RegisterUiState.Error("手机号不能为空")
                Log.e(TAG, "注册验证失败: 手机号不能为空")
                return@launch
            }
            if (province.isBlank() || city.isBlank()) {
                _uiState.value = RegisterUiState.Error("所在地不能为空")
                Log.e(TAG, "注册验证失败: 所在地不能为空")
                return@launch
            }
            
            try {
                Log.d(TAG, "开始向后端API注册用户")
                // 1. 先调用后端API注册
                registerToBackend(
                    username, 
                    password, 
                    nickname, 
                    email, 
                    companyName, 
                    mobileNumber, 
                    province, 
                    city, 
                    businessLicensePath, 
                    idCardFrontPath, 
                    idCardBackPath,
                    avatarPath
                )
                Log.d(TAG, "后端API注册成功，开始XMPP账号注册")
                
                // 2. 然后注册XMPP账号
                val xmppResult = xmppManager.register(
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
                    idCardBackPath = idCardBackPath,
                    avatarPath = avatarPath
                )
                
                if (xmppResult.isSuccess) {
                    Log.d(TAG, "XMPP账号注册成功")
                    _uiState.value = RegisterUiState.Success
                } else {
                    val exception = xmppResult.exceptionOrNull()
                    val errorMessage = exception?.message ?: "未知错误"
                    Log.e(TAG, "XMPP账号注册失败: $errorMessage", exception)
                    _uiState.value = RegisterUiState.Error(exception?.javaClass?.simpleName ?: errorMessage)
                }
            } catch (e: Exception) {
                Log.e(TAG, "注册过程发生异常: ${e.message}", e)
                _uiState.value = RegisterUiState.Error("注册失败: ${e.message}")
            }
        }
    }
    
    /**
     * 向后端API注册用户
     */
    private suspend fun registerToBackend(
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
        idCardBackPath: String? = null,
        avatarPath: String? = null
    ) {
        try {
            // 创建文本部分参数映射
            val params = mutableMapOf<String, RequestBody>().apply {
                put("username", username.toRequestBody("text/plain".toMediaTypeOrNull()))
                put("password", password.toRequestBody("text/plain".toMediaTypeOrNull()))
                
                // 处理可能为空的参数
                if (!nickname.isNullOrBlank()) {
                    put("nickname", nickname.toRequestBody("text/plain".toMediaTypeOrNull()))
                }
                
                if (!email.isNullOrBlank()) {
                    put("email", email.toRequestBody("text/plain".toMediaTypeOrNull()))
                }
                
                put("companyName", companyName.toRequestBody("text/plain".toMediaTypeOrNull()))
                
                // 确保使用正确的参数名称 phoneNumber (后端需要)
                put("phoneNumber", mobileNumber.toRequestBody("text/plain".toMediaTypeOrNull()))
                
                put("province", province.toRequestBody("text/plain".toMediaTypeOrNull()))
                put("city", city.toRequestBody("text/plain".toMediaTypeOrNull()))
            }
            
            Log.d(TAG, "准备上传的文本参数: username=$username, email=$email, companyName=$companyName, " +
                    "phoneNumber=$mobileNumber, province=$province, city=$city")
            
            // 创建文件部分，使用更安全的文件处理
            var businessLicensePart: MultipartBody.Part? = null
            var idCardFrontPart: MultipartBody.Part? = null
            var idCardBackPart: MultipartBody.Part? = null
            
            // 处理营业执照
            businessLicensePath?.let { path ->
                Log.d(TAG, "准备上传营业执照文件: $path")
                val file = File(path)
                if (file.exists() && file.canRead() && file.length() > 0) {
                    try {
                        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                        businessLicensePart = MultipartBody.Part.createFormData("businessLicense", file.name, requestBody)
                        Log.d(TAG, "营业执照文件准备完成: ${file.length()} 字节")
                    } catch (e: Exception) {
                        Log.e(TAG, "营业执照文件处理失败: ${e.message}", e)
                    }
                } else {
                    Log.e(TAG, "营业执照文件无效: 存在=${file.exists()}, 可读=${file.canRead()}, 大小=${file.length()}")
                }
            }
            
            // 处理身份证正面
            idCardFrontPath?.let { path ->
                Log.d(TAG, "准备上传身份证正面文件: $path")
                val file = File(path)
                if (file.exists() && file.canRead() && file.length() > 0) {
                    try {
                        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                        idCardFrontPart = MultipartBody.Part.createFormData("idCardFront", file.name, requestBody)
                        Log.d(TAG, "身份证正面文件准备完成: ${file.length()} 字节")
                    } catch (e: Exception) {
                        Log.e(TAG, "身份证正面文件处理失败: ${e.message}", e)
                    }
                } else {
                    Log.e(TAG, "身份证正面文件无效: 存在=${file.exists()}, 可读=${file.canRead()}, 大小=${file.length()}")
                }
            }
            
            // 处理身份证背面
            idCardBackPath?.let { path ->
                Log.d(TAG, "准备上传身份证背面文件: $path")
                val file = File(path)
                if (file.exists() && file.canRead() && file.length() > 0) {
                    try {
                        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                        idCardBackPart = MultipartBody.Part.createFormData("idCardBack", file.name, requestBody)
                        Log.d(TAG, "身份证背面文件准备完成: ${file.length()} 字节")
                    } catch (e: Exception) {
                        Log.e(TAG, "身份证背面文件处理失败: ${e.message}", e)
                    }
                } else {
                    Log.e(TAG, "身份证背面文件无效: 存在=${file.exists()}, 可读=${file.canRead()}, 大小=${file.length()}")
                }
            }
            
            // 处理头像
            var avatarPart: MultipartBody.Part? = null
            avatarPath?.let { path ->
                Log.d(TAG, "准备上传头像文件: $path")
                val file = File(path)
                if (file.exists() && file.canRead() && file.length() > 0) {
                    try {
                        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                        avatarPart = MultipartBody.Part.createFormData("avatar", file.name, requestBody)
                        Log.d(TAG, "头像文件准备完成: ${file.length()} 字节")
                    } catch (e: Exception) {
                        Log.e(TAG, "头像文件处理失败: ${e.message}", e)
                    }
                } else {
                    Log.e(TAG, "头像文件无效: 存在=${file.exists()}, 可读=${file.canRead()}, 大小=${file.length()}")
                }
            }
            
            // 发送请求
            Log.d(TAG, "开始发送注册请求到后端API")
            val response = userApiService.register(
                params,
                businessLicensePart,
                idCardFrontPart,
                idCardBackPart,
                avatarPart
            )
            
            if (response.isSuccessful) {
                Log.d(TAG, "后端API注册成功: ${response.code()}")
            } else {
                val errorBody = response.errorBody()?.string() ?: "未知错误"
                Log.e(TAG, "后端API注册失败: ${response.code()}, ${response.message()}, 错误信息: $errorBody")
                throw Exception("后端API注册失败: ${response.code()} ${response.message()} - $errorBody")
            }
        } catch (e: Exception) {
            Log.e(TAG, "后端API注册异常: ${e.message}", e)
            throw Exception("后端API注册失败: ${e.message}")
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