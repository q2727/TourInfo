package com.example.travalms.ui.screens

/**
 * 节点类型枚举，用于区分层级结构中的不同节点类型
 */
enum class SubscriptionNodeType {
    CATEGORY,  // 类别
    PROVINCE,  // 省份
    CITY,      // 城市
    ATTRACTION, // 景点
    COMPANY    // 公司
}

/**
 * 层级树形结构节点，用于订阅界面和发布节点选择界面
 */
data class SubscriptionNodeItem(
    val id: String,               // 节点唯一ID
    val name: String,             // 节点显示名称
    val type: SubscriptionNodeType, // 节点类型
    val children: List<SubscriptionNodeItem> = emptyList(), // 子节点列表
    val hasHighlight: Boolean = false // 是否高亮显示，用于标记特殊节点
) 