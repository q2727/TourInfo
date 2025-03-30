package com.example.travalms.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travalms.ui.theme.PrimaryColor
import com.example.travalms.ui.theme.TextOnPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostsScreen(
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPublishClick: () -> Unit,
    onMessageClick: () -> Unit,
    onProfileClick: () -> Unit,
    onItemClick: (PostItem) -> Unit,
    onPublishNewClick: () -> Unit
) {
    val tabs = listOf("全部", "行程", "地接", "租车", "代订", "其他")
    var selectedTabIndex by remember { mutableStateOf(0) }
    
    // 模拟数据
    val postItems = listOf(
        PostItem(
            id = 1,
            title = "上海外国语大学体验+迪士尼6日夏令营",
            dates = "7月25日8月7、9、11、13、15、17、19、21、23、25日",
            feature = "1.在上海外国语大学浸入式英语环境中",
            remainingSlots = 10,
            price = 2580,
            daysExpired = 5
        ),
        PostItem(
            id = 2,
            title = "上海外国语大学体验+迪士尼6日夏令营",
            dates = "7月25日8月7、9、11、13、15、17、19、21、23、25日",
            feature = "1.在上海外国语大学浸入式英语环境中",
            remainingSlots = 10,
            price = 2580,
            daysExpired = 5
        ),
        PostItem(
            id = 3,
            title = "上海外国语大学体验+迪士尼6日夏令营",
            dates = "7月25日8月7、9、11、13、15、17、19、21、23、25日",
            feature = "1.在上海外国语大学浸入式英语环境中",
            remainingSlots = 10,
            price = 2580,
            daysExpired = 5
        )
    )
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("我的发布", fontWeight = FontWeight.Bold) },
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
                    selected = true,
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
                    selected = false,
                    onClick = onProfileClick,
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
        ) {
            // 发布新信息按钮
            Button(
                onClick = onPublishNewClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "发布新信息",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
            
            // 已发布信息标题
            Text(
                text = "已发布信息",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            // 标签选择器
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            if (selectedTabIndex == 0) PrimaryColor.copy(alpha = 0.15f) else Color(0xFFF5F5F5)
                        )
                        .clickable { selectedTabIndex = 0 },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "有效信息",
                        color = if (selectedTabIndex == 0) PrimaryColor else Color.Gray
                    )
                }
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            if (selectedTabIndex == 1) PrimaryColor.copy(alpha = 0.15f) else Color(0xFFF5F5F5)
                        )
                        .clickable { selectedTabIndex = 1 },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "已失效",
                        color = if (selectedTabIndex == 1) PrimaryColor else Color.Gray
                    )
                }
            }
            
            // 发布列表
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(postItems) { post ->
                    PostItemCard(
                        post = post,
                        onItemClick = onItemClick
                    )
                }
            }
        }
    }
}

@Composable
fun PostItemCard(post: PostItem, onItemClick: (PostItem) -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onItemClick(post) },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = PrimaryColor.copy(alpha = 0.15f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 标题和菜单按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = post.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                
                IconButton(
                    onClick = { /* 显示菜单 */ },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "更多选项"
                    )
                }
            }
            
            // 发团日期
            Text(
                text = "发团日期：${post.dates}",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
            
            // 行程特色
            Text(
                text = "行程特色：${post.feature}",
                fontSize = 14.sp,
                color = Color.DarkGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 底部信息：过期天数、剩余名额、价格
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 过期信息
                Text(
                    text = "${post.daysExpired}天后过期",
                    fontSize = 14.sp,
                    color = Color.Red
                )
                
                // 剩余名额和价格
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "剩余名额：",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                    
                    Text(
                        text = "${post.remainingSlots}",
                        fontSize = 14.sp,
                        color = Color(0xFFFF9800),
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Text(
                        text = "¥${post.price}元/人",
                        fontSize = 16.sp,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// 帖子数据模型
data class PostItem(
    val id: Int,
    val title: String,
    val dates: String,
    val feature: String,
    val remainingSlots: Int,
    val price: Int,
    val daysExpired: Int
) 