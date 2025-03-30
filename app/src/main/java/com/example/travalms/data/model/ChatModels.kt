package com.example.travalms.data.model

import java.time.LocalDateTime

data class ChatMessage(
    val id: String,
    val senderId: String,
    val senderName: String,
    val content: String,
    val timestamp: LocalDateTime,
    val isRead: Boolean = false
)

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