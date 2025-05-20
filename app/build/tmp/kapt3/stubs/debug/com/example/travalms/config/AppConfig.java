package com.example.travalms.config;

import java.lang.System;

/**
 * 应用程序配置常量
 * 集中管理所有硬编码的IP地址和服务器配置
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/example/travalms/config/AppConfig;", "", "()V", "API_BASE_URL", "", "API_SERVER_ADDRESS", "API_SERVER_PORT", "AVATAR_SERVER_ADDRESS", "PROD_API_BASE_URL", "PROD_IMAGE_BASE_URL", "XMPP_SERVER_HOST", "app_debug"})
public final class AppConfig {
    @org.jetbrains.annotations.NotNull
    public static final com.example.travalms.config.AppConfig INSTANCE = null;
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String API_SERVER_ADDRESS = "10.63.152.125";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String API_SERVER_PORT = "8080";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String API_BASE_URL = "http://10.63.152.125:8080/";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PROD_API_BASE_URL = "http://42.193.112.197/";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PROD_IMAGE_BASE_URL = "http://42.193.112.197/static/";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String XMPP_SERVER_HOST = "120.46.26.49";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String AVATAR_SERVER_ADDRESS = "10.63.152.125";
    
    private AppConfig() {
        super();
    }
}