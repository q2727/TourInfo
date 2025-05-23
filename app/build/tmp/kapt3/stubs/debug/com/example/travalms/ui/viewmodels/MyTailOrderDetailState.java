package com.example.travalms.ui.viewmodels;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.travalms.api.dto.TailOrderResponse;
import com.example.travalms.data.api.NetworkModule;
import com.example.travalms.data.api.TailOrderApiService;
import com.example.travalms.data.remote.XMPPManager;
import com.example.travalms.ui.screens.TailOrder;
import kotlinx.coroutines.flow.StateFlow;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 我的尾单详情视图模型状态
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\'\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\u0002\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u000f\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010\u0010\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J+\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u00c6\u0001J\u0013\u0010\u0012\u001a\u00020\u00032\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0014\u001a\u00020\u0015H\u00d6\u0001J\t\u0010\u0016\u001a\u00020\u0007H\u00d6\u0001R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u000bR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0017"}, d2 = {"Lcom/example/travalms/ui/viewmodels/MyTailOrderDetailState;", "", "isLoading", "", "tailOrder", "Lcom/example/travalms/ui/screens/TailOrder;", "error", "", "(ZLcom/example/travalms/ui/screens/TailOrder;Ljava/lang/String;)V", "getError", "()Ljava/lang/String;", "()Z", "getTailOrder", "()Lcom/example/travalms/ui/screens/TailOrder;", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class MyTailOrderDetailState {
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable
    private final com.example.travalms.ui.screens.TailOrder tailOrder = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String error = null;
    
    /**
     * 我的尾单详情视图模型状态
     */
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.ui.viewmodels.MyTailOrderDetailState copy(boolean isLoading, @org.jetbrains.annotations.Nullable
    com.example.travalms.ui.screens.TailOrder tailOrder, @org.jetbrains.annotations.Nullable
    java.lang.String error) {
        return null;
    }
    
    /**
     * 我的尾单详情视图模型状态
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * 我的尾单详情视图模型状态
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * 我的尾单详情视图模型状态
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public MyTailOrderDetailState() {
        super();
    }
    
    public MyTailOrderDetailState(boolean isLoading, @org.jetbrains.annotations.Nullable
    com.example.travalms.ui.screens.TailOrder tailOrder, @org.jetbrains.annotations.Nullable
    java.lang.String error) {
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
}