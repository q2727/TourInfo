package com.example.travalms.data.api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * 提供网络相关的单例实例
 */
object NetworkModule {
    // 请确认这个IP地址是否正确，并且服务器在这个端口上运行
    private const val BASE_URL = "http://192.168.100.6:8080/"
    private const val TIMEOUT = 60L // 增加超时时间到60秒
    private const val TAG = "NetworkAPI"
    
    /**
     * 自定义拦截器，用于记录请求和响应日志
     */
    private class LoggingInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val requestTime = System.nanoTime()
            
            // 记录请求URL和参数
            val requestLog = StringBuilder()
            requestLog.append("Request: ${request.method} ${request.url}")
            
            // 获取并记录请求体（如果有）
            if (request.body != null) {
                val buffer = Buffer()
                request.body!!.writeTo(buffer)
                val charset = Charset.forName("UTF-8")
                val bodyStr = buffer.readString(charset)
                requestLog.append("\nRequest Body: $bodyStr")
            }
            
            // 记录请求头
            requestLog.append("\nHeaders:")
            for (header in request.headers) {
                requestLog.append("\n  ${header.first}: ${header.second}")
            }
            
            Log.d(TAG, requestLog.toString())
            
            // 执行请求并记录响应
            val response: Response
            try {
                response = chain.proceed(request)
                Log.d(TAG, "连接成功，获取响应中...")
            } catch (e: Exception) {
                Log.e(TAG, "请求失败: ${e.message}", e)
                Log.e(TAG, "目标服务器: ${request.url}")
                throw e
            }
            
            val responseTime = System.nanoTime()
            val responseTimeMs = (responseTime - requestTime) / 1_000_000
            
            // 记录响应
            val responseLog = StringBuilder()
            responseLog.append("Response: ${response.code} for ${request.url} in ${responseTimeMs}ms")
            
            // 获取并记录响应体（如果有）
            val responseBody = response.body
            if (responseBody != null) {
                val bodyString = responseBody.string()
                responseLog.append("\nResponse Body: $bodyString")

                // 由于响应体只能读取一次，我们需要创建一个新的响应返回给调用方
                val wrappedBody = ResponseBody.create(responseBody.contentType(), bodyString)
                return response.newBuilder().body(wrappedBody).build()
            }

            Log.d(TAG, responseLog.toString())

            return response
        }
    }
    
    /**
     * 创建OkHttpClient实例
     */
    private fun provideOkHttpClient(): OkHttpClient {
        // 标准HTTP日志拦截器
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true) // 启用连接失败重试
            .addInterceptor(loggingInterceptor)
            .addInterceptor(LoggingInterceptor())  // 添加自定义日志拦截器
            .build()
    }
    
    /**
     * 创建Gson实例
     */
    private fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }
    
    /**
     * 创建Retrofit实例
     */
    private fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    
    /**
     * 提供UserApiService实例
     */
    fun provideUserApiService(): UserApiService {
        val okHttpClient = provideOkHttpClient()
        val gson = provideGson()
        val retrofit = provideRetrofit(okHttpClient, gson)
        return retrofit.create(UserApiService::class.java)
    }

    /**
     * 提供群聊API服务
     */
    fun provideGroupChatApiService(): GroupChatApiService {
        val okHttpClient = provideOkHttpClient()
        val gson = provideGson()
        val retrofit = provideRetrofit(okHttpClient, gson)
        return retrofit.create(GroupChatApiService::class.java)
    }
}