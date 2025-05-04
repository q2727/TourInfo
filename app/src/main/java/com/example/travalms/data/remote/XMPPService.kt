package com.example.travalms.data.remote

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.travalms.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

/**
 * 前台服务，用于保持XMPP连接活跃，即使应用在后台运行
 */
@AndroidEntryPoint
class XMPPService : Service() {
    companion object {
        private const val TAG = "XMPPService"
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "XMPPServiceChannel"
        private const val WAKE_LOCK_TAG = "XMPPService:WakeLock"
        
        // 用于确保服务仅开始一次
        private val isServiceRunning = AtomicBoolean(false)
        
        /**
         * 启动服务的便捷方法
         */
        fun startService(context: Context) {
            if (isServiceRunning.compareAndSet(false, true)) {
                Log.d(TAG, "启动XMPP服务")
                val intent = Intent(context, XMPPService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            } else {
                Log.d(TAG, "XMPP服务已在运行")
            }
        }
        
        /**
         * 停止服务的便捷方法
         */
        fun stopService(context: Context) {
            Log.d(TAG, "停止XMPP服务")
            val intent = Intent(context, XMPPService::class.java)
            context.stopService(intent)
            isServiceRunning.set(false)
        }
    }
    
    // 协程作用域，用于后台任务
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    // XMPP管理器实例
    private lateinit var xmppManager: XMPPManager
    
    // 注入群聊管理器
    @Inject
    lateinit var groupChatManager: GroupChatManager
    
    // 注入群聊加入处理器
    @Inject
    lateinit var groupChatJoinHandler: GroupChatJoinHandler
    
    // WakeLock，防止设备休眠时断开连接
    private var wakeLock: PowerManager.WakeLock? = null
    
    // 监测连接状态的周期（秒）- 降低间隔提高响应速度
    private val monitorInterval = 15L
    
    // 是否启用更激进的保活策略
    private val useAggressiveKeepAlive = true
    
    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "XMPP服务创建")
        
        // 获取XMPPManager实例
        xmppManager = XMPPManager.getInstance()
        
        // 设置群聊加入完成的回调，用于同步群聊列表
        groupChatManager.setOnGroupChatsJoinedCallback {
            Log.d(TAG, "群聊加入完成，开始同步群聊列表")
            // 通过独立的协程调用同步方法，避免可能的死锁
            serviceScope.launch {
                try {
                    delay(1000) // 稍微延迟，确保所有群聊已正确加入
                    groupChatJoinHandler.syncGroupChatsFromServer()
                } catch (e: Exception) {
                    Log.e(TAG, "同步群聊列表失败: ${e.message}", e)
                }
            }
        }
        
        // 创建通知渠道（API 26+）
        createNotificationChannel()
        
        // 创建并发送前台服务通知
        val notification = createNotification("XMPP服务正在运行")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
        
        // 获取唤醒锁，保持CPU运行
        acquireWakeLock()
        
        // 监控连接状态
        startConnectionMonitor()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "XMPP服务启动或收到命令")
        
        // 处理intent actions
        intent?.let { 
            when (it.action) {
                else -> {
                    // 处理其他意图动作或无动作的情况
                    Log.d(TAG, "收到其他命令或无指定命令: ${it.action ?: "无action"}")
                }
            }
        }
        
        // 如果服务被系统杀死并重新启动，我们希望继续运行
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? {
        // 此服务不支持绑定
        return null
    }
    
    override fun onDestroy() {
        Log.d(TAG, "XMPP服务销毁")
        
        // 释放唤醒锁
        releaseWakeLock()
        
        // 取消所有协程
        serviceScope.cancel()
        
        super.onDestroy()
        
        // 重置运行状态
        isServiceRunning.set(false)
    }
    
    /**
     * 创建通知渠道（Android 8.0+）
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "XMPP连接服务"
            val description = "保持XMPP连接活跃的通知渠道"
            val importance = NotificationManager.IMPORTANCE_LOW // 低优先级，减少用户干扰
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                this.description = description
                enableLights(false)
                enableVibration(false)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    /**
     * 创建前台服务通知
     */
    private fun createNotification(contentText: String): Notification {
        // 创建PendingIntent，点击通知会打开主Activity
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        // 构建通知
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("TravalMS 正在运行")
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.ic_dialog_info)  // 使用临时图标，应替换为应用自己的图标
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)  // 用户无法滑动删除
            .build()
    }
    
    /**
     * 更新前台服务通知
     */
    private fun updateNotification(contentText: String) {
        val notification = createNotification(contentText)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    
    /**
     * 获取唤醒锁，防止设备休眠时断开连接
     */
    private fun acquireWakeLock() {
        if (wakeLock == null) {
            val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
            wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                WAKE_LOCK_TAG
            ).apply {
                setReferenceCounted(false)
                // 使用无限时长的WakeLock，防止系统休眠导致连接断开
                acquire()
                Log.d(TAG, "获取永久唤醒锁以保持连接")
            }
        } else if (!(wakeLock?.isHeld ?: false)) {
            // 如果WakeLock存在但未持有，重新获取
            wakeLock?.acquire()
            Log.d(TAG, "重新获取唤醒锁")
        }
    }
    
    /**
     * 释放唤醒锁
     */
    private fun releaseWakeLock() {
        wakeLock?.let {
            if (it.isHeld) {
                it.release()
                Log.d(TAG, "释放唤醒锁")
            }
            wakeLock = null
        }
    }
    
    /**
     * 启动连接监控，定期检查XMPP连接状态
     */
    private fun startConnectionMonitor() {
        serviceScope.launch {
            // 监听连接状态变化
            xmppManager.connectionState.collectLatest { state ->
                val statusText = when (state) {
                    ConnectionState.AUTHENTICATED -> "已连接到聊天服务器"
                    ConnectionState.CONNECTING -> "正在连接..."
                    ConnectionState.AUTHENTICATING -> "正在验证身份..."
                    ConnectionState.ERROR -> "连接出错"
                    ConnectionState.DISCONNECTED -> "已断开连接"
                    ConnectionState.CONNECTION_CLOSED -> "连接已关闭"
                    ConnectionState.RECONNECTING -> "正在重新连接..."
                    else -> "未知状态"
                }
                updateNotification(statusText)
                
                // 如果连接断开，尝试重新连接
                if (state == ConnectionState.DISCONNECTED || 
                    state == ConnectionState.ERROR || 
                    state == ConnectionState.CONNECTION_CLOSED) {
                    tryReconnect()
                }
            }
        }
        
        // 定期检查连接状态
        serviceScope.launch {
            while (true) {
                delay(monitorInterval * 1000)
                checkConnection()
            }
        }
    }
    
    /**
     * 检查XMPP连接状态，如果需要则重新连接
     */
    private fun checkConnection() {
        // 确保WakeLock有效
        if (wakeLock == null || !(wakeLock?.isHeld ?: false)) {
            acquireWakeLock()
        }
        
        val connection = xmppManager.currentConnection
        if (connection == null || !connection.isConnected) {
            Log.d(TAG, "定期检查: 连接不存在或已断开")
            tryReconnect()
            return
        }
        
        if (!connection.isAuthenticated) {
            Log.d(TAG, "定期检查: 连接存在但未认证")
            tryReconnect()
            return
        }
        
        // 检查连接状态
        if (xmppManager.connectionState.value != ConnectionState.AUTHENTICATED) {
            Log.d(TAG, "定期检查: 状态不是AUTHENTICATED")
            tryReconnect()
            return
        }
        
        // 发送ping以保持连接活跃
        serviceScope.launch {
            try {
                val pingResult = xmppManager.sendPing()
                if (pingResult.isSuccess && pingResult.getOrNull() == true) {
                    Log.d(TAG, "Ping成功，连接保持活跃")
                    // 如果启用了激进保活，发送空白presence更新
                    if (useAggressiveKeepAlive) {
                        try {
                            xmppManager.sendKeepAlivePresence()
                            Log.d(TAG, "发送了保活presence")
                        } catch (e: Exception) {
                            Log.e(TAG, "发送保活presence失败: ${e.message}")
                        }
                    }
                } else {
                    Log.w(TAG, "Ping失败，可能需要重新连接")
                    // 如果ping失败，尝试重新连接
                    tryReconnect()
                }
            } catch (e: Exception) {
                Log.e(TAG, "发送ping失败: ${e.message}")
                tryReconnect()
            }
        }
    }
    
    /**
     * 尝试重新连接到XMPP服务器
     */
    private fun tryReconnect() {
        // 这里需要访问应用的登录凭据
        // 可以从SharedPreferences或安全存储中获取
        serviceScope.launch {
            try {
                // 从安全存储中获取凭据
                val username = getUsername()
                val password = getPassword()
                
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    Log.d(TAG, "尝试重新连接XMPP: 用户=$username")
                    
                    // 首先确保彻底断开旧连接 - 这很重要，避免资源冲突
                    try {
                        // 显式调用logout而不是disconnect，以确保完整清理
                        xmppManager.forceDisconnect()
                        // 等待足够的时间确保连接完全释放
                        delay(3000)
                    } catch (e: Exception) {
                        Log.e(TAG, "断开旧连接时出错: ${e.message}", e)
                        // 继续尝试连接
                    }
                    
                    val result = xmppManager.login(username, password)
                    if (result.isSuccess) {
                        Log.d(TAG, "XMPP重新连接成功")
                        updateNotification("已连接到聊天服务器")
                    } else {
                        Log.e(TAG, "XMPP重新连接失败: ${result.exceptionOrNull()?.message}")
                        updateNotification("重新连接失败")
                    }
                } else {
                    Log.w(TAG, "无法重新连接: 凭据不可用")
                    updateNotification("未找到登录信息")
                }
            } catch (e: Exception) {
                Log.e(TAG, "重新连接时出错: ${e.message}", e)
                updateNotification("重新连接时出错")
            }
        }
    }
    
    /**
     * 从安全存储中获取用户名
     */
    private fun getUsername(): String {
        // 实现从SharedPreferences或其他存储中获取用户名
        // 这只是一个示例，你应该使用更安全的存储方式
        val prefs = getSharedPreferences("xmpp_prefs", Context.MODE_PRIVATE)
        return prefs.getString("username", "") ?: ""
    }
    
    /**
     * 从安全存储中获取密码
     */
    private fun getPassword(): String {
        // 实现从SharedPreferences或其他存储中获取密码
        // 这只是一个示例，你应该使用更安全的存储方式，如EncryptedSharedPreferences
        val prefs = getSharedPreferences("xmpp_prefs", Context.MODE_PRIVATE)
        return prefs.getString("password", "") ?: ""
    }
} 