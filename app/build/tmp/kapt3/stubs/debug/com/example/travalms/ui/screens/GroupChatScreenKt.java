package com.example.travalms.ui.screens;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.layout.ContentScale;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextOverflow;
import androidx.navigation.NavController;
import coil.request.ImageRequest;
import com.example.travalms.model.GroupChatMessage;
import com.example.travalms.data.model.GroupMember;
import com.example.travalms.data.model.GroupRoom;
import com.example.travalms.ui.viewmodels.GroupChatViewModel;
import com.example.travalms.data.remote.XMPPManager;
import java.time.format.DateTimeFormatter;
import android.util.Log;

@kotlin.Metadata(mv = {1, 8, 0}, k = 2, d1 = {"\u0000:\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0007\u001a8\u0010\u0006\u001a\u00020\u00012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\rH\u0007\u001a*\u0010\u000e\u001a\u00020\u00012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\rH\u0007\u001a\u0018\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0007\u00a8\u0006\u0014"}, d2 = {"GroupChatScreen", "", "navController", "Landroidx/navigation/NavController;", "roomJid", "", "GroupMembersDialog", "members", "", "Lcom/example/travalms/data/model/GroupMember;", "onDismiss", "Lkotlin/Function0;", "onInvite", "Lkotlin/Function1;", "InviteUserDialog", "MessageBubble", "message", "Lcom/example/travalms/model/GroupChatMessage;", "timeFormatter", "Ljava/time/format/DateTimeFormatter;", "app_debug"})
public final class GroupChatScreenKt {
    
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    public static final void GroupChatScreen(@org.jetbrains.annotations.NotNull
    androidx.navigation.NavController navController, @org.jetbrains.annotations.NotNull
    java.lang.String roomJid) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void MessageBubble(@org.jetbrains.annotations.NotNull
    com.example.travalms.model.GroupChatMessage message, @org.jetbrains.annotations.NotNull
    java.time.format.DateTimeFormatter timeFormatter) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void GroupMembersDialog(@org.jetbrains.annotations.NotNull
    java.util.List<com.example.travalms.data.model.GroupMember> members, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onInvite) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void InviteUserDialog(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onInvite) {
    }
}