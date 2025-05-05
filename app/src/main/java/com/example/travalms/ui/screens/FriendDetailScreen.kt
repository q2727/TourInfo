package com.example.travalms.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travalms.ui.components.LoadingIndicator
import com.example.travalms.ui.components.UserAvatar
import com.example.travalms.ui.viewmodels.FriendDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendDetailScreen(
    username: String,
    onBack: () -> Unit,
    onSendMessage: (String, String) -> Unit // 参数：(username, nickname)
) {
    val context = LocalContext.current
    val viewModel: FriendDetailViewModel = viewModel(
        factory = FriendDetailViewModel.Factory(username, context.applicationContext as android.app.Application)
    )
    val state by viewModel.state.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("用户详情") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4285F4),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    LoadingIndicator(modifier = Modifier.fillMaxSize())
                }
                state.error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = state.error ?: "未知错误",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onBack) {
                            Text("返回")
                        }
                    }
                }
                state.userInfo != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // 用户基本信息卡片
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 2.dp
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // 头像
                                UserAvatar(
                                    username = state.userInfo?.get("username")?.toString() ?: "",
                                    size = 80.dp
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // 昵称
                                Text(
                                    text = state.userInfo?.get("nickname")?.toString() 
                                        ?: state.userInfo?.get("username")?.toString() 
                                        ?: "未知用户",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        
                        // 详细信息卡片
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                // 公司信息
                                if ((state.userInfo?.get("companyName")?.toString() ?: "").isNotEmpty()) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Home,
                                            contentDescription = "公司",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = state.userInfo?.get("companyName")?.toString() ?: "",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                    if ((state.userInfo?.get("phoneNumber")?.toString() ?: "").isNotEmpty() ||
                                        (state.userInfo?.get("email")?.toString() ?: "").isNotEmpty() ||
                                        (state.userInfo?.get("province")?.toString() ?: "").isNotEmpty()
                                    ) {
                                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                                    }
                                }

                                // 电话
                                if ((state.userInfo?.get("phoneNumber")?.toString() ?: "").isNotEmpty()) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Phone,
                                            contentDescription = "电话",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = state.userInfo?.get("phoneNumber")?.toString() ?: "",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                    if ((state.userInfo?.get("email")?.toString() ?: "").isNotEmpty() ||
                                        (state.userInfo?.get("province")?.toString() ?: "").isNotEmpty()
                                    ) {
                                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                                    }
                                }

                                // 居住地
                                val province = state.userInfo?.get("province")?.toString() ?: ""
                                val city = state.userInfo?.get("city")?.toString() ?: ""
                                if (province.isNotEmpty() || city.isNotEmpty()) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = "居住地",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = buildString {
                                                if (province.isNotEmpty()) {
                                                    append(province)
                                                }
                                                if (city.isNotEmpty()) {
                                                    if (province.isNotEmpty()) {
                                                        append(" ")
                                                    }
                                                    append(city)
                                                }
                                            },
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                    if ((state.userInfo?.get("email")?.toString() ?: "").isNotEmpty()) {
                                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                                    }
                                }

                                // 邮箱
                                if ((state.userInfo?.get("email")?.toString() ?: "").isNotEmpty()) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Email,
                                            contentDescription = "邮箱",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = state.userInfo?.get("email")?.toString() ?: "",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    // 底部发送消息按钮
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Button(
                            onClick = {
                                val username = state.userInfo?.get("username")?.toString() ?: ""
                                val nickname = state.userInfo?.get("nickname")?.toString() 
                                    ?: state.userInfo?.get("username")?.toString()
                                    ?: "未知用户"
                                onSendMessage(username, nickname)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4285F4)
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "发送消息",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("发送消息")
                        }
                    }
                }
            }
        }
    }
}