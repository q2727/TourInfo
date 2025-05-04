package com.example.travalms.ui.screens;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextOverflow;
import com.example.travalms.data.model.ChatMessage;
import com.example.travalms.data.remote.XMPPManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import android.util.Log;
import com.example.travalms.ui.viewmodels.ChatViewModel;

@kotlin.Metadata(mv = {1, 8, 0}, k = 2, d1 = {"\u00000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a8\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00032\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u001a \u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0007\u00a8\u0006\u0011"}, d2 = {"ChatRoomScreen", "", "sessionId", "", "targetName", "targetType", "onBackClick", "Lkotlin/Function0;", "chatViewModel", "Lcom/example/travalms/ui/viewmodels/ChatViewModel;", "MessageItem", "message", "Lcom/example/travalms/data/model/ChatMessage;", "isFromCurrentUser", "", "timeFormatter", "Ljava/time/format/DateTimeFormatter;", "app_debug"})
public final class ChatRoomScreenKt {
    
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    public static final void ChatRoomScreen(@org.jetbrains.annotations.NotNull
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull
    java.lang.String targetName, @org.jetbrains.annotations.NotNull
    java.lang.String targetType, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClick, @org.jetbrains.annotations.NotNull
    com.example.travalms.ui.viewmodels.ChatViewModel chatViewModel) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void MessageItem(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.model.ChatMessage message, boolean isFromCurrentUser, @org.jetbrains.annotations.NotNull
    java.time.format.DateTimeFormatter timeFormatter) {
    }
}