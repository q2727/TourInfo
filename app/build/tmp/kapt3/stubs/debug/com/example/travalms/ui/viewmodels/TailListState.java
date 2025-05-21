package com.example.travalms.ui.viewmodels;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.travalms.data.remote.XMPPManager;
import com.example.travalms.data.remote.PubSubNotification;
import com.example.travalms.data.repository.TailOrderRepository;
import com.example.travalms.data.repository.TailOrderRepositoryImpl;
import com.example.travalms.ui.screens.TailOrder;
import kotlinx.coroutines.flow.StateFlow;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 尾单列表视图模型状态
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0016\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BU\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\u0005\u0012\b\b\u0002\u0010\n\u001a\u00020\b\u0012\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\u0002\u0010\fJ\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\u000b\u0010\u0017\u001a\u0004\u0018\u00010\bH\u00c6\u0003J\u000f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\b0\u0005H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\bH\u00c6\u0003J\u000f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003JY\u0010\u001b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\u00052\b\b\u0002\u0010\n\u001a\u00020\b2\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0001J\u0013\u0010\u001c\u001a\u00020\u00032\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001e\u001a\u00020\u001fH\u00d6\u0001J\t\u0010 \u001a\u00020\bH\u00d6\u0001R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0012R\u0011\u0010\n\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000eR\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0010\u00a8\u0006!"}, d2 = {"Lcom/example/travalms/ui/viewmodels/TailListState;", "", "isLoading", "", "tailOrders", "", "Lcom/example/travalms/ui/screens/TailOrder;", "error", "", "favoriteOrderIds", "searchQuery", "filteredTailOrders", "(ZLjava/util/List;Ljava/lang/String;Ljava/util/List;Ljava/lang/String;Ljava/util/List;)V", "getError", "()Ljava/lang/String;", "getFavoriteOrderIds", "()Ljava/util/List;", "getFilteredTailOrders", "()Z", "getSearchQuery", "getTailOrders", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class TailListState {
    private final boolean isLoading = false;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.example.travalms.ui.screens.TailOrder> tailOrders = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String error = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<java.lang.String> favoriteOrderIds = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String searchQuery = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.example.travalms.ui.screens.TailOrder> filteredTailOrders = null;
    
    /**
     * 尾单列表视图模型状态
     */
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.ui.viewmodels.TailListState copy(boolean isLoading, @org.jetbrains.annotations.NotNull
    java.util.List<com.example.travalms.ui.screens.TailOrder> tailOrders, @org.jetbrains.annotations.Nullable
    java.lang.String error, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> favoriteOrderIds, @org.jetbrains.annotations.NotNull
    java.lang.String searchQuery, @org.jetbrains.annotations.NotNull
    java.util.List<com.example.travalms.ui.screens.TailOrder> filteredTailOrders) {
        return null;
    }
    
    /**
     * 尾单列表视图模型状态
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * 尾单列表视图模型状态
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * 尾单列表视图模型状态
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public TailListState() {
        super();
    }
    
    public TailListState(boolean isLoading, @org.jetbrains.annotations.NotNull
    java.util.List<com.example.travalms.ui.screens.TailOrder> tailOrders, @org.jetbrains.annotations.Nullable
    java.lang.String error, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> favoriteOrderIds, @org.jetbrains.annotations.NotNull
    java.lang.String searchQuery, @org.jetbrains.annotations.NotNull
    java.util.List<com.example.travalms.ui.screens.TailOrder> filteredTailOrders) {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.example.travalms.ui.screens.TailOrder> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.example.travalms.ui.screens.TailOrder> getTailOrders() {
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
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getFavoriteOrderIds() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getSearchQuery() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.example.travalms.ui.screens.TailOrder> component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.example.travalms.ui.screens.TailOrder> getFilteredTailOrders() {
        return null;
    }
}