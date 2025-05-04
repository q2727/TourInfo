package com.example.travalms.ui.viewmodels;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.example.travalms.data.model.GroupRoom;
import com.example.travalms.data.remote.XMPPManager;
import kotlinx.coroutines.flow.StateFlow;

/**
 * 群聊列表界面的ViewModel
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0016\u001a\u00020\u0017J*\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u00042\u0006\u0010\u001a\u001a\u00020\u00042\b\b\u0002\u0010\u001b\u001a\u00020\u00042\b\b\u0002\u0010\u001c\u001a\u00020\u000bJ\u0006\u0010\u001d\u001a\u00020\u0017R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0019\u0010\u0010\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00040\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000fR\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000b0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000fR\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lcom/example/travalms/ui/viewmodels/GroupViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "TAG", "", "_availableRooms", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/example/travalms/data/model/GroupRoom;", "_errorMessage", "_loading", "", "availableRooms", "Lkotlinx/coroutines/flow/StateFlow;", "getAvailableRooms", "()Lkotlinx/coroutines/flow/StateFlow;", "errorMessage", "getErrorMessage", "loading", "getLoading", "xmppManager", "Lcom/example/travalms/data/remote/XMPPManager;", "clearError", "", "createRoom", "name", "nickname", "description", "isPrivate", "loadAvailableRooms", "app_debug"})
public final class GroupViewModel extends androidx.lifecycle.ViewModel {
    private final java.lang.String TAG = "GroupViewModel";
    private final com.example.travalms.data.remote.XMPPManager xmppManager = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.example.travalms.data.model.GroupRoom>> _availableRooms = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.example.travalms.data.model.GroupRoom>> availableRooms = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _loading = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> loading = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _errorMessage = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> errorMessage = null;
    
    public GroupViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.example.travalms.data.model.GroupRoom>> getAvailableRooms() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getLoading() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getErrorMessage() {
        return null;
    }
    
    /**
     * 加载可用的群聊房间
     */
    public final void loadAvailableRooms() {
    }
    
    /**
     * 创建新群聊
     */
    public final void createRoom(@org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    java.lang.String nickname, @org.jetbrains.annotations.NotNull
    java.lang.String description, boolean isPrivate) {
    }
    
    /**
     * 清除错误信息
     */
    public final void clearError() {
    }
}