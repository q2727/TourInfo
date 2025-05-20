package com.example.travalms.api.dto

import com.google.gson.annotations.SerializedName


/**
 * 用于向后端发送的尾单数据模型
 */
data class TailOrderRequest(
    @SerializedName("jid") val jid: String,
    @SerializedName("title") val title: String,
    @SerializedName("itinerary") val itinerary: String, // 对应 TailListItem.description
    @SerializedName("expiryDate") val expiryDate: String, // ISO 8601 格式的日期字符串, 对应 TailListItem.endDate
    @SerializedName("publishingNodes") val publishingNodes: List<String>,
    @SerializedName("productDetails") val productDetails: String // 可以是 TailListItem 其他字段的 JSON 字符串
) 