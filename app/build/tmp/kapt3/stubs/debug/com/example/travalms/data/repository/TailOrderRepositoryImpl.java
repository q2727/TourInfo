package com.example.travalms.data.repository;

import com.example.travalms.ui.screens.TailOrder;
import com.example.travalms.data.remote.XMPPManager;

/**
 * TailOrder仓库实现，使用单例模式
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010#\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0007\u0018\u0000 \u00162\u00020\u0001:\u0001\u0016B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u0002J\u001b\u0010\f\u001a\u0004\u0018\u00010\b2\u0006\u0010\r\u001a\u00020\u0005H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\b0\u0010H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J!\u0010\u0012\u001a\u00020\u00132\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u0013H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0017"}, d2 = {"Lcom/example/travalms/data/repository/TailOrderRepositoryImpl;", "Lcom/example/travalms/data/repository/TailOrderRepository;", "()V", "favoriteTailOrders", "", "", "mockTailOrders", "", "Lcom/example/travalms/ui/screens/TailOrder;", "xmppManager", "Lcom/example/travalms/data/remote/XMPPManager;", "createMockTailOrders", "getTailOrderById", "id", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTailOrders", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateFavoriteStatus", "", "isFavorite", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class TailOrderRepositoryImpl implements com.example.travalms.data.repository.TailOrderRepository {
    @org.jetbrains.annotations.NotNull
    public static final com.example.travalms.data.repository.TailOrderRepositoryImpl.Companion Companion = null;
    @kotlin.jvm.Volatile
    private static volatile com.example.travalms.data.repository.TailOrderRepositoryImpl INSTANCE;
    private final com.example.travalms.data.remote.XMPPManager xmppManager = null;
    private final java.util.List<com.example.travalms.ui.screens.TailOrder> mockTailOrders = null;
    private final java.util.Set<java.lang.String> favoriteTailOrders = null;
    
    private TailOrderRepositoryImpl() {
        super();
    }
    
    /**
     * 获取所有尾单列表
     */
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public java.lang.Object getTailOrders(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.example.travalms.ui.screens.TailOrder>> continuation) {
        return null;
    }
    
    /**
     * 根据ID获取尾单详情
     */
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public java.lang.Object getTailOrderById(@org.jetbrains.annotations.NotNull
    java.lang.String id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.example.travalms.ui.screens.TailOrder> continuation) {
        return null;
    }
    
    /**
     * 更新尾单收藏状态
     */
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public java.lang.Object updateFavoriteStatus(@org.jetbrains.annotations.NotNull
    java.lang.String id, boolean isFavorite, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Boolean> continuation) {
        return null;
    }
    
    private final java.util.List<com.example.travalms.ui.screens.TailOrder> createMockTailOrders() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0004R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/example/travalms/data/repository/TailOrderRepositoryImpl$Companion;", "", "()V", "INSTANCE", "Lcom/example/travalms/data/repository/TailOrderRepositoryImpl;", "getInstance", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.example.travalms.data.repository.TailOrderRepositoryImpl getInstance() {
            return null;
        }
    }
}