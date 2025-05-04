package com.example.travalms.data.remote;

import android.util.Log;
import javax.inject.Inject;

/**
 * XMPP服务定位器，用于提供对XMPP相关服务的全局访问
 * 这是一个简单的服务定位器模式实现，用于在非依赖注入环境中获取服务实例
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 \u000b2\u00020\u0001:\u0001\u000bB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0004R\"\u0010\u0005\u001a\u0004\u0018\u00010\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\f"}, d2 = {"Lcom/example/travalms/data/remote/XMPPServiceLocator;", "", "()V", "<set-?>", "Lcom/example/travalms/data/remote/GroupChatJoinHandler;", "groupChatJoinHandler", "getGroupChatJoinHandler", "()Lcom/example/travalms/data/remote/GroupChatJoinHandler;", "setGroupChatJoinHandler", "", "handler", "Companion", "app_debug"})
public final class XMPPServiceLocator {
    @org.jetbrains.annotations.NotNull
    public static final com.example.travalms.data.remote.XMPPServiceLocator.Companion Companion = null;
    private static final java.lang.String TAG = "XMPPServiceLocator";
    @kotlin.jvm.Volatile
    private static volatile com.example.travalms.data.remote.XMPPServiceLocator INSTANCE;
    @org.jetbrains.annotations.Nullable
    private com.example.travalms.data.remote.GroupChatJoinHandler groupChatJoinHandler;
    
    private XMPPServiceLocator() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.example.travalms.data.remote.GroupChatJoinHandler getGroupChatJoinHandler() {
        return null;
    }
    
    /**
     * 设置群聊加入处理器
     * 应在应用启动时通过依赖注入设置
     */
    public final void setGroupChatJoinHandler(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.remote.GroupChatJoinHandler handler) {
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0007\u001a\u00020\u0004R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/example/travalms/data/remote/XMPPServiceLocator$Companion;", "", "()V", "INSTANCE", "Lcom/example/travalms/data/remote/XMPPServiceLocator;", "TAG", "", "getInstance", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.example.travalms.data.remote.XMPPServiceLocator getInstance() {
            return null;
        }
    }
}