package com.example.travalms.ui.viewmodels;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.example.travalms.data.remote.XMPPManager;
import com.example.travalms.ui.screens.SubscriptionNodeItem;
import kotlinx.coroutines.flow.StateFlow;

/**
 * 订阅视图模型状态数据类
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0012\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BE\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\u0002\u0010\nJ\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u00c6\u0003J\u000f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u00c6\u0003J\u000b\u0010\u0015\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003JI\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0007H\u00c6\u0001J\u0013\u0010\u0017\u001a\u00020\u00032\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001J\t\u0010\u001b\u001a\u00020\u0007H\u00d6\u0001R\u0013\u0010\t\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\rR\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000f\u00a8\u0006\u001c"}, d2 = {"Lcom/example/travalms/ui/viewmodels/SubscriptionState;", "", "isLoading", "", "isLoggedIn", "selectedNodeIds", "", "", "subscribedNodeIds", "errorMessage", "(ZZLjava/util/List;Ljava/util/List;Ljava/lang/String;)V", "getErrorMessage", "()Ljava/lang/String;", "()Z", "getSelectedNodeIds", "()Ljava/util/List;", "getSubscribedNodeIds", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class SubscriptionState {
    private final boolean isLoading = false;
    private final boolean isLoggedIn = false;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<java.lang.String> selectedNodeIds = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<java.lang.String> subscribedNodeIds = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String errorMessage = null;
    
    /**
     * 订阅视图模型状态数据类
     */
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.ui.viewmodels.SubscriptionState copy(boolean isLoading, boolean isLoggedIn, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> selectedNodeIds, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> subscribedNodeIds, @org.jetbrains.annotations.Nullable
    java.lang.String errorMessage) {
        return null;
    }
    
    /**
     * 订阅视图模型状态数据类
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * 订阅视图模型状态数据类
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * 订阅视图模型状态数据类
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public SubscriptionState() {
        super();
    }
    
    public SubscriptionState(boolean isLoading, boolean isLoggedIn, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> selectedNodeIds, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> subscribedNodeIds, @org.jetbrains.annotations.Nullable
    java.lang.String errorMessage) {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    public final boolean component2() {
        return false;
    }
    
    public final boolean isLoggedIn() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getSelectedNodeIds() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getSubscribedNodeIds() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getErrorMessage() {
        return null;
    }
}