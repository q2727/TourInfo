package com.example.travalms.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travalms.ui.theme.PrimaryColor
import com.example.travalms.data.remote.XMPPManager
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.foundation.background
import org.jxmpp.jid.BareJid
import org.jxmpp.jid.impl.JidCreate

data class PersonInfo(
    val name: String,
    val isVerified: Boolean = false,
    val phone: String = "",
    val qq: String = "",
    val wechat: String = "",
    val company: String = "",
    val introduction: String = "",
    val status: String = "离线", // 添加在线状态
    val email: String = "" // 添加邮箱字段
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonDetailScreen(
    personId: String?,
    onBackClick: () -> Unit,
    onChatClick: (String) -> Unit,
    onCompanyClick: (String) -> Unit
) {
    // 状态管理
    var personInfo by remember { mutableStateOf<PersonInfo?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // 加载好友信息
    LaunchedEffect(personId) {
        if (personId.isNullOrEmpty()) {
            errorMessage = "无效的用户ID"
            isLoading = false
            return@LaunchedEffect
        }
        
        try {
            Log.d("PersonDetail", "开始加载用户信息: $personId")
            
            // 尝试解析JID
            val jid = try {
                JidCreate.bareFrom(personId)
            } catch (e: Exception) {
                Log.e("PersonDetail", "JID解析失败: ${e.message}")
                null
            }
            
            if (jid != null) {
                // 获取好友的VCard或用户资料
                val profileResult = XMPPManager.getInstance().getFriendProfile(jid)
                val presenceResult = XMPPManager.getInstance().getFriendPresence(jid)
                
                if (profileResult.isSuccess) {
                    val profileData = profileResult.getOrDefault(emptyMap())
                    Log.d("PersonDetail", "成功获取好友资料: $profileData")
                    
                    // 获取好友状态
                    val status = if (presenceResult.isSuccess) {
                        presenceResult.getOrDefault("离线")
                    } else {
                        "离线"
                    }
                    
                    personInfo = PersonInfo(
                        name = profileData["nickname"] ?: jid.toString().split("@").firstOrNull() ?: "未知用户",
                        isVerified = profileData["verified"] == "true",
                        phone = profileData["phone"] ?: "",
                        qq = profileData["qq"] ?: "",
                        wechat = profileData["wechat"] ?: "",
                        company = profileData["company"] ?: "",
                        introduction = profileData["introduction"] ?: "暂无简介",
                        status = status,
                        email = profileData["email"] ?: "" // 直接使用服务器返回的email字段
                    )
                } else {
                    // 如果获取VCard失败，使用基本信息
                    val localPart = jid.toString().split("@").firstOrNull() ?: ""
                    
                    // 获取好友状态
                    val status = if (presenceResult.isSuccess) {
                        presenceResult.getOrDefault("离线")
                    } else {
                        "离线"
                    }
                    
                    // 生成邮箱 - 使用与XMPPManager完全相同的方式
                    val email = if (localPart.all { it.isDigit() }) {
                        "$localPart@qq.com" // 对于纯数字的用户名，使用QQ邮箱格式
                    } else {
                        "$localPart@${XMPPManager.SERVER_DOMAIN}" // 否则使用默认域名
                    }
                    
                    personInfo = PersonInfo(
                        name = localPart,
                        isVerified = false,
                        introduction = "暂无简介",
                        status = status,
                        email = email // 使用与XMPPManager完全相同的方式生成的邮箱
                    )
                    
                    Log.w("PersonDetail", "获取用户资料失败，使用基本信息: ${profileResult.exceptionOrNull()?.message}")
                }
            } else {
                // 如果JID无效，使用默认测试数据
                personInfo = PersonInfo(
                    name = "用户$personId",
                    isVerified = false,
                    phone = "",
                    qq = "",
                    wechat = "",
                    company = "",
                    introduction = "暂无简介",
                    status = "离线",
                    email = if (personId.isNullOrEmpty() || !personId.all { it.isDigit() }) 
                        "${personId.orEmpty()}@${XMPPManager.SERVER_DOMAIN}" 
                        else "${personId}@qq.com" // 与XMPPManager保持一致，对纯数字用QQ邮箱
                )
                
                Log.w("PersonDetail", "无法解析JID，使用默认数据")
            }
            
        } catch (e: Exception) {
            Log.e("PersonDetail", "加载用户信息失败", e)
            errorMessage = "加载用户信息失败: ${e.message}"
            scope.launch {
                snackbarHostState.showSnackbar("加载用户信息失败，请稍后重试")
            }
        } finally {
            isLoading = false
        }
    }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("好友详情", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PrimaryColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                // 显示加载中
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = PrimaryColor
                )
            } else if (errorMessage != null) {
                // 显示错误
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = errorMessage ?: "未知错误",
                        color = MaterialTheme.colorScheme.error
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = onBackClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryColor
                        )
                    ) {
                        Text("返回")
                    }
                }
            } else if (personInfo != null) {
                // 使用Column包装内容和底部按钮
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // 显示个人信息
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // 姓名行
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "姓名",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                modifier = Modifier.width(80.dp)
                            )
                            
                            Text(
                                text = personInfo?.name ?: "",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            
                            if (personInfo?.isVerified == true) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = PrimaryColor,
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                ) {
                                    Text(
                                        text = "实名认证",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                        
                        // 在线状态
                        InfoRow(label = "状态", value = personInfo?.status ?: "离线") {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .padding(end = 4.dp)
                                    .background(
                                        color = when {
                                            (personInfo?.status ?: "").contains("在线") && !(personInfo?.status ?: "").contains("离线") -> Color.Green
                                            (personInfo?.status ?: "").contains("忙碌") -> Color(0xFFFFA500) // Orange
                                            (personInfo?.status ?: "").contains("离开") -> Color(0xFFFFFF00) // Yellow
                                            else -> Color.Gray
                                        },
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            )
                        }
                        
                        // 电话
                        if (personInfo?.phone?.isNotEmpty() == true) {
                            InfoRow(label = "电话", value = personInfo?.phone ?: "")
                        }
                        
                        // QQ
                        if (personInfo?.qq?.isNotEmpty() == true) {
                            InfoRow(label = "QQ", value = personInfo?.qq ?: "")
                        }
                        
                        // 微信
                        if (personInfo?.wechat?.isNotEmpty() == true) {
                            InfoRow(label = "微信", value = personInfo?.wechat ?: "")
                        }
                        
                        // 邮箱
                        if (personInfo?.email?.isNotEmpty() == true) {
                            InfoRow(label = "邮箱", value = personInfo?.email ?: "")
                        }
                        
                        // 所属公司
                        if (personInfo?.company?.isNotEmpty() == true) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onCompanyClick(personInfo?.company ?: "") }
                            ) {
                                Text(
                                    text = "所属公司",
                                    fontSize = 16.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.width(80.dp)
                                )
                                
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = personInfo?.company ?: "",
                                        fontSize = 16.sp,
                                        color = PrimaryColor
                                    )
                                    
                                    Spacer(modifier = Modifier.width(4.dp))
                                    
                                    Surface(
                                        color = PrimaryColor.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.ArrowBack,
                                            contentDescription = "前往",
                                            tint = PrimaryColor,
                                            modifier = Modifier
                                                .size(16.dp)
                                                .padding(2.dp)
                                                .rotate(180f)
                                        )
                                    }
                                }
                            }
                        }
                        
                        // 简介
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "简介",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            
                            Text(
                                text = personInfo?.introduction ?: "",
                                fontSize = 16.sp
                            )
                        }
                    }
                    
                    // 底部聊天按钮
                    Button(
                        onClick = { onChatClick(personId ?: "") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryColor
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Email,
                                contentDescription = "聊天",
                                modifier = Modifier.size(20.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "发起聊天",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    prefix: @Composable (() -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.width(80.dp)
        )
        
        if (prefix != null) {
            prefix()
        }
        
        Text(
            text = value,
            fontSize = 16.sp
        )
    }
} 