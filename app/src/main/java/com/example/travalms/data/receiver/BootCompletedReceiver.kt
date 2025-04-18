package com.example.travalms.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.travalms.data.remote.XMPPService
import com.example.travalms.data.remote.XMPPManager

/**
 * 启动接收器，在设备重启后自动启动XMPP服务
 */
class BootCompletedReceiver : BroadcastReceiver() {
    private val TAG = "BootCompletedReceiver"
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d(TAG, "设备已重启，检查是否需要启动XMPP服务")
            
            // 检查是否有保存的凭据
            val prefs = context.getSharedPreferences("xmpp_prefs", Context.MODE_PRIVATE)
            val username = prefs.getString("username", "")
            val password = prefs.getString("password", "")
            
            if (!username.isNullOrEmpty() && !password.isNullOrEmpty()) {
                Log.d(TAG, "发现保存的凭据，启动XMPP服务")
                
                // 启动XMPP服务
                XMPPService.startService(context)
            } else {
                Log.d(TAG, "没有保存的凭据，不启动XMPP服务")
            }
        }
    }
} 