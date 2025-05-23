package com.example.travalms.ui.screens;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.example.travalms.ui.viewmodels.MyTailOrderDetailViewModel;

@kotlin.Metadata(mv = {1, 8, 0}, k = 2, d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a,\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00030\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\u0006H\u0007\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"TAG", "", "MyTailOrderDetailScreen", "", "tailOrderId", "onBack", "Lkotlin/Function0;", "onDeleted", "app_debug"})
public final class MyTailOrderDetailScreenKt {
    private static final java.lang.String TAG = "MyTailOrderDetailScreen";
    
    /**
     * 我的尾单详情界面
     * 与普通尾单详情界面不同，只显示精简信息和删除按钮
     */
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    public static final void MyTailOrderDetailScreen(@org.jetbrains.annotations.NotNull
    java.lang.String tailOrderId, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onDeleted) {
    }
}