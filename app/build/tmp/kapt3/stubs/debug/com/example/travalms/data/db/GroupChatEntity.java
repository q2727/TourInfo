package com.example.travalms.data.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.travalms.data.model.GroupRoom;
import java.time.LocalDateTime;

/**
 * 群聊实体类 - 用于Room数据库存储用户加入的群聊信息
 */
@androidx.room.TypeConverters(value = {com.example.travalms.data.db.DateConverters.class})
@androidx.room.Entity(tableName = "group_chats")
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0087\b\u0018\u0000 ,2\u00020\u0001:\u0001,BS\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\u000b\u0012\b\b\u0002\u0010\r\u001a\u00020\u0007\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u000fJ\t\u0010\u001c\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0007H\u00c6\u0003J\t\u0010 \u001a\u00020\tH\u00c6\u0003J\t\u0010!\u001a\u00020\u000bH\u00c6\u0003J\t\u0010\"\u001a\u00020\u000bH\u00c6\u0003J\t\u0010#\u001a\u00020\u0007H\u00c6\u0003J\u000b\u0010$\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003Je\u0010%\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\r\u001a\u00020\u00072\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010&\u001a\u00020\t2\b\u0010\'\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010(\u001a\u00020\u0007H\u00d6\u0001J\u0006\u0010)\u001a\u00020*J\t\u0010+\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0012R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\f\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0014R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0011R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0011R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0011R\u0011\u0010\r\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0018\u00a8\u0006-"}, d2 = {"Lcom/example/travalms/data/db/GroupChatEntity;", "", "roomJid", "", "name", "description", "memberCount", "", "isPrivate", "", "joinTime", "Ljava/time/LocalDateTime;", "lastActivityTime", "unreadCount", "lastMessage", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IZLjava/time/LocalDateTime;Ljava/time/LocalDateTime;ILjava/lang/String;)V", "getDescription", "()Ljava/lang/String;", "()Z", "getJoinTime", "()Ljava/time/LocalDateTime;", "getLastActivityTime", "getLastMessage", "getMemberCount", "()I", "getName", "getRoomJid", "getUnreadCount", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toGroupRoom", "Lcom/example/travalms/data/model/GroupRoom;", "toString", "Companion", "app_debug"})
public final class GroupChatEntity {
    @org.jetbrains.annotations.NotNull
    @androidx.room.PrimaryKey
    private final java.lang.String roomJid = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String name = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String description = null;
    private final int memberCount = 0;
    private final boolean isPrivate = false;
    @org.jetbrains.annotations.NotNull
    private final java.time.LocalDateTime joinTime = null;
    @org.jetbrains.annotations.NotNull
    private final java.time.LocalDateTime lastActivityTime = null;
    private final int unreadCount = 0;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String lastMessage = null;
    @org.jetbrains.annotations.NotNull
    public static final com.example.travalms.data.db.GroupChatEntity.Companion Companion = null;
    
    /**
     * 群聊实体类 - 用于Room数据库存储用户加入的群聊信息
     */
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.data.db.GroupChatEntity copy(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    java.lang.String description, int memberCount, boolean isPrivate, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime joinTime, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime lastActivityTime, int unreadCount, @org.jetbrains.annotations.Nullable
    java.lang.String lastMessage) {
        return null;
    }
    
    /**
     * 群聊实体类 - 用于Room数据库存储用户加入的群聊信息
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * 群聊实体类 - 用于Room数据库存储用户加入的群聊信息
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * 群聊实体类 - 用于Room数据库存储用户加入的群聊信息
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public GroupChatEntity(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    java.lang.String description, int memberCount, boolean isPrivate, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime joinTime, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime lastActivityTime, int unreadCount, @org.jetbrains.annotations.Nullable
    java.lang.String lastMessage) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getRoomJid() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getDescription() {
        return null;
    }
    
    public final int component4() {
        return 0;
    }
    
    public final int getMemberCount() {
        return 0;
    }
    
    public final boolean component5() {
        return false;
    }
    
    public final boolean isPrivate() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.time.LocalDateTime component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.time.LocalDateTime getJoinTime() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.time.LocalDateTime component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.time.LocalDateTime getLastActivityTime() {
        return null;
    }
    
    public final int component8() {
        return 0;
    }
    
    public final int getUnreadCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getLastMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.data.model.GroupRoom toGroupRoom() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a8\u0006\t"}, d2 = {"Lcom/example/travalms/data/db/GroupChatEntity$Companion;", "", "()V", "fromGroupRoom", "Lcom/example/travalms/data/db/GroupChatEntity;", "groupRoom", "Lcom/example/travalms/data/model/GroupRoom;", "joinTime", "Ljava/time/LocalDateTime;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.example.travalms.data.db.GroupChatEntity fromGroupRoom(@org.jetbrains.annotations.NotNull
        com.example.travalms.data.model.GroupRoom groupRoom, @org.jetbrains.annotations.NotNull
        java.time.LocalDateTime joinTime) {
            return null;
        }
    }
}