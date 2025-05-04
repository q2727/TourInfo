package com.example.travalms.data.model;

import org.jxmpp.jid.BareJid;
import com.example.travalms.data.model.ChatMessage;

/**
 * 联系人数据模型
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0019\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001BW\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u000eJ\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010\u001e\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010\u001f\u001a\u0004\u0018\u00010\tH\u00c6\u0003J\u000b\u0010 \u001a\u0004\u0018\u00010\u000bH\u00c6\u0003J\t\u0010!\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\"\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003Ja\u0010#\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\b\b\u0002\u0010\f\u001a\u00020\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001J\u0013\u0010$\u001a\u00020%2\b\u0010&\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\'\u001a\u00020\u0003H\u00d6\u0001J\t\u0010(\u001a\u00020\u0005H\u00d6\u0001R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0013\u0010\b\u001a\u0004\u0018\u00010\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0013\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0010R\u0013\u0010\r\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0010R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0010R\u0011\u0010\f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0012\u00a8\u0006)"}, d2 = {"Lcom/example/travalms/data/model/ContactItem;", "", "id", "", "name", "", "status", "avatarUrl", "jid", "Lorg/jxmpp/jid/BareJid;", "lastMessage", "Lcom/example/travalms/data/model/ChatMessage;", "unreadCount", "originalId", "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/jxmpp/jid/BareJid;Lcom/example/travalms/data/model/ChatMessage;ILjava/lang/String;)V", "getAvatarUrl", "()Ljava/lang/String;", "getId", "()I", "getJid", "()Lorg/jxmpp/jid/BareJid;", "getLastMessage", "()Lcom/example/travalms/data/model/ChatMessage;", "getName", "getOriginalId", "getStatus", "getUnreadCount", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class ContactItem {
    private final int id = 0;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String name = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String status = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String avatarUrl = null;
    @org.jetbrains.annotations.Nullable
    private final org.jxmpp.jid.BareJid jid = null;
    @org.jetbrains.annotations.Nullable
    private final com.example.travalms.data.model.ChatMessage lastMessage = null;
    private final int unreadCount = 0;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String originalId = null;
    
    /**
     * 联系人数据模型
     */
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.data.model.ContactItem copy(int id, @org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    java.lang.String status, @org.jetbrains.annotations.Nullable
    java.lang.String avatarUrl, @org.jetbrains.annotations.Nullable
    org.jxmpp.jid.BareJid jid, @org.jetbrains.annotations.Nullable
    com.example.travalms.data.model.ChatMessage lastMessage, int unreadCount, @org.jetbrains.annotations.Nullable
    java.lang.String originalId) {
        return null;
    }
    
    /**
     * 联系人数据模型
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * 联系人数据模型
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * 联系人数据模型
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public ContactItem(int id, @org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    java.lang.String status, @org.jetbrains.annotations.Nullable
    java.lang.String avatarUrl, @org.jetbrains.annotations.Nullable
    org.jxmpp.jid.BareJid jid, @org.jetbrains.annotations.Nullable
    com.example.travalms.data.model.ChatMessage lastMessage, int unreadCount, @org.jetbrains.annotations.Nullable
    java.lang.String originalId) {
        super();
    }
    
    public final int component1() {
        return 0;
    }
    
    public final int getId() {
        return 0;
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
    public final java.lang.String getStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getAvatarUrl() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final org.jxmpp.jid.BareJid component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final org.jxmpp.jid.BareJid getJid() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.example.travalms.data.model.ChatMessage component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.example.travalms.data.model.ChatMessage getLastMessage() {
        return null;
    }
    
    public final int component7() {
        return 0;
    }
    
    public final int getUnreadCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getOriginalId() {
        return null;
    }
}