package com.example.travalms.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * 聊天应用数据库类
 */
@Database(
    entities = [MessageEntity::class, GroupChatEntity::class], 
    version = 2, 
    exportSchema = true // 修改为true以导出schema
)
@TypeConverters(DateConverters::class)
abstract class ChatDatabase : RoomDatabase() {
    /**
     * 获取消息DAO
     */
    abstract fun messageDao(): MessageDao
    
    /**
     * 获取群聊DAO
     */
    abstract fun groupChatDao(): GroupChatDao
    
    companion object {
        @Volatile
        private var INSTANCE: ChatDatabase? = null
        
        /**
         * 获取数据库实例，如果不存在则创建
         */
        fun getDatabase(context: Context): ChatDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChatDatabase::class.java,
                    "chat_database"
                )
                .fallbackToDestructiveMigration() // 如果版本升级，重建数据库
                .build()
                
                INSTANCE = instance
                instance
            }
        }
    }
} 