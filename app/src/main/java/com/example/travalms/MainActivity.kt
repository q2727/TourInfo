package com.example.travalms

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.travalms.ui.navigation.AppNavigation
import com.example.travalms.ui.theme.TravalMSTheme
import org.jivesoftware.smack.SmackConfiguration
import com.example.travalms.data.remote.XMPPService
import com.example.travalms.data.remote.XMPPManager
import dagger.hilt.android.AndroidEntryPoint
import android.content.res.Configuration

/**
 * MainActivity是应用程序的主活动，负责初始化界面
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"
    
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 初始化Smack配置
        initializeSmack()
        
        // 启动XMPP服务
        startXMPPService()
        
        // 启用边缘到边缘的显示，提供更大的屏幕显示区域
        enableEdgeToEdge()
        
        // 设置内容视图为Compose布局，使用Jetpack Compose构建UI
        setContent {
            // 应用自定义主题，TravalMSTheme包含应用的颜色、排版等
            TravalMSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
    
    /**
     * 初始化Smack库配置
     */
    private fun initializeSmack() {
        // 设置Smack配置
        SmackConfiguration.DEBUG = true
        System.setProperty("smack.debugEnabled", "true")
        System.setProperty("smack.dnssec.disabled", "true")
    }
    
    /**
     * 启动XMPP服务
     */
    private fun startXMPPService() {
        try {
            Log.d(TAG, "尝试启动XMPP服务")
            XMPPService.startService(this)
        } catch (e: Exception) {
            Log.e(TAG, "启动XMPP服务失败: ${e.message}", e)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        
        // 检查是否真的需要停止服务
        // 通常应用关闭时不应停止服务，因为我们希望保持XMPP连接
        // 但如果是用户主动退出登录，则应该停止服务
        
        // 检查是否已登录，如果未登录则停止服务
        if (XMPPManager.getInstance().connectionState.value.toString() != "AUTHENTICATED") {
            Log.d(TAG, "用户未登录，停止XMPP服务")
            XMPPService.stopService(this)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // 处理配置变化，但不重建Activity
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // 保存必要的状态
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // 恢复必要的状态
    }
}
