package com.example.travalms.data.db;

import androidx.room.*;
import kotlinx.coroutines.flow.Flow;

/**
 * 消息数据访问对象接口
 */
@androidx.room.Dao
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0011\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\bJ\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\nH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\nH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u001c\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\n0\u000e2\u0006\u0010\u0006\u001a\u00020\u0007H\'J\u001f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\f0\n2\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\bJ!\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u0007H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013J\u0019\u0010\u0014\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\fH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0016J\u001f\u0010\u0017\u001a\u00020\u00032\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\f0\nH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0019J)\u0010\u001a\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u00072\u0006\u0010\u001c\u001a\u00020\u001dH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001e\u00f8\u0001\u0001\u0082\u0002\n\n\u0002\b\u0019\n\u0004\b!0\u0001\u00a8\u0006\u001f\u00c0\u0006\u0001"}, d2 = {"Lcom/example/travalms/data/db/MessageDao;", "", "deleteAllMessages", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteSessionMessages", "sessionId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllSessionIds", "", "getLastMessagesForAllSessions", "Lcom/example/travalms/data/db/MessageEntity;", "getMessagesForSession", "Lkotlinx/coroutines/flow/Flow;", "getMessagesForSessionSync", "getUnreadCountForSession", "", "currentUserJid", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertMessage", "message", "(Lcom/example/travalms/data/db/MessageEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertMessages", "messages", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateReadStatus", "senderId", "isRead", "", "(Ljava/lang/String;Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface MessageDao {
    
    /**
     * 插入单条消息，如果ID已存在则替换
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Insert(onConflict = 1)
    public abstract java.lang.Object insertMessage(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.db.MessageEntity message, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * 批量插入消息，如果ID已存在则替换
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Insert(onConflict = 1)
    public abstract java.lang.Object insertMessages(@org.jetbrains.annotations.NotNull
    java.util.List<com.example.travalms.data.db.MessageEntity> messages, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * 获取指定会话的所有消息
     * @param sessionId 会话ID
     * @return 消息列表Flow
     */
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM messages WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.travalms.data.db.MessageEntity>> getMessagesForSession(@org.jetbrains.annotations.NotNull
    java.lang.String sessionId);
    
    /**
     * 获取指定会话的所有消息（非Flow版本）
     * @param sessionId 会话ID
     * @return 消息列表
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "SELECT * FROM messages WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    public abstract java.lang.Object getMessagesForSessionSync(@org.jetbrains.annotations.NotNull
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.example.travalms.data.db.MessageEntity>> continuation);
    
    /**
     * 更新消息的已读状态
     * @param sessionId 会话ID
     * @param isRead 是否已读
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "UPDATE messages SET isRead = :isRead WHERE sessionId = :sessionId AND senderId = :senderId")
    public abstract java.lang.Object updateReadStatus(@org.jetbrains.annotations.NotNull
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull
    java.lang.String senderId, boolean isRead, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * 获取所有的会话ID
     * @return 会话ID列表
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "SELECT DISTINCT sessionId FROM messages")
    public abstract java.lang.Object getAllSessionIds(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.String>> continuation);
    
    /**
     * 获取每个会话的最后一条消息
     * @return 每个会话的最后一条消息
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "SELECT * FROM messages WHERE id IN (SELECT id FROM messages GROUP BY sessionId HAVING MAX(timestamp))")
    public abstract java.lang.Object getLastMessagesForAllSessions(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.example.travalms.data.db.MessageEntity>> continuation);
    
    /**
     * 获取一个会话中未读消息的数量
     * @param sessionId 会话ID
     * @param currentUserJid 当前用户JID（排除自己发送的消息）
     * @return 未读消息数量
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "SELECT COUNT(*) FROM messages WHERE sessionId = :sessionId AND senderId != :currentUserJid AND isRead = 0")
    public abstract java.lang.Object getUnreadCountForSession(@org.jetbrains.annotations.NotNull
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull
    java.lang.String currentUserJid, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Integer> continuation);
    
    /**
     * 删除一个会话的所有消息
     * @param sessionId 会话ID
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "DELETE FROM messages WHERE sessionId = :sessionId")
    public abstract java.lang.Object deleteSessionMessages(@org.jetbrains.annotations.NotNull
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * 删除所有消息
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "DELETE FROM messages")
    public abstract java.lang.Object deleteAllMessages(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
}