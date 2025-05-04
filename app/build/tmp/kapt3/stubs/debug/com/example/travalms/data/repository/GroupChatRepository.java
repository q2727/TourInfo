package com.example.travalms.data.repository;

import com.example.travalms.data.db.GroupChatDao;
import com.example.travalms.data.db.GroupChatEntity;
import com.example.travalms.data.db.GroupChatMessageDao;
import com.example.travalms.data.db.GroupChatMessageEntity;
import com.example.travalms.data.model.GroupRoom;
import com.example.travalms.model.GroupChatMessage;
import kotlinx.coroutines.flow.Flow;
import java.time.LocalDateTime;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 群聊仓库 - 负责管理群聊数据和消息的存取
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\f\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0019\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bJ\u0019\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000eH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ\u0019\u0010\u0010\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000eH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ\u0012\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00130\u0012J\'\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\u00132\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\u000eH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0017J(\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\u00130\u00122\u0006\u0010\r\u001a\u00020\u000e2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0012J\u001c\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00130\u00122\b\b\u0002\u0010\u001b\u001a\u00020\u001cJ\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001c0\u0012J\u0019\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\r\u001a\u00020\u000eH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ\u0019\u0010 \u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000eH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ\u0019\u0010!\u001a\u00020\b2\u0006\u0010\"\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010#J\u001a\u0010$\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00130\u00122\u0006\u0010%\u001a\u00020\u000eJ\u0019\u0010&\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bJ/\u0010\'\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000e2\n\b\u0002\u0010(\u001a\u0004\u0018\u00010\u000e2\b\b\u0002\u0010)\u001a\u00020\u001cH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010*R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006+"}, d2 = {"Lcom/example/travalms/data/repository/GroupChatRepository;", "", "groupChatDao", "Lcom/example/travalms/data/db/GroupChatDao;", "groupChatMessageDao", "Lcom/example/travalms/data/db/GroupChatMessageDao;", "(Lcom/example/travalms/data/db/GroupChatDao;Lcom/example/travalms/data/db/GroupChatMessageDao;)V", "addGroupChat", "", "groupRoom", "Lcom/example/travalms/data/model/GroupRoom;", "(Lcom/example/travalms/data/model/GroupRoom;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearMessageHistoryForRoom", "roomJid", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearUnreadCount", "getAllGroupChats", "Lkotlinx/coroutines/flow/Flow;", "", "getMessagesForRoom", "Lcom/example/travalms/model/GroupChatMessage;", "roomName", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMessagesForRoomFlow", "roomNameFlow", "getRecentGroupChats", "limit", "", "getTotalUnreadCount", "isGroupChatJoined", "", "removeGroupChat", "saveGroupMessage", "message", "(Lcom/example/travalms/model/GroupChatMessage;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchGroupChats", "query", "updateGroupChat", "updateGroupChatActivity", "lastMessage", "incrementUnread", "(Ljava/lang/String;Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@javax.inject.Singleton
public final class GroupChatRepository {
    private final com.example.travalms.data.db.GroupChatDao groupChatDao = null;
    private final com.example.travalms.data.db.GroupChatMessageDao groupChatMessageDao = null;
    
    @javax.inject.Inject
    public GroupChatRepository(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.db.GroupChatDao groupChatDao, @org.jetbrains.annotations.NotNull
    com.example.travalms.data.db.GroupChatMessageDao groupChatMessageDao) {
        super();
    }
    
    /**
     * 获取所有加入的群聊
     */
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.travalms.data.model.GroupRoom>> getAllGroupChats() {
        return null;
    }
    
    /**
     * 添加群聊到本地数据库
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object addGroupChat(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.model.GroupRoom groupRoom, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    /**
     * 更新群聊信息
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object updateGroupChat(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.model.GroupRoom groupRoom, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    /**
     * 删除已加入的群聊
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object removeGroupChat(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    /**
     * 检查群聊是否已加入
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object isGroupChatJoined(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Boolean> continuation) {
        return null;
    }
    
    /**
     * 更新群聊活动信息
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object updateGroupChatActivity(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.Nullable
    java.lang.String lastMessage, int incrementUnread, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    /**
     * 清除群聊未读消息计数
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object clearUnreadCount(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    /**
     * 获取所有群聊的未读消息总数
     */
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.lang.Integer> getTotalUnreadCount() {
        return null;
    }
    
    /**
     * 搜索群聊
     */
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.travalms.data.model.GroupRoom>> searchGroupChats(@org.jetbrains.annotations.NotNull
    java.lang.String query) {
        return null;
    }
    
    /**
     * 获取最近活跃的群聊
     */
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.travalms.data.model.GroupRoom>> getRecentGroupChats(int limit) {
        return null;
    }
    
    /**
     * Save a single group chat message to the database.
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object saveGroupMessage(@org.jetbrains.annotations.NotNull
    com.example.travalms.model.GroupChatMessage message, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    /**
     * Get all messages for a specific room from the database.
     * Requires roomName to convert back to domain model.
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getMessagesForRoom(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    java.lang.String roomName, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.example.travalms.model.GroupChatMessage>> continuation) {
        return null;
    }
    
    /**
     * Get all messages for a specific room as a Flow.
     * Requires roomName to convert back to domain model.
     */
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.travalms.model.GroupChatMessage>> getMessagesForRoomFlow(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    kotlinx.coroutines.flow.Flow<java.lang.String> roomNameFlow) {
        return null;
    }
    
    /**
     * Delete all messages for a specific room.
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object clearMessageHistoryForRoom(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
}