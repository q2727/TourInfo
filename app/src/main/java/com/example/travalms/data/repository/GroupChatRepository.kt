package com.example.travalms.data.repository

import com.example.travalms.data.db.GroupChatDao
import com.example.travalms.data.db.GroupChatEntity
import com.example.travalms.data.model.GroupRoom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 群聊仓库 - 负责管理群聊数据的存取
 */
@Singleton
class GroupChatRepository @Inject constructor(
    private val groupChatDao: GroupChatDao
) {
    /**
     * 获取所有加入的群聊
     */
    fun getAllGroupChats(): Flow<List<GroupRoom>> {
        return groupChatDao.getAllGroupChats()
            .map { entities -> entities.map { it.toGroupRoom() } }
    }
    
    /**
     * 添加群聊到本地数据库
     */
    suspend fun addGroupChat(groupRoom: GroupRoom) {
        val entity = GroupChatEntity.fromGroupRoom(groupRoom)
        groupChatDao.insertGroupChat(entity)
    }
    
    /**
     * 更新群聊信息
     */
    suspend fun updateGroupChat(groupRoom: GroupRoom) {
        // 先获取现有的群聊记录以保留其他信息
        val existingChat = groupChatDao.getGroupChatByJid(groupRoom.roomJid)
        if (existingChat != null) {
            val updatedEntity = existingChat.copy(
                name = groupRoom.name,
                description = groupRoom.description,
                memberCount = groupRoom.memberCount,
                isPrivate = groupRoom.isPrivate
            )
            groupChatDao.updateGroupChat(updatedEntity)
        } else {
            // 如果不存在，则添加新记录
            addGroupChat(groupRoom)
        }
    }
    
    /**
     * 删除已加入的群聊
     */
    suspend fun removeGroupChat(roomJid: String) {
        groupChatDao.deleteGroupChatByJid(roomJid)
    }
    
    /**
     * 检查群聊是否已加入
     */
    suspend fun isGroupChatJoined(roomJid: String): Boolean {
        return groupChatDao.isGroupChatJoined(roomJid) > 0
    }
    
    /**
     * 更新群聊活动信息
     */
    suspend fun updateGroupChatActivity(
        roomJid: String, 
        lastMessage: String? = null,
        incrementUnread: Int = 0
    ) {
        groupChatDao.updateGroupChatActivity(
            roomJid = roomJid,
            lastActivityTime = LocalDateTime.now(),
            incrementUnread = incrementUnread,
            lastMessage = lastMessage
        )
    }
    
    /**
     * 清除群聊未读消息计数
     */
    suspend fun clearUnreadCount(roomJid: String) {
        groupChatDao.clearUnreadCount(roomJid)
    }
    
    /**
     * 获取所有群聊的未读消息总数
     */
    fun getTotalUnreadCount(): Flow<Int> {
        return groupChatDao.getTotalUnreadCount().map { it ?: 0 }
    }
    
    /**
     * 搜索群聊
     */
    fun searchGroupChats(query: String): Flow<List<GroupRoom>> {
        return groupChatDao.searchGroupChats(query)
            .map { entities -> entities.map { it.toGroupRoom() } }
    }
    
    /**
     * 获取最近活跃的群聊
     */
    fun getRecentGroupChats(limit: Int = 10): Flow<List<GroupRoom>> {
        return groupChatDao.getRecentGroupChats(limit)
            .map { entities -> entities.map { it.toGroupRoom() } }
    }
} 