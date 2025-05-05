package com.example.travalms.data.remote;

import android.util.Log;
import com.example.travalms.model.GroupChatMessage;
import com.example.travalms.data.model.GroupMember;
import com.example.travalms.data.model.GroupRoom;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.SharedFlow;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import java.time.LocalDateTime;
import org.jivesoftware.smack.SmackConfiguration;
import com.example.travalms.data.model.ChatInvitation;
import kotlinx.coroutines.flow.StateFlow;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.InvitationListener;
import java.util.UUID;

/**
 * XMPPGroupChatManager - 专门负责XMPP群聊(MUC)功能的类
 *
 * 将XMPPManager中的群聊功能解构出来，实现更清晰的代码组织
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u009a\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010#\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\r\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J*\u0010-\u001a\b\u0012\u0004\u0012\u00020(0.2\u0006\u0010/\u001a\u00020\u0010H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b0\u00101J\u000e\u00102\u001a\u00020(2\u0006\u0010/\u001a\u00020\u0010J\u0010\u00103\u001a\u00020\u00062\u0006\u00104\u001a\u00020\u0006H\u0002J\u0006\u00105\u001a\u00020(JP\u00106\u001a\b\u0012\u0004\u0012\u00020\'0.2\u0006\u00107\u001a\u00020\u00062\u0006\u00108\u001a\u00020\u00062\b\b\u0002\u00109\u001a\u00020\u00062\b\b\u0002\u0010:\u001a\u00020;2\b\b\u0002\u0010<\u001a\u00020;H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b=\u0010>JP\u0010?\u001a\b\u0012\u0004\u0012\u00020\'0.2\u0006\u00107\u001a\u00020\u00062\u0006\u00108\u001a\u00020\u00062\b\b\u0002\u00109\u001a\u00020\u00062\b\b\u0002\u0010:\u001a\u00020;2\b\b\u0002\u0010<\u001a\u00020;H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b@\u0010>J4\u0010A\u001a\b\u0012\u0004\u0012\u00020(0.2\u0006\u0010B\u001a\u00020\u00062\b\b\u0002\u0010C\u001a\u00020\u0006H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\bD\u0010EJ(\u0010F\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\'0\u000f0.H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\bG\u0010HJ\f\u0010I\u001a\b\u0012\u0004\u0012\u00020\u00060\u000fJ\u0017\u0010J\u001a\b\u0012\u0004\u0012\u00020\'0\u000fH\u0086@\u00f8\u0001\u0002\u00a2\u0006\u0002\u0010HJ0\u0010K\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020L0\u000f0.2\u0006\u0010B\u001a\u00020\u0006H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\bM\u0010NJ\u0010\u0010O\u001a\u00020\u00062\u0006\u0010P\u001a\u00020\u0006H\u0002J\u0006\u0010Q\u001a\u00020(J<\u0010R\u001a\b\u0012\u0004\u0012\u00020(0.2\u0006\u0010B\u001a\u00020\u00062\u0006\u0010S\u001a\u00020\u00062\b\b\u0002\u0010C\u001a\u00020\u0006H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\bT\u0010UJ\b\u0010V\u001a\u00020;H\u0002J2\u0010W\u001a\b\u0012\u0004\u0012\u00020\'0.2\u0006\u0010B\u001a\u00020\u00062\u0006\u00108\u001a\u00020\u0006H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\bX\u0010EJ*\u0010Y\u001a\b\u0012\u0004\u0012\u00020(0.2\u0006\u0010B\u001a\u00020\u0006H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\bZ\u0010NJ\u0006\u0010[\u001a\u00020(J\u0010\u0010\\\u001a\u00020(2\u0006\u0010]\u001a\u00020^H\u0002J\u000e\u0010_\u001a\u00020(2\u0006\u0010/\u001a\u00020\u0010J\u000e\u0010`\u001a\u00020(2\u0006\u0010a\u001a\u00020\u0006J2\u0010b\u001a\b\u0012\u0004\u0012\u00020(0.2\u0006\u0010B\u001a\u00020\u00062\u0006\u0010]\u001a\u00020\u0006H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\bc\u0010EJ\u001a\u0010d\u001a\u00020(2\u0012\u0010e\u001a\u000e\u0012\u0004\u0012\u00020\'\u0012\u0004\u0012\u00020(0&J\b\u0010f\u001a\u00020(H\u0002J\u0006\u0010g\u001a\u00020(J\u0006\u0010h\u001a\u00020(J\u0006\u0010i\u001a\u00020(J\b\u0010j\u001a\u00020(H\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\b0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\f0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u001d\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f0\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0010\u0010 \u001a\u0004\u0018\u00010!X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0013\u0010\"\u001a\u0004\u0018\u00010\u00128F\u00a2\u0006\u0006\u001a\u0004\b#\u0010$R\u001c\u0010%\u001a\u0010\u0012\u0004\u0012\u00020\'\u0012\u0004\u0012\u00020(\u0018\u00010&X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00060*X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010+\u001a\b\u0012\u0004\u0012\u00020\b0\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u001fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000f\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\n\u0002\b\u0019\u00a8\u0006k"}, d2 = {"Lcom/example/travalms/data/remote/XMPPGroupChatManager;", "", "xmppManager", "Lcom/example/travalms/data/remote/XMPPManager;", "(Lcom/example/travalms/data/remote/XMPPManager;)V", "CONFERENCE_DOMAIN", "", "MAX_RECENT_IDS", "", "TAG", "_groupMessageFlow", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "Lcom/example/travalms/model/GroupChatMessage;", "_invitations", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/example/travalms/data/model/ChatInvitation;", "_mucManager", "Lorg/jivesoftware/smackx/muc/MultiUserChatManager;", "_unreadInvitationCount", "connectionListener", "Lorg/jivesoftware/smack/ConnectionListener;", "groupChatMessageListener", "Lorg/jivesoftware/smack/StanzaListener;", "groupMessageFlow", "Lkotlinx/coroutines/flow/SharedFlow;", "getGroupMessageFlow", "()Lkotlinx/coroutines/flow/SharedFlow;", "invitations", "Lkotlinx/coroutines/flow/StateFlow;", "getInvitations", "()Lkotlinx/coroutines/flow/StateFlow;", "mucInvitationListener", "Lorg/jivesoftware/smackx/muc/InvitationListener;", "mucManager", "getMucManager", "()Lorg/jivesoftware/smackx/muc/MultiUserChatManager;", "onJoinRoomListener", "Lkotlin/Function1;", "Lcom/example/travalms/data/model/GroupRoom;", "", "recentlyProcessedMessageIds", "", "unreadInvitationCount", "getUnreadInvitationCount", "acceptInvitation", "Lkotlin/Result;", "invitation", "acceptInvitation-gIAlu-s", "(Lcom/example/travalms/data/model/ChatInvitation;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addInvitation", "cleanJidString", "jidString", "cleanupOnLogout", "createGroupRoom", "roomName", "nickname", "description", "membersOnly", "", "persistent", "createGroupRoom-hUnOzRk", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createGroupRoomEnhanced", "createGroupRoomEnhanced-hUnOzRk", "destroyRoom", "roomJid", "reason", "destroyRoom-0E7RQCE", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAvailableRooms", "getAvailableRooms-IoAF18A", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getJoinedRoomJids", "getJoinedRooms", "getRoomMembers", "Lcom/example/travalms/data/model/GroupMember;", "getRoomMembers-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRoomNameFromJid", "roomJidStr", "initMucManager", "inviteUserToRoom", "userJid", "inviteUserToRoom-BWLJW6A", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "isMucManagerInitialized", "joinRoom", "joinRoom-0E7RQCE", "leaveRoom", "leaveRoom-gIAlu-s", "markAllInvitationsAsRead", "processGroupChatMessage", "message", "Lorg/jivesoftware/smack/packet/Message;", "rejectInvitation", "removeInvitation", "invitationId", "sendGroupMessage", "sendGroupMessage-0E7RQCE", "setOnJoinRoomListener", "listener", "setupGroupChatInvitationListener", "setupGroupChatMessageListener", "syncGroupChatsFromServer", "updateConnectionListener", "updateUnreadInvitationCount", "app_debug"})
public final class XMPPGroupChatManager {
    private final com.example.travalms.data.remote.XMPPManager xmppManager = null;
    private final java.lang.String TAG = "XMPPGroupChatManager";
    private final java.lang.String CONFERENCE_DOMAIN = "conference.localhost";
    private org.jivesoftware.smackx.muc.MultiUserChatManager _mucManager;
    private final kotlinx.coroutines.flow.MutableSharedFlow<com.example.travalms.model.GroupChatMessage> _groupMessageFlow = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.SharedFlow<com.example.travalms.model.GroupChatMessage> groupMessageFlow = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.example.travalms.data.model.ChatInvitation>> _invitations = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.example.travalms.data.model.ChatInvitation>> invitations = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> _unreadInvitationCount = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> unreadInvitationCount = null;
    private org.jivesoftware.smack.StanzaListener groupChatMessageListener;
    private org.jivesoftware.smackx.muc.InvitationListener mucInvitationListener;
    private kotlin.jvm.functions.Function1<? super com.example.travalms.data.model.GroupRoom, kotlin.Unit> onJoinRoomListener;
    private final java.util.Set<java.lang.String> recentlyProcessedMessageIds = null;
    private final int MAX_RECENT_IDS = 100;
    private final org.jivesoftware.smack.ConnectionListener connectionListener = null;
    
    public XMPPGroupChatManager(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.remote.XMPPManager xmppManager) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final org.jivesoftware.smackx.muc.MultiUserChatManager getMucManager() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.SharedFlow<com.example.travalms.model.GroupChatMessage> getGroupMessageFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.example.travalms.data.model.ChatInvitation>> getInvitations() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getUnreadInvitationCount() {
        return null;
    }
    
    /**
     * 设置房间加入监听器
     * @param listener 当成功加入房间时调用的回调函数
     */
    public final void setOnJoinRoomListener(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.example.travalms.data.model.GroupRoom, kotlin.Unit> listener) {
    }
    
    /**
     * 更新连接监听器
     */
    public final void updateConnectionListener() {
    }
    
    /**
     * 初始化MUC管理器
     */
    public final void initMucManager() {
    }
    
    /**
     * 设置群聊消息监听器
     */
    public final void setupGroupChatMessageListener() {
    }
    
    /**
     * 处理群聊消息
     */
    private final void processGroupChatMessage(org.jivesoftware.smack.packet.Message message) {
    }
    
    private final java.lang.String getRoomNameFromJid(java.lang.String roomJidStr) {
        return null;
    }
    
    /**
     * 设置群聊邀请监听器
     */
    private final void setupGroupChatInvitationListener() {
    }
    
    /**
     * 辅助方法: 清理JID字符串，移除不合法字符
     */
    private final java.lang.String cleanJidString(java.lang.String jidString) {
        return null;
    }
    
    /**
     * 添加新的邀请
     */
    public final void addInvitation(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.model.ChatInvitation invitation) {
    }
    
    /**
     * 删除邀请
     */
    public final void removeInvitation(@org.jetbrains.annotations.NotNull
    java.lang.String invitationId) {
    }
    
    /**
     * 更新未读邀请数量
     */
    private final void updateUnreadInvitationCount() {
    }
    
    /**
     * 标记所有邀请为已读
     */
    public final void markAllInvitationsAsRead() {
    }
    
    /**
     * 拒绝邀请
     */
    public final void rejectInvitation(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.model.ChatInvitation invitation) {
    }
    
    /**
     * 获取已加入的房间JID列表
     */
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getJoinedRoomJids() {
        return null;
    }
    
    /**
     * 检查MUC管理器是否已初始化
     */
    private final boolean isMucManagerInitialized() {
        return false;
    }
    
    /**
     * Retrieves a list of rooms the user has joined
     * @return List of GroupRoom objects representing joined rooms
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getJoinedRooms(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.example.travalms.data.model.GroupRoom>> continuation) {
        return null;
    }
    
    /**
     * Cleans up resources and listeners when the user logs out or the connection is lost.
     * This should be called from XMPPManager during its disconnect/logout process.
     */
    public final void cleanupOnLogout() {
    }
    
    /**
     * 从服务器同步群聊信息
     * 通过XMPPServiceLocator获取GroupChatJoinHandler实例并调用其同步方法
     */
    public final void syncGroupChatsFromServer() {
    }
}