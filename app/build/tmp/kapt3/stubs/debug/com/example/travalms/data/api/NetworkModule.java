package com.example.travalms.data.api;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * 提供网络相关的单例实例
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001:\u0001\u0014B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\b\u001a\u00020\tJ\b\u0010\n\u001a\u00020\u000bH\u0002J\b\u0010\f\u001a\u00020\rH\u0002J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u000bH\u0002J\u0006\u0010\u0012\u001a\u00020\u0013R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/example/travalms/data/api/NetworkModule;", "", "()V", "BASE_URL", "", "TAG", "TIMEOUT", "", "provideGroupChatApiService", "Lcom/example/travalms/data/api/GroupChatApiService;", "provideGson", "Lcom/google/gson/Gson;", "provideOkHttpClient", "Lokhttp3/OkHttpClient;", "provideRetrofit", "Lretrofit2/Retrofit;", "okHttpClient", "gson", "provideUserApiService", "Lcom/example/travalms/data/api/UserApiService;", "LoggingInterceptor", "app_debug"})
public final class NetworkModule {
    @org.jetbrains.annotations.NotNull
    public static final com.example.travalms.data.api.NetworkModule INSTANCE = null;
    private static final java.lang.String BASE_URL = "http://192.168.100.6:8080/";
    private static final long TIMEOUT = 60L;
    private static final java.lang.String TAG = "NetworkAPI";
    
    private NetworkModule() {
        super();
    }
    
    /**
     * 创建OkHttpClient实例
     */
    private final okhttp3.OkHttpClient provideOkHttpClient() {
        return null;
    }
    
    /**
     * 创建Gson实例
     */
    private final com.google.gson.Gson provideGson() {
        return null;
    }
    
    /**
     * 创建Retrofit实例
     */
    private final retrofit2.Retrofit provideRetrofit(okhttp3.OkHttpClient okHttpClient, com.google.gson.Gson gson) {
        return null;
    }
    
    /**
     * 提供UserApiService实例
     */
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.data.api.UserApiService provideUserApiService() {
        return null;
    }
    
    /**
     * 提供群聊API服务
     */
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.data.api.GroupChatApiService provideGroupChatApiService() {
        return null;
    }
    
    /**
     * 自定义拦截器，用于记录请求和响应日志
     */
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016\u00a8\u0006\u0007"}, d2 = {"Lcom/example/travalms/data/api/NetworkModule$LoggingInterceptor;", "Lokhttp3/Interceptor;", "()V", "intercept", "Lokhttp3/Response;", "chain", "Lokhttp3/Interceptor$Chain;", "app_debug"})
    static final class LoggingInterceptor implements okhttp3.Interceptor {
        
        public LoggingInterceptor() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        @kotlin.jvm.Throws(exceptionClasses = {java.io.IOException.class})
        @java.lang.Override
        public okhttp3.Response intercept(@org.jetbrains.annotations.NotNull
        okhttp3.Interceptor.Chain chain) throws java.io.IOException {
            return null;
        }
    }
}