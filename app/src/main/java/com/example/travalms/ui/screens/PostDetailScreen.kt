package com.example.travalms.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travalms.data.repository.FavoriteManager
import com.example.travalms.ui.theme.PrimaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    postId: String,
    onBackClick: () -> Unit,
    onChatClick: () -> Unit,
    onCompanyClick: (String) -> Unit,
    onPersonClick: (String) -> Unit
) {
    var isFavorite by remember { mutableStateOf(FavoriteManager.isFavorite(postId)) }
    
    val postTitle = "桂林山水5日游"
    val agency = "广西旅游集团"
    val price = "¥2580"

    val showMorePrices = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("信息详情", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        FavoriteManager.toggleFavorite(postId, postTitle, agency, price)
                        isFavorite = !isFavorite
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = if (isFavorite) "取消收藏" else "收藏",
                            tint = if (isFavorite) Color(0xFFFFC107) else Color.White.copy(alpha = 0.7f)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PrimaryColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            // 底部咨询按钮
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
                        text = "聊天咨询",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // 路线名称
            InfoItem(
                label = "路线名称",
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "川西五日游",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "认证",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )
            
            // 同业价格
            InfoItem(
                label = "同业价格",
                content = {
                    Column {
                        // 成人价格
                        Text(
                            text = "成人: 1330 元/人",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        // 儿童价格
                        Text(
                            text = "儿童: 980 元/人",
                            fontSize = 16.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        
                        // 老人价格
                        Text(
                            text = "老人: 1180 元/人",
                            fontSize = 16.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            )
            
            // 剩余名额
            InfoItem(
                label = "剩余名额",
                content = {
                    Text(
                        text = "3",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
            
            // 起止地点
            InfoItem(
                label = "起止地点",
                content = {
                    Text(
                        text = "广东-广州",
                        fontSize = 16.sp
                    )
                }
            )
            
            // 团型
            InfoItem(
                label = "团型",
                content = {
                    Text(
                        text = "纯玩团",
                        fontSize = 16.sp
                    )
                }
            )
            
            // 旅行时间
            InfoItem(
                label = "旅行时间",
                content = {
                    Text(
                        text = "2025-03-11   -   2025-03-21",
                        fontSize = 16.sp
                    )
                }
            )
            
            // 行程描述
            InfoItem(
                label = "行程描述",
                content = {
                    Column {
                        Text(
                            text = "发团日期:7月25日,8月7,9,11,13,15,17,19,21,23,25日",
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "行程特色：1.在上海外国语大学浸入式英语环境中学习英语，培养孩子良好的英语语感及口语运用能力。",
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                        Text(
                            text = "2.上海滩景点畅游、博物馆、知名大学参访，展开真正的上海文化寻根游学之旅。",
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                        Text(
                            text = "3.上海迪斯尼乐园畅游，学习游乐两不误。",
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                        Text(
                            text = "4.在项目和活动中锻炼团队合作能力，通过多样的活动，不断发现自我，增添自信。",
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "认证",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )
            
            // 详情链接
            InfoItem(
                label = "详情链接",
                content = {
                    Text(
                        text = "www.tour-detail.com",
                        fontSize = 16.sp,
                        color = Color(0xFF2196F3)
                    )
                }
            )
            
            // 发布者信息
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "发布者信息",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clickable { onCompanyClick("上海旅行社") }
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "公司",
                            tint = PrimaryColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "上海旅行社")
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = PrimaryColor.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Text(
                                text = "官方认证",
                                color = PrimaryColor,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Call,
                            contentDescription = "电话",
                            tint = PrimaryColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "13904814140",
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Call,
                            contentDescription = "电话",
                            tint = PrimaryColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "17722223333",
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "客服电话",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.End
                        )
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "ID",
                            tint = PrimaryColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "123456",
                            fontSize = 14.sp
                        )
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { onPersonClick("曾圆圆") }
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "联系人",
                            tint = PrimaryColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "曾圆圆",
                            fontSize = 14.sp
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun InfoItem(
    label: String,
    content: @Composable () -> Unit,
    backgroundColor: Color = Color.White
) {
    Surface(
        color = backgroundColor,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.width(80.dp)
            )
            
            Box(
                modifier = Modifier.weight(1f)
            ) {
                content()
            }
        }
    }
    
    Divider(color = Color.LightGray.copy(alpha = 0.5f))
} 