package com.example.travalms.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * 文件工具类
 * 提供从Uri获取真实文件路径等功能
 */
object FileUtil {

    /**
     * 从Uri获取文件真实路径
     */
    fun getRealPathFromUri(context: Context, uri: Uri): String? {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } 
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                try {
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), 
                        id.toLong()
                    )
                    return getDataColumn(context, contentUri, null, null)
                } catch (e: NumberFormatException) {
                    // 处理id不是数字的情况
                    return getFilePathFromDownloadsDocument(context, uri)
                }
            } 
            // MediaProvider
            else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                
                var contentUri: Uri? = null
                when (type) {
                    "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } 
        // MediaStore (and general)
        else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } 
        // File
        else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        
        // 如果以上方法都无法获取路径，则复制文件到缓存目录
        return copyFileToCache(context, uri)
    }
    
    /**
     * 从下载文档URI获取文件路径
     */
    private fun getFilePathFromDownloadsDocument(context: Context, uri: Uri): String? {
        val fileName = getFileName(context, uri)
        if (fileName != null) {
            val cacheDir = context.externalCacheDir ?: context.cacheDir
            val destinationFile = File(cacheDir, fileName)
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    FileOutputStream(destinationFile).use { outputStream ->
                        val buffer = ByteArray(4 * 1024) // 4k buffer
                        var read: Int
                        while (inputStream.read(buffer).also { read = it } != -1) {
                            outputStream.write(buffer, 0, read)
                        }
                        outputStream.flush()
                        return destinationFile.absolutePath
                    }
                }
            } catch (e: IOException) {
                Log.e("FileUtil", "Could not copy file", e)
            }
        }
        return null
    }
    
    /**
     * 获取文件名
     */
    private fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (columnIndex != -1) {
                        result = cursor.getString(columnIndex)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1 && cut != null) {
                result = result?.substring(cut + 1)
            }
        }
        return result
    }
    
    /**
     * 复制文件到缓存目录
     */
    private fun copyFileToCache(context: Context, uri: Uri): String? {
        val fileName = getFileName(context, uri) ?: return null
        val cacheDir = context.externalCacheDir ?: context.cacheDir
        val destinationFile = File(cacheDir, fileName)
        
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                copyInputStreamToFile(inputStream, destinationFile)
                return destinationFile.absolutePath
            }
        } catch (e: IOException) {
            Log.e("FileUtil", "Could not copy file", e)
        }
        return null
    }
    
    /**
     * 复制输入流到文件
     */
    private fun copyInputStreamToFile(inputStream: InputStream, file: File) {
        try {
            FileOutputStream(file).use { outputStream ->
                val buffer = ByteArray(4 * 1024) // 4k buffer
                var read: Int
                while (inputStream.read(buffer).also { read = it } != -1) {
                    outputStream.write(buffer, 0, read)
                }
                outputStream.flush()
            }
        } catch (e: IOException) {
            Log.e("FileUtil", "Could not copy input stream to file", e)
        }
    }
    
    /**
     * 获取数据列
     */
    private fun getDataColumn(
        context: Context, 
        uri: Uri?, 
        selection: String?, 
        selectionArgs: Array<String>?
    ): String? {
        uri ?: return null
        
        val column = "_data"
        val projection = arrayOf(column)
        
        try {
            context.contentResolver.query(uri, projection, selection, selectionArgs, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(columnIndex)
                }
            }
        } catch (e: Exception) {
            Log.e("FileUtil", "Could not get data column", e)
        }
        return null
    }
    
    /**
     * 判断是否是外部存储文档
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }
    
    /**
     * 判断是否是下载文档
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }
    
    /**
     * 判断是否是媒体文档
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }
} 