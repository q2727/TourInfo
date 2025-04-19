package com.example.travalms.data.model

import org.jxmpp.jid.BareJid
import com.example.travalms.data.model.ChatMessage

/**
 * 联系人数据模型
 */
data class ContactItem(
    val id: Int,
    val name: String,
    val status: String,
    // Consider using a String URL or URI for avatar instead of ResId for flexibility
    val avatarUrl: String? = null, // Example using URL
    // val avatarResId: Int, // Original, less flexible
    val jid: BareJid? = null, // Add optional JID field
    val lastMessage: ChatMessage? = null, // 最后一条消息
    val unreadCount: Int = 0, // 未读消息数量
    val originalId: String? = null // 原始ID/JID，用于导航
) 