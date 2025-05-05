package com.example.travalms.di;

import com.example.travalms.data.api.GroupChatApiClient;
import com.example.travalms.data.remote.GroupChatJoinHandler;
import com.example.travalms.data.remote.GroupChatManager;
import com.example.travalms.data.remote.XMPPGroupChatManager;
import com.example.travalms.data.remote.XMPPManager;
import com.example.travalms.data.remote.XMPPServiceLocator;
import com.example.travalms.data.repository.GroupChatRepository;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

/**
 * 聊天模块 - 提供与聊天相关的依赖
 */
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007J \u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0004H\u0007J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0007J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\t\u001a\u00020\nH\u0007J\b\u0010\u0010\u001a\u00020\nH\u0007J\b\u0010\u0011\u001a\u00020\u0012H\u0007\u00a8\u0006\u0013"}, d2 = {"Lcom/example/travalms/di/ChatModule;", "", "()V", "provideGroupChatApiClient", "Lcom/example/travalms/data/api/GroupChatApiClient;", "provideGroupChatJoinHandler", "Lcom/example/travalms/data/remote/GroupChatJoinHandler;", "groupChatRepository", "Lcom/example/travalms/data/repository/GroupChatRepository;", "xmppManager", "Lcom/example/travalms/data/remote/XMPPManager;", "groupChatApiClient", "provideGroupChatManager", "Lcom/example/travalms/data/remote/GroupChatManager;", "provideXMPPGroupChatManager", "Lcom/example/travalms/data/remote/XMPPGroupChatManager;", "provideXMPPManager", "provideXMPPServiceLocator", "Lcom/example/travalms/data/remote/XMPPServiceLocator;", "app_debug"})
@dagger.Module
public final class ChatModule {
    @org.jetbrains.annotations.NotNull
    public static final com.example.travalms.di.ChatModule INSTANCE = null;
    
    private ChatModule() {
        super();
    }
    
    /**
     * 提供XMPPManager实例
     */
    @org.jetbrains.annotations.NotNull
    @javax.inject.Singleton
    @dagger.Provides
    public final com.example.travalms.data.remote.XMPPManager provideXMPPManager() {
        return null;
    }
    
    /**
     * 提供XMPPGroupChatManager实例
     * 注意：由于XMPPGroupChatManager是由XMPPManager创建的，
     * 这个方法目前不会被使用，保留仅为参考
     */
    @org.jetbrains.annotations.NotNull
    @javax.inject.Singleton
    @dagger.Provides
    public final com.example.travalms.data.remote.XMPPGroupChatManager provideXMPPGroupChatManager(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.remote.XMPPManager xmppManager) {
        return null;
    }
    
    /**
     * 提供GroupChatManager实例
     */
    @org.jetbrains.annotations.NotNull
    @javax.inject.Singleton
    @dagger.Provides
    public final com.example.travalms.data.remote.GroupChatManager provideGroupChatManager(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.repository.GroupChatRepository groupChatRepository, @org.jetbrains.annotations.NotNull
    com.example.travalms.data.remote.XMPPManager xmppManager) {
        return null;
    }
    
    /**
     * 提供XMPPServiceLocator实例
     */
    @org.jetbrains.annotations.NotNull
    @javax.inject.Singleton
    @dagger.Provides
    public final com.example.travalms.data.remote.XMPPServiceLocator provideXMPPServiceLocator() {
        return null;
    }
    
    /**
     * 提供GroupChatJoinHandler实例
     */
    @org.jetbrains.annotations.NotNull
    @javax.inject.Singleton
    @dagger.Provides
    public final com.example.travalms.data.remote.GroupChatJoinHandler provideGroupChatJoinHandler(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.repository.GroupChatRepository groupChatRepository, @org.jetbrains.annotations.NotNull
    com.example.travalms.data.remote.XMPPManager xmppManager, @org.jetbrains.annotations.NotNull
    com.example.travalms.data.api.GroupChatApiClient groupChatApiClient) {
        return null;
    }
    
    /**
     * 提供GroupChatApiClient实例
     */
    @org.jetbrains.annotations.NotNull
    @javax.inject.Singleton
    @dagger.Provides
    public final com.example.travalms.data.api.GroupChatApiClient provideGroupChatApiClient() {
        return null;
    }
}