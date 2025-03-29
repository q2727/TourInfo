package com.example.travalms.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travalms.xmpp.XMPPManager
import kotlinx.coroutines.launch

/**
 * 登录界面组件
 * 提供用户登录和注册功能
 */
@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit
) {
    // 获取当前上下文，用于显示Toast
    val context = LocalContext.current
    
    // 创建协程作用域，用于执行异步操作
    val scope = rememberCoroutineScope()
    
    // 创建XMPP管理器实例
    val xmppManager = remember { XMPPManager() }
    
    // 用户名和密码状态
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 主布局
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 标题
        Text(
            text = "登录",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 用户名输入框
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("输入用户名") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 密码输入框
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("输入密码") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 登录按钮
        Button(
            onClick = {
                // 执行登录操作
                scope.launch {
                    // 调用XMPP管理器登录方法
                    val result = xmppManager.login(username, password)
                    
                    // 处理登录结果
                    result.fold(
                        onSuccess = {
                            Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show()
                        },
                        onFailure = { e ->
                            Toast.makeText(context, "登录失败: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "登录",
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 注册和忘记密码按钮行
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 注册按钮
            TextButton(
                onClick = onRegisterClick
            ) {
                Text("立即注册")
            }
            
            // 忘记密码按钮
            TextButton(
                onClick = { /* 忘记密码逻辑，待实现 */ }
            ) {
                Text("忘记密码?")
            }
        }
    }
} 