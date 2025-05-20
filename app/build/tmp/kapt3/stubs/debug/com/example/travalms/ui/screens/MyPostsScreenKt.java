package com.example.travalms.ui.screens;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.example.travalms.ui.viewmodels.MyPublishedTailsViewModel;
import com.example.travalms.util.CityNameMapping;

@kotlin.Metadata(mv = {1, 8, 0}, k = 2, d1 = {"\u0000$\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u001a\u009e\u0001\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\u0014\b\u0002\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00010\u000b2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00010\u000b2\b\b\u0002\u0010\u000e\u001a\u00020\u000fH\u0007\u001ah\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\f2\u0014\b\u0002\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00010\u000b2\u0014\b\u0002\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00010\u000b2\u0014\b\u0002\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00010\u000b2\u0014\b\u0002\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00010\u000bH\u0007\u00a8\u0006\u0016"}, d2 = {"MyPostsScreen", "", "onBackClick", "Lkotlin/Function0;", "onHomeClick", "onPublishClick", "onMessageClick", "onProfileClick", "onPublishNewClick", "onTailListClick", "onEditPost", "Lkotlin/Function1;", "Lcom/example/travalms/ui/screens/PostItem;", "onTailOrderClick", "viewModel", "Lcom/example/travalms/ui/viewmodels/MyPublishedTailsViewModel;", "PostItemCard", "post", "onItemClick", "onRepost", "onRefresh", "onDelete", "app_debug"})
public final class MyPostsScreenKt {
    
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    public static final void MyPostsScreen(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onHomeClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onPublishClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onMessageClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onProfileClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onPublishNewClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onTailListClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.example.travalms.ui.screens.PostItem, kotlin.Unit> onEditPost, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.example.travalms.ui.screens.PostItem, kotlin.Unit> onTailOrderClick, @org.jetbrains.annotations.NotNull
    com.example.travalms.ui.viewmodels.MyPublishedTailsViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    public static final void PostItemCard(@org.jetbrains.annotations.NotNull
    com.example.travalms.ui.screens.PostItem post, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.example.travalms.ui.screens.PostItem, kotlin.Unit> onItemClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.example.travalms.ui.screens.PostItem, kotlin.Unit> onRepost, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.example.travalms.ui.screens.PostItem, kotlin.Unit> onRefresh, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.example.travalms.ui.screens.PostItem, kotlin.Unit> onDelete) {
    }
}