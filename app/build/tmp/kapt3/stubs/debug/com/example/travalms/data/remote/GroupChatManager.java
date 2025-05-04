package com.example.travalms.data.remote;

import android.util.Log;
import com.example.travalms.data.repository.GroupChatRepository;
import kotlinx.coroutines.Dispatchers;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 群聊管理器，负责管理群聊的加入和离开
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u000e\u001a\u00020\r2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\r0\fR\u000e\u0010\u0007\u001a\u00020\bX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\r\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/example/travalms/data/remote/GroupChatManager;", "", "groupChatRepository", "Lcom/example/travalms/data/repository/GroupChatRepository;", "xmppManager", "Lcom/example/travalms/data/remote/XMPPManager;", "(Lcom/example/travalms/data/repository/GroupChatRepository;Lcom/example/travalms/data/remote/XMPPManager;)V", "TAG", "", "coroutineScope", "Lkotlinx/coroutines/CoroutineScope;", "onGroupChatsJoinedCallback", "Lkotlin/Function0;", "", "setOnGroupChatsJoinedCallback", "callback", "app_debug"})
@javax.inject.Singleton
public final class GroupChatManager {
    private final com.example.travalms.data.repository.GroupChatRepository groupChatRepository = null;
    private final com.example.travalms.data.remote.XMPPManager xmppManager = null;
    private final java.lang.String TAG = "GroupChatManager";
    private final kotlinx.coroutines.CoroutineScope coroutineScope = null;
    private kotlin.jvm.functions.Function0<kotlin.Unit> onGroupChatsJoinedCallback;
    
    @javax.inject.Inject
    public GroupChatManager(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.repository.GroupChatRepository groupChatRepository, @org.jetbrains.annotations.NotNull
    com.example.travalms.data.remote.XMPPManager xmppManager) {
        super();
    }
    
    /**
     * 设置群聊加入完成回调
     */
    public final void setOnGroupChatsJoinedCallback(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> callback) {
    }
}