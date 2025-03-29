package com.example.travalms.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travalms.xmpp.XMPPManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import org.jxmpp.jid.parts.Localpart
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.stringprep.XmppStringprepException
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jivesoftware.smack.ConnectionConfiguration

/**
 * 注册屏幕组件
 * 提供用户注册功能
 */
@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    // 获取当前上下文，用于显示Toast
    val context = LocalContext.current
    
    // 创建协程作用域，用于执行异步操作
    val scope = rememberCoroutineScope()
    
    // 创建XMPP管理器实例
    val xmppManager = remember { XMPPManager() }
    
    // 用户输入状态
    var username by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    // 顶层布局，提供背景色和填充
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 顶部区域：标题栏
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 返回按钮
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "返回"
                    )
                }
                
                // 标题文本
                Text(
                    text = "注册",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                // 空白占位符（平衡布局）
                Spacer(modifier = Modifier.width(48.dp))
            }
            
            // 注册表单容器
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                ) {
                    // 用户名输入框
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("用户名") },
                        leadingIcon = { 
                            Icon(Icons.Default.Person, contentDescription = "用户名图标") 
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 昵称输入框
                    OutlinedTextField(
                        value = nickname,
                        onValueChange = { nickname = it },
                        label = { Text("昵称") },
                        leadingIcon = { 
                            Icon(Icons.Default.Person, contentDescription = "昵称图标") 
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 邮箱输入框
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("电子邮箱") },
                        leadingIcon = { 
                            Icon(Icons.Default.Email, contentDescription = "邮箱图标") 
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 密码输入框
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("设置密码 (8-20位)") },
                        leadingIcon = { 
                            Icon(Icons.Default.Lock, contentDescription = "密码图标") 
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 确认密码输入框
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("确认密码") },
                        leadingIcon = { 
                            Icon(Icons.Default.Lock, contentDescription = "确认密码图标") 
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }
            
            // 注册按钮
            Button(
                onClick = {
                    // 检查密码是否匹配
                    if (password != confirmPassword) {
                        Toast.makeText(context, "两次输入的密码不一致", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    // 检查密码长度
                    if (password.length < 8 || password.length > 20) {
                        Toast.makeText(context, "密码长度应为8-20位", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    // 检查其他字段是否填写
                    if (username.isBlank() || nickname.isBlank()) {
                        Toast.makeText(context, "用户名和昵称为必填项", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    // 执行注册操作
                    scope.launch {
                        // 显示加载中提示
                        Toast.makeText(context, "正在注册，请稍候...", Toast.LENGTH_SHORT).show()
                        
                        // 调用修复后的注册方法
                        val result = xmppManager.register(username, password, nickname, email.takeIf { it.isNotBlank() })
                        result.fold(
                            onSuccess = {
                                Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show()
                                onLoginClick() // 注册成功后返回登录页面
                            },
                            onFailure = { e ->
                                Toast.makeText(context, "注册失败: ${e.message}", Toast.LENGTH_SHORT).show()
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
                    text = "立即注册",
                    fontSize = 16.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 已有账号提示
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("已有账号? ")
                TextButton(onClick = onLoginClick) {
                    Text("去登录")
                }
            }
        }
    }
} 