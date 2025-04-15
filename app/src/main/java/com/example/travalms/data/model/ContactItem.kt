package com.example.travalms.data.model

/**
 * 联系人数据模型
 */
data class ContactItem(
    val id: Int,
    val name: String,
    val status: String,
    // Consider using a String URL or URI for avatar instead of ResId for flexibility
    val avatarUrl: String? = null // Example using URL
    // val avatarResId: Int // Original, less flexible
) 