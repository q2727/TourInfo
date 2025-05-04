package com.example.travalms.ui.viewmodels;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.example.travalms.data.model.ChatInvitation;
import com.example.travalms.data.remote.XMPPManager;
import kotlinx.coroutines.flow.StateFlow;

/**
 * 管理群聊邀请的ViewModel
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\bJ\u0006\u0010\u0016\u001a\u00020\u0014J\u0006\u0010\u0017\u001a\u00020\u0014J\u000e\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\bJ\b\u0010\u0019\u001a\u00020\u0014H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\n0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010\u00a8\u0006\u001a"}, d2 = {"Lcom/example/travalms/ui/viewmodels/InvitationViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "TAG", "", "_invitations", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/example/travalms/data/model/ChatInvitation;", "_unreadCount", "", "groupChatManager", "Lcom/example/travalms/data/remote/XMPPGroupChatManager;", "invitations", "Lkotlinx/coroutines/flow/StateFlow;", "getInvitations", "()Lkotlinx/coroutines/flow/StateFlow;", "unreadCount", "getUnreadCount", "acceptInvitation", "", "invitation", "markAllAsRead", "refreshInvitations", "rejectInvitation", "updateUnreadCount", "app_debug"})
public final class InvitationViewModel extends androidx.lifecycle.ViewModel {
    private final java.lang.String TAG = "InvitationViewModel";
    private final com.example.travalms.data.remote.XMPPGroupChatManager groupChatManager = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.example.travalms.data.model.ChatInvitation>> _invitations = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.example.travalms.data.model.ChatInvitation>> invitations = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> _unreadCount = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> unreadCount = null;
    
    public InvitationViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.example.travalms.data.model.ChatInvitation>> getInvitations() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getUnreadCount() {
        return null;
    }
    
    /**
     * 接受邀请
     */
    public final void acceptInvitation(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.model.ChatInvitation invitation) {
    }
    
    /**
     * 拒绝邀请
     */
    public final void rejectInvitation(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.model.ChatInvitation invitation) {
    }
    
    /**
     * 更新未读邀请数量
     */
    private final void updateUnreadCount() {
    }
    
    /**
     * 标记所有邀请为已读
     */
    public final void markAllAsRead() {
    }
    
    /**
     * 手动刷新邀请列表
     */
    public final void refreshInvitations() {
    }
}