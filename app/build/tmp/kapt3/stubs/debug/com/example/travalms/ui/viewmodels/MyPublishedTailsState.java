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
 * 我的发布界面数据状态
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B+\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\u0002\u0010\tJ\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\u000b\u0010\u0011\u001a\u0004\u0018\u00010\bH\u00c6\u0003J/\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\bH\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00032\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0015\u001a\u00020\u0016H\u00d6\u0001J\t\u0010\u0017\u001a\u00020\bH\u00d6\u0001R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\fR\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u0018"}, d2 = {"Lcom/example/travalms/ui/viewmodels/MyPublishedTailsState;", "", "isLoading", "", "publishedTails", "", "Lcom/example/travalms/ui/screens/PostItem;", "errorMessage", "", "(ZLjava/util/List;Ljava/lang/String;)V", "getErrorMessage", "()Ljava/lang/String;", "()Z", "getPublishedTails", "()Ljava/util/List;", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class MyPublishedTailsState {
    private final boolean isLoading = false;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.example.travalms.ui.screens.PostItem> publishedTails = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String errorMessage = null;
    
    /**
     * 我的发布界面数据状态
     */
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.ui.viewmodels.MyPublishedTailsState copy(boolean isLoading, @org.jetbrains.annotations.NotNull
    java.util.List<com.example.travalms.ui.screens.PostItem> publishedTails, @org.jetbrains.annotations.Nullable
    java.lang.String errorMessage) {
        return null;
    }
    
    /**
     * 我的发布界面数据状态
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * 我的发布界面数据状态
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * 我的发布界面数据状态
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public MyPublishedTailsState() {
        super();
    }
    
    public MyPublishedTailsState(boolean isLoading, @org.jetbrains.annotations.NotNull
    java.util.List<com.example.travalms.ui.screens.PostItem> publishedTails, @org.jetbrains.annotations.Nullable
    java.lang.String errorMessage) {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.example.travalms.ui.screens.PostItem> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.example.travalms.ui.screens.PostItem> getPublishedTails() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getErrorMessage() {
        return null;
    }
}