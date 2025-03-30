package com.example.travalms.ui.theme

import androidx.compose.ui.graphics.Color

// 从图片中提取的官方认证蓝色
val OfficialBlue = Color(0xFF5C6BC0)      // 官方认证按钮的紫蓝色

// 主色调 - 官方认证蓝色
val PrimaryColor = OfficialBlue           // 主色调使用官方认证蓝色
val PrimaryVariant = Color(0xFF3F51B5)    // 深一点的官方蓝色
val PrimaryLight = Color(0xFFE8EAF6)      // 浅官方蓝色背景

// 强调色 - 也使用相同的官方认证蓝色
val AccentColor = OfficialBlue            // 强调色也使用官方认证蓝色
val AccentVariant = Color(0xFF3F51B5)     // 深一点的官方蓝色
val AccentLight = Color(0xFFE8EAF6)       // 浅官方蓝色背景

// 文本和背景
val TextPrimary = Color.Black
val TextSecondary = Color.DarkGray
val TextOnPrimary = Color.White
val BackgroundColor = Color(0xFFF5F5F5)
val SurfaceColor = Color.White

// 功能色 - 也全部改为官方认证蓝色系
val SuccessColor = OfficialBlue.copy(alpha = 0.8f)
val ErrorColor = OfficialBlue.copy(alpha = 0.9f)
val WarningColor = OfficialBlue.copy(alpha = 0.7f)