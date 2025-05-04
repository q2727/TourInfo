package com.example.travalms.ui.screens;

import android.util.Log;
import android.widget.Toast;
import androidx.compose.foundation.layout.*;
import androidx.compose.foundation.text.KeyboardOptions;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.input.ImeAction;
import androidx.compose.ui.text.style.TextAlign;
import androidx.navigation.NavController;
import com.example.travalms.data.model.ContactItem;
import com.example.travalms.data.remote.XMPPManager;
import com.example.travalms.ui.navigation.AppRoutes;
import com.example.travalms.ui.viewmodels.GroupViewModel;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.impl.JidCreate;

@kotlin.Metadata(mv = {1, 8, 0}, k = 2, d1 = {"\u0000(\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u001a\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a&\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\fH\u0007\u00a8\u0006\r"}, d2 = {"CreateGroupScreen", "", "navController", "Landroidx/navigation/NavController;", "groupViewModel", "Lcom/example/travalms/ui/viewmodels/GroupViewModel;", "FriendItem", "name", "", "isSelected", "", "onClick", "Lkotlin/Function0;", "app_debug"})
public final class CreateGroupScreenKt {
    
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    public static final void CreateGroupScreen(@org.jetbrains.annotations.NotNull
    androidx.navigation.NavController navController, @org.jetbrains.annotations.NotNull
    com.example.travalms.ui.viewmodels.GroupViewModel groupViewModel) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void FriendItem(@org.jetbrains.annotations.NotNull
    java.lang.String name, boolean isSelected, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
}