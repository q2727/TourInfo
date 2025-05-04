package com.example.travalms.data.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import kotlinx.coroutines.flow.Flow;
import java.time.LocalDateTime;

/**
 * 群聊数据访问对象接口 - 定义对群聊表的操作
 */
@androidx.room.Dao
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0019\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ\u0019\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u000e0\rH\'J\u001b\u0010\u000f\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u000e0\r2\u0006\u0010\u0011\u001a\u00020\u0012H\'J\u0010\u0010\u0013\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00120\rH\'J\u0019\u0010\u0014\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ\u001f\u0010\u0015\u001a\u00020\u00032\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\t0\u000eH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0017J\u0019\u0010\u0018\u001a\u00020\u00122\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u000e0\r2\u0006\u0010\u001a\u001a\u00020\u0005H\'J\u0019\u0010\u001b\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ3\u0010\u001c\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u00122\b\u0010 \u001a\u0004\u0018\u00010\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010!\u00f8\u0001\u0001\u0082\u0002\n\n\u0002\b\u0019\n\u0004\b!0\u0001\u00a8\u0006\"\u00c0\u0006\u0001"}, d2 = {"Lcom/example/travalms/data/db/GroupChatDao;", "", "clearUnreadCount", "", "roomJid", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteGroupChat", "groupChat", "Lcom/example/travalms/data/db/GroupChatEntity;", "(Lcom/example/travalms/data/db/GroupChatEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteGroupChatByJid", "getAllGroupChats", "Lkotlinx/coroutines/flow/Flow;", "", "getGroupChatByJid", "getRecentGroupChats", "limit", "", "getTotalUnreadCount", "insertGroupChat", "insertGroupChats", "groupChats", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "isGroupChatJoined", "searchGroupChats", "query", "updateGroupChat", "updateGroupChatActivity", "lastActivityTime", "Ljava/time/LocalDateTime;", "incrementUnread", "lastMessage", "(Ljava/lang/String;Ljava/time/LocalDateTime;ILjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface GroupChatDao {
    
    /**
     * 插入新的群聊记录，如果已存在则替换
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Insert(onConflict = 1)
    public abstract java.lang.Object insertGroupChat(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.db.GroupChatEntity groupChat, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * 插入多个群聊记录
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Insert(onConflict = 1)
    public abstract java.lang.Object insertGroupChats(@org.jetbrains.annotations.NotNull
    java.util.List<com.example.travalms.data.db.GroupChatEntity> groupChats, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * 更新群聊信息
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Update
    public abstract java.lang.Object updateGroupChat(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.db.GroupChatEntity groupChat, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * 删除群聊记录
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Delete
    public abstract java.lang.Object deleteGroupChat(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.db.GroupChatEntity groupChat, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * 根据roomJid删除群聊
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "DELETE FROM group_chats WHERE roomJid = :roomJid")
    public abstract java.lang.Object deleteGroupChatByJid(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * 获取所有加入的群聊，按最后活动时间降序排列
     */
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM group_chats ORDER BY lastActivityTime DESC")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.travalms.data.db.GroupChatEntity>> getAllGroupChats();
    
    /**
     * 根据群聊JID获取群聊信息
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "SELECT * FROM group_chats WHERE roomJid = :roomJid")
    public abstract java.lang.Object getGroupChatByJid(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.example.travalms.data.db.GroupChatEntity> continuation);
    
    /**
     * 检查群聊是否已加入
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "SELECT COUNT(*) FROM group_chats WHERE roomJid = :roomJid")
    public abstract java.lang.Object isGroupChatJoined(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Integer> continuation);
    
    /**
     * 更新群聊的最后活动时间和未读消息数
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "UPDATE group_chats SET lastActivityTime = :lastActivityTime, unreadCount = unreadCount + :incrementUnread, lastMessage = :lastMessage WHERE roomJid = :roomJid")
    public abstract java.lang.Object updateGroupChatActivity(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime lastActivityTime, int incrementUnread, @org.jetbrains.annotations.Nullable
    java.lang.String lastMessage, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * 清除指定群聊的未读消息计数
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "UPDATE group_chats SET unreadCount = 0 WHERE roomJid = :roomJid")
    public abstract java.lang.Object clearUnreadCount(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * 获取所有群聊的未读消息总数
     */
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT SUM(unreadCount) FROM group_chats")
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Integer> getTotalUnreadCount();
    
    /**
     * 搜索群聊名称
     */
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM group_chats WHERE name LIKE \'%\' || :query || \'%\' ORDER BY lastActivityTime DESC")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.travalms.data.db.GroupChatEntity>> searchGroupChats(@org.jetbrains.annotations.NotNull
    java.lang.String query);
    
    /**
     * 获取最近活跃的群聊
     */
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM group_chats ORDER BY lastActivityTime DESC LIMIT :limit")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.travalms.data.db.GroupChatEntity>> getRecentGroupChats(int limit);
}