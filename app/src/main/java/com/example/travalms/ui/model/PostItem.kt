package com.example.travalms.ui.model

/**
 * 尾单数据模型
 * 用于UI展示的统一数据结构
 */
data class PostItem(
    val id: Int,
    val title: String,
    val feature: String,
    val publisher: String,
    val publishTime: Long,
    val dates: String = "",
    val remainingSlots: Int = 0,
    val price: String = "",
    val daysExpired: Int = 0,
    val publishLocations: List<String> = emptyList(),
    val productId: Int? = null,
    val productTitle: String? = null
) {
    companion object {
        /**
         * 创建默认实例
         */
        fun createDefault(id: Int = 0) = PostItem(
            id = id,
            title = "",
            feature = "",
            publisher = "",
            publishTime = System.currentTimeMillis(),
            dates = "",
            remainingSlots = 0,
            price = "¥0",
            daysExpired = 0,
            publishLocations = emptyList(),
            productId = null,
            productTitle = null
        )
    }
} 