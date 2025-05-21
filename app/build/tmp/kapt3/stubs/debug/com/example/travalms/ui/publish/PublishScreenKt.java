package com.example.travalms.ui.publish;

import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import androidx.compose.ui.window.DialogProperties;
import androidx.compose.ui.unit.Dp;
import androidx.navigation.NavHostController;
import com.example.travalms.ui.navigation.AppRoutes;
import java.util.*;
import com.example.travalms.ui.viewmodels.TailListItem;
import com.example.travalms.ui.viewmodels.PublishViewModel;
import android.util.Log;
import com.example.travalms.data.remote.ConnectionState;
import com.example.travalms.data.api.NetworkModule;
import com.example.travalms.data.api.ProductResponseItem;
import androidx.compose.ui.text.style.TextAlign;

@kotlin.Metadata(mv = {1, 8, 0}, k = 2, d1 = {"\u0000X\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\u001a\u001a\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a2\u0010\u0006\u001a\u00020\u00012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\n2\u0006\u0010\f\u001a\u00020\u000bH\u0007\u001as\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u000f2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\n2\u0006\u0010\u0011\u001a\u00020\u000f2\b\b\u0002\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u000f2\b\b\u0002\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u0016\u001a\u00020\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u001bH\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001c\u0010\u001d\u001a8\u0010\u001e\u001a\u00020\u00012\u0006\u0010\u001f\u001a\u00020\u000f2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u000f0!2\b\b\u0002\u0010\"\u001a\u00020\u000f2\u000e\b\u0002\u0010#\u001a\b\u0012\u0004\u0012\u00020\u000f0!H\u0007\u001a\u0018\u0010$\u001a\u00020\u00012\u0006\u0010\u001f\u001a\u00020\u000f2\u0006\u0010%\u001a\u00020\u000fH\u0007\u001aH\u0010&\u001a\u00020\u00012\f\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\f\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\f\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\u0006\u0010+\u001a\u00020,H\u0007\u0082\u0002\u000b\n\u0005\b\u00a1\u001e0\u0001\n\u0002\b\u0019\u00a8\u0006-"}, d2 = {"CustomDatePicker", "", "datePickerState", "Landroidx/compose/material3/DatePickerState;", "modifier", "Landroidx/compose/ui/Modifier;", "DatePickerDialog", "onDismiss", "Lkotlin/Function0;", "onDateSelected", "Lkotlin/Function1;", "Ljava/time/LocalDate;", "initialDate", "FormTextField", "value", "", "onValueChange", "placeholder", "defaultContent", "label", "backgroundColor", "Landroidx/compose/ui/graphics/Color;", "singleLine", "", "maxLines", "", "height", "Landroidx/compose/ui/unit/Dp;", "FormTextField-Iq5l2Kk", "(Ljava/lang/String;Lkotlin/jvm/functions/Function1;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JZIF)V", "MessagePreviewCard", "title", "features", "", "validPeriod", "publishLocations", "MessagePreviewSection", "content", "PublishScreen", "onBackClick", "onHomeClick", "onMessageClick", "onProfileClick", "navController", "Landroidx/navigation/NavHostController;", "app_debug"})
public final class PublishScreenKt {
    
    /**
     * 发布页面
     */
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public static final void PublishScreen(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onHomeClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onMessageClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onProfileClick, @org.jetbrains.annotations.NotNull
    androidx.navigation.NavHostController navController) {
    }
    
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public static final void DatePickerDialog(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.time.LocalDate, kotlin.Unit> onDateSelected, @org.jetbrains.annotations.NotNull
    java.time.LocalDate initialDate) {
    }
    
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    public static final void CustomDatePicker(@org.jetbrains.annotations.NotNull
    androidx.compose.material3.DatePickerState datePickerState, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * 消息预览卡片，用于显示发布后的消息预览
     */
    @androidx.compose.runtime.Composable
    public static final void MessagePreviewCard(@org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> features, @org.jetbrains.annotations.NotNull
    java.lang.String validPeriod, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> publishLocations) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void MessagePreviewSection(@org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    java.lang.String content) {
    }
}