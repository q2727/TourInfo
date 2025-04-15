package com.example.travalms.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
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
    onPublishNewClick: () -> Unit,
    onTailListClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    
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
    
    // 根据搜索文本筛选发布项目
    val filteredPostItems = postItems.filter { postItem ->
        if (searchText.isEmpty()) {
            true // 如果搜索文本为空，显示所有项目
        } else {
            // 在标题、日期或特色中搜索
            postItem.title.contains(searchText, ignoreCase = true) ||
            postItem.dates.contains(searchText, ignoreCase = true) ||
            postItem.feature.contains(searchText, ignoreCase = true)
        }
    }
    
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
                    selected = true,
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
            // 搜索栏和发布按钮
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 搜索框
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .background(
                            color = Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 搜索图标
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "搜索",
                            tint = Color.Gray,
                            modifier = Modifier.padding(start = 8.dp, end = 12.dp)
                        )
                        
                        // 输入框
                        BasicTextField(
                            value = searchText,
                            onValueChange = { newValue -> searchText = newValue },
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 8.dp),
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                Box {
                                    // 当文本为空时显示提示文字
                                    if (searchText.isEmpty()) {
                                        Text(
                                            text = "搜索",
                                            color = Color.Gray,
                                            fontSize = 16.sp
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )
                    }
                }
                
                // 发布按钮
                Button(
                    onClick = onPublishNewClick,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3)
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "发布",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        
                        Text(
                            text = "发布",
                            color = Color.White,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
            
            // 发布列表
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredPostItems) { postItem ->
                    PostItemCard(
                        post = postItem,
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 标题
            Text(
                text = post.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            
            // 行程特色部分
            Text(
                text = "行程特色：",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
            
            // 分条展示特色（这里我们将单行文本拆分为多行展示）
            val features = listOf(
                "1.在上海外国语大学浸入式英语环境中学习英语，培养孩子良好的英语语感及口语运用能力。",
                "2.上海淮景点畅游、博物馆、知名大学参访，展开真正的上海文化寻根游学之旅。",
                "3.上海迪斯尼乐园畅游，学习游乐两不误。"
            )
            
            features.forEach { feature ->
                Text(
                    text = feature,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
            
            // 有效期
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                
                Text(
                    text = "有效期：",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                
                Text(
                    text = "3天6:30",
                    fontSize = 14.sp,
                    color = Color(0xFFFF6E40),
                    fontWeight = FontWeight.Medium
                )
            }
            
            // 底部分隔线
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.LightGray
            )
            
            // 发布节点
            Text(
                text = "发布节点：",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            // 发布地点标签
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                listOf("北京", "上海", "海淀").forEach { location ->
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .background(
                                color = PrimaryColor.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = location,
                            fontSize = 12.sp,
                            color = PrimaryColor
                        )
                    }
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
    val daysExpired: Int,
    val publisher: String = "张伟", // 默认值
    val publishLocations: List<String> = listOf("北京", "上海", "海淀") // 默认值
) 