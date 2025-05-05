package com.example.travalms.data.api;

import retrofit2.Response;
import retrofit2.http.*;

/**
 * 群聊API服务接口
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\r\bf\u0018\u00002\u00020\u0001J?\u0010\u0002\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00040\u00032\u001a\b\u0001\u0010\u0006\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00040\u0007H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\bJ-\u0010\t\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00040\u00032\b\b\u0001\u0010\n\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bJ-\u0010\f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00040\u00032\b\b\u0001\u0010\r\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bJ7\u0010\u000e\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00040\u00032\b\b\u0001\u0010\r\u001a\u00020\u00052\b\b\u0001\u0010\n\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ9\u0010\u0010\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00040\u00032\u0014\b\u0001\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u0004H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012J9\u0010\u0013\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00040\u00032\u0014\b\u0001\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u0004H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012\u00f8\u0001\u0001\u0082\u0002\n\n\u0002\b\u0019\n\u0004\b!0\u0001\u00a8\u0006\u0014\u00c0\u0006\u0001"}, d2 = {"Lcom/example/travalms/data/api/GroupChatApiService;", "", "batchJoinRooms", "Lretrofit2/Response;", "", "", "records", "", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRoomMembers", "roomJid", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUserRooms", "username", "isUserInRoom", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "joinRoom", "params", "(Ljava/util/Map;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "leaveRoom", "app_debug"})
public abstract interface GroupChatApiService {
    
    /**
     * 加入群聊
     * @param params 群聊参数
     * @return 加入结果
     */
    @org.jetbrains.annotations.Nullable
    @retrofit2.http.POST(value = "api/groupchat/join")
    public abstract java.lang.Object joinRoom(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Body
    java.util.Map<java.lang.String, java.lang.String> params, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.Map<java.lang.String, java.lang.Object>>> continuation);
    
    /**
     * 离开群聊
     * @param params 退出参数
     * @return 离开结果
     */
    @org.jetbrains.annotations.Nullable
    @retrofit2.http.POST(value = "api/groupchat/leave")
    public abstract java.lang.Object leaveRoom(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Body
    java.util.Map<java.lang.String, java.lang.String> params, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.Map<java.lang.String, java.lang.Object>>> continuation);
    
    /**
     * 获取用户加入的群聊
     * @param username 用户名
     * @return 群聊列表
     */
    @org.jetbrains.annotations.Nullable
    @retrofit2.http.GET(value = "api/groupchat/rooms/{username}")
    public abstract java.lang.Object getUserRooms(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Path(value = "username")
    java.lang.String username, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.Map<java.lang.String, java.lang.Object>>> continuation);
    
    /**
     * 获取群聊成员
     * @param roomJid 群聊JID
     * @return 成员列表
     */
    @org.jetbrains.annotations.Nullable
    @retrofit2.http.GET(value = "api/groupchat/members/{roomJid}")
    public abstract java.lang.Object getRoomMembers(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Path(value = "roomJid")
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.Map<java.lang.String, java.lang.Object>>> continuation);
    
    /**
     * 检查用户是否在群聊中
     * @param username 用户名
     * @param roomJid 群聊JID
     * @return 检查结果
     */
    @org.jetbrains.annotations.Nullable
    @retrofit2.http.GET(value = "api/groupchat/check")
    public abstract java.lang.Object isUserInRoom(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Query(value = "username")
    java.lang.String username, @org.jetbrains.annotations.NotNull
    @retrofit2.http.Query(value = "roomJid")
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.Map<java.lang.String, java.lang.Object>>> continuation);
    
    /**
     * 批量加入群聊
     * @param records 群聊记录列表
     * @return 加入结果
     */
    @org.jetbrains.annotations.Nullable
    @retrofit2.http.POST(value = "api/groupchat/batch")
    public abstract java.lang.Object batchJoinRooms(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Body
    java.util.List<? extends java.util.Map<java.lang.String, java.lang.String>> records, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.Map<java.lang.String, java.lang.Object>>> continuation);
}