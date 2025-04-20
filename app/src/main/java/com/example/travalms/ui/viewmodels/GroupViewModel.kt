package com.example.travalms.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travalms.data.model.GroupRoom
import com.example.travalms.data.remote.XMPPManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 群聊列表界面的ViewModel
 */
class GroupViewModel : ViewModel() {
    private val TAG = "GroupViewModel"
    
    // 获取XMPPManager及群聊管理器
    private val xmppManager = XMPPManager.getInstance()
    
    // 可用的聊天室列表
    private val _availableRooms = MutableStateFlow<List<GroupRoom>>(emptyList())
    val availableRooms: StateFlow<List<GroupRoom>> = _availableRooms.asStateFlow()
    
    // 加载状态
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    
    // 错误信息
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    /**
     * 加载可用的群聊房间
     */
    fun loadAvailableRooms() {
        _loading.value = true
        viewModelScope.launch {
            try {
                val result = xmppManager.groupChatManager.getAvailableRooms()
                if (result.isSuccess) {
                    _availableRooms.value = result.getOrDefault(emptyList())
                    Log.d(TAG, "成功获取 ${_availableRooms.value.size} 个可用房间")
                } else {
                    _errorMessage.value = "获取房间列表失败: ${result.exceptionOrNull()?.message ?: "未知错误"}"
                    Log.e(TAG, "获取房间列表失败", result.exceptionOrNull())
                }
            } catch (e: Exception) {
                Log.e(TAG, "获取房间列表异常", e)
                _errorMessage.value = "获取房间列表异常: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
    
    /**
     * 创建新群聊
     */
    fun createRoom(
        name: String,
        nickname: String,
        description: String = "",
        isPrivate: Boolean = false
    ) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val result = xmppManager.groupChatManager.createGroupRoom(
                    roomName = name,
                    nickname = nickname,
                    description = description,
                    membersOnly = isPrivate,
                    persistent = true
                )
                
                if (result.isSuccess) {
                    // 创建成功后刷新房间列表
                    loadAvailableRooms()
                    _errorMessage.value = "创建成功"
                } else {
                    _errorMessage.value = "创建房间失败: ${result.exceptionOrNull()?.message ?: "未知错误"}"
                    Log.e(TAG, "创建房间失败", result.exceptionOrNull())
                }
            } catch (e: Exception) {
                Log.e(TAG, "创建房间异常", e)
                _errorMessage.value = "创建房间异常: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
    
    /**
     * 清除错误信息
     */
    fun clearError() {
        _errorMessage.value = null
    }
} 