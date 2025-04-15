package com.example.travalms.data.model

/**
 * 消息数据模型（保留原有的）
 */
data class Message(
    val id: Int,
    val title: String, // Or perhaps sender/receiver info?
    val content: String,
    val time: String, // Consider using a more structured timestamp type (e.g., Long or Instant)
    val isRead: Boolean,
    val relatedPostId: Int // What does this relate to?
) 