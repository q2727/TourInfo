package com.example.travalms.model

/**
 * 群聊房间信息模型
 */
data class GroupRoom(
    val jid: String,                  // 房间JID (如: room_name@conference.server.com)
    val name: String,                 // 房间显示名称
    val description: String = "",     // 房间描述
    val subject: String = "",         // 房间主题
    val occupantsCount: Int = 0,      // 成员数量
    val membersOnly: Boolean = false, // 是否仅允许成员加入
    val persistent: Boolean = true,   // 是否为持久房间
    val lastMessage: String = "",     // 最后一条消息
    val lastMessageTime: String = "", // 最后消息时间
    val unreadCount: Int = 0,         // 未读消息数
    val avatarUrl: String = ""        // 群头像URL
) 