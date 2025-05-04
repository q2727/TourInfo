package com.example.travalms.data.api

import android.util.Log
import com.example.travalms.data.model.GroupRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 群聊API客户端，封装群聊API调用
 */
class GroupChatApiClient {
    private val TAG = "GroupChatApiClient"
    private val apiService = NetworkModule.provideGroupChatApiService()
    
    /**
     * 加入群聊
     * @param username 用户名
     * @param roomJid 群聊JID
     * @param roomName 群聊名称
     * @param nickname 用户在群聊中的昵称
     * @return 加入结果
     */
    suspend fun joinRoom(
        username: String,
        roomJid: String,
        roomName: String? = null,
        nickname: String? = null
    ): Result<Map<String, Any>> = withContext(Dispatchers.IO) {
        try {
            val params = mutableMapOf(
                "username" to username,
                "roomJid" to roomJid
            )
            
            if (roomName != null) params["roomName"] = roomName
            if (nickname != null) params["nickname"] = nickname
            
            val response = apiService.joinRoom(params)
            
            if (response.isSuccessful) {
                Log.d(TAG, "加入群聊成功: $roomJid")
                Result.success(response.body() ?: mapOf("success" to true))
            } else {
                val errorMsg = "加入群聊失败: ${response.code()} - ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "加入群聊异常", e)
            Result.failure(e)
        }
    }
    
    /**
     * 离开群聊
     * @param username 用户名
     * @param roomJid 群聊JID
     * @return 离开结果
     */
    suspend fun leaveRoom(username: String, roomJid: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val params = mapOf(
                "username" to username,
                "roomJid" to roomJid
            )
            
            val response = apiService.leaveRoom(params)
            
            if (response.isSuccessful) {
                val body = response.body()
                val success = body?.get("success") as? Boolean ?: false
                Log.d(TAG, "离开群聊结果: $success")
                Result.success(success)
            } else {
                val errorMsg = "离开群聊失败: ${response.code()} - ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "离开群聊异常", e)
            Result.failure(e)
        }
    }
    
    /**
     * 获取用户加入的群聊
     * @param username 用户名
     * @return 群聊列表
     */
    @Suppress("UNCHECKED_CAST")
    suspend fun getUserRooms(username: String): Result<List<Map<String, Any>>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getUserRooms(username)
            
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val rooms = body["rooms"] as? List<Map<String, Any>> ?: emptyList()
                Log.d(TAG, "获取到用户群聊: ${rooms.size}个")
                Result.success(rooms)
            } else {
                val errorMsg = "获取用户群聊失败: ${response.code()} - ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "获取用户群聊异常", e)
            Result.failure(e)
        }
    }
    
    /**
     * 批量加入群聊
     * @param records 群聊记录列表
     * @return 加入结果
     */
    suspend fun batchJoinRooms(records: List<Map<String, String>>): Result<Map<String, Any>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.batchJoinRooms(records)
            
            if (response.isSuccessful) {
                Log.d(TAG, "批量加入群聊成功: ${records.size}个")
                Result.success(response.body() ?: mapOf("success" to true))
            } else {
                val errorMsg = "批量加入群聊失败: ${response.code()} - ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "批量加入群聊异常", e)
            Result.failure(e)
        }
    }
    
    /**
     * 从API响应转换为GroupRoom对象
     * @param room API返回的群聊数据
     * @return GroupRoom对象
     */
    private fun mapToGroupRoom(room: Map<String, Any>): GroupRoom {
        return GroupRoom(
            roomJid = room["roomJid"] as String,
            name = room["roomName"] as String,
            description = "", // API未返回
            memberCount = 0, // API未返回
            isPrivate = false // API未返回
        )
    }
} 