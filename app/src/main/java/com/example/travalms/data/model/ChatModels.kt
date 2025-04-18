package com.example.travalms.data.model

import java.time.LocalDateTime

/**
 * 聊天消息数据模型
 */
data class ChatMessage(
    val id: String,                // 消息唯一ID
    val senderId: String,          // 发送者ID/JID
    val senderName: String,        // 发送者名称
    val content: String,           // 消息内容
    val timestamp: LocalDateTime,  // 发送时间
    val isRead: Boolean = false,   // 消息已读状态
    val messageType: MessageType = MessageType.TEXT, // 消息类型
    val recipientId: String? = null // 接收者ID/JID
)

/**
 * 聊天消息类型
 */
enum class MessageType {
    TEXT,       // 文本消息
    IMAGE,      // 图片消息
    FILE,       // 文件消息
    SYSTEM      // 系统消息
}

data class ChatSession(
    val id: String,
    val targetId: String,
    val targetName: String,
    val targetType: String, // "company" or "person"
    val lastMessage: ChatMessage? = null,
    val unreadCount: Int = 0
)

// 添加订阅推送消息模型
data class NotificationMessage(
    val id: String,
    val title: String,
    val content: String,
    val timestamp: LocalDateTime,
    val isRead: Boolean = false,
    val relatedPostId: String // 关联的景点/帖子ID
) 