package com.example.travalms.data.api

import com.example.travalms.api.dto.TailOrderRequest
import com.example.travalms.api.dto.TailOrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TailOrderApiService {
    @POST("api/tail-orders") // 与后端 TailOrderController 中的 @RequestMapping("/api/tail-orders") 和 @PostMapping 对应
    suspend fun publishTailOrder(@Body tailOrderRequest: TailOrderRequest): Response<TailOrderResponse>
    
    // 获取指定用户JID的尾单发布历史
    @GET("api/tail-orders/user")
    suspend fun getUserTailOrders(@Query("jid") jid: String): Response<List<TailOrderResponse>>
    
    // 删除尾单
    @retrofit2.http.DELETE("api/tail-orders/{id}")
    suspend fun deleteTailOrder(@Path("id") id: Long): Response<Void>
} 