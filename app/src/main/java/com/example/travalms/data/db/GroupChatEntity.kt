package com.example.travalms.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.travalms.data.model.GroupRoom
import java.time.LocalDateTime

/**
 * 群聊实体类 - 用于Room数据库存储用户加入的群聊信息
 */
@Entity(tableName = "group_chats")
@TypeConverters(DateConverters::class)
data class GroupChatEntity(
    @PrimaryKey
    val roomJid: String,
    val name: String,
    val description: String,
    val memberCount: Int,
    val isPrivate: Boolean,
    val joinTime: LocalDateTime,
    val lastActivityTime: LocalDateTime,
    val unreadCount: Int = 0,
    val lastMessage: String? = null
) {
    // 转换为领域模型
    fun toGroupRoom(): GroupRoom {
        return GroupRoom(
            roomJid = roomJid,
            name = name,
            description = description,
            memberCount = memberCount,
            isPrivate = isPrivate,
            canEdit = false // 默认不可编辑
        )
    }
    
    companion object {
        // 从领域模型转换为实体
        fun fromGroupRoom(groupRoom: GroupRoom, joinTime: LocalDateTime = LocalDateTime.now()): GroupChatEntity {
            return GroupChatEntity(
                roomJid = groupRoom.roomJid,
                name = groupRoom.name,
                description = groupRoom.description,
                memberCount = groupRoom.memberCount,
                isPrivate = groupRoom.isPrivate,
                joinTime = joinTime,
                lastActivityTime = LocalDateTime.now()
            )
        }
    }
} 