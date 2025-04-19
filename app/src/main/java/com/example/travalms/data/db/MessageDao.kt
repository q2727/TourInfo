package com.example.travalms.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * 消息数据访问对象接口
 */
@Dao
interface MessageDao {
    /**
     * 插入单条消息，如果ID已存在则替换
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)
    
    /**
     * 批量插入消息，如果ID已存在则替换
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)
    
    /**
     * 获取指定会话的所有消息
     * @param sessionId 会话ID
     * @return 消息列表Flow
     */
    @Query("SELECT * FROM messages WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    fun getMessagesForSession(sessionId: String): Flow<List<MessageEntity>>
    
    /**
     * 获取指定会话的所有消息（非Flow版本）
     * @param sessionId 会话ID
     * @return 消息列表
     */
    @Query("SELECT * FROM messages WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    suspend fun getMessagesForSessionSync(sessionId: String): List<MessageEntity>
    
    /**
     * 更新消息的已读状态
     * @param sessionId 会话ID
     * @param isRead 是否已读
     */
    @Query("UPDATE messages SET isRead = :isRead WHERE sessionId = :sessionId AND senderId = :senderId")
    suspend fun updateReadStatus(sessionId: String, senderId: String, isRead: Boolean)
    
    /**
     * 获取所有的会话ID
     * @return 会话ID列表
     */
    @Query("SELECT DISTINCT sessionId FROM messages")
    suspend fun getAllSessionIds(): List<String>
    
    /**
     * 获取每个会话的最后一条消息
     * @return 每个会话的最后一条消息
     */
    @Query("SELECT * FROM messages WHERE id IN (SELECT id FROM messages GROUP BY sessionId HAVING MAX(timestamp))")
    suspend fun getLastMessagesForAllSessions(): List<MessageEntity>
    
    /**
     * 获取一个会话中未读消息的数量
     * @param sessionId 会话ID
     * @param currentUserJid 当前用户JID（排除自己发送的消息）
     * @return 未读消息数量
     */
    @Query("SELECT COUNT(*) FROM messages WHERE sessionId = :sessionId AND senderId != :currentUserJid AND isRead = 0")
    suspend fun getUnreadCountForSession(sessionId: String, currentUserJid: String): Int
    
    /**
     * 删除一个会话的所有消息
     * @param sessionId 会话ID
     */
    @Query("DELETE FROM messages WHERE sessionId = :sessionId")
    suspend fun deleteSessionMessages(sessionId: String)
    
    /**
     * 删除所有消息
     */
    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()
} 