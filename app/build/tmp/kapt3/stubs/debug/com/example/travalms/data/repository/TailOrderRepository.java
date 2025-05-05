package com.example.travalms.data.repository;

import com.example.travalms.ui.screens.TailOrder;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u001b\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\bH\u00a6@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tJ!\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u000bH\u00a6@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\r\u00f8\u0001\u0001\u0082\u0002\n\n\u0002\b\u0019\n\u0004\b!0\u0001\u00a8\u0006\u000e\u00c0\u0006\u0001"}, d2 = {"Lcom/example/travalms/data/repository/TailOrderRepository;", "", "getTailOrderById", "Lcom/example/travalms/ui/screens/TailOrder;", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTailOrders", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateFavoriteStatus", "", "isFavorite", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface TailOrderRepository {
    
    /**
     * 获取所有尾单列表
     */
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getTailOrders(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.example.travalms.ui.screens.TailOrder>> continuation);
    
    /**
     * 根据ID获取尾单详情
     */
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getTailOrderById(@org.jetbrains.annotations.NotNull
    java.lang.String id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.example.travalms.ui.screens.TailOrder> continuation);
    
    /**
     * 更新尾单收藏状态
     */
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object updateFavoriteStatus(@org.jetbrains.annotations.NotNull
    java.lang.String id, boolean isFavorite, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Boolean> continuation);
}