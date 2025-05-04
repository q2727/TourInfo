package com.example.travalms.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.travalms.model.GroupChatMessage // Use the main model
import java.time.LocalDateTime

/**
 * Group Chat Message Entity for Room database storage.
 * 注意：不再存储isFromMe字段，而是在加载时根据当前用户动态判断
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
    val messageType: String           // Message type (e.g., TEXT, SYSTEM) - Store as String
) {
    // Convert Entity to Domain Model
    fun toGroupChatMessage(roomName: String): GroupChatMessage {
        return GroupChatMessage(
            id = id,
            roomJid = roomJid,
            roomName = roomName,
            senderJid = senderJid,
            senderNickname = senderNickname,
            content = content,
            timestamp = timestamp,
            // 不再包含isFromMe字段，显示时将动态判断
            isFromMe = false, // 默认值，将在显示时根据当前用户动态判断
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
                // 不再保存isFromMe字段
                messageType = message.messageType.name // Store enum name as String
            )
        }
    }
} 