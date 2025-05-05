package com.example.travalms.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件工具类
 * 提供从Uri获取真实文件路径等功能
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0002J;\u0010\u000f\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\u0010\u001a\u0004\u0018\u00010\u00042\u000e\u0010\u0011\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0012H\u0002\u00a2\u0006\u0002\u0010\u0013J\u001a\u0010\u0014\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u001a\u0010\u0015\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0018\u0010\u0016\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0010\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0010\u0010\u001a\u001a\u00020\u00182\u0006\u0010\u0007\u001a\u00020\bH\u0002\u00a8\u0006\u001b"}, d2 = {"Lcom/example/travalms/utils/FileUtil;", "", "()V", "copyFileToCache", "", "context", "Landroid/content/Context;", "uri", "Landroid/net/Uri;", "copyInputStreamToFile", "", "inputStream", "Ljava/io/InputStream;", "file", "Ljava/io/File;", "getDataColumn", "selection", "selectionArgs", "", "(Landroid/content/Context;Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)Ljava/lang/String;", "getFileName", "getFilePathFromDownloadsDocument", "getRealPathFromUri", "isDownloadsDocument", "", "isExternalStorageDocument", "isMediaDocument", "app_debug"})
public final class FileUtil {
    @org.jetbrains.annotations.NotNull
    public static final com.example.travalms.utils.FileUtil INSTANCE = null;
    
    private FileUtil() {
        super();
    }
    
    /**
     * 从Uri获取文件真实路径
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getRealPathFromUri(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    android.net.Uri uri) {
        return null;
    }
    
    /**
     * 从下载文档URI获取文件路径
     */
    private final java.lang.String getFilePathFromDownloadsDocument(android.content.Context context, android.net.Uri uri) {
        return null;
    }
    
    /**
     * 获取文件名
     */
    private final java.lang.String getFileName(android.content.Context context, android.net.Uri uri) {
        return null;
    }
    
    /**
     * 复制文件到缓存目录
     */
    private final java.lang.String copyFileToCache(android.content.Context context, android.net.Uri uri) {
        return null;
    }
    
    /**
     * 复制输入流到文件
     */
    private final void copyInputStreamToFile(java.io.InputStream inputStream, java.io.File file) {
    }
    
    /**
     * 获取数据列
     */
    private final java.lang.String getDataColumn(android.content.Context context, android.net.Uri uri, java.lang.String selection, java.lang.String[] selectionArgs) {
        return null;
    }
    
    /**
     * 判断是否是外部存储文档
     */
    private final boolean isExternalStorageDocument(android.net.Uri uri) {
        return false;
    }
    
    /**
     * 判断是否是下载文档
     */
    private final boolean isDownloadsDocument(android.net.Uri uri) {
        return false;
    }
    
    /**
     * 判断是否是媒体文档
     */
    private final boolean isMediaDocument(android.net.Uri uri) {
        return false;
    }
}