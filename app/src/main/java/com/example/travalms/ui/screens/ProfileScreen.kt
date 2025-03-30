package com.example.travalms.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
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

import androidx.navigation.NavController
import com.example.travalms.ui.navigation.AppRoutes

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
    navController: NavController
) {
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
                    icon = { Icon(Icons.Filled.Home, contentDescription = "首页") },
                    label = { Text("首页", fontSize = 12.sp) },
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
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    // 用户信息
                    Column {
                        Text(
                            text = "用户昵称",
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
            
            // 功能列表 - 把"我的收藏"放在靠前位置
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
                    // 移动上来的"我的收藏"选项
                    ListItem(
                        headlineContent = { Text("我的收藏") },
                        leadingContent = { 
                            Icon(
                                Icons.Filled.Star, 
                                contentDescription = "我的收藏",
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
                            navController.navigate(AppRoutes.MY_FAVORITES)
                        }
                    )
                    
                    Divider()
                    
                    // 我的发布
                    ListItem(
                        headlineContent = { Text("我的发布") },
                        leadingContent = { 
                            Icon(
                                Icons.Filled.List, 
                                contentDescription = "我的发布",
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
                            navController.navigate(AppRoutes.MY_POSTS)
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