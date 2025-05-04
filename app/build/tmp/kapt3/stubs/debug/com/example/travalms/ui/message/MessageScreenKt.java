package com.example.travalms.ui.message;

import android.util.Log;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.navigation.NavController;
import com.example.travalms.data.model.ContactItem;
import com.example.travalms.data.remote.XMPPManager;
import com.example.travalms.ui.navigation.AppRoutes;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.impl.JidCreate;
import androidx.compose.material.ExperimentalMaterialApi;
import com.example.travalms.data.model.ChatMessage;
import com.example.travalms.ui.viewmodels.ChatViewModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import androidx.compose.material.DismissDirection;
import androidx.compose.material.DismissValue;
import androidx.compose.material.FractionalThreshold;
import com.example.travalms.utils.PinyinUtils;
import com.example.travalms.data.model.GroupRoom;
import com.example.travalms.ui.viewmodels.InvitationViewModel;
import android.widget.Toast;
import kotlinx.coroutines.Dispatchers;

@kotlin.Metadata(mv = {1, 8, 0}, k = 2, d1 = {"\u0000T\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001aB\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\bH\u0007\u001a\\\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\u0011\u001a\u00020\u00122\b\b\u0002\u0010\u0013\u001a\u00020\u0014H\u0007\u001aP\u0010\u0015\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u00032\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\bH\u0007\u001a0\u0010\u0018\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u00052\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\u001c\u001a\u00020\u001dH\u0007\u001a\u0012\u0010\u001e\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u001f\u001a\u00020\u001aH\u0002\u001a\u0012\u0010 \u001a\u00020\u001a2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0002\u001a\u0010\u0010#\u001a\u00020\u001a2\u0006\u0010$\u001a\u00020%H\u0002\u001a\b\u0010&\u001a\u00020\u001aH\u0002\u001a\u0010\u0010\'\u001a\u00020\u001a2\u0006\u0010(\u001a\u00020\u001aH\u0002\u00a8\u0006)"}, d2 = {"ContactListItem", "", "friend", "Lcom/example/travalms/data/model/ContactItem;", "isCompanyTab", "", "isAdded", "onAddFriend", "Lkotlin/Function0;", "onClick", "MessageScreen", "navController", "Landroidx/navigation/NavController;", "onHomeClick", "onPublishClick", "onTailListClick", "onProfileClick", "chatViewModel", "Lcom/example/travalms/ui/viewmodels/ChatViewModel;", "invitationViewModel", "Lcom/example/travalms/ui/viewmodels/InvitationViewModel;", "SwipeToDeleteContactItem", "contact", "onDelete", "TabItem", "title", "", "isSelected", "modifier", "Landroidx/compose/ui/Modifier;", "extractFirstLetter", "name", "formatLastMessage", "message", "Lcom/example/travalms/data/model/ChatMessage;", "formatMessageTime", "time", "Ljava/time/LocalDateTime;", "getCurrentUserJid", "jidToUsername", "jid", "app_debug"})
public final class MessageScreenKt {
    
    /**
     * 消息列表屏幕
     *
     * 注：此实现使用了实验性API，添加注解以抑制警告
     */
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material.ExperimentalMaterialApi.class, androidx.compose.material3.ExperimentalMaterial3Api.class})
    public static final void MessageScreen(@org.jetbrains.annotations.NotNull
    androidx.navigation.NavController navController, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onHomeClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onPublishClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onTailListClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onProfileClick, @org.jetbrains.annotations.NotNull
    com.example.travalms.ui.viewmodels.ChatViewModel chatViewModel, @org.jetbrains.annotations.NotNull
    com.example.travalms.ui.viewmodels.InvitationViewModel invitationViewModel) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void TabItem(@org.jetbrains.annotations.NotNull
    java.lang.String title, boolean isSelected, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void ContactListItem(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.model.ContactItem friend, boolean isCompanyTab, boolean isAdded, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onAddFriend, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    private static final java.lang.String formatLastMessage(com.example.travalms.data.model.ChatMessage message) {
        return null;
    }
    
    /**
     * 从名称中提取首字母，支持中文拼音
     *
     * 优先级:
     * 1. 如果名称包含括号，从括号中提取首字母（如 "陈明 (Chen Ming)" 提取 "C"）
     * 2. 如果名称以中文开头，根据拼音提取首字母（如 "曾小美" 提取 "Z"）
     * 3. 其他情况直接提取名称首字母的大写形式
     */
    private static final java.lang.String extractFirstLetter(java.lang.String name) {
        return null;
    }
    
    private static final java.lang.String formatMessageTime(java.time.LocalDateTime time) {
        return null;
    }
    
    private static final java.lang.String getCurrentUserJid() {
        return null;
    }
    
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material.ExperimentalMaterialApi.class})
    public static final void SwipeToDeleteContactItem(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.model.ContactItem contact, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onDelete, boolean isCompanyTab, boolean isAdded, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onAddFriend, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    private static final java.lang.String jidToUsername(java.lang.String jid) {
        return null;
    }
}