package com.example.travalms.ui.viewmodels;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.example.travalms.api.dto.TailOrderResponse;
import com.example.travalms.data.api.NetworkModule;
import com.example.travalms.data.remote.ConnectionState;
import com.example.travalms.data.remote.XMPPManager;
import com.example.travalms.ui.model.PostItem;
import kotlinx.coroutines.flow.StateFlow;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * 管理"我的发布"界面数据的ViewModel
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0007\u0018\u0000 \"2\u00020\u0001:\u0001\"B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0002J\u001c\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00192\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0019H\u0002J\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u001d\u001a\u00020\u0006J\u0006\u0010\u001e\u001a\u00020\u001fJ\u001a\u0010 \u001a\u00020\u001f2\u0012\u0010!\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005R \u0010\u0003\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R#\u0010\n\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u00050\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\rR\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/example/travalms/ui/viewmodels/MyPublishedTailsViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "_originalTailOrders", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "", "Lcom/example/travalms/api/dto/TailOrderResponse;", "_uiState", "Lcom/example/travalms/ui/viewmodels/MyPublishedTailsState;", "originalTailOrders", "Lkotlinx/coroutines/flow/StateFlow;", "getOriginalTailOrders", "()Lkotlinx/coroutines/flow/StateFlow;", "tailOrderApiService", "Lcom/example/travalms/data/api/TailOrderApiService;", "uiState", "getUiState", "xmppManager", "Lcom/example/travalms/data/remote/XMPPManager;", "calculateDaysExpired", "", "expiryDateStr", "", "convertToPostItems", "", "Lcom/example/travalms/ui/model/PostItem;", "tailOrders", "getOriginalTailOrderById", "id", "loadUserPublishedTails", "", "updateOriginalTailOrders", "newData", "Companion", "app_debug"})
public final class MyPublishedTailsViewModel extends androidx.lifecycle.ViewModel {
    private final com.example.travalms.data.remote.XMPPManager xmppManager = null;
    private final com.example.travalms.data.api.TailOrderApiService tailOrderApiService = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.travalms.ui.viewmodels.MyPublishedTailsState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.example.travalms.ui.viewmodels.MyPublishedTailsState> uiState = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.Map<java.lang.Long, com.example.travalms.api.dto.TailOrderResponse>> _originalTailOrders = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.util.Map<java.lang.Long, com.example.travalms.api.dto.TailOrderResponse>> originalTailOrders = null;
    @org.jetbrains.annotations.NotNull
    public static final com.example.travalms.ui.viewmodels.MyPublishedTailsViewModel.Companion Companion = null;
    private static com.example.travalms.ui.viewmodels.MyPublishedTailsViewModel instance;
    
    public MyPublishedTailsViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.example.travalms.ui.viewmodels.MyPublishedTailsState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.util.Map<java.lang.Long, com.example.travalms.api.dto.TailOrderResponse>> getOriginalTailOrders() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.example.travalms.api.dto.TailOrderResponse getOriginalTailOrderById(long id) {
        return null;
    }
    
    public final void updateOriginalTailOrders(@org.jetbrains.annotations.NotNull
    java.util.Map<java.lang.Long, com.example.travalms.api.dto.TailOrderResponse> newData) {
    }
    
    public final void loadUserPublishedTails() {
    }
    
    /**
     * 将API返回的TailOrderResponse转换为UI使用的PostItem
     */
    private final java.util.List<com.example.travalms.ui.model.PostItem> convertToPostItems(java.util.List<com.example.travalms.api.dto.TailOrderResponse> tailOrders) {
        return null;
    }
    
    /**
     * 计算尾单过期天数
     */
    private final int calculateDaysExpired(java.lang.String expiryDateStr) {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0004R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/example/travalms/ui/viewmodels/MyPublishedTailsViewModel$Companion;", "", "()V", "instance", "Lcom/example/travalms/ui/viewmodels/MyPublishedTailsViewModel;", "getInstance", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.example.travalms.ui.viewmodels.MyPublishedTailsViewModel getInstance() {
            return null;
        }
    }
}