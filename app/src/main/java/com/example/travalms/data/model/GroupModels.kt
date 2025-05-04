package com.example.travalms.data.model

import java.time.LocalDateTime

/**
 * 群聊房间信息
 */
data class GroupRoom(
    val roomJid: String,
    val name: String,
    val description: String,
    val memberCount: Int,
    val isPrivate: Boolean,
    val canEdit: Boolean = false
)

/**
 * 群聊成员信息
 */
data class GroupMember(
    val jid: String,
    val nickname: String,
    val role: String,
    val affiliation: String
) {
    companion object {
        const val ROLE_MODERATOR = "moderator"
        const val ROLE_PARTICIPANT = "participant"
        const val ROLE_VISITOR = "visitor"
        const val ROLE_NONE = "none"
        
        const val AFFILIATION_OWNER = "owner"
        const val AFFILIATION_ADMIN = "admin"
        const val AFFILIATION_MEMBER = "member"
        const val AFFILIATION_NONE = "none"
    }
    
    // 判断是否是管理员
    val isAdmin: Boolean
        get() = affiliation == AFFILIATION_OWNER || affiliation == AFFILIATION_ADMIN || role == ROLE_MODERATOR
    
    // 获取在线状态
    val status: String
        get() = "在线" // 默认状态，实际应用中可能需要根据用户的真实状态判断
}

/**
 * 群聊消息
 */
data class GroupChatMessage(
    val roomJid: String,
    val senderJid: String,
    val senderNickname: String,
    val content: String,
    val timestamp: LocalDateTime,
    val isFromMe: Boolean,
    val messageType: MessageType = MessageType.NORMAL,
    val id: String,
    val roomName: String
) {
    // 消息类型
    enum class MessageType {
        NORMAL,  // 普通消息
        SYSTEM,  // 系统消息
        NOTIFICATION  // 通知消息
    }
} 