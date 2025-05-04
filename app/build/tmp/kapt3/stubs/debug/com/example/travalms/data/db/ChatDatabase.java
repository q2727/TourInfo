package com.example.travalms.data.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * 聊天应用数据库类
 */
@androidx.room.TypeConverters(value = {com.example.travalms.data.db.DateConverters.class})
@androidx.room.Database(entities = {com.example.travalms.data.db.MessageEntity.class, com.example.travalms.data.db.GroupChatEntity.class, com.example.travalms.data.db.GroupChatMessageEntity.class}, version = 4, exportSchema = true)
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \t2\u00020\u0001:\u0001\tB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&\u00a8\u0006\n"}, d2 = {"Lcom/example/travalms/data/db/ChatDatabase;", "Landroidx/room/RoomDatabase;", "()V", "groupChatDao", "Lcom/example/travalms/data/db/GroupChatDao;", "groupChatMessageDao", "Lcom/example/travalms/data/db/GroupChatMessageDao;", "messageDao", "Lcom/example/travalms/data/db/MessageDao;", "Companion", "app_debug"})
public abstract class ChatDatabase extends androidx.room.RoomDatabase {
    @org.jetbrains.annotations.NotNull
    public static final com.example.travalms.data.db.ChatDatabase.Companion Companion = null;
    @kotlin.jvm.Volatile
    private static volatile com.example.travalms.data.db.ChatDatabase INSTANCE;
    
    public ChatDatabase() {
        super();
    }
    
    /**
     * 获取消息DAO
     */
    @org.jetbrains.annotations.NotNull
    public abstract com.example.travalms.data.db.MessageDao messageDao();
    
    /**
     * 获取群聊DAO
     */
    @org.jetbrains.annotations.NotNull
    public abstract com.example.travalms.data.db.GroupChatDao groupChatDao();
    
    /**
     * 获取群聊消息DAO
     */
    @org.jetbrains.annotations.NotNull
    public abstract com.example.travalms.data.db.GroupChatMessageDao groupChatMessageDao();
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/example/travalms/data/db/ChatDatabase$Companion;", "", "()V", "INSTANCE", "Lcom/example/travalms/data/db/ChatDatabase;", "getDatabase", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        /**
         * 获取数据库实例，如果不存在则创建
         */
        @org.jetbrains.annotations.NotNull
        public final com.example.travalms.data.db.ChatDatabase getDatabase(@org.jetbrains.annotations.NotNull
        android.content.Context context) {
            return null;
        }
    }
}