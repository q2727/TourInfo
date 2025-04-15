package com.example.travalms.data.model

/**
 * MessageItem数据类（保留原有的）
 * Represents a message preview in a list
 */
data class MessageItem(
    val id: Int,
    val title: String, // Typically sender name or group name
    val content: String, // Last message preview
    val timestamp: String, // Consider using a more structured timestamp type
    val isRead: Boolean = false,
    val isHighlighted: Boolean = false,
    val avatarUrl: String? = null // Add avatar URL for consistency
) 