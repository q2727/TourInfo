package com.example.travalms.ui.screens;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextOverflow;
import androidx.navigation.NavController;
import com.example.travalms.data.model.TravelItem;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.ui.layout.Placeable;

@kotlin.Metadata(mv = {1, 8, 0}, k = 2, d1 = {"\u0000`\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001aG\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u001c\u0010\u0006\u001a\u0018\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\u0007\u00a2\u0006\u0002\b\t\u00a2\u0006\u0002\b\nH\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u000b\u0010\f\u001aa\u0010\r\u001a\u00020\u00012\u0011\u0010\u000e\u001a\r\u0012\u0004\u0012\u00020\u00010\u000f\u00a2\u0006\u0002\b\t2\u0011\u0010\u0010\u001a\r\u0012\u0004\u0012\u00020\u00010\u000f\u00a2\u0006\u0002\b\t2\u0006\u0010\u0011\u001a\u00020\u00122\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\u0006\u0010\u0014\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u0005H\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0016\u0010\u0017\u001aA\u0010\u0018\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u001a2\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u001b\u001a\u00020\u001c2\b\b\u0002\u0010\u001d\u001a\u00020\u001e2\u0011\u0010\u0006\u001a\r\u0012\u0004\u0012\u00020\u00010\u000f\u00a2\u0006\u0002\b\tH\u0007\u001ap\u0010\u001f\u001a\u00020\u00012\u0012\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\u0012\u0010%\u001a\u000e\u0012\u0004\u0012\u00020&\u0012\u0004\u0012\u00020\u00010\u00072\u0006\u0010\'\u001a\u00020(H\u0007\u001a2\u0010)\u001a\u00020\u00012\u0006\u0010*\u001a\u00020+2\u0012\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00010\u000fH\u0007\u0082\u0002\u000b\n\u0005\b\u00a1\u001e0\u0001\n\u0002\b\u0019\u00a8\u0006,"}, d2 = {"BottomNavigation", "", "modifier", "Landroidx/compose/ui/Modifier;", "backgroundColor", "Landroidx/compose/ui/graphics/Color;", "content", "Lkotlin/Function1;", "Landroidx/compose/foundation/layout/RowScope;", "Landroidx/compose/runtime/Composable;", "Lkotlin/ExtensionFunctionType;", "BottomNavigation-bw27NRU", "(Landroidx/compose/ui/Modifier;JLkotlin/jvm/functions/Function1;)V", "BottomNavigationItem", "icon", "Lkotlin/Function0;", "label", "selected", "", "onClick", "selectedContentColor", "unselectedContentColor", "BottomNavigationItem-VT9Kpxs", "(Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;ZLkotlin/jvm/functions/Function0;JJ)V", "FlowRow", "maxItemsInEachRow", "", "horizontalArrangement", "Landroidx/compose/foundation/layout/Arrangement$Horizontal;", "verticalArrangement", "Landroidx/compose/foundation/layout/Arrangement$Vertical;", "HomeScreen", "onItemClick", "onPublishClick", "onMessageClick", "onProfileClick", "onTailListClick", "onCompanyClick", "", "navController", "Landroidx/navigation/NavController;", "TravelItemCard", "item", "Lcom/example/travalms/data/model/TravelItem;", "app_debug"})
public final class HomeScreenKt {
    
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    public static final void HomeScreen(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onItemClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onPublishClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onMessageClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onProfileClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onTailListClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onCompanyClick, @org.jetbrains.annotations.NotNull
    androidx.navigation.NavController navController) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void TravelItemCard(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.model.TravelItem item, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onItemClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onCompanyClick) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void FlowRow(int maxItemsInEachRow, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull
    androidx.compose.foundation.layout.Arrangement.Horizontal horizontalArrangement, @org.jetbrains.annotations.NotNull
    androidx.compose.foundation.layout.Arrangement.Vertical verticalArrangement, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> content) {
    }
}