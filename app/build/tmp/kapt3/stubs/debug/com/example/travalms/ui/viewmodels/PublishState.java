package com.example.travalms.ui.viewmodels;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.example.travalms.data.remote.XMPPManager;
import com.example.travalms.data.remote.PubSubNotification;
import com.example.travalms.ui.screens.SubscriptionNodeItem;
import com.example.travalms.ui.screens.SubscriptionNodeType;
import com.example.travalms.data.remote.ConnectionState;
import com.example.travalms.api.dto.TailOrderRequest;
import com.example.travalms.api.dto.TailOrderResponse;
import com.example.travalms.data.api.NetworkModule;
import com.example.travalms.data.api.TailOrderApiService;
import kotlinx.coroutines.flow.StateFlow;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 发布视图模型状态
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u001d\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B]\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0006\u0012\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\u0002\u0010\fJ\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u001b\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\u000f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0010J\u000b\u0010\u001e\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003Jf\u0010\u001f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00062\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0006H\u00c6\u0001\u00a2\u0006\u0002\u0010 J\u0013\u0010!\u001a\u00020\u00032\b\u0010\"\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010#\u001a\u00020$H\u00d6\u0001J\t\u0010%\u001a\u00020\u0006H\u00d6\u0001R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0015\u0010\n\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u0011\u001a\u0004\b\u000f\u0010\u0010R\u0013\u0010\b\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000eR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0013R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0013R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0016\u00a8\u0006&"}, d2 = {"Lcom/example/travalms/ui/viewmodels/PublishState;", "", "isPublishing", "", "selectedNodeIds", "", "", "publishSuccess", "errorMessage", "selectedNodeNames", "apiSuccess", "apiErrorMessage", "(ZLjava/util/List;ZLjava/lang/String;Ljava/util/List;Ljava/lang/Boolean;Ljava/lang/String;)V", "getApiErrorMessage", "()Ljava/lang/String;", "getApiSuccess", "()Ljava/lang/Boolean;", "Ljava/lang/Boolean;", "getErrorMessage", "()Z", "getPublishSuccess", "getSelectedNodeIds", "()Ljava/util/List;", "getSelectedNodeNames", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "(ZLjava/util/List;ZLjava/lang/String;Ljava/util/List;Ljava/lang/Boolean;Ljava/lang/String;)Lcom/example/travalms/ui/viewmodels/PublishState;", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class PublishState {
    private final boolean isPublishing = false;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<java.lang.String> selectedNodeIds = null;
    private final boolean publishSuccess = false;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String errorMessage = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<java.lang.String> selectedNodeNames = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.Boolean apiSuccess = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String apiErrorMessage = null;
    
    /**
     * 发布视图模型状态
     */
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.ui.viewmodels.PublishState copy(boolean isPublishing, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> selectedNodeIds, boolean publishSuccess, @org.jetbrains.annotations.Nullable
    java.lang.String errorMessage, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> selectedNodeNames, @org.jetbrains.annotations.Nullable
    java.lang.Boolean apiSuccess, @org.jetbrains.annotations.Nullable
    java.lang.String apiErrorMessage) {
        return null;
    }
    
    /**
     * 发布视图模型状态
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * 发布视图模型状态
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * 发布视图模型状态
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public PublishState() {
        super();
    }
    
    public PublishState(boolean isPublishing, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> selectedNodeIds, boolean publishSuccess, @org.jetbrains.annotations.Nullable
    java.lang.String errorMessage, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> selectedNodeNames, @org.jetbrains.annotations.Nullable
    java.lang.Boolean apiSuccess, @org.jetbrains.annotations.Nullable
    java.lang.String apiErrorMessage) {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    public final boolean isPublishing() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getSelectedNodeIds() {
        return null;
    }
    
    public final boolean component3() {
        return false;
    }
    
    public final boolean getPublishSuccess() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getErrorMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getSelectedNodeNames() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Boolean component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Boolean getApiSuccess() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getApiErrorMessage() {
        return null;
    }
}