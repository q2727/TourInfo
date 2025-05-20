package com.example.travalms.data.api;

import retrofit2.http.GET;
import com.example.travalms.config.AppConfig;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u001b\u0010\u0002\u001a\f\u0012\u0004\u0012\u00020\u00040\u0003j\u0002`\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006\u00f8\u0001\u0001\u0082\u0002\n\n\u0002\b\u0019\n\u0004\b!0\u0001\u00a8\u0006\u0007\u00c0\u0006\u0001"}, d2 = {"Lcom/example/travalms/data/api/ProductApiService;", "", "getProducts", "", "Lcom/example/travalms/data/api/ProductResponseItem;", "Lcom/example/travalms/data/api/ProductResponse;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface ProductApiService {
    
    @org.jetbrains.annotations.Nullable
    @retrofit2.http.GET(value = "/api/SearchProduct/LoadProduct")
    public abstract java.lang.Object getProducts(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.example.travalms.data.api.ProductResponseItem>> continuation);
}