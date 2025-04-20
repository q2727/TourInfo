package com.example.travalms.model

import java.time.LocalDateTime

/**
 * 群聊消息数据模型
 */
data class GroupChatMessage(
    val id: String,                   // 消息唯一ID
    val roomJid: String,              // 群聊房间JID
    val roomName: String,             // 群聊房间显示名称
    val senderJid: String? = null,    // 发送者JID (如果可见)
    val senderNickname: String,       // 发送者在群里的昵称
    val content: String,              // 消息内容
    val timestamp: LocalDateTime,     // 消息时间戳
    val isFromMe: Boolean = false,    // 是否由当前用户发送
    val messageType: MessageType = MessageType.TEXT // 消息类型
) {
    enum class MessageType {
        TEXT,       // 普通文本消息
        SYSTEM,     // 系统消息(如xxx加入了群聊)
        IMAGE,      // 图片消息(未来实现)
        FILE        // 文件消息(未来实现)
    }
} 