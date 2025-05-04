package com.example.travalms.data.repository

import com.example.travalms.data.db.GroupChatDao
import com.example.travalms.data.db.GroupChatEntity
import com.example.travalms.data.db.GroupChatMessageDao
import com.example.travalms.data.db.GroupChatMessageEntity
import com.example.travalms.data.model.GroupRoom
import com.example.travalms.model.GroupChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 群聊仓库 - 负责管理群聊数据和消息的存取
 */
@Singleton
class GroupChatRepository @Inject constructor(
    private val groupChatDao: GroupChatDao,
    private val groupChatMessageDao: GroupChatMessageDao
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

    // --- Group Chat Message Operations --- START

    /**
     * Save a single group chat message to the database.
     */
    suspend fun saveGroupMessage(message: GroupChatMessage) {
        val entity = GroupChatMessageEntity.fromGroupChatMessage(message)
        groupChatMessageDao.insertMessage(entity)
        // Optionally update the GroupChatEntity's lastActivityTime and lastMessage
        groupChatDao.updateGroupChatActivity(
            roomJid = message.roomJid,
            lastActivityTime = LocalDateTime.now(),
            lastMessage = message.content,
            incrementUnread = if (!message.isFromMe) 1 else 0 // Increment only if received
        )
    }

    /**
     * Get all messages for a specific room from the database.
     * Requires roomName to convert back to domain model.
     */
    suspend fun getMessagesForRoom(roomJid: String, roomName: String): List<GroupChatMessage> {
        return groupChatMessageDao.getMessagesForRoom(roomJid).map { 
            // Need the roomName to convert entity back to domain model fully
            it.toGroupChatMessage(roomName)
        }
    }

    /**
     * Get all messages for a specific room as a Flow.
     * Requires roomName to convert back to domain model.
     */
    fun getMessagesForRoomFlow(roomJid: String, roomNameFlow: Flow<String>): Flow<List<GroupChatMessage>> {
        // Combine fetching entities with the room name flow
        // This is a bit complex; a simpler approach might be to fetch roomName once
        // or adjust the domain model/entity conversion.
        // For now, returning Flow<List<GroupChatMessageEntity>> might be simpler
        // and let the ViewModel handle the conversion using the current room name.
        
        // Simplified approach: Return Flow of Entities
        // return groupChatMessageDao.getMessagesForRoomFlow(roomJid)
        
        // Alternative: Fetch roomName and map (might be less efficient if roomName changes)
        return groupChatMessageDao.getMessagesForRoomFlow(roomJid).map { entities ->
             // Placeholder: Need a way to get current roomName here for mapping
             // For now, using roomJid as a fallback name
             val currentRoomName = roomJid // Replace with actual name fetching if needed
             entities.map { it.toGroupChatMessage(currentRoomName) }
        }
        
        // Consider refactoring toGroupChatMessage if roomName isn't strictly needed
        // or pass it differently.
    }

    /**
     * Delete all messages for a specific room.
     */
    suspend fun clearMessageHistoryForRoom(roomJid: String) {
        groupChatMessageDao.deleteMessagesForRoom(roomJid)
    }
    
    // --- Group Chat Message Operations --- END
} 