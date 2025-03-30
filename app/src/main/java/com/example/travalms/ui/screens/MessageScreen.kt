package com.example.travalms.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travalms.ui.theme.PrimaryColor
import com.example.travalms.ui.theme.TextOnPrimary
import java.time.format.DateTimeFormatter

/**
 * 消息界面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(
    onBackClick: () -> Unit,
    onSubscribeSettingClick: () -> Unit,
    onMessageClick: (Int) -> Unit,
    onHomeClick: () -> Unit,
    onPublishClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("消息中心", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = onSubscribeSettingClick) {
                        Icon(Icons.Filled.Settings, contentDescription = "订阅设置")
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
                    selected = true,
                    onClick = { /* 已在消息页面 */ },
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = Color.Gray
                )
                
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "我的") },
                    label = { Text("我的", fontSize = 12.sp) },
                    selected = false,
                    onClick = onProfileClick,
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = Color.Gray
                )
            }
        }
    ) { paddingValues ->
        // 创建一些模拟消息数据
        val messages = remember {
            listOf(
                Message(
                    id = 1,
                    title = "新订单通知",
                    content = "您的产品「三亚五日游」有新的咨询",
                    time = "10分钟前",
                    isRead = false,
                    relatedPostId = 101 // 关联的帖子ID
                ),
                Message(
                    id = 2,
                    title = "产品更新",
                    content = "「云南丽江七日游」价格已更新",
                    time = "30分钟前",
                    isRead = true,
                    relatedPostId = 102
                ),
                Message(
                    id = 3,
                    title = "系统通知",
                    content = "您的账号已完成实名认证",
                    time = "1小时前",
                    isRead = true,
                    relatedPostId = 103
                )
            )
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 消息列表
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(messages) { message ->
                    // 每条消息项，点击跳转到关联帖子详情
                    MessageItem(
                        message = message,
                        onClick = { onMessageClick(message.relatedPostId) }
                    )
                    
                    Divider(color = Color(0xFFEEEEEE))
                }
            }
        }
    }
}

/**
 * 消息项组件
 */
@Composable
fun MessageItem(
    message: Message,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 未读标记
        if (!message.isRead) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(PrimaryColor, shape = CircleShape)
                    .padding(end = 8.dp)
            )
        } else {
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        // 消息内容
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(
                text = message.title,
                fontWeight = if (!message.isRead) FontWeight.Bold else FontWeight.Normal,
                fontSize = 16.sp
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = message.content,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
        
        // 时间显示
        Text(
            text = message.time,
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

/**
 * 消息数据模型
 */
data class Message(
    val id: Int,
    val title: String,
    val content: String,
    val time: String,
    val isRead: Boolean,
    val relatedPostId: Int // 增加关联帖子ID字段
)

@Composable
fun MessageItemCard(
    item: MessageItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isHighlighted) Color(0xFFFFFAE6) else Color(0xFFE0F7E0)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 标题和收藏图标
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "收藏",
                    tint = if (item.isFavorite) Color(0xFFFFC107) else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            // 价格
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¥${item.price}",
                    fontSize = 18.sp,
                    color = Color(0xFFFF6E40),
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "元/人",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(start = 4.dp)
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                item.tags.forEach { tag ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .border(1.dp, if (tag == "纯玩团") Color(0xFF00B894) else Color(0xFFAAAAAA), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = tag,
                            fontSize = 12.sp,
                            color = if (tag == "纯玩团") Color(0xFF00B894) else Color.DarkGray
                        )
                    }
                }
            }
            
            // 出团日期
            Text(
                text = "发团日期: ${item.dates}",
                fontSize = 14.sp,
                color = Color.DarkGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            
            // 行程特色
            Text(
                text = "行程特色: ${item.features}",
                fontSize = 14.sp,
                color = Color.DarkGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.LightGray
            )
            
            // 旅行社信息
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "旅行社",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                
                Spacer(modifier = Modifier.width(4.dp))
                
                Text(
                    text = item.agency,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                
                if (item.isVerified) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF00B894), RoundedCornerShape(2.dp))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "官方认证",
                            fontSize = 10.sp,
                            color = Color.White
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "联系人",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                
                Spacer(modifier = Modifier.width(4.dp))
                
                Text(
                    text = item.contact,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
            
            // 联系方式
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "电话",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                
                Spacer(modifier = Modifier.width(4.dp))
                
                Text(
                    text = item.contactPhone,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "备用电话",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                
                Spacer(modifier = Modifier.width(4.dp))
                
                Text(
                    text = item.contactPhone,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
            
            // 微信和QQ
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "微信",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                
                Spacer(modifier = Modifier.width(4.dp))
                
                Text(
                    text = item.secondaryPhone,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "QQ",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                
                Spacer(modifier = Modifier.width(4.dp))
                
                Text(
                    text = item.wechat,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

// 消息项数据模型
data class MessageItem(
    val id: String,
    val title: String,
    val price: Int,
    val dates: String,
    val features: String,
    val agency: String,
    val contactPhone: String,
    val secondaryPhone: String,
    val wechat: String,
    val contact: String,
    val tags: List<String>,
    val isVerified: Boolean,
    val isFavorite: Boolean,
    val isHighlighted: Boolean, // 是否高亮显示（第一个项目为黄色背景）
    val relatedPostId: Int
)
