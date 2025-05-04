package com.example.travalms.data.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.travalms.model.GroupChatMessage;
import java.time.LocalDateTime;

/**
 * Group Chat Message Entity for Room database storage.
 * 注意：不再存储isFromMe字段，而是在加载时根据当前用户动态判断
 */
@androidx.room.TypeConverters(value = {com.example.travalms.data.db.DateConverters.class})
@androidx.room.Entity(tableName = "group_chat_messages")
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0087\b\u0018\u0000 &2\u00020\u0001:\u0001&B?\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u0017\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\tH\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003JQ\u0010\u001c\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010 \u001a\u00020!H\u00d6\u0001J\u000e\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020\u0003J\t\u0010%\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\rR\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\rR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006\'"}, d2 = {"Lcom/example/travalms/data/db/GroupChatMessageEntity;", "", "id", "", "roomJid", "senderJid", "senderNickname", "content", "timestamp", "Ljava/time/LocalDateTime;", "messageType", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/time/LocalDateTime;Ljava/lang/String;)V", "getContent", "()Ljava/lang/String;", "getId", "getMessageType", "getRoomJid", "getSenderJid", "getSenderNickname", "getTimestamp", "()Ljava/time/LocalDateTime;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", "other", "hashCode", "", "toGroupChatMessage", "Lcom/example/travalms/model/GroupChatMessage;", "roomName", "toString", "Companion", "app_debug"})
public final class GroupChatMessageEntity {
    @org.jetbrains.annotations.NotNull
    @androidx.room.PrimaryKey
    private final java.lang.String id = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String roomJid = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String senderJid = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String senderNickname = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String content = null;
    @org.jetbrains.annotations.NotNull
    private final java.time.LocalDateTime timestamp = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String messageType = null;
    @org.jetbrains.annotations.NotNull
    public static final com.example.travalms.data.db.GroupChatMessageEntity.Companion Companion = null;
    
    /**
     * Group Chat Message Entity for Room database storage.
     * 注意：不再存储isFromMe字段，而是在加载时根据当前用户动态判断
     */
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.data.db.GroupChatMessageEntity copy(@org.jetbrains.annotations.NotNull
    java.lang.String id, @org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.Nullable
    java.lang.String senderJid, @org.jetbrains.annotations.NotNull
    java.lang.String senderNickname, @org.jetbrains.annotations.NotNull
    java.lang.String content, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime timestamp, @org.jetbrains.annotations.NotNull
    java.lang.String messageType) {
        return null;
    }
    
    /**
     * Group Chat Message Entity for Room database storage.
     * 注意：不再存储isFromMe字段，而是在加载时根据当前用户动态判断
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * Group Chat Message Entity for Room database storage.
     * 注意：不再存储isFromMe字段，而是在加载时根据当前用户动态判断
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * Group Chat Message Entity for Room database storage.
     * 注意：不再存储isFromMe字段，而是在加载时根据当前用户动态判断
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public GroupChatMessageEntity(@org.jetbrains.annotations.NotNull
    java.lang.String id, @org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.Nullable
    java.lang.String senderJid, @org.jetbrains.annotations.NotNull
    java.lang.String senderNickname, @org.jetbrains.annotations.NotNull
    java.lang.String content, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime timestamp, @org.jetbrains.annotations.NotNull
    java.lang.String messageType) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getRoomJid() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getSenderJid() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getSenderNickname() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getContent() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.time.LocalDateTime component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.time.LocalDateTime getTimestamp() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getMessageType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.model.GroupChatMessage toGroupChatMessage(@org.jetbrains.annotations.NotNull
    java.lang.String roomName) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/example/travalms/data/db/GroupChatMessageEntity$Companion;", "", "()V", "fromGroupChatMessage", "Lcom/example/travalms/data/db/GroupChatMessageEntity;", "message", "Lcom/example/travalms/model/GroupChatMessage;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.example.travalms.data.db.GroupChatMessageEntity fromGroupChatMessage(@org.jetbrains.annotations.NotNull
        com.example.travalms.model.GroupChatMessage message) {
            return null;
        }
    }
}