package com.example.travalms.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.travalms.data.api.NetworkModule
import kotlinx.coroutines.launch

/**
 * 用户头像组件 - 通过用户名获取并显示用户真实头像
 */
@Composable
fun UserAvatar(
    username: String,
    size: Dp = 48.dp,
    backgroundColor: Color = Color(0xFF4169E1),
    showInitialsWhenLoading: Boolean = true
) {
    var avatarUrl by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userApiService = NetworkModule.provideUserApiService()

    // 当组件首次显示或username变化时获取头像
    LaunchedEffect(username) {
        if (username.isNotEmpty()) {
            scope.launch {
                try {
                    val response = userApiService.getUserInfo(username)
                    if (response.isSuccessful && response.body() != null) {
                        val userData = response.body()!!
                        var url = userData["avatarUrl"]?.toString()

                        // 处理头像URL，将localhost替换为实际IP
                        if (url != null && url.isNotEmpty()) {
                            if (url.contains("localhost")) {
                                url = url.replace("localhost", "192.168.100.6")
                            }
                            avatarUrl = url
                            Log.d("UserAvatar", "获取到用户头像: $avatarUrl")
                        }
                    } else {
                        Log.e("UserAvatar", "获取用户信息失败: ${response.code()}")
                    }
                } catch (e: Exception) {
                    Log.e("UserAvatar", "获取用户信息异常: ${e.message}", e)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (avatarUrl != null) {
            // 显示真实头像
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(avatarUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Avatar of $username",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(size)
            )
        } else {
            // 显示默认头像（用户名首字母或默认图标）
            if (showInitialsWhenLoading && username.isNotEmpty()) {
                Text(
                    text = username.first().toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = "默认头像",
                    tint = Color.White,
                    modifier = Modifier.size(size * 0.6f)
                )
            }
        }
    }
} 