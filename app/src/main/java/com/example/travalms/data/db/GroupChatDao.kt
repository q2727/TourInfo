package com.example.travalms.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * 群聊数据访问对象接口 - 定义对群聊表的操作
 */
@Dao
interface GroupChatDao {
    /**
     * 插入新的群聊记录，如果已存在则替换
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroupChat(groupChat: GroupChatEntity)
    
    /**
     * 插入多个群聊记录
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroupChats(groupChats: List<GroupChatEntity>)
    
    /**
     * 更新群聊信息
     */
    @Update
    suspend fun updateGroupChat(groupChat: GroupChatEntity)
    
    /**
     * 删除群聊记录
     */
    @Delete
    suspend fun deleteGroupChat(groupChat: GroupChatEntity)
    
    /**
     * 根据roomJid删除群聊
     */
    @Query("DELETE FROM group_chats WHERE roomJid = :roomJid")
    suspend fun deleteGroupChatByJid(roomJid: String)
    
    /**
     * 获取所有加入的群聊，按最后活动时间降序排列
     */
    @Query("SELECT * FROM group_chats ORDER BY lastActivityTime DESC")
    fun getAllGroupChats(): Flow<List<GroupChatEntity>>
    
    /**
     * 根据群聊JID获取群聊信息
     */
    @Query("SELECT * FROM group_chats WHERE roomJid = :roomJid")
    suspend fun getGroupChatByJid(roomJid: String): GroupChatEntity?
    
    /**
     * 检查群聊是否已加入
     */
    @Query("SELECT COUNT(*) FROM group_chats WHERE roomJid = :roomJid")
    suspend fun isGroupChatJoined(roomJid: String): Int
    
    /**
     * 更新群聊的最后活动时间和未读消息数
     */
    @Query("UPDATE group_chats SET lastActivityTime = :lastActivityTime, unreadCount = unreadCount + :incrementUnread, lastMessage = :lastMessage WHERE roomJid = :roomJid")
    suspend fun updateGroupChatActivity(roomJid: String, lastActivityTime: LocalDateTime, incrementUnread: Int, lastMessage: String?)
    
    /**
     * 清除指定群聊的未读消息计数
     */
    @Query("UPDATE group_chats SET unreadCount = 0 WHERE roomJid = :roomJid")
    suspend fun clearUnreadCount(roomJid: String)
    
    /**
     * 获取所有群聊的未读消息总数
     */
    @Query("SELECT SUM(unreadCount) FROM group_chats")
    fun getTotalUnreadCount(): Flow<Int?>
    
    /**
     * 搜索群聊名称
     */
    @Query("SELECT * FROM group_chats WHERE name LIKE '%' || :query || '%' ORDER BY lastActivityTime DESC")
    fun searchGroupChats(query: String): Flow<List<GroupChatEntity>>
    
    /**
     * 获取最近活跃的群聊
     */
    @Query("SELECT * FROM group_chats ORDER BY lastActivityTime DESC LIMIT :limit")
    fun getRecentGroupChats(limit: Int): Flow<List<GroupChatEntity>>
} 