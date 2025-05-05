package com.example.travalms.data.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.*;

/**
 * 用户API服务接口
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001J-\u0010\u0002\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00040\u00032\b\b\u0001\u0010\u0006\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0007J-\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u00032\u0014\b\u0001\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u0004H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJb\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\u0019\b\u0001\u0010\f\u001a\u0013\u0012\u0004\u0012\u00020\u0005\u0012\t\u0012\u00070\r\u00a2\u0006\u0002\b\u000e0\u00042\n\b\u0001\u0010\u000f\u001a\u0004\u0018\u00010\u00102\n\b\u0001\u0010\u0011\u001a\u0004\u0018\u00010\u00102\n\b\u0001\u0010\u0012\u001a\u0004\u0018\u00010\u00102\n\b\u0001\u0010\u0013\u001a\u0004\u0018\u00010\u0010H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0014\u00f8\u0001\u0001\u0082\u0002\n\n\u0002\b\u0019\n\u0004\b!0\u0001\u00a8\u0006\u0015\u00c0\u0006\u0001"}, d2 = {"Lcom/example/travalms/data/api/UserApiService;", "", "getUserInfo", "Lretrofit2/Response;", "", "", "username", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "login", "credentials", "(Ljava/util/Map;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "register", "params", "Lokhttp3/RequestBody;", "Lkotlin/jvm/JvmSuppressWildcards;", "businessLicense", "Lokhttp3/MultipartBody$Part;", "idCardFront", "idCardBack", "avatar", "(Ljava/util/Map;Lokhttp3/MultipartBody$Part;Lokhttp3/MultipartBody$Part;Lokhttp3/MultipartBody$Part;Lokhttp3/MultipartBody$Part;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface UserApiService {
    
    /**
     * 用户注册 (带文件上传)
     */
    @org.jetbrains.annotations.Nullable
    @retrofit2.http.POST(value = "api/users/register")
    @retrofit2.http.Multipart
    public abstract java.lang.Object register(@org.jetbrains.annotations.NotNull
    @retrofit2.http.PartMap
    java.util.Map<java.lang.String, okhttp3.RequestBody> params, @org.jetbrains.annotations.Nullable
    @retrofit2.http.Part
    okhttp3.MultipartBody.Part businessLicense, @org.jetbrains.annotations.Nullable
    @retrofit2.http.Part
    okhttp3.MultipartBody.Part idCardFront, @org.jetbrains.annotations.Nullable
    @retrofit2.http.Part
    okhttp3.MultipartBody.Part idCardBack, @org.jetbrains.annotations.Nullable
    @retrofit2.http.Part
    okhttp3.MultipartBody.Part avatar, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.lang.Object>> continuation);
    
    /**
     * 用户登录
     */
    @org.jetbrains.annotations.Nullable
    @retrofit2.http.POST(value = "api/users/login")
    public abstract java.lang.Object login(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Body
    java.util.Map<java.lang.String, java.lang.String> credentials, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.lang.String>> continuation);
    
    /**
     * 获取用户信息
     */
    @org.jetbrains.annotations.Nullable
    @retrofit2.http.GET(value = "api/users/{username}")
    public abstract java.lang.Object getUserInfo(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Path(value = "username")
    java.lang.String username, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.Map<java.lang.String, java.lang.Object>>> continuation);
}