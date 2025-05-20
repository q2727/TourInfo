package com.example.travalms.api.dto

import com.google.gson.annotations.SerializedName

/**
 * 发布节点DTO响应数据
 */
data class PublishingNodeResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("nodeName") val nodeName: String
)

/**
 * 后端返回的尾单数据模型
 */
data class TailOrderResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("jid") val jid: String,
    @SerializedName("title") val title: String,
    @SerializedName("itinerary") val itinerary: String,
    @SerializedName("expiryDate") val expiryDate: String,
    @SerializedName("productDetails") val productDetails: String,
    @SerializedName("publishingNodes") val publishingNodes: List<PublishingNodeResponse>? // 节点对象列表
) 