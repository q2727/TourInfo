package com.example.travalms.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.travalms.data.api.NetworkModule
import com.example.travalms.ui.theme.PrimaryColor
import com.example.travalms.ui.theme.BackgroundColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    onBackClick: () -> Unit
) {
    // 状态变量
    var username by remember { mutableStateOf<String?>("加载中...") }
    var nickname by remember { mutableStateOf<String?>("") }
    var email by remember { mutableStateOf<String?>("") }
    var companyName by remember { mutableStateOf<String?>("") }
    var phoneNumber by remember { mutableStateOf<String?>("") }
    var province by remember { mutableStateOf<String?>("") }
    var city by remember { mutableStateOf<String?>("") }
    
    // 照片URL
    var avatarUrl by remember { mutableStateOf<String?>(null) }
    var businessLicenseUrl by remember { mutableStateOf<String?>(null) }
    var idCardFrontUrl by remember { mutableStateOf<String?>(null) }
    var idCardBackUrl by remember { mutableStateOf<String?>(null) }
    
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
                        nickname = userData["nickname"]?.toString()
                        email = userData["email"]?.toString()
                        companyName = userData["companyName"]?.toString()
                        phoneNumber = userData["phoneNumber"]?.toString()
                        province = userData["province"]?.toString()
                        city = userData["city"]?.toString()
                        
                        // 处理照片URL，将localhost替换为实际IP
                        avatarUrl = processImageUrl(userData["avatarUrl"]?.toString())
                        businessLicenseUrl = processImageUrl(userData["businessLicenseUrl"]?.toString())
                        idCardFrontUrl = processImageUrl(userData["idCardFrontUrl"]?.toString())
                        idCardBackUrl = processImageUrl(userData["idCardBackUrl"]?.toString())
                        
                        Log.d("EditProfileScreen", "获取到用户信息: $userData")
                    } else {
                        Log.e("EditProfileScreen", "获取用户信息失败: ${response.code()}")
                        username = "加载失败"
                    }
                } catch (e: Exception) {
                    Log.e("EditProfileScreen", "获取用户信息异常", e)
                    username = "加载失败"
                }
            } else {
                Log.e("EditProfileScreen", "未获取到当前用户名")
                username = "未登录"
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("编辑个人资料", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundColor)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 头像部分
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f))
                    .border(2.dp, PrimaryColor, CircleShape)
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
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "默认头像",
                        tint = PrimaryColor,
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.Center)
                    )
                }
                
                // 编辑图标
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(PrimaryColor)
                        .clickable { /* 实现选择头像的功能 */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "编辑头像",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 用户基本信息卡片
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "基本信息",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = PrimaryColor
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    InfoItem(title = "用户名", value = username ?: "")
                    InfoItem(title = "昵称", value = nickname ?: "")
                    InfoItem(title = "邮箱", value = email ?: "")
                    InfoItem(title = "公司名称", value = companyName ?: "")
                    InfoItem(title = "电话号码", value = phoneNumber ?: "")
                    InfoItem(title = "省份", value = province ?: "")
                    InfoItem(title = "城市", value = city ?: "")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 资质认证照片卡片
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "资质认证照片",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = PrimaryColor
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 营业执照照片
                    Text(
                        text = "营业执照",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    ImageCard(
                        imageUrl = businessLicenseUrl,
                        contentDescription = "营业执照",
                        context = context,
                        onClick = { /* 实现选择营业执照照片的功能 */ }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 身份证正面照片
                    Text(
                        text = "身份证正面",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    ImageCard(
                        imageUrl = idCardFrontUrl,
                        contentDescription = "身份证正面",
                        context = context,
                        onClick = { /* 实现选择身份证正面照片的功能 */ }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 身份证背面照片
                    Text(
                        text = "身份证背面",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    ImageCard(
                        imageUrl = idCardBackUrl,
                        contentDescription = "身份证背面",
                        context = context,
                        onClick = { /* 实现选择身份证背面照片的功能 */ }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 保存按钮
            Button(
                onClick = { /* 实现保存功能 */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
            ) {
                Text("保存修改", fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun InfoItem(title: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = value.ifEmpty { "未设置" },
            fontSize = 16.sp,
            color = if (value.isEmpty()) Color.LightGray else Color.Black
        )
    }
    
    Divider(modifier = Modifier.padding(vertical = 8.dp))
}

@Composable
fun ImageCard(
    imageUrl: String?,
    contentDescription: String,
    context: Context,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray.copy(alpha = 0.3f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = contentDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "添加照片",
                    tint = PrimaryColor,
                    modifier = Modifier.size(40.dp)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "点击上传$contentDescription",
                    color = PrimaryColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// 处理图片URL，将localhost替换为实际IP
private fun processImageUrl(url: String?): String? {
    if (url == null || url.isEmpty()) return null
    return if (url.contains("localhost")) {
        url.replace("localhost", "192.168.100.6")
    } else {
        url
    }
}

// 从SharedPreferences获取当前登录的用户名
private fun getCurrentUsername(context: Context): String {
    val prefs = context.getSharedPreferences("xmpp_prefs", Context.MODE_PRIVATE)
    return prefs.getString("username", "") ?: ""
} 