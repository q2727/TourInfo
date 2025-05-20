package com.example.travalms.data.api

import retrofit2.http.GET
import com.example.travalms.config.AppConfig

// 产品API数据结构
// duration: "6天5晚" 需要拆分为 days=6, nights=5

data class ProductResponseItem(
    val title: String,
    val image: String,
    val description: String,
    val duration: String,
    val features: String,
    val provider: String,
    val price: Double,
    val discount: String?,
    val productId: Int
) {
    fun getImageUrl(): String? = if (image.isNotBlank()) AppConfig.PROD_IMAGE_BASE_URL + image else null
}

typealias ProductResponse = List<ProductResponseItem>

interface ProductApiService {
    @GET("/api/SearchProduct/LoadProduct")
    suspend fun getProducts(): ProductResponse
} 