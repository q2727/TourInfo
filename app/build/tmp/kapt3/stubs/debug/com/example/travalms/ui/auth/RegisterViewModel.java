package com.example.travalms.ui.auth;

import android.net.Uri;
import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.example.travalms.data.remote.XMPPManager;
import com.example.travalms.data.api.UserApiService;
import com.example.travalms.data.api.NetworkModule;
import kotlinx.coroutines.flow.StateFlow;
import java.io.File;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0010\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002Jz\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u00042\b\u0010\u0014\u001a\u0004\u0018\u00010\u00042\b\u0010\u0015\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0016\u001a\u00020\u00042\u0006\u0010\u0017\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0019\u001a\u00020\u00042\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u0004J\u0085\u0001\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u00042\b\u0010\u0014\u001a\u0004\u0018\u00010\u00042\b\u0010\u0015\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0016\u001a\u00020\u00042\u0006\u0010\u0017\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0019\u001a\u00020\u00042\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u0004H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001fJ\u0006\u0010 \u001a\u00020\u0011R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006!"}, d2 = {"Lcom/example/travalms/ui/auth/RegisterViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "TAG", "", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/example/travalms/ui/auth/RegisterUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "userApiService", "Lcom/example/travalms/data/api/UserApiService;", "xmppManager", "Lcom/example/travalms/data/remote/XMPPManager;", "performRegister", "", "username", "password", "nickname", "email", "companyName", "mobileNumber", "province", "city", "businessLicensePath", "idCardFrontPath", "idCardBackPath", "avatarPath", "registerToBackend", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "resetState", "app_debug"})
public final class RegisterViewModel extends androidx.lifecycle.ViewModel {
    private final java.lang.String TAG = "RegisterViewModel";
    private final com.example.travalms.data.remote.XMPPManager xmppManager = null;
    private final com.example.travalms.data.api.UserApiService userApiService = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.travalms.ui.auth.RegisterUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.example.travalms.ui.auth.RegisterUiState> uiState = null;
    
    public RegisterViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.example.travalms.ui.auth.RegisterUiState> getUiState() {
        return null;
    }
    
    /**
     * 执行注册操作
     */
    public final void performRegister(@org.jetbrains.annotations.NotNull
    java.lang.String username, @org.jetbrains.annotations.NotNull
    java.lang.String password, @org.jetbrains.annotations.Nullable
    java.lang.String nickname, @org.jetbrains.annotations.Nullable
    java.lang.String email, @org.jetbrains.annotations.NotNull
    java.lang.String companyName, @org.jetbrains.annotations.NotNull
    java.lang.String mobileNumber, @org.jetbrains.annotations.NotNull
    java.lang.String province, @org.jetbrains.annotations.NotNull
    java.lang.String city, @org.jetbrains.annotations.Nullable
    java.lang.String businessLicensePath, @org.jetbrains.annotations.Nullable
    java.lang.String idCardFrontPath, @org.jetbrains.annotations.Nullable
    java.lang.String idCardBackPath, @org.jetbrains.annotations.Nullable
    java.lang.String avatarPath) {
    }
    
    /**
     * 向后端API注册用户
     */
    private final java.lang.Object registerToBackend(java.lang.String username, java.lang.String password, java.lang.String nickname, java.lang.String email, java.lang.String companyName, java.lang.String mobileNumber, java.lang.String province, java.lang.String city, java.lang.String businessLicensePath, java.lang.String idCardFrontPath, java.lang.String idCardBackPath, java.lang.String avatarPath, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    /**
     * 重置UI状态到初始状态
     */
    public final void resetState() {
    }
}