package com.example.travalms.ui.screens;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.navigation.NavController;
import com.example.travalms.ui.viewmodels.TailListViewModel;
import androidx.compose.material.ExperimentalMaterialApi;
import com.example.travalms.ui.navigation.AppRoutes;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import androidx.compose.foundation.text.KeyboardOptions;
import androidx.compose.ui.text.input.ImeAction;

@kotlin.Metadata(mv = {1, 8, 0}, k = 2, d1 = {"\u0000>\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\u001a\u00d0\u0001\u0010\u0000\u001a\u00020\u00012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\u00032\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\u00032\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\u00032\u0018\u0010\u000e\u001a\u0014\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\u000f2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\u00032\u0006\u0010\u0011\u001a\u00020\u00122\b\b\u0002\u0010\u0013\u001a\u00020\u0014H\u0007\u001a\u008e\u0001\u0010\u0015\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u00042\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\u0012\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u00010\u00032\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0007\u00a8\u0006\u001a"}, d2 = {"TailListScreen", "", "onTailOrderClick", "Lkotlin/Function1;", "Lcom/example/travalms/ui/screens/TailOrder;", "onHomeClick", "Lkotlin/Function0;", "onPublishClick", "onMessageClick", "onProfileClick", "onCompanyClick", "", "onContactClick", "onPersonClick", "onReportItem", "Lkotlin/Function2;", "onDeleteItem", "navController", "Landroidx/navigation/NavController;", "viewModel", "Lcom/example/travalms/ui/viewmodels/TailListViewModel;", "TailOrderItem", "tailOrder", "onClick", "onFavoriteClick", "", "app_debug"})
public final class TailListScreenKt {
    
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class, androidx.compose.material.ExperimentalMaterialApi.class})
    public static final void TailListScreen(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.example.travalms.ui.screens.TailOrder, kotlin.Unit> onTailOrderClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onHomeClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onPublishClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onMessageClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onProfileClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onCompanyClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onContactClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onPersonClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Unit> onReportItem, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onDeleteItem, @org.jetbrains.annotations.NotNull
    androidx.navigation.NavController navController, @org.jetbrains.annotations.NotNull
    com.example.travalms.ui.viewmodels.TailListViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    public static final void TailOrderItem(@org.jetbrains.annotations.NotNull
    com.example.travalms.ui.screens.TailOrder tailOrder, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onCompanyClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onContactClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onPersonClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onReportItem, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onDeleteItem, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onFavoriteClick, @org.jetbrains.annotations.NotNull
    androidx.navigation.NavController navController, @org.jetbrains.annotations.NotNull
    com.example.travalms.ui.viewmodels.TailListViewModel viewModel) {
    }
}