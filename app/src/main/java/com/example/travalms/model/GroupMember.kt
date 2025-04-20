package com.example.travalms.model

/**
 * 群聊成员信息模型
 */
data class GroupMember(
    val jid: String? = null,         // 成员JID (可能不可见)
    val nickname: String,            // 在群里的昵称
    val affiliation: Affiliation,    // 成员身份
    val role: Role,                  // 成员角色
    val status: String = "离线",      // 在线状态
    val avatarUrl: String = ""       // 头像URL
) {
    // 身份类型 (持久, 关系到权限)
    enum class Affiliation {
        OWNER,      // 所有者
        ADMIN,      // 管理员
        MEMBER,     // 成员
        OUTCAST,    // 被踢出的
        NONE        // 无特殊身份
    }
    
    // 角色类型 (临时, 关系到当前会话权限)
    enum class Role {
        MODERATOR,  // 主持人
        PARTICIPANT,// 参与者
        VISITOR,    // 访客
        NONE        // 无角色
    }
    
    // 是否是管理员
    val isAdmin: Boolean
        get() = affiliation == Affiliation.ADMIN || affiliation == Affiliation.OWNER
        
    // 是否是普通成员
    val isMember: Boolean
        get() = affiliation == Affiliation.MEMBER
} 