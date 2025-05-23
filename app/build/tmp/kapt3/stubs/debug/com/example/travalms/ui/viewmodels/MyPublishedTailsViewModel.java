package com.example.travalms.ui.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import com.example.travalms.api.dto.TailOrderResponse;
import com.example.travalms.data.api.NetworkModule;
import com.example.travalms.data.remote.ConnectionState;
import com.example.travalms.data.remote.PubSubNotification;
import com.example.travalms.data.remote.XMPPManager;
import com.example.travalms.ui.model.PostItem;
import kotlinx.coroutines.flow.StateFlow;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

/**
 * 管理"我的发布"界面数据的ViewModel
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b\u0007\u0018\u0000 -2\u00020\u0001:\u0001-B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J\u001c\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001b2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\t0\u001bH\u0002J\u000e\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\bJ\u0010\u0010!\u001a\u0004\u0018\u00010\t2\u0006\u0010\"\u001a\u00020\bJ \u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&2\u0006\u0010 \u001a\u00020\b2\u0006\u0010\'\u001a\u00020\u0019H\u0002J\u0006\u0010(\u001a\u00020\u001fJ\u0011\u0010)\u001a\u00020\u001fH\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010*J\u001a\u0010+\u001a\u00020\u001f2\u0012\u0010,\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007R \u0010\u0005\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R#\u0010\f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u00070\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000b0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000fR\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006."}, d2 = {"Lcom/example/travalms/ui/viewmodels/MyPublishedTailsViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_originalTailOrders", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "", "Lcom/example/travalms/api/dto/TailOrderResponse;", "_uiState", "Lcom/example/travalms/ui/viewmodels/MyPublishedTailsState;", "originalTailOrders", "Lkotlinx/coroutines/flow/StateFlow;", "getOriginalTailOrders", "()Lkotlinx/coroutines/flow/StateFlow;", "tailOrderApiService", "Lcom/example/travalms/data/api/TailOrderApiService;", "uiState", "getUiState", "xmppManager", "Lcom/example/travalms/data/remote/XMPPManager;", "calculateDaysExpired", "", "expiryDateStr", "", "convertToPostItems", "", "Lcom/example/travalms/ui/model/PostItem;", "tailOrders", "deleteTailOrder", "", "tailOrderId", "getOriginalTailOrderById", "id", "isMatchingTailOrder", "", "notification", "Lcom/example/travalms/data/remote/PubSubNotification;", "title", "loadUserPublishedTails", "resetXmppConnectionAfterDelete", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateOriginalTailOrders", "newData", "Companion", "app_debug"})
public final class MyPublishedTailsViewModel extends androidx.lifecycle.AndroidViewModel {
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
    
    public MyPublishedTailsViewModel(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
        super(null);
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
    
    /**
     * 删除尾单
     * 从后端数据库删除尾单，并从所有发布节点中删除对应的XMPP消息
     * @param tailOrderId 要删除的尾单ID
     */
    public final void deleteTailOrder(long tailOrderId) {
    }
    
    /**
     * 重置XMPP连接
     */
    private final java.lang.Object resetXmppConnectionAfterDelete(kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    /**
     * 检查通知是否匹配指定的尾单ID和标题
     */
    private final boolean isMatchingTailOrder(com.example.travalms.data.remote.PubSubNotification notification, long tailOrderId, java.lang.String title) {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/example/travalms/ui/viewmodels/MyPublishedTailsViewModel$Companion;", "", "()V", "instance", "Lcom/example/travalms/ui/viewmodels/MyPublishedTailsViewModel;", "getInstance", "app", "Landroid/app/Application;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.example.travalms.ui.viewmodels.MyPublishedTailsViewModel getInstance(@org.jetbrains.annotations.NotNull
        android.app.Application app) {
            return null;
        }
    }
}