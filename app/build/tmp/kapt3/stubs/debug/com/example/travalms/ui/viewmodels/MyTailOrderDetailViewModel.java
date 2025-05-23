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
 * 我的尾单详情视图模型
 * 专门用于显示当前用户发布的尾单详情
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001:\u0001\u0014B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u0010\u001a\u00020\u0011J\b\u0010\u0012\u001a\u00020\u0011H\u0002J\b\u0010\u0013\u001a\u00020\u0011H\u0002R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/example/travalms/ui/viewmodels/MyTailOrderDetailViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "tailOrderId", "", "application", "Landroid/app/Application;", "(Ljava/lang/String;Landroid/app/Application;)V", "_state", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/example/travalms/ui/viewmodels/MyTailOrderDetailState;", "state", "Lkotlinx/coroutines/flow/StateFlow;", "getState", "()Lkotlinx/coroutines/flow/StateFlow;", "tailOrderApiService", "Lcom/example/travalms/data/api/TailOrderApiService;", "deleteTailOrder", "", "loadMyTailOrderDetail", "refreshMyPublishedTails", "Factory", "app_debug"})
public final class MyTailOrderDetailViewModel extends androidx.lifecycle.AndroidViewModel {
    private final java.lang.String tailOrderId = null;
    private final android.app.Application application = null;
    private final com.example.travalms.data.api.TailOrderApiService tailOrderApiService = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.travalms.ui.viewmodels.MyTailOrderDetailState> _state = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.example.travalms.ui.viewmodels.MyTailOrderDetailState> state = null;
    
    public MyTailOrderDetailViewModel(@org.jetbrains.annotations.NotNull
    java.lang.String tailOrderId, @org.jetbrains.annotations.NotNull
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.example.travalms.ui.viewmodels.MyTailOrderDetailState> getState() {
        return null;
    }
    
    /**
     * 加载我的尾单详情
     */
    private final void loadMyTailOrderDetail() {
    }
    
    /**
     * 删除尾单
     */
    public final void deleteTailOrder() {
    }
    
    /**
     * 刷新我的发布列表
     */
    private final void refreshMyPublishedTails() {
    }
    
    /**
     * ViewModel Factory
     */
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J%\u0010\u0007\u001a\u0002H\b\"\b\b\u0000\u0010\b*\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\b0\u000bH\u0016\u00a2\u0006\u0002\u0010\fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/example/travalms/ui/viewmodels/MyTailOrderDetailViewModel$Factory;", "Landroidx/lifecycle/ViewModelProvider$Factory;", "tailOrderId", "", "application", "Landroid/app/Application;", "(Ljava/lang/String;Landroid/app/Application;)V", "create", "T", "Landroidx/lifecycle/ViewModel;", "modelClass", "Ljava/lang/Class;", "(Ljava/lang/Class;)Landroidx/lifecycle/ViewModel;", "app_debug"})
    public static final class Factory implements androidx.lifecycle.ViewModelProvider.Factory {
        private final java.lang.String tailOrderId = null;
        private final android.app.Application application = null;
        
        public Factory(@org.jetbrains.annotations.NotNull
        java.lang.String tailOrderId, @org.jetbrains.annotations.NotNull
        android.app.Application application) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        @kotlin.Suppress(names = {"UNCHECKED_CAST"})
        @java.lang.Override
        public <T extends androidx.lifecycle.ViewModel>T create(@org.jetbrains.annotations.NotNull
        java.lang.Class<T> modelClass) {
            return null;
        }
    }
}