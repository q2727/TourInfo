package com.example.travalms.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travalms.ui.theme.PrimaryColor

data class PersonInfo(
    val name: String,
    val isVerified: Boolean = false,
    val phone: String = "",
    val qq: String = "",
    val wechat: String = "",
    val company: String = "",
    val introduction: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonDetailScreen(
    personId: String?,
    onBackClick: () -> Unit,
    onChatClick: () -> Unit,
    onCompanyClick: (String) -> Unit
) {
    // 在实际应用中，应根据personId从数据源获取个人信息
    // 这里使用硬编码的示例数据进行演示
    val person = PersonInfo(
        name = "曾圆圆",
        isVerified = true,
        phone = "13904814140",
        qq = "17722223333",
        wechat = "11122233",
        company = "彩云旅游社",
        introduction = "为旅客提供个性化旅游服务。欢迎合作"
    )
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("个人详情", fontWeight = FontWeight.Bold) },
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 主要内容区域
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
                        text = person.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    if (person.isVerified) {
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
                
                // 电话
                InfoRow(label = "电话", value = person.phone)
                
                // QQ
                InfoRow(label = "QQ", value = person.qq)
                
                // 微信
                InfoRow(label = "微信", value = person.wechat)
                
                // 所属公司
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCompanyClick(person.company) }
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
                            text = person.company,
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
                
                // 简介
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "简介",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = person.introduction,
                        fontSize = 16.sp
                    )
                }
            }
            
            // 底部聊天按钮
            Button(
                onClick = onChatClick,
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

@Composable
private fun InfoRow(label: String, value: String) {
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
        
        Text(
            text = value,
            fontSize = 16.sp
        )
    }
} 