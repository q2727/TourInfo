package com.example.travalms.ui.auth;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import com.example.travalms.data.remote.XMPPManager;
import com.example.travalms.data.remote.XMPPService;
import kotlinx.coroutines.flow.StateFlow;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011J\u0006\u0010\u0013\u001a\u00020\u000fJ\u0006\u0010\u0014\u001a\u00020\u000fJ\u000e\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u0016\u001a\u00020\u0007R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/example/travalms/ui/auth/LoginViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/example/travalms/ui/auth/LoginUiState;", "applicationContext", "Landroid/content/Context;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "xmppManager", "Lcom/example/travalms/data/remote/XMPPManager;", "Login", "", "username", "", "password", "logout", "resetState", "setContext", "context", "app_debug"})
public final class LoginViewModel extends androidx.lifecycle.ViewModel {
    private final com.example.travalms.data.remote.XMPPManager xmppManager = null;
    private android.content.Context applicationContext;
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.travalms.ui.auth.LoginUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.example.travalms.ui.auth.LoginUiState> uiState = null;
    
    public LoginViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.example.travalms.ui.auth.LoginUiState> getUiState() {
        return null;
    }
    
    /**
     * 设置应用上下文
     * 通常在构造ViewModel时从主Activity或应用类获得
     */
    public final void setContext(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
    }
    
    /**
     * 执行登录操作
     */
    public final void Login(@org.jetbrains.annotations.NotNull
    java.lang.String username, @org.jetbrains.annotations.NotNull
    java.lang.String password) {
    }
    
    /**
     * 重置UI状态到初始状态 (例如，在错误提示后或成功导航后)
     */
    public final void resetState() {
    }
    
    /**
     * 退出登录
     */
    public final void logout() {
    }
}