package com.example.travalms.model;

import java.lang.System;

/**
 * 群聊成员信息模型
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0011\n\u0002\u0010\b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001:\u0002%&B=\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u000bJ\u000b\u0010\u0019\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\bH\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0003H\u00c6\u0003JG\u0010\u001f\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010 \u001a\u00020\u00112\b\u0010!\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\"\u001a\u00020#H\u00d6\u0001J\t\u0010$\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0010\u001a\u00020\u00118F\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0012R\u0011\u0010\u0013\u001a\u00020\u00118F\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0012R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000fR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000fR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u000f\u00a8\u0006\'"}, d2 = {"Lcom/example/travalms/model/GroupMember;", "", "jid", "", "nickname", "affiliation", "Lcom/example/travalms/model/GroupMember$Affiliation;", "role", "Lcom/example/travalms/model/GroupMember$Role;", "status", "avatarUrl", "(Ljava/lang/String;Ljava/lang/String;Lcom/example/travalms/model/GroupMember$Affiliation;Lcom/example/travalms/model/GroupMember$Role;Ljava/lang/String;Ljava/lang/String;)V", "getAffiliation", "()Lcom/example/travalms/model/GroupMember$Affiliation;", "getAvatarUrl", "()Ljava/lang/String;", "isAdmin", "", "()Z", "isMember", "getJid", "getNickname", "getRole", "()Lcom/example/travalms/model/GroupMember$Role;", "getStatus", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "", "toString", "Affiliation", "Role", "app_debug"})
public final class GroupMember {
    @org.jetbrains.annotations.Nullable
    private final java.lang.String jid = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String nickname = null;
    @org.jetbrains.annotations.NotNull
    private final com.example.travalms.model.GroupMember.Affiliation affiliation = null;
    @org.jetbrains.annotations.NotNull
    private final com.example.travalms.model.GroupMember.Role role = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String status = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String avatarUrl = null;
    
    /**
     * 群聊成员信息模型
     */
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.model.GroupMember copy(@org.jetbrains.annotations.Nullable
    java.lang.String jid, @org.jetbrains.annotations.NotNull
    java.lang.String nickname, @org.jetbrains.annotations.NotNull
    com.example.travalms.model.GroupMember.Affiliation affiliation, @org.jetbrains.annotations.NotNull
    com.example.travalms.model.GroupMember.Role role, @org.jetbrains.annotations.NotNull
    java.lang.String status, @org.jetbrains.annotations.NotNull
    java.lang.String avatarUrl) {
        return null;
    }
    
    /**
     * 群聊成员信息模型
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * 群聊成员信息模型
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * 群聊成员信息模型
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public GroupMember(@org.jetbrains.annotations.Nullable
    java.lang.String jid, @org.jetbrains.annotations.NotNull
    java.lang.String nickname, @org.jetbrains.annotations.NotNull
    com.example.travalms.model.GroupMember.Affiliation affiliation, @org.jetbrains.annotations.NotNull
    com.example.travalms.model.GroupMember.Role role, @org.jetbrains.annotations.NotNull
    java.lang.String status, @org.jetbrains.annotations.NotNull
    java.lang.String avatarUrl) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getJid() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getNickname() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.model.GroupMember.Affiliation component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.model.GroupMember.Affiliation getAffiliation() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.model.GroupMember.Role component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.model.GroupMember.Role getRole() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getAvatarUrl() {
        return null;
    }
    
    public final boolean isAdmin() {
        return false;
    }
    
    public final boolean isMember() {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007\u00a8\u0006\b"}, d2 = {"Lcom/example/travalms/model/GroupMember$Affiliation;", "", "(Ljava/lang/String;I)V", "OWNER", "ADMIN", "MEMBER", "OUTCAST", "NONE", "app_debug"})
    public static enum Affiliation {
        /*public static final*/ OWNER /* = new OWNER() */,
        /*public static final*/ ADMIN /* = new ADMIN() */,
        /*public static final*/ MEMBER /* = new MEMBER() */,
        /*public static final*/ OUTCAST /* = new OUTCAST() */,
        /*public static final*/ NONE /* = new NONE() */;
        
        Affiliation() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/example/travalms/model/GroupMember$Role;", "", "(Ljava/lang/String;I)V", "MODERATOR", "PARTICIPANT", "VISITOR", "NONE", "app_debug"})
    public static enum Role {
        /*public static final*/ MODERATOR /* = new MODERATOR() */,
        /*public static final*/ PARTICIPANT /* = new PARTICIPANT() */,
        /*public static final*/ VISITOR /* = new VISITOR() */,
        /*public static final*/ NONE /* = new NONE() */;
        
        Role() {
        }
    }
}