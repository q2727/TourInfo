package com.example.travalms.data.remote

import android.util.Log
import com.example.travalms.data.repository.GroupChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 群聊管理器，负责管理群聊的加入和离开
 */
@Singleton
class GroupChatManager @Inject constructor(
    private val groupChatRepository: GroupChatRepository,
    private val xmppManager: XMPPManager
) {
    private val TAG = "GroupChatManager"
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    
    // 群聊加入完成回调
    private var onGroupChatsJoinedCallback: (() -> Unit)? = null
    
    /**
     * 设置群聊加入完成回调
     */
    fun setOnGroupChatsJoinedCallback(callback: () -> Unit) {
        onGroupChatsJoinedCallback = callback
    }
} 