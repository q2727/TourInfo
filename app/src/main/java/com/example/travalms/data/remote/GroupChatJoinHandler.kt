package com.example.travalms.data.remote

import android.util.Log
import com.example.travalms.data.model.GroupRoom
import com.example.travalms.data.repository.GroupChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 群聊加入处理器，用于在加入群聊时保存群聊信息到本地数据库
 */
@Singleton
class GroupChatJoinHandler @Inject constructor(
    private val groupChatRepository: GroupChatRepository,
    private val xmppManager: XMPPManager
) {
    private val TAG = "GroupChatJoinHandler"
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    // 通过XMPPManager获取XMPPGroupChatManager
    private val xmppGroupChatManager: XMPPGroupChatManager
        get() = xmppManager.groupChatManager
    
    init {
        setupJoinListener()
    }
    
    /**
     * 设置群聊加入监听器
     */
    private fun setupJoinListener() {
        coroutineScope.launch {
            // 为XMPPGroupChatManager添加回调函数，当成功加入群聊时保存群聊信息
            xmppGroupChatManager.setOnJoinRoomListener { groupRoom ->
                saveJoinedGroupChat(groupRoom)
            }
        }
    }
    
    /**
     * 保存加入的群聊信息到数据库
     */
    private fun saveJoinedGroupChat(groupRoom: GroupRoom) {
        coroutineScope.launch {
            try {
                // 检查群聊是否已经存在于数据库中
                val isJoined = groupChatRepository.isGroupChatJoined(groupRoom.roomJid)
                
                if (!isJoined) {
                    // 如果不存在，添加新群聊
                    Log.d(TAG, "保存新加入的群聊: ${groupRoom.name}")
                    groupChatRepository.addGroupChat(groupRoom)
                } else {
                    // 如果已存在，更新群聊信息
                    Log.d(TAG, "更新已加入的群聊: ${groupRoom.name}")
                    groupChatRepository.updateGroupChat(groupRoom)
                }
            } catch (e: Exception) {
                Log.e(TAG, "保存群聊信息失败: ${e.message}", e)
            }
        }
    }
    
    /**
     * 删除离开的群聊
     */
    fun removeGroupChat(roomJid: String) {
        coroutineScope.launch {
            try {
                Log.d(TAG, "从数据库中删除群聊: $roomJid")
                groupChatRepository.removeGroupChat(roomJid)
            } catch (e: Exception) {
                Log.e(TAG, "删除群聊失败: ${e.message}", e)
            }
        }
    }
    
    /**
     * 同步群聊列表
     * 将服务器上的群聊列表与本地数据库同步
     */
    fun syncGroupChatList() {
        coroutineScope.launch {
            try {
                Log.d(TAG, "开始同步群聊列表")
                
                // 获取已加入的群聊列表
                val joinedRooms = xmppGroupChatManager.getJoinedRooms()
                if (joinedRooms.isEmpty()) {
                    Log.d(TAG, "没有加入的群聊，无需同步")
                    return@launch
                }
                
                // 为所有已加入的群聊创建实体
                joinedRooms.forEach { groupRoom ->
                    saveJoinedGroupChat(groupRoom)
                }
                
                Log.d(TAG, "群聊列表同步完成")
            } catch (e: Exception) {
                Log.e(TAG, "同步群聊列表失败: ${e.message}", e)
            }
        }
    }
} 