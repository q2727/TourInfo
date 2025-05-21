package com.example.travalms.data.api;

import com.example.travalms.api.dto.TailOrderRequest;
import com.example.travalms.api.dto.TailOrderResponse;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J!\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0007J\'\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u00032\b\b\u0001\u0010\u000b\u001a\u00020\fH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\rJ!\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\n0\u00032\b\b\u0001\u0010\u000f\u001a\u00020\u0010H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011\u00f8\u0001\u0001\u0082\u0002\n\n\u0002\b\u0019\n\u0004\b!0\u0001\u00a8\u0006\u0012\u00c0\u0006\u0001"}, d2 = {"Lcom/example/travalms/data/api/TailOrderApiService;", "", "deleteTailOrder", "Lretrofit2/Response;", "Ljava/lang/Void;", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUserTailOrders", "", "Lcom/example/travalms/api/dto/TailOrderResponse;", "jid", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "publishTailOrder", "tailOrderRequest", "Lcom/example/travalms/api/dto/TailOrderRequest;", "(Lcom/example/travalms/api/dto/TailOrderRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface TailOrderApiService {
    
    @org.jetbrains.annotations.Nullable
    @retrofit2.http.POST(value = "api/tail-orders")
    public abstract java.lang.Object publishTailOrder(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Body
    com.example.travalms.api.dto.TailOrderRequest tailOrderRequest, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.example.travalms.api.dto.TailOrderResponse>> continuation);
    
    @org.jetbrains.annotations.Nullable
    @retrofit2.http.GET(value = "api/tail-orders/user")
    public abstract java.lang.Object getUserTailOrders(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Query(value = "jid")
    java.lang.String jid, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.example.travalms.api.dto.TailOrderResponse>>> continuation);
    
    @org.jetbrains.annotations.Nullable
    @retrofit2.http.DELETE(value = "api/tail-orders/{id}")
    public abstract java.lang.Object deleteTailOrder(@retrofit2.http.Path(value = "id")
    long id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.lang.Void>> continuation);
}