package com.example.travalms.data.remote

import android.util.Log
import com.example.travalms.data.api.GroupChatApiClient
import com.example.travalms.data.model.GroupRoom
import com.example.travalms.data.repository.GroupChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 群聊加入处理器，用于同步服务器与本地的群聊记录，
 * 并在用户登录时加入用户在服务器上记录的所有群聊。
 */
@Singleton
class GroupChatJoinHandler @Inject constructor(
    private val groupChatRepository: GroupChatRepository,
    private val xmppManager: XMPPManager,
    private val groupChatApiClient: GroupChatApiClient
) {
    private val TAG = "GroupChatJoinHandler"
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    // 通过XMPPManager获取XMPPGroupChatManager
    private val xmppGroupChatManager: XMPPGroupChatManager
        get() = xmppManager.groupChatManager
    
    init {
        setupJoinListener()
        Log.d(TAG, "GroupChatJoinHandler已初始化，准备处理群聊同步")
    }
    
    /**
     * 设置群聊加入监听器
     */
    private fun setupJoinListener() {
        coroutineScope.launch {
            // 为XMPPGroupChatManager添加回调函数，当成功加入群聊时同步到服务器
            xmppGroupChatManager.setOnJoinRoomListener { groupRoom ->
                syncRoomToServer(groupRoom)
            }
        }
    }
    
    /**
     * 将群聊同步到服务器
     * @param groupRoom 群聊信息
     */
    private fun syncRoomToServer(groupRoom: GroupRoom) {
        coroutineScope.launch {
            try {
                val currentUsername = getCurrentUsername() ?: return@launch
                
                // 将加入的群聊同步到服务器
                val result = groupChatApiClient.joinRoom(
                    username = currentUsername,
                    roomJid = groupRoom.roomJid,
                    roomName = groupRoom.name
                )
                
                if (result.isSuccess) {
                    Log.d(TAG, "群聊已同步到服务器: ${groupRoom.name}")
                } else {
                    Log.e(TAG, "同步群聊到服务器失败: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "同步群聊到服务器异常", e)
            }
        }
    }
    
    /**
     * 从服务器获取用户的群聊列表，并加入这些群聊
     */
    fun syncGroupChatsFromServer() {
        coroutineScope.launch {
            try {
                val currentUsername = getCurrentUsername()
                if (currentUsername.isNullOrEmpty()) {
                    Log.e(TAG, "无法获取当前用户名，跳过同步群聊")
                    return@launch
                }
                
                Log.d(TAG, "开始从服务器同步群聊，用户: $currentUsername")
                
                // 从服务器获取用户加入的群聊列表
                val result = groupChatApiClient.getUserRooms(currentUsername)
                
                if (result.isSuccess) {
                    val rooms = result.getOrNull() ?: emptyList()
                    Log.d(TAG, "从服务器获取到 ${rooms.size} 个群聊")
                    
                    // 将服务器返回的群聊信息保存到本地仓库
                    try {
                        rooms.forEach { room ->
                            val roomJid = room["roomJid"] as? String ?: return@forEach
                            val roomName = room["roomName"] as? String ?: roomJid.substringBefore("@")
                            val groupRoom = GroupRoom(
                                roomJid = roomJid, 
                                name = roomName, 
                                description = "", 
                                memberCount = 0, 
                                isPrivate = false
                            )
                            groupChatRepository.addGroupChat(groupRoom)
                            Log.d(TAG, "已保存群聊到本地数据库: $roomName ($roomJid)")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "保存群聊到本地数据库异常", e)
                    }
                    
                    // 遍历群聊列表并加入
                    for (room in rooms) {
                        val roomJid = room["roomJid"] as? String ?: continue
                        val roomName = room["roomName"] as? String ?: roomJid.substringBefore("@")
                        val nickname = room["nickname"] as? String ?: currentUsername
                        
                        // 加入群聊
                        joinRoom(roomJid, roomName, nickname)
                    }
                } else {
                    Log.e(TAG, "从服务器获取群聊失败: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "从服务器同步群聊异常", e)
            }
        }
    }
    
    /**
     * 加入群聊
     * @param roomJid 群聊JID
     * @param roomName 群聊名称
     * @param nickname 用户在群聊中的昵称
     */
    private suspend fun joinRoom(roomJid: String, roomName: String, nickname: String) {
        try {
            Log.d(TAG, "正在加入群聊: $roomJid, 名称: $roomName")
            val result = xmppGroupChatManager.joinRoom(roomJid, nickname)
            if (result.isSuccess) {
                Log.d(TAG, "成功加入群聊: $roomName")
            } else {
                Log.e(TAG, "加入群聊失败: ${result.exceptionOrNull()?.message}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "加入群聊异常", e)
        }
    }
    
    /**
     * 获取当前登录用户的用户名
     * @return 用户名
     */
    private fun getCurrentUsername(): String? {
        val currentJid = xmppManager.currentConnection?.user?.asEntityBareJidString() ?: return null
        return if (currentJid.contains("@")) {
            currentJid.substringBefore("@")
        } else {
            currentJid
        }
    }
    
    /**
     * 删除离开的群聊
     */
    fun removeGroupChat(roomJid: String) {
        coroutineScope.launch {
            try {
                val currentUsername = getCurrentUsername() ?: return@launch
                
                // 从服务器删除群聊关联
                val result = groupChatApiClient.leaveRoom(currentUsername, roomJid)
                
                if (result.isSuccess) {
                    Log.d(TAG, "已从服务器中删除群聊关联: $roomJid")
                } else {
                    Log.e(TAG, "从服务器删除群聊关联失败: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "删除群聊关联异常", e)
            }
        }
    }
} 