package com.example.travalms.ui.screens

/**
 * 表示一个尾单的数据模型
 */
data class TailOrder(
    val id: Int,
    val title: String,
    val company: String,
    val companyId: String,
    val contactPerson: String,
    val contactPersonId: String,
    val contactPhone: String,
    val price: String,
    val remainingDays: String,
    val remainingHours: String,
    val content: List<String>,  // 行程内容列表
    val summary: String,  // 行程概要
    val isFavorite: Boolean = false,
    val publisherJid: String = "",  // 发布者的XMPP JID
    val productId: Long? = null,  // 关联的产品ID
    val productTitle: String? = null  // 关联的产品标题
) 