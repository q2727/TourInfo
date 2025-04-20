package com.example.travalms.data.model

import java.util.Date
import java.util.UUID

/**
 * 表示一个聊天邀请
 * @param id 邀请的唯一标识符
 * @param roomJid 群聊房间的JID
 * @param roomName 群聊名称
 * @param senderJid 发送邀请的用户JID
 * @param senderName 发送邀请的用户名称
 * @param reason 邀请原因
 * @param timestamp 收到邀请的时间戳
 * @param isGroupChat 是否为群聊邀请
 */
data class ChatInvitation(
    val id: String = UUID.randomUUID().toString(),
    val roomJid: String,
    val roomName: String,
    val senderJid: String,
    val senderName: String = senderJid.substringBefore('@'),
    val reason: String = "邀请你加入群聊",
    val timestamp: Date = Date(),
    val isGroupChat: Boolean = true
) {
    /**
     * 邀请的简短描述
     */
    val shortDescription: String
        get() = "$senderName 邀请你加入 \"$roomName\""
    
    /**
     * 格式化的时间字符串
     */
    val formattedTime: String
        get() {
            val now = Date()
            val diff = now.time - timestamp.time
            return when {
                diff < 60 * 1000 -> "刚刚"
                diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)} 分钟前"
                diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)} 小时前"
                else -> "${diff / (24 * 60 * 60 * 1000)} 天前"
            }
        }
} 