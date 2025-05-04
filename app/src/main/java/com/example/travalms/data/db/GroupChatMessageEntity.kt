package com.example.travalms.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.travalms.model.GroupChatMessage // Use the main model
import java.time.LocalDateTime

/**
 * Group Chat Message Entity for Room database storage.
 */
@Entity(tableName = "group_chat_messages")
@TypeConverters(DateConverters::class) // Assuming DateConverters exists
data class GroupChatMessageEntity(
    @PrimaryKey
    val id: String,                   // Message unique ID
    val roomJid: String,              // Group chat room JID (indexed for querying)
    val senderJid: String?,           // Sender JID (nullable)
    val senderNickname: String,       // Sender nickname
    val content: String,              // Message content
    val timestamp: LocalDateTime,     // Message timestamp (indexed for sorting)
    val isFromMe: Boolean,            // Is the message from the current user?
    val messageType: String           // Message type (e.g., TEXT, SYSTEM) - Store as String
) {
    // Convert Entity to Domain Model
    fun toGroupChatMessage(roomName: String): GroupChatMessage { // Need roomName potentially
        return GroupChatMessage(
            id = id,
            roomJid = roomJid,
            roomName = roomName, // Might need to fetch this separately or pass it in
            senderJid = senderJid,
            senderNickname = senderNickname,
            content = content,
            timestamp = timestamp,
            isFromMe = isFromMe,
            messageType = try {
                GroupChatMessage.MessageType.valueOf(messageType)
            } catch (e: IllegalArgumentException) {
                GroupChatMessage.MessageType.TEXT // Default to TEXT if type is unknown
            }
        )
    }

    companion object {
        // Convert Domain Model to Entity
        fun fromGroupChatMessage(message: GroupChatMessage): GroupChatMessageEntity {
            return GroupChatMessageEntity(
                id = message.id,
                roomJid = message.roomJid,
                senderJid = message.senderJid,
                senderNickname = message.senderNickname,
                content = message.content,
                timestamp = message.timestamp,
                isFromMe = message.isFromMe,
                messageType = message.messageType.name // Store enum name as String
            )
        }
    }
} 