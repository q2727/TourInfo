package com.example.travalms.ui.viewmodels;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.travalms.data.api.NetworkModule;
import com.example.travalms.data.api.UserApiService;
import com.example.travalms.ui.screens.TailOrder;
import com.example.travalms.ui.screens.PostItem;
import com.example.travalms.ui.viewmodels.MyPublishedTailsViewModel;
import kotlinx.coroutines.flow.StateFlow;

/**
 * 尾单详情视图模型状态
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010$\n\u0002\b\u0010\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B?\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\u0016\b\u0002\u0010\b\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0001\u0018\u00010\t\u00a2\u0006\u0002\u0010\nJ\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u0013\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010\u0014\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\u0017\u0010\u0015\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0001\u0018\u00010\tH\u00c6\u0003JC\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\u0016\b\u0002\u0010\b\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0001\u0018\u00010\tH\u00c6\u0001J\u0013\u0010\u0017\u001a\u00020\u00032\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001J\t\u0010\u001b\u001a\u00020\u0007H\u00d6\u0001R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\rR\u001f\u0010\b\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0001\u0018\u00010\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\u001c"}, d2 = {"Lcom/example/travalms/ui/viewmodels/TailOrderDetailState;", "", "isLoading", "", "tailOrder", "Lcom/example/travalms/ui/screens/TailOrder;", "error", "", "publisherInfo", "", "(ZLcom/example/travalms/ui/screens/TailOrder;Ljava/lang/String;Ljava/util/Map;)V", "getError", "()Ljava/lang/String;", "()Z", "getPublisherInfo", "()Ljava/util/Map;", "getTailOrder", "()Lcom/example/travalms/ui/screens/TailOrder;", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class TailOrderDetailState {
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable
    private final com.example.travalms.ui.screens.TailOrder tailOrder = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String error = null;
    @org.jetbrains.annotations.Nullable
    private final java.util.Map<java.lang.String, java.lang.Object> publisherInfo = null;
    
    /**
     * 尾单详情视图模型状态
     */
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.ui.viewmodels.TailOrderDetailState copy(boolean isLoading, @org.jetbrains.annotations.Nullable
    com.example.travalms.ui.screens.TailOrder tailOrder, @org.jetbrains.annotations.Nullable
    java.lang.String error, @org.jetbrains.annotations.Nullable
    java.util.Map<java.lang.String, ? extends java.lang.Object> publisherInfo) {
        return null;
    }
    
    /**
     * 尾单详情视图模型状态
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * 尾单详情视图模型状态
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * 尾单详情视图模型状态
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public TailOrderDetailState() {
        super();
    }
    
    public TailOrderDetailState(boolean isLoading, @org.jetbrains.annotations.Nullable
    com.example.travalms.ui.screens.TailOrder tailOrder, @org.jetbrains.annotations.Nullable
    java.lang.String error, @org.jetbrains.annotations.Nullable
    java.util.Map<java.lang.String, ? extends java.lang.Object> publisherInfo) {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.example.travalms.ui.screens.TailOrder component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.example.travalms.ui.screens.TailOrder getTailOrder() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getError() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.util.Map<java.lang.String, java.lang.Object> component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.util.Map<java.lang.String, java.lang.Object> getPublisherInfo() {
        return null;
    }
}