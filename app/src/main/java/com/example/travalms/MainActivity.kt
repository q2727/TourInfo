package com.example.travalms

import android.os.Build
import android.os.Bundle
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

/**
 * MainActivity是应用程序的主活动，负责初始化界面
 */
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}
