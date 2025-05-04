package com.example.travalms.data.model;

import java.util.Date;
import java.util.UUID;

/**
 * 表示一个聊天邀请
 * @param id 邀请的唯一标识符
 * @param roomJid 群聊房间的JID
 * @param roomName 群聊名称
 * @param senderJid 发送邀请的用户JID
 * @param senderName 发送邀请的用户名称
 * @param reason 邀请原因
 * @param timestamp 收到邀请的时间戳
 * @param isGroupChat 是否为群聊邀请
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u001b\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BO\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u00a2\u0006\u0002\u0010\rJ\t\u0010\u001c\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010 \u001a\u00020\u0003H\u00c6\u0003J\t\u0010!\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\"\u001a\u00020\nH\u00c6\u0003J\t\u0010#\u001a\u00020\fH\u00c6\u0003JY\u0010$\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\fH\u00c6\u0001J\u0013\u0010%\u001a\u00020\f2\b\u0010&\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\'\u001a\u00020(H\u00d6\u0001J\t\u0010)\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u000e\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u0012R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0010R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0010R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0010R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0010R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0010R\u0011\u0010\u0018\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u0010R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001b\u00a8\u0006*"}, d2 = {"Lcom/example/travalms/data/model/ChatInvitation;", "", "id", "", "roomJid", "roomName", "senderJid", "senderName", "reason", "timestamp", "Ljava/util/Date;", "isGroupChat", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Date;Z)V", "formattedTime", "getFormattedTime", "()Ljava/lang/String;", "getId", "()Z", "getReason", "getRoomJid", "getRoomName", "getSenderJid", "getSenderName", "shortDescription", "getShortDescription", "getTimestamp", "()Ljava/util/Date;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class ChatInvitation {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String id = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String roomJid = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String roomName = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String senderJid = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String senderName = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String reason = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.Date timestamp = null;
    private final boolean isGroupChat = false;
    
    /**
     * 表示一个聊天邀请
     * @param id 邀请的唯一标识符
     * @param roomJid 群聊房间的JID
     * @param roomName 群聊名称
     * @param senderJid 发送邀请的用户JID
     * @param senderName 发送邀请的用户名称
     * @param reason 邀请原因
     * @param timestamp 收到邀请的时间戳
     * @param isGroupChat 是否为群聊邀请
     */
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.data.model.ChatInvitation copy(@org.jetbrains.annotations.NotNull
    java.lang.String id, @org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    java.lang.String roomName, @org.jetbrains.annotations.NotNull
    java.lang.String senderJid, @org.jetbrains.annotations.NotNull
    java.lang.String senderName, @org.jetbrains.annotations.NotNull
    java.lang.String reason, @org.jetbrains.annotations.NotNull
    java.util.Date timestamp, boolean isGroupChat) {
        return null;
    }
    
    /**
     * 表示一个聊天邀请
     * @param id 邀请的唯一标识符
     * @param roomJid 群聊房间的JID
     * @param roomName 群聊名称
     * @param senderJid 发送邀请的用户JID
     * @param senderName 发送邀请的用户名称
     * @param reason 邀请原因
     * @param timestamp 收到邀请的时间戳
     * @param isGroupChat 是否为群聊邀请
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * 表示一个聊天邀请
     * @param id 邀请的唯一标识符
     * @param roomJid 群聊房间的JID
     * @param roomName 群聊名称
     * @param senderJid 发送邀请的用户JID
     * @param senderName 发送邀请的用户名称
     * @param reason 邀请原因
     * @param timestamp 收到邀请的时间戳
     * @param isGroupChat 是否为群聊邀请
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * 表示一个聊天邀请
     * @param id 邀请的唯一标识符
     * @param roomJid 群聊房间的JID
     * @param roomName 群聊名称
     * @param senderJid 发送邀请的用户JID
     * @param senderName 发送邀请的用户名称
     * @param reason 邀请原因
     * @param timestamp 收到邀请的时间戳
     * @param isGroupChat 是否为群聊邀请
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public ChatInvitation(@org.jetbrains.annotations.NotNull
    java.lang.String id, @org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    java.lang.String roomName, @org.jetbrains.annotations.NotNull
    java.lang.String senderJid, @org.jetbrains.annotations.NotNull
    java.lang.String senderName, @org.jetbrains.annotations.NotNull
    java.lang.String reason, @org.jetbrains.annotations.NotNull
    java.util.Date timestamp, boolean isGroupChat) {
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
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getRoomName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getSenderJid() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getSenderName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getReason() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.Date component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.Date getTimestamp() {
        return null;
    }
    
    public final boolean component8() {
        return false;
    }
    
    public final boolean isGroupChat() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getShortDescription() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getFormattedTime() {
        return null;
    }
}