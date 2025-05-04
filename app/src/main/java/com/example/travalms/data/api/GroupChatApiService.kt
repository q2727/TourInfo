package com.example.travalms.data.api

import retrofit2.Response
import retrofit2.http.*

/**
 * 群聊API服务接口
 */
interface GroupChatApiService {

    /**
     * 加入群聊
     * @param params 群聊参数
     * @return 加入结果
     */
    @POST("api/groupchat/join")
    suspend fun joinRoom(@Body params: Map<String, String>): Response<Map<String, Any>>

    /**
     * 离开群聊
     * @param params 退出参数
     * @return 离开结果
     */
    @POST("api/groupchat/leave")
    suspend fun leaveRoom(@Body params: Map<String, String>): Response<Map<String, Any>>

    /**
     * 获取用户加入的群聊
     * @param username 用户名
     * @return 群聊列表
     */
    @GET("api/groupchat/rooms/{username}")
    suspend fun getUserRooms(@Path("username") username: String): Response<Map<String, Any>>

    /**
     * 获取群聊成员
     * @param roomJid 群聊JID
     * @return 成员列表
     */
    @GET("api/groupchat/members/{roomJid}")
    suspend fun getRoomMembers(@Path("roomJid") roomJid: String): Response<Map<String, Any>>

    /**
     * 检查用户是否在群聊中
     * @param username 用户名
     * @param roomJid 群聊JID
     * @return 检查结果
     */
    @GET("api/groupchat/check")
    suspend fun isUserInRoom(
        @Query("username") username: String,
        @Query("roomJid") roomJid: String
    ): Response<Map<String, Any>>

    /**
     * 批量加入群聊
     * @param records 群聊记录列表
     * @return 加入结果
     */
    @POST("api/groupchat/batch")
    suspend fun batchJoinRooms(@Body records: List<Map<String, String>>): Response<Map<String, Any>>
} 