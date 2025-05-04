package com.example.travalms.ui.viewmodels;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.example.travalms.model.GroupChatMessage;
import com.example.travalms.data.model.GroupMember;
import com.example.travalms.data.model.GroupRoom;
import com.example.travalms.data.remote.XMPPManager;
import com.example.travalms.data.repository.GroupChatRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.inject.Inject;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 群聊ViewModel，负责管理群聊数据和操作
 */
@dagger.hilt.android.lifecycle.HiltViewModel
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010$\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010#\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u000f\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010#\u001a\u00020$J\u0014\u0010%\u001a\b\u0012\u0004\u0012\u00020\u001e0\u00112\u0006\u0010&\u001a\u00020\bJ\u0014\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\u0006\u0010&\u001a\u00020\bJ\u001a\u0010(\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00110\u00142\u0006\u0010&\u001a\u00020\bJ\u0016\u0010)\u001a\u00020$2\u0006\u0010&\u001a\u00020\b2\u0006\u0010*\u001a\u00020\bJ\u0016\u0010+\u001a\u00020$2\u0006\u0010&\u001a\u00020\b2\u0006\u0010,\u001a\u00020\bJ\u0010\u0010-\u001a\u00020$2\u0006\u0010&\u001a\u00020\bH\u0002J\u000e\u0010.\u001a\u00020$2\u0006\u0010&\u001a\u00020\bJ\u001a\u0010/\u001a\u00020$2\u0006\u00100\u001a\u00020\u00122\b\b\u0002\u00101\u001a\u00020\u000eH\u0002J\u0016\u00102\u001a\u00020$2\u0006\u0010&\u001a\u00020\b2\u0006\u00100\u001a\u00020\bR\u000e\u0010\u0007\u001a\u00020\bX\u0082D\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R&\u0010\u000f\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00110\u00100\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0013\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0019\u0010\u0017\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0016R \u0010\u001b\u001a\u0014\u0012\u0004\u0012\u00020\b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001e0\u001d0\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R)\u0010\u001f\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00110\u00100\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0016R\u0014\u0010!\u001a\b\u0012\u0004\u0012\u00020\b0\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00063"}, d2 = {"Lcom/example/travalms/ui/viewmodels/GroupChatViewModel;", "Landroidx/lifecycle/ViewModel;", "xmppManager", "Lcom/example/travalms/data/remote/XMPPManager;", "groupChatRepository", "Lcom/example/travalms/data/repository/GroupChatRepository;", "(Lcom/example/travalms/data/remote/XMPPManager;Lcom/example/travalms/data/repository/GroupChatRepository;)V", "TAG", "", "_currentRoom", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/example/travalms/data/model/GroupRoom;", "_errorMessage", "_loading", "", "_roomMessages", "", "", "Lcom/example/travalms/model/GroupChatMessage;", "currentRoom", "Lkotlinx/coroutines/flow/StateFlow;", "getCurrentRoom", "()Lkotlinx/coroutines/flow/StateFlow;", "errorMessage", "getErrorMessage", "loading", "getLoading", "roomMembers", "Ljava/util/concurrent/ConcurrentHashMap;", "", "Lcom/example/travalms/data/model/GroupMember;", "roomMessagesState", "getRoomMessagesState", "sentMessageFingerprints", "", "clearError", "", "getRoomMembers", "roomJid", "getRoomMessages", "getRoomMessagesFlow", "inviteUser", "userJid", "joinRoom", "nickname", "loadInitialMessages", "loadRoomMembers", "processIncomingMessage", "message", "saveToDb", "sendMessage", "app_debug"})
public final class GroupChatViewModel extends androidx.lifecycle.ViewModel {
    private final com.example.travalms.data.remote.XMPPManager xmppManager = null;
    private final com.example.travalms.data.repository.GroupChatRepository groupChatRepository = null;
    private final java.lang.String TAG = "GroupChatViewModel";
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.travalms.data.model.GroupRoom> _currentRoom = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.example.travalms.data.model.GroupRoom> currentRoom = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _loading = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> loading = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _errorMessage = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> errorMessage = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.Map<java.lang.String, java.util.List<com.example.travalms.model.GroupChatMessage>>> _roomMessages = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.util.Map<java.lang.String, java.util.List<com.example.travalms.model.GroupChatMessage>>> roomMessagesState = null;
    private final java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.List<com.example.travalms.data.model.GroupMember>> roomMembers = null;
    private final java.util.Set<java.lang.String> sentMessageFingerprints = null;
    
    @javax.inject.Inject
    public GroupChatViewModel(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.remote.XMPPManager xmppManager, @org.jetbrains.annotations.NotNull
    com.example.travalms.data.repository.GroupChatRepository groupChatRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.example.travalms.data.model.GroupRoom> getCurrentRoom() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getLoading() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getErrorMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.util.Map<java.lang.String, java.util.List<com.example.travalms.model.GroupChatMessage>>> getRoomMessagesState() {
        return null;
    }
    
    /**
     * 加入聊天室
     */
    public final void joinRoom(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    java.lang.String nickname) {
    }
    
    /**
     * 加载房间成员列表
     */
    public final void loadRoomMembers(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid) {
    }
    
    /**
     * 发送消息
     */
    public final void sendMessage(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    java.lang.String message) {
    }
    
    /**
     * 邀请用户加入群聊
     */
    public final void inviteUser(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid, @org.jetbrains.annotations.NotNull
    java.lang.String userJid) {
    }
    
    /**
     * 获取特定房间的消息 Flow
     */
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.example.travalms.model.GroupChatMessage>> getRoomMessagesFlow(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid) {
        return null;
    }
    
    /**
     * 获取房间消息 (保留旧方法，可能用于非响应式场景，但主要使用 Flow)
     */
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.example.travalms.model.GroupChatMessage> getRoomMessages(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid) {
        return null;
    }
    
    /**
     * 获取房间成员
     */
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.example.travalms.data.model.GroupMember> getRoomMembers(@org.jetbrains.annotations.NotNull
    java.lang.String roomJid) {
        return null;
    }
    
    /**
     * Function to load initial messages
     */
    private final void loadInitialMessages(java.lang.String roomJid) {
    }
    
    /**
     * 处理收到的消息
     * @param saveToDb Whether to save this message to the database
     */
    private final void processIncomingMessage(com.example.travalms.model.GroupChatMessage message, boolean saveToDb) {
    }
    
    /**
     * 清除错误信息
     */
    public final void clearError() {
    }
}