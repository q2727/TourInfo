package com.example.travalms.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travalms.ui.theme.PrimaryColor

data class CompanyInfo(
    val name: String,
    val isVerified: Boolean = false,
    val contactPerson: String = "",
    val phone: String = "",
    val landline: String = "",
    val qq: String = "",
    val wechat: String = "",
    val businessType: String = "",
    val introduction: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyDetailScreen(
    companyId: String?,
    onBackClick: () -> Unit,
    onChatClick: () -> Unit
) {
    // 在实际应用中，应根据companyId从数据源获取公司信息
    // 这里使用硬编码的示例数据进行演示
    val company = CompanyInfo(
        name = "彩云旅游团",
        isVerified = true,
        contactPerson = "曾圆圆",
        phone = "13904814140",
        landline = "400-2345-5555",
        qq = "17722223333",
        wechat = "11122233",
        businessType = "同业社",
        introduction = "为旅客提供个性化旅游服务。欢迎合作"
    )
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("企业详情", fontWeight = FontWeight.Bold) },
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
                // 企业名称行
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "企业名称",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.width(80.dp)
                    )
                    
                    Text(
                        text = company.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    if (company.isVerified) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = PrimaryColor,
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Text(
                                text = "官方认证",
                                color = Color.White,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
                
                // 联系人
                InfoRow(label = "联系人", value = company.contactPerson)
                
                // 电话
                InfoRow(label = "电话", value = company.phone)
                
                // 座机
                InfoRow(label = "座机", value = company.landline)
                
                // QQ
                InfoRow(label = "QQ", value = company.qq)
                
                // 微信
                InfoRow(label = "微信", value = company.wechat) 
                
                // 业务
                InfoRow(label = "业务", value = company.businessType)
                
                // 简介
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "简介",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = company.introduction,
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