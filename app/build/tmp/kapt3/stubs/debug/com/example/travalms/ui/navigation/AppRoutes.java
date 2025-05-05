package com.example.travalms.ui.navigation;

import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.compose.runtime.Composable;
import androidx.navigation.NavHostController;
import androidx.navigation.NavType;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import android.content.Context;
import com.example.travalms.data.remote.XMPPManager;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u001a\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lcom/example/travalms/ui/navigation/AppRoutes;", "", "()V", "CHAT_ROOM", "", "COMPANY_BINDING", "COMPANY_DETAIL", "COMPANY_REGISTER", "CREATE_GROUP", "FRIEND_DETAIL", "GROUP_CHAT", "GROUP_LIST", "HOME", "LOGIN", "MESSAGE", "MESSAGE_LIST", "MY_FAVORITES", "MY_POSTS", "PERSON_DETAIL", "POST_DETAIL", "POST_EDIT", "PROFILE", "PROFILE_EDIT", "PUBLISH", "PUBLISH_NODE_SELECTOR", "REGISTER", "SUBSCRIBE_SETTING", "TAIL_LIST", "TAIL_ORDER_DETAIL", "VERIFICATION", "app_debug"})
public final class AppRoutes {
    @org.jetbrains.annotations.NotNull
    public static final com.example.travalms.ui.navigation.AppRoutes INSTANCE = null;
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String LOGIN = "login";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String REGISTER = "register";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String HOME = "home";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String MY_POSTS = "my_posts";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PUBLISH = "publish";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String MESSAGE = "message";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String SUBSCRIBE_SETTING = "subscribe_setting";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PROFILE = "profile";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PROFILE_EDIT = "profile_edit";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String COMPANY_BINDING = "company_binding";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String VERIFICATION = "verification";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String COMPANY_REGISTER = "company_register";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String POST_DETAIL = "post_detail/{postId}";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String POST_EDIT = "post_edit/{postId}";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String COMPANY_DETAIL = "company_detail/{companyId}";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PERSON_DETAIL = "person_detail/{personId}";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String CHAT_ROOM = "chat_room/{sessionId}/{targetName}/{targetType}";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String MESSAGE_LIST = "message_list";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String MY_FAVORITES = "my_favorites";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String TAIL_LIST = "tail_list";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PUBLISH_NODE_SELECTOR = "publish_node_selector";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String GROUP_LIST = "group_list";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String GROUP_CHAT = "group_chat/{roomJid}";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String CREATE_GROUP = "create_group";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String TAIL_ORDER_DETAIL = "tail_order_detail/{tailOrderId}";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String FRIEND_DETAIL = "friend_detail/{username}";
    
    private AppRoutes() {
        super();
    }
}