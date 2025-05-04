package com.example.travalms.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travalms.ui.theme.PrimaryColor
import com.example.travalms.ui.theme.BackgroundColor
import com.example.travalms.ui.theme.WarningColor
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import com.example.travalms.data.remote.XMPPManager
import androidx.navigation.NavController
import com.example.travalms.ui.navigation.AppRoutes
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import com.example.travalms.R
import android.content.Context
import android.util.Log
import androidx.compose.ui.layout.ContentScale
import com.example.travalms.data.api.NetworkModule
import com.example.travalms.data.api.UserApiService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit,
    onProfileEditClick: () -> Unit,
    onVerificationClick: () -> Unit,
    onBusinessCertClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPublishClick: () -> Unit,
    onMessageClick: () -> Unit,
    onTailListClick: () -> Unit,
    navController: NavController
) {
    var nickname by remember { mutableStateOf<String?>("加载中...") }
    var username by remember { mutableStateOf<String?>("用户") }
    var avatarUrl by remember { mutableStateOf<String?>(null) }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userApiService = NetworkModule.provideUserApiService()

    LaunchedEffect(Unit) {
        scope.launch {
            // 从SharedPreferences获取当前登录的用户名
            val currentUsername = getCurrentUsername(context)
            if (currentUsername.isNotEmpty()) {
                try {
                    // 使用UserApiService获取用户信息
                    val response = userApiService.getUserInfo(currentUsername)
                    if (response.isSuccessful && response.body() != null) {
                        val userData = response.body()!!
                        username = currentUsername
                        nickname = userData["nickname"]?.toString() ?: currentUsername
                        // 替换头像URL中的localhost为实际IP地址
                        var avatarUrlStr = userData["avatarUrl"]?.toString()
                        if (avatarUrlStr != null && avatarUrlStr.contains("localhost")) {
                            avatarUrlStr = avatarUrlStr.replace("localhost", "192.168.100.6")
                            Log.d("ProfileScreen", "修正后的头像URL: $avatarUrlStr")
                        }
                        avatarUrl = avatarUrlStr
                        Log.d("ProfileScreen", "获取到用户头像: $avatarUrl")
                    } else {
                        Log.e("ProfileScreen", "获取用户信息失败: ${response.code()}")
                        nickname = "加载失败"
                    }
                } catch (e: Exception) {
                    Log.e("ProfileScreen", "获取用户信息异常", e)
                    nickname = "加载失败"
                }
            } else {
                Log.e("ProfileScreen", "未获取到当前用户名")
                nickname = "未登录"
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("个人中心", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PrimaryColor,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomNavigation(
                backgroundColor = Color.White,
                modifier = Modifier.height(56.dp)
            ) {
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "产品") },
                    label = { Text("产品", fontSize = 12.sp) },
                    selected = false,
                    onClick = onHomeClick,
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = Color.Gray
                )
                
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Add, contentDescription = "发布") },
                    label = { Text("发布", fontSize = 12.sp) },
                    selected = false,
                    onClick = onPublishClick,
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = Color.Gray
                )
                
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = "尾单") },
                    label = { Text("尾单", fontSize = 12.sp) },
                    selected = false,
                    onClick = onTailListClick,
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = Color.Gray
                )
                
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Email, contentDescription = "消息") },
                    label = { Text("消息", fontSize = 12.sp) },
                    selected = false,
                    onClick = onMessageClick,
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = Color.Gray
                )
                
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "我的") },
                    label = { Text("我的", fontSize = 12.sp) },
                    selected = true,
                    onClick = { /* 已在个人页面 */ },
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = Color.Gray
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundColor)
                .verticalScroll(rememberScrollState())
        ) {
            // 用户资料卡片
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryColor)
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // 头像
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.3f))
                            .clickable(onClick = onProfileEditClick)
                    ) {
                        if (avatarUrl != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(avatarUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "用户头像",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            // 默认头像
                            Icon(
                                Icons.Filled.Person,
                                contentDescription = "默认头像",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    // 用户信息
                    Column {
                        Text(
                            text = nickname ?: username ?: "用户",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "完善资料 >",
                            color = Color.White,
                            fontSize = 14.sp,
                            modifier = Modifier.clickable(onClick = onProfileEditClick)
                        )
                    }
                }
            }
            
            // 功能列表
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    // 订阅管理
                    ListItem(
                        headlineContent = { Text("订阅管理") },
                        leadingContent = { 
                            Icon(
                                imageVector = Icons.Filled.List, 
                                contentDescription = "订阅管理",
                                tint = Color(0xFF00C2A8)
                            )
                        },
                        trailingContent = { 
                            Icon(
                                Icons.Filled.KeyboardArrowRight, 
                                contentDescription = "进入"
                            )
                        },
                        modifier = Modifier.clickable {
                            // 导航到订阅节点界面
                            navController.navigate(AppRoutes.SUBSCRIBE_SETTING)
                        }
                    )
                    
                    Divider()
                    
                    // 资质管理
                    ListItem(
                        headlineContent = { Text("资质管理") },
                        leadingContent = { 
                            Icon(
                                imageVector = Icons.Filled.Person, 
                                contentDescription = "资质管理",
                                tint = Color(0xFF4169E1)
                            )
                        },
                        trailingContent = { 
                            Icon(
                                Icons.Filled.KeyboardArrowRight, 
                                contentDescription = "进入"
                            )
                        },
                        modifier = Modifier.clickable {
                            // 导航到资质管理页面
                        }
                    )
                    
                    Divider()
                    
                    // 可发布节点查询
                    ListItem(
                        headlineContent = { Text("可发布节点查询") },
                        leadingContent = { 
                            Icon(
                                imageVector = Icons.Filled.Person, 
                                contentDescription = "可发布节点查询",
                                tint = Color(0xFF4169E1)
                            )
                        },
                        trailingContent = { 
                            Icon(
                                Icons.Filled.KeyboardArrowRight, 
                                contentDescription = "进入"
                            )
                        },
                        modifier = Modifier.clickable {
                            // 导航到可发布节点查询页面
                        }
                    )
                    
                    Divider()
                    
                    // 发布管理
                    ListItem(
                        headlineContent = { Text("发布管理") },
                        leadingContent = { 
                            Icon(
                                imageVector = Icons.Filled.Person, 
                                contentDescription = "发布管理",
                                tint = Color(0xFF4169E1)
                            )
                        },
                        trailingContent = { 
                            Icon(
                                Icons.Filled.KeyboardArrowRight, 
                                contentDescription = "进入"
                            )
                        },
                        modifier = Modifier.clickable {
                            // 导航到发布节点选择界面
                            navController.navigate(AppRoutes.PUBLISH_NODE_SELECTOR)
                        }
                    )
                    
                    Divider()
                    
                    // 设置
                    ListItem(
                        headlineContent = { Text("设置") },
                        leadingContent = { 
                            Icon(
                                Icons.Filled.Settings, 
                                contentDescription = "设置",
                                tint = PrimaryColor
                            )
                        },
                        trailingContent = { 
                            Icon(
                                Icons.Filled.KeyboardArrowRight, 
                                contentDescription = "进入"
                            )
                        },
                        modifier = Modifier.clickable {
                            // 导航到设置页面
                        }
                    )
                }
            }
            
            // 认证项
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    // 实名认证 - 使用ListItem与企业认证保持一致
                    ListItem(
                        headlineContent = { Text("实名认证") },
                        leadingContent = { 
                            Icon(
                                Icons.Filled.Person, 
                                contentDescription = "实名认证",
                                tint = PrimaryColor
                            )
                        },
                        trailingContent = { 
                            Icon(
                                Icons.Filled.KeyboardArrowRight, 
                                contentDescription = "进入"
                            )
                        },
                        modifier = Modifier.clickable {
                            onVerificationClick()
                        }
                    )
                    
                    Divider()
                    
                    // 企业认证 - 使用更合适的图标
                    ListItem(
                        headlineContent = { Text("企业认证") },
                        leadingContent = { 
                            Icon(
                                Icons.Filled.Home, 
                                contentDescription = "企业认证",
                                tint = PrimaryColor
                            )
                        },
                        trailingContent = { 
                            Icon(
                                Icons.Filled.KeyboardArrowRight, 
                                contentDescription = "进入"
                            )
                        },
                        modifier = Modifier.clickable {
                            onBusinessCertClick()
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // 退出登录按钮
            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WarningColor.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = "退出登录",
                    fontSize = 16.sp,
                    color = Color(0xFF424242)
                )
            }
        }
    }
}

/**
 * 从SharedPreferences获取当前登录的用户名
 * @param context 上下文
 * @return 当前登录的用户名，如果未登录则返回空字符串
 */
private fun getCurrentUsername(context: Context): String {
    val prefs = context.getSharedPreferences("xmpp_prefs", Context.MODE_PRIVATE)
    return prefs.getString("username", "") ?: ""
} 