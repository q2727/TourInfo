package com.example.travalms.ui.viewmodels;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.example.travalms.data.remote.XMPPManager;
import com.example.travalms.ui.screens.SubscriptionNodeItem;
import kotlinx.coroutines.flow.StateFlow;

/**
 * 订阅视图模型，简化版，专注于订阅功能
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\b\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0014\u0010\u000e\u001a\u00020\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011J\u0006\u0010\u0013\u001a\u00020\u000fJ\b\u0010\u0014\u001a\u00020\u000fH\u0002J\b\u0010\u0015\u001a\u00020\u000fH\u0014J\u0014\u0010\u0016\u001a\u00020\u000f2\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00040\u0011J\u0014\u0010\u0018\u001a\u00020\u000f2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00040\u0011R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/example/travalms/ui/viewmodels/SubscriptionViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "TAG", "", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/example/travalms/ui/viewmodels/SubscriptionState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "xmppManager", "Lcom/example/travalms/data/remote/XMPPManager;", "buildNodeNameMap", "", "nodes", "", "Lcom/example/travalms/ui/screens/SubscriptionNodeItem;", "clearError", "loadSubscribedNodes", "onCleared", "setSelectedNodeIds", "nodeIds", "subscribeToNodes", "selectedNodes", "app_debug"})
public final class SubscriptionViewModel extends androidx.lifecycle.ViewModel {
    private final com.example.travalms.data.remote.XMPPManager xmppManager = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.travalms.ui.viewmodels.SubscriptionState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.example.travalms.ui.viewmodels.SubscriptionState> uiState = null;
    private final java.lang.String TAG = "SubscriptionViewModel";
    
    public SubscriptionViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.example.travalms.ui.viewmodels.SubscriptionState> getUiState() {
        return null;
    }
    
    /**
     * 加载已订阅的节点列表
     */
    private final void loadSubscribedNodes() {
    }
    
    /**
     * 订阅选中的节点
     */
    public final void subscribeToNodes(@org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> selectedNodes) {
    }
    
    /**
     * 构建节点ID到名称的映射
     */
    public final void buildNodeNameMap(@org.jetbrains.annotations.NotNull
    java.util.List<com.example.travalms.ui.screens.SubscriptionNodeItem> nodes) {
    }
    
    /**
     * 设置选中的节点ID列表
     */
    public final void setSelectedNodeIds(@org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> nodeIds) {
    }
    
    /**
     * 清除错误消息
     */
    public final void clearError() {
    }
    
    @java.lang.Override
    protected void onCleared() {
    }
}