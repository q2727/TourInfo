package com.example.travalms.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import kotlinx.coroutines.flow.Flow;

/**
 * DAO for GroupChatMessageEntity.
 */
@androidx.room.Dao
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\bg\u0018\u00002\u00020\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u001b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u001f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\n2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\n0\f2\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0019\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ\u001f\u0010\u0010\u001a\u00020\u00032\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\b0\nH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012\u00f8\u0001\u0001\u0082\u0002\n\n\u0002\b\u0019\n\u0004\b!0\u0001\u00a8\u0006\u0013\u00c0\u0006\u0001"}, d2 = {"Lcom/example/travalms/data/db/GroupChatMessageDao;", "", "deleteMessagesForRoom", "", "roomJid", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLatestMessageForRoom", "Lcom/example/travalms/data/db/GroupChatMessageEntity;", "getMessagesForRoom", "", "getMessagesForRoomFlow", "Lkotlinx/coroutines/flow/Flow;", "insertMessage", "message", "(Lcom/example/travalms/data/db/GroupChatMessageEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertMessages", "messages", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface GroupChatMessageDao {
    
    /**
     * Insert a single message.
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Insert(onConflict = 1)
    public abstract java.lang.Object insertMessage(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.db.GroupChatMessageEntity message, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * Insert multiple messages.
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Insert(onConflict = 1)
    public abstract java.lang.Object insertMessages(@org.jetbrains.annotations.NotNull
    java.util.List<com.example.travalms.data.db.GroupChatMessageEntity> messages, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * Get all messages for a specific group chat room, ordered by timestamp.
     * @param roomJid The JID of the group chat room.
     * @return Flow emitting the list of messages.
     */
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM group_chat_messages WHERE roomJid = :roomJid ORDER BY timestamp ASC")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.travalms.data.db.GroupChatMessageEntity>> getMessagesForRoomFlow(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid);
    
    /**
     * Get all messages for a specific group chat room, ordered by timestamp (non-Flow).
     * @param roomJid The JID of the group chat room.
     * @return List of messages.
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "SELECT * FROM group_chat_messages WHERE roomJid = :roomJid ORDER BY timestamp ASC")
    public abstract java.lang.Object getMessagesForRoom(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.example.travalms.data.db.GroupChatMessageEntity>> continuation);
    
    /**
     * Delete all messages for a specific group chat room.
     * @param roomJid The JID of the group chat room.
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "DELETE FROM group_chat_messages WHERE roomJid = :roomJid")
    public abstract java.lang.Object deleteMessagesForRoom(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * Get the latest message for a specific group chat room.
     * @param roomJid The JID of the group chat room.
     * @return The latest message entity, or null if none found.
     */
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "SELECT * FROM group_chat_messages WHERE roomJid = :roomJid ORDER BY timestamp DESC LIMIT 1")
    public abstract java.lang.Object getLatestMessageForRoom(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.example.travalms.data.db.GroupChatMessageEntity> continuation);
}