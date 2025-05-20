package com.example.travalms.ui.viewmodels;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.example.travalms.api.dto.TailOrderResponse;
import com.example.travalms.data.api.NetworkModule;
import com.example.travalms.data.remote.ConnectionState;
import com.example.travalms.data.remote.XMPPManager;
import com.example.travalms.ui.screens.PostItem;
import kotlinx.coroutines.flow.StateFlow;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * 管理"我的发布"界面数据的ViewModel
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00192\u00020\u0001:\u0001\u0019B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\u001c\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u0013H\u0002J\u0006\u0010\u0017\u001a\u00020\u0018R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/example/travalms/ui/viewmodels/MyPublishedTailsViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/example/travalms/ui/viewmodels/MyPublishedTailsState;", "tailOrderApiService", "Lcom/example/travalms/data/api/TailOrderApiService;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "xmppManager", "Lcom/example/travalms/data/remote/XMPPManager;", "calculateDaysExpired", "", "expiryDateStr", "", "convertToPostItems", "", "Lcom/example/travalms/ui/screens/PostItem;", "tailOrders", "Lcom/example/travalms/api/dto/TailOrderResponse;", "loadUserPublishedTails", "", "Companion", "app_debug"})
public final class MyPublishedTailsViewModel extends androidx.lifecycle.ViewModel {
    private final com.example.travalms.data.remote.XMPPManager xmppManager = null;
    private final com.example.travalms.data.api.TailOrderApiService tailOrderApiService = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.travalms.ui.viewmodels.MyPublishedTailsState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.example.travalms.ui.viewmodels.MyPublishedTailsState> uiState = null;
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
    
    public final void loadUserPublishedTails() {
    }
    
    /**
     * 将API返回的TailOrderResponse转换为UI使用的PostItem
     */
    private final java.util.List<com.example.travalms.ui.screens.PostItem> convertToPostItems(java.util.List<com.example.travalms.api.dto.TailOrderResponse> tailOrders) {
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