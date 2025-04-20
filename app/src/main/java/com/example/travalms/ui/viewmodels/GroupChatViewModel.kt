package com.example.travalms.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travalms.data.model.GroupChatMessage
import com.example.travalms.data.model.GroupMember
import com.example.travalms.data.model.GroupRoom
import com.example.travalms.data.remote.XMPPManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

/**
 * 群聊ViewModel，负责管理群聊数据和操作
 */
class GroupChatViewModel : ViewModel() {
    private val TAG = "GroupChatViewModel"

    // 获取XMPPManager及群聊管理器
    private val xmppManager = XMPPManager.getInstance()

    // 当前房间
    private val _currentRoom = MutableStateFlow<GroupRoom?>(null)
    val currentRoom: StateFlow<GroupRoom?> = _currentRoom.asStateFlow()

    // 加载状态
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    // 错误信息
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // 房间消息缓存
    private val roomMessages = ConcurrentHashMap<String, MutableList<GroupChatMessage>>()

    // 房间成员缓存
    private val roomMembers = ConcurrentHashMap<String, MutableList<GroupMember>>()

    init {
        // 监听群聊消息
        viewModelScope.launch {
            xmppManager.groupChatManager.groupMessageFlow.collect { message ->
                processIncomingMessage(message)
            }
        }
    }

    /**
     * 加入聊天室
     */
    fun joinRoom(roomJid: String, nickname: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val result = xmppManager.groupChatManager.joinRoom(roomJid, nickname)
                if (result.isSuccess) {
                    _currentRoom.value = result.getOrNull()
                } else {
                    _errorMessage.value =
                        "加入房间失败: ${result.exceptionOrNull()?.message ?: "未知错误"}"
                }
            } catch (e: Exception) {
                Log.e(TAG, "加入房间异常", e)
                _errorMessage.value = "加入房间异常: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * 加载房间成员列表
     */
    fun loadRoomMembers(roomJid: String) {
        viewModelScope.launch {
            try {
                val result = xmppManager.groupChatManager.getRoomMembers(roomJid)
                if (result.isSuccess) {
                    val members = result.getOrDefault(emptyList())
                    roomMembers[roomJid] = members.toMutableList()
                } else {
                    Log.e(TAG, "获取房间成员失败", result.exceptionOrNull())
                }
            } catch (e: Exception) {
                Log.e(TAG, "加载房间成员异常", e)
            }
        }
    }

    /**
     * 发送消息
     */
    fun sendMessage(roomJid: String, message: String) {
        viewModelScope.launch {
            try {
                val result = xmppManager.groupChatManager.sendGroupMessage(roomJid, message)
                if (!result.isSuccess) {
                    _errorMessage.value =
                        "发送消息失败: ${result.exceptionOrNull()?.message ?: "未知错误"}"
                }
            } catch (e: Exception) {
                Log.e(TAG, "发送消息异常", e)
                _errorMessage.value = "发送消息异常: ${e.message}"
            }
        }
    }

    /**
     * 邀请用户加入群聊
     */
    fun inviteUser(roomJid: String, userJid: String) {
        viewModelScope.launch {
            try {
                val result = xmppManager.groupChatManager.inviteUserToRoom(roomJid, userJid)
                if (result.isSuccess) {
                    _errorMessage.value = "邀请已发送"
                } else {
                    _errorMessage.value =
                        "邀请失败: ${result.exceptionOrNull()?.message ?: "未知错误"}"
                }
            } catch (e: Exception) {
                Log.e(TAG, "邀请用户异常", e)
                _errorMessage.value = "邀请用户异常: ${e.message}"
            }
        }
    }


    /**
     * 获取房间消息
     */
    fun getRoomMessages(roomJid: String): List<GroupChatMessage> {
        return roomMessages[roomJid] ?: mutableListOf()
    }

    /**
     * 获取房间成员
     */
    fun getRoomMembers(roomJid: String): List<GroupMember> {
        return roomMembers[roomJid] ?: mutableListOf()
    }

    /**
     * 处理收到的消息
     */
    private fun processIncomingMessage(message: GroupChatMessage) {
        val roomJid = message.roomJid

        // 添加消息到缓存
        roomMessages.getOrPut(roomJid) { mutableListOf() }.add(message)

        // 更新UI (ViewModel中的状态已经变更，Compose会自动更新UI)
    }

    /**
     * 清除错误信息
     */
    fun clearError() {
        _errorMessage.value = null
    }
}