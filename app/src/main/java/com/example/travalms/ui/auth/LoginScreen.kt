package com.example.travalms.ui.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travalms.ui.theme.PrimaryColor
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState

/**
 * 登录界面组件
 * 提供用户登录和注册功能
 */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit = {},
    // 注入ViewModel
    loginViewModel: LoginViewModel = viewModel() 
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    // val scope = rememberCoroutineScope() // No longer needed here
    val context = LocalContext.current
    // val xmppManager = remember { XMPPManager() } // No longer needed here

    // 从ViewModel收集UI状态
    val uiState by loginViewModel.uiState.collectAsState()

    // 使用LaunchedEffect处理一次性事件（如Toast显示和导航）
    LaunchedEffect(key1 = uiState) {
        when (val state = uiState) {
            is LoginUiState.Success -> {
                Toast.makeText(context, "登录成功!", Toast.LENGTH_SHORT).show()
                onLoginSuccess() // 导航
                // loginViewModel.resetState() // Optional: Reset state after navigation
            }
            is LoginUiState.Error -> {
                Toast.makeText(context, "登录失败: ${state.message}", Toast.LENGTH_LONG).show()
                loginViewModel.resetState() // Reset state after showing error
            }
            else -> {
                // Idle or Loading state, no immediate action needed here
            }
        }
    }

    // 简化的渐变背景
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        PrimaryColor.copy(alpha = 0.05f),
                        PrimaryColor.copy(alpha = 0.1f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            
            // 简化的Logo设计 - 使用文字代替图标
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(PrimaryColor.copy(alpha = 0.3f))
                    .border(2.dp, PrimaryColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "TIP",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 系统名称
            Text(
                text = "TravalIP",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryColor
            )
            
            Text(
                text = "旅游信息交换平台",
                fontSize = 16.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(50.dp))
            
            // 用户名输入框 - 使用Person图标替代
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("输入用户名") },
                leadingIcon = { 
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "用户",
                        tint = PrimaryColor
                    ) 
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryColor,
                    unfocusedBorderColor = Color.LightGray
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 密码输入框 - 始终隐藏密码，不使用切换图标
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("输入密码") },
                leadingIcon = { 
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = "密码",
                        tint = PrimaryColor
                    ) 
                },
                // 不使用trailing icon切换密码可见性
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryColor,
                    unfocusedBorderColor = Color.LightGray
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 登录按钮
            Button(
                onClick = {
                    // 调用ViewModel中的登录方法
                    loginViewModel.performLogin(username, password)
                },
                // 根据加载状态禁用按钮
                enabled = uiState != LoginUiState.Loading, 
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                // 根据加载状态显示文本或加载指示器
                if (uiState == LoginUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "登录",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 底部链接
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onRegisterClick) {
                    Text(
                        text = "立即注册",
                        color = PrimaryColor,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                TextButton(onClick = onForgotPasswordClick) {
                    Text(
                        text = "忘记密码?",
                        color = PrimaryColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // 版权信息
            Text(
                text = "© 2023 TravalIP 旅游信息交换平台",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }
    }
} 