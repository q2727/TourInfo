package com.example.travalms.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO for GroupChatMessageEntity.
 */
@Dao
interface GroupChatMessageDao {

    /**
     * Insert a single message.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: GroupChatMessageEntity)

    /**
     * Insert multiple messages.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<GroupChatMessageEntity>)

    /**
     * Get all messages for a specific group chat room, ordered by timestamp.
     * @param roomJid The JID of the group chat room.
     * @return Flow emitting the list of messages.
     */
    @Query("SELECT * FROM group_chat_messages WHERE roomJid = :roomJid ORDER BY timestamp ASC")
    fun getMessagesForRoomFlow(roomJid: String): Flow<List<GroupChatMessageEntity>>

    /**
     * Get all messages for a specific group chat room, ordered by timestamp (non-Flow).
     * @param roomJid The JID of the group chat room.
     * @return List of messages.
     */
    @Query("SELECT * FROM group_chat_messages WHERE roomJid = :roomJid ORDER BY timestamp ASC")
    suspend fun getMessagesForRoom(roomJid: String): List<GroupChatMessageEntity>

    /**
     * Delete all messages for a specific group chat room.
     * @param roomJid The JID of the group chat room.
     */
    @Query("DELETE FROM group_chat_messages WHERE roomJid = :roomJid")
    suspend fun deleteMessagesForRoom(roomJid: String)

    /**
     * Get the latest message for a specific group chat room.
     * @param roomJid The JID of the group chat room.
     * @return The latest message entity, or null if none found.
     */
    @Query("SELECT * FROM group_chat_messages WHERE roomJid = :roomJid ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestMessageForRoom(roomJid: String): GroupChatMessageEntity?
} 