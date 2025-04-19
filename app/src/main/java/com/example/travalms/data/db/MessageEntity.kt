package com.example.travalms.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.travalms.data.model.ChatMessage
import com.example.travalms.data.model.MessageType
import java.time.LocalDateTime

/**
 * 聊天消息实体类 - 用于Room数据库存储
 */
@Entity(tableName = "messages")
@TypeConverters(DateConverters::class)
data class MessageEntity(
    @PrimaryKey
    val id: String,
    val senderId: String,
    val senderName: String,
    val recipientId: String?,
    val content: String,
    val timestamp: LocalDateTime,
    val isRead: Boolean,
    val sessionId: String, // 会话ID，用于快速检索某个会话的消息
    val messageType: String = MessageType.TEXT.name
) {
    // 转换为领域模型
    fun toChatMessage(): ChatMessage {
        return ChatMessage(
            id = id,
            senderId = senderId,
            senderName = senderName,
            recipientId = recipientId,
            content = content,
            timestamp = timestamp,
            isRead = isRead,
            messageType = MessageType.valueOf(messageType)
        )
    }
    
    companion object {
        // 从领域模型转换为实体
        fun fromChatMessage(message: ChatMessage, sessionId: String): MessageEntity {
            return MessageEntity(
                id = message.id,
                senderId = message.senderId,
                senderName = message.senderName,
                recipientId = message.recipientId,
                content = message.content,
                timestamp = message.timestamp,
                isRead = message.isRead,
                sessionId = sessionId,
                messageType = message.messageType.name
            )
        }
    }
} 