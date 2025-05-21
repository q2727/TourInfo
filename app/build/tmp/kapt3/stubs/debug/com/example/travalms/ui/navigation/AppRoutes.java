package com.example.travalms.ui.navigation;

import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.compose.runtime.Composable;
import androidx.navigation.NavHostController;
import androidx.navigation.NavType;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import android.content.Context;
import androidx.compose.material3.SnackbarHostState;
import com.example.travalms.data.remote.XMPPManager;

/**
 * 应用导航路由常量
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u001b\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/example/travalms/ui/navigation/AppRoutes;", "", "()V", "CHAT_ROOM", "", "COMPANY_BINDING", "COMPANY_DETAIL", "CREATE_GROUP", "FRIEND_DETAIL", "GROUP_CHAT", "HOME", "LOGIN", "MESSAGE", "MY_FAVORITES", "MY_POSTS", "MY_TAIL_ORDER_DETAIL", "PERSON_DETAIL", "POST_DETAIL", "POST_EDIT", "PRODUCT_DETAIL", "PROFILE", "PROFILE_EDIT", "PUBLISH", "PUBLISH_NODE_SELECTOR", "REGISTER", "SEARCH", "SPLASH", "SUBSCRIBE_SETTING", "TAIL_LIST", "TAIL_ORDER_DETAIL", "VERIFICATION", "app_debug"})
public final class AppRoutes {
    @org.jetbrains.annotations.NotNull
    public static final com.example.travalms.ui.navigation.AppRoutes INSTANCE = null;
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String SPLASH = "splash";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String LOGIN = "login";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String REGISTER = "register";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String HOME = "home";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String TAIL_LIST = "tailList";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String TAIL_ORDER_DETAIL = "tailOrderDetail/{tailOrderId}";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String MY_TAIL_ORDER_DETAIL = "myTailOrderDetail/{tailOrderId}";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String POST_DETAIL = "postDetail/{postId}";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String POST_EDIT = "postEdit/{postId}";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String MESSAGE = "message";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String GROUP_CHAT = "group_chat/{groupJid}";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String CHAT_ROOM = "chat_room/{sessionId}/{targetName}/{targetType}";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PUBLISH = "publish";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PUBLISH_NODE_SELECTOR = "publishNodeSelector";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String MY_POSTS = "myPosts";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String MY_FAVORITES = "myFavorites";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PRODUCT_DETAIL = "productDetail/{productId}";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String SEARCH = "search";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PROFILE = "profile";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PROFILE_EDIT = "profileEdit";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String VERIFICATION = "verification";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String COMPANY_BINDING = "companyBinding";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String COMPANY_DETAIL = "companyDetail/{companyId}";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PERSON_DETAIL = "personDetail/{personId}";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String CREATE_GROUP = "createGroup";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String SUBSCRIBE_SETTING = "subscribeSetting";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String FRIEND_DETAIL = "friendDetail/{username}";
    
    private AppRoutes() {
        super();
    }
}