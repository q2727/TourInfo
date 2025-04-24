package com.example.travalms.di

import android.content.Context
import com.example.travalms.data.db.ChatDatabase
import com.example.travalms.data.db.GroupChatDao
import com.example.travalms.data.db.MessageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 数据库依赖注入模块
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    /**
     * 提供数据库实例
     */
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ChatDatabase {
        return ChatDatabase.getDatabase(context)
    }
    
    /**
     * 提供消息DAO
     */
    @Provides
    fun provideMessageDao(database: ChatDatabase): MessageDao {
        return database.messageDao()
    }
    
    /**
     * 提供群聊DAO
     */
    @Provides
    fun provideGroupChatDao(database: ChatDatabase): GroupChatDao {
        return database.groupChatDao()
    }
} 