package com.example.travalms.ui.screens;

import android.util.Log;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.CardDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextOverflow;
import android.widget.Toast;
import com.example.travalms.data.model.ChatInvitation;

@kotlin.Metadata(mv = {1, 8, 0}, k = 2, d1 = {"\u0000\"\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\u001a,\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001aL\u0010\u0007\u001a\u00020\u00012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u000b2\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u000bH\u0007\u00a8\u0006\f"}, d2 = {"InvitationItem", "", "invitation", "Lcom/example/travalms/data/model/ChatInvitation;", "onAccept", "Lkotlin/Function0;", "onReject", "NotificationsScreen", "invitations", "", "onClose", "Lkotlin/Function1;", "app_debug"})
public final class NotificationsScreenKt {
    
    /**
     * 通知屏幕，显示群聊邀请通知
     *
     * @param invitations 邀请列表
     * @param onClose 关闭通知的回调
     * @param onAccept 接受邀请的回调
     * @param onReject 拒绝邀请的回调
     */
    @androidx.compose.runtime.Composable
    public static final void NotificationsScreen(@org.jetbrains.annotations.NotNull
    java.util.List<com.example.travalms.data.model.ChatInvitation> invitations, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onClose, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.example.travalms.data.model.ChatInvitation, kotlin.Unit> onAccept, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.example.travalms.data.model.ChatInvitation, kotlin.Unit> onReject) {
    }
    
    /**
     * 邀请项，显示单个邀请的信息和操作按钮
     */
    @androidx.compose.runtime.Composable
    public static final void InvitationItem(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.model.ChatInvitation invitation, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onAccept, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onReject) {
    }
}