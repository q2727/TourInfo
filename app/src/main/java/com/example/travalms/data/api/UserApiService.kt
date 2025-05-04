package com.example.travalms.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * 用户API服务接口
 */
interface UserApiService {
    
    /**
     * 用户注册 (带文件上传)
     */
    @Multipart
    @POST("api/users/register")
    suspend fun register(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part businessLicense: MultipartBody.Part?,
        @Part idCardFront: MultipartBody.Part?,
        @Part idCardBack: MultipartBody.Part?,
        @Part avatar: MultipartBody.Part?
    ): Response<Any>
    
    /**
     * 用户登录
     */
    @POST("api/users/login")
    suspend fun login(@Body credentials: Map<String, String>): Response<String>
    
    /**
     * 获取用户信息
     */
    @GET("api/users/{username}")
    suspend fun getUserInfo(@Path("username") username: String): Response<Map<String, Any>>
} 