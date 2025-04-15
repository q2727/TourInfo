package com.example.travalms.ui.message

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import com.example.travalms.data.model.ContactItem
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api

/**
 * 消息界面 - 按照图片样式重新实现
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(
    onBackClick: () -> Unit = {},
    onSubscribeSettingClick: () -> Unit = {},
    onMessageClick: (Int) -> Unit = {},
    onHomeClick: () -> Unit,
    onPublishClick: () -> Unit,
    onProfileClick: () -> Unit,
    onTailListClick: () -> Unit
) {
    // 状态管理
    var selectedTab by remember { mutableStateOf(1) } // 默认选中"好友"选项卡
    var searchText by remember { mutableStateOf("") }
    
    // 字母索引列表
    val alphabet = ('A'..'Z').map { it.toString() }
    var selectedLetter by remember { mutableStateOf<String?>(null) }
    
    // 模拟好友数据 - 注意：avatarResId已改为avatarUrl
    val friends = listOf(
        ContactItem(
            id = 1,
            name = "哈铁国旅",
            status = "目前哈尔滨冰雪大世界7日还有2人空缺"
        ),
        ContactItem(
            id = 2,
            name = "新疆导游",
            status = "目前哈尔滨冰雪大世界7日还有2人空缺"
        ),
        ContactItem(
            id = 3,
            name = "内蒙古旅行推荐官",
            status = "目前哈尔滨冰雪大世界7日还有2人空缺"
        ),
        ContactItem(
            id = 4,
            name = "北京向导",
            status = "目前哈尔滨冰雪大世界7日还有2人空缺"
        )
    )
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("好友", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PrimaryColor,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                modifier = Modifier.height(56.dp)
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "产品") },
                    label = { Text("产品", fontSize = 12.sp) },
                    selected = false,
                    onClick = onHomeClick,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryColor,
                        selectedTextColor = PrimaryColor,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
                
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Add, contentDescription = "发布") },
                    label = { Text("发布", fontSize = 12.sp) },
                    selected = false,
                    onClick = onPublishClick,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryColor,
                        selectedTextColor = PrimaryColor,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
                
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = "尾单") },
                    label = { Text("尾单", fontSize = 12.sp) },
                    selected = false,
                    onClick = onTailListClick,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryColor,
                        selectedTextColor = PrimaryColor,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
                
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Email, contentDescription = "消息") },
                    label = { Text("消息", fontSize = 12.sp) },
                    selected = true,
                    onClick = { /* 已在消息页面 */ },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryColor,
                        selectedTextColor = PrimaryColor,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
                
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "我的") },
                    label = { Text("我的", fontSize = 12.sp) },
                    selected = false,
                    onClick = onProfileClick,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryColor,
                        selectedTextColor = PrimaryColor,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 搜索框和添加好友按钮
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
                        .height(40.dp)
                        .background(
                            color = Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
            ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "搜索",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        BasicTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 8.dp),
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                Box {
                                    if (searchText.isEmpty()) {
                                        Text(
                                            text = "搜索",
                                            color = Color.Gray,
                                            fontSize = 14.sp
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )
                    }
                }
                
                // 添加好友按钮
                Button(
                    onClick = { /* 添加好友逻辑 */ },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "+好友",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
            
            // 选项卡
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF0F0F0))
            ) {
                TabItem(
                    title = "全部消息",
                    isSelected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                
                TabItem(
                    title = "好友",
                    isSelected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                
                TabItem(
                    title = "群聊",
                    isSelected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
                
                TabItem(
                    title = "公司黄页",
                    isSelected = selectedTab == 3,
                    onClick = { selectedTab = 3 }
                )
            }
            
            // 主内容区域
            Box(modifier = Modifier.weight(1f)) {
                // 好友列表
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(friends) { friend ->
                        ContactListItem(friend = friend) {
                            onMessageClick(friend.id)
                        }
                        Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                    }
                }
                
                // 右侧字母导航
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    alphabet.forEach { letter ->
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { selectedLetter = letter },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = letter,
                                fontSize = 12.sp,
                                color = if (selectedLetter == letter) PrimaryColor else Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TabItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .background(if (isSelected) Color.White else Color(0xFFF0F0F0))
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                color = if (isSelected) PrimaryColor else Color.Gray,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
            
            if (isSelected) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(20.dp)
                        .height(2.dp)
                        .background(PrimaryColor)
                )
            }
        }
    }
}

@Composable
fun ContactListItem(
    friend: ContactItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 头像
            Box(
                modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        )
        
        // 名称和状态
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        ) {
            Text(
                text = friend.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = friend.status,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
