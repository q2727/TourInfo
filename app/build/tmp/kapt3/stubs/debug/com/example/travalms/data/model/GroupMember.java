package com.example.travalms.data.model;

import java.time.LocalDateTime;

/**
 * 群聊成员信息
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u000e\n\u0002\u0010\b\n\u0002\b\u0003\b\u0087\b\u0018\u0000 \u001c2\u00020\u0001:\u0001\u001cB%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0007J\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J1\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0017\u001a\u00020\u000b2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001J\t\u0010\u001b\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u000b8F\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\tR\u0011\u0010\u0010\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\t\u00a8\u0006\u001d"}, d2 = {"Lcom/example/travalms/data/model/GroupMember;", "", "jid", "", "nickname", "role", "affiliation", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getAffiliation", "()Ljava/lang/String;", "isAdmin", "", "()Z", "getJid", "getNickname", "getRole", "status", "getStatus", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "", "toString", "Companion", "app_debug"})
public final class GroupMember {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String jid = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String nickname = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String role = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String affiliation = null;
    @org.jetbrains.annotations.NotNull
    public static final com.example.travalms.data.model.GroupMember.Companion Companion = null;
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String ROLE_MODERATOR = "moderator";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String ROLE_PARTICIPANT = "participant";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String ROLE_VISITOR = "visitor";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String ROLE_NONE = "none";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String AFFILIATION_OWNER = "owner";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String AFFILIATION_ADMIN = "admin";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String AFFILIATION_MEMBER = "member";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String AFFILIATION_NONE = "none";
    
    /**
     * 群聊成员信息
     */
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.data.model.GroupMember copy(@org.jetbrains.annotations.NotNull
    java.lang.String jid, @org.jetbrains.annotations.NotNull
    java.lang.String nickname, @org.jetbrains.annotations.NotNull
    java.lang.String role, @org.jetbrains.annotations.NotNull
    java.lang.String affiliation) {
        return null;
    }
    
    /**
     * 群聊成员信息
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * 群聊成员信息
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * 群聊成员信息
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public GroupMember(@org.jetbrains.annotations.NotNull
    java.lang.String jid, @org.jetbrains.annotations.NotNull
    java.lang.String nickname, @org.jetbrains.annotations.NotNull
    java.lang.String role, @org.jetbrains.annotations.NotNull
    java.lang.String affiliation) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
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
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getRole() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getAffiliation() {
        return null;
    }
    
    public final boolean isAdmin() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getStatus() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/example/travalms/data/model/GroupMember$Companion;", "", "()V", "AFFILIATION_ADMIN", "", "AFFILIATION_MEMBER", "AFFILIATION_NONE", "AFFILIATION_OWNER", "ROLE_MODERATOR", "ROLE_NONE", "ROLE_PARTICIPANT", "ROLE_VISITOR", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}