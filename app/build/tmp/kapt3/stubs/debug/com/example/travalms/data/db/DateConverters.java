package com.example.travalms.data.db;

import androidx.room.TypeConverter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 日期转换器 - 用于LocalDateTime与Long之间的转换
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0019\u0010\u0003\u001a\u0004\u0018\u00010\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007\u00a2\u0006\u0002\u0010\u0007J\u0019\u0010\b\u001a\u0004\u0018\u00010\u00062\b\u0010\t\u001a\u0004\u0018\u00010\u0004H\u0007\u00a2\u0006\u0002\u0010\n\u00a8\u0006\u000b"}, d2 = {"Lcom/example/travalms/data/db/DateConverters;", "", "()V", "dateToTimestamp", "", "date", "Ljava/time/LocalDateTime;", "(Ljava/time/LocalDateTime;)Ljava/lang/Long;", "fromTimestamp", "value", "(Ljava/lang/Long;)Ljava/time/LocalDateTime;", "app_debug"})
public final class DateConverters {
    
    public DateConverters() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.TypeConverter
    public final java.time.LocalDateTime fromTimestamp(@org.jetbrains.annotations.Nullable
    java.lang.Long value) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.TypeConverter
    public final java.lang.Long dateToTimestamp(@org.jetbrains.annotations.Nullable
    java.time.LocalDateTime date) {
        return null;
    }
}