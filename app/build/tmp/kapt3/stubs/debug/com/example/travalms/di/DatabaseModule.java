package com.example.travalms.di;

import android.content.Context;
import com.example.travalms.data.db.ChatDatabase;
import com.example.travalms.data.db.GroupChatDao;
import com.example.travalms.data.db.MessageDao;
import com.example.travalms.data.db.GroupChatMessageDao;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

/**
 * 数据库依赖注入模块
 */
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0004H\u0007J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\t\u001a\u00020\u0004H\u0007J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0004H\u0007\u00a8\u0006\u000e"}, d2 = {"Lcom/example/travalms/di/DatabaseModule;", "", "()V", "provideDatabase", "Lcom/example/travalms/data/db/ChatDatabase;", "context", "Landroid/content/Context;", "provideGroupChatDao", "Lcom/example/travalms/data/db/GroupChatDao;", "database", "provideGroupChatMessageDao", "Lcom/example/travalms/data/db/GroupChatMessageDao;", "provideMessageDao", "Lcom/example/travalms/data/db/MessageDao;", "app_debug"})
@dagger.Module
public final class DatabaseModule {
    @org.jetbrains.annotations.NotNull
    public static final com.example.travalms.di.DatabaseModule INSTANCE = null;
    
    private DatabaseModule() {
        super();
    }
    
    /**
     * 提供数据库实例
     */
    @org.jetbrains.annotations.NotNull
    @dagger.Provides
    @javax.inject.Singleton
    public final com.example.travalms.data.db.ChatDatabase provideDatabase(@org.jetbrains.annotations.NotNull
    @dagger.hilt.android.qualifiers.ApplicationContext
    android.content.Context context) {
        return null;
    }
    
    /**
     * 提供消息DAO
     */
    @org.jetbrains.annotations.NotNull
    @dagger.Provides
    public final com.example.travalms.data.db.MessageDao provideMessageDao(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.db.ChatDatabase database) {
        return null;
    }
    
    /**
     * 提供群聊DAO
     */
    @org.jetbrains.annotations.NotNull
    @dagger.Provides
    public final com.example.travalms.data.db.GroupChatDao provideGroupChatDao(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.db.ChatDatabase database) {
        return null;
    }
    
    /**
     * 提供群聊消息DAO
     */
    @org.jetbrains.annotations.NotNull
    @dagger.Provides
    public final com.example.travalms.data.db.GroupChatMessageDao provideGroupChatMessageDao(@org.jetbrains.annotations.NotNull
    com.example.travalms.data.db.ChatDatabase database) {
        return null;
    }
}