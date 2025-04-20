package com.example.travalms.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travalms.data.model.ChatInvitation
import com.example.travalms.data.remote.XMPPManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 管理群聊邀请的ViewModel
 */
class InvitationViewModel : ViewModel() {
    private val TAG = "InvitationViewModel"
    
    // 获取XMPPGroupChatManager实例
    private val groupChatManager = XMPPManager.getInstance().groupChatManager
    
    // 所有收到的邀请
    private val _invitations = MutableStateFlow<List<ChatInvitation>>(emptyList())
    val invitations: StateFlow<List<ChatInvitation>> = _invitations.asStateFlow()
    
    // 未读邀请数量
    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()
    
    init {
        // 初始化时订阅GroupChatManager的邀请列表
        viewModelScope.launch {
            try {
                // 立即获取当前邀请列表
                val currentInvitations = groupChatManager.invitations.value
                if (currentInvitations.isNotEmpty()) {
                    Log.d(TAG, "初始化: 发现 ${currentInvitations.size} 个邀请")
                    _invitations.value = currentInvitations
                    updateUnreadCount()
                }
                
                // 订阅邀请列表流
                groupChatManager.invitations.collectLatest { invitationList ->
                    Log.d(TAG, "订阅流更新: 收到 ${invitationList.size} 个邀请")
                    _invitations.value = invitationList
                    updateUnreadCount()
                }
            } catch (e: Exception) {
                Log.e(TAG, "订阅邀请列表失败", e)
            }
        }
        
        // 调试当前状态
        viewModelScope.launch {
            _invitations.collectLatest { invites ->
                Log.d(TAG, "InvitationViewModel 当前状态: ${invites.size} 个邀请, ${_unreadCount.value} 个未读")
            }
        }
    }
    
    /**
     * 接受邀请
     */
    fun acceptInvitation(invitation: ChatInvitation) {
        viewModelScope.launch {
            Log.d(TAG, "接受邀请: ${invitation.roomJid}")
            
            try {
                // 调用GroupChatManager接受邀请
                val result = groupChatManager.acceptInvitation(invitation)
                
                if (result.isSuccess) {
                    Log.d(TAG, "邀请已成功接受: ${invitation.roomJid}")
                } else {
                    Log.e(TAG, "接受邀请失败: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "接受邀请异常", e)
            }
        }
    }
    
    /**
     * 拒绝邀请
     */
    fun rejectInvitation(invitation: ChatInvitation) {
        Log.d(TAG, "拒绝邀请: ${invitation.roomJid}")
        // 调用GroupChatManager拒绝邀请
        groupChatManager.rejectInvitation(invitation)
    }
    
    /**
     * 更新未读邀请数量
     */
    private fun updateUnreadCount() {
        val count = groupChatManager.unreadInvitationCount.value
        Log.d(TAG, "更新未读邀请数量: $count")
        _unreadCount.value = count
    }
    
    /**
     * 标记所有邀请为已读
     */
    fun markAllAsRead() {
        Log.d(TAG, "标记所有邀请为已读")
        groupChatManager.markAllInvitationsAsRead()
        _unreadCount.value = 0
    }
    
    /**
     * 手动刷新邀请列表
     */
    fun refreshInvitations() {
        Log.d(TAG, "手动刷新邀请列表")
        // 重新同步邀请列表
        _invitations.value = groupChatManager.invitations.value
        updateUnreadCount()
    }
} 