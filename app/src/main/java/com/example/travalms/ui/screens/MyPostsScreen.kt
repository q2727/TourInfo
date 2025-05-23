package com.example.travalms.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travalms.ui.theme.PrimaryColor
import com.example.travalms.ui.viewmodels.MyPublishedTailsViewModel
import com.example.travalms.util.CityNameMapping
import com.example.travalms.ui.model.PostItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostsScreen(
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPublishClick: () -> Unit,
    onMessageClick: () -> Unit,
    onProfileClick: () -> Unit,
    onPublishNewClick: () -> Unit,
    onTailListClick: () -> Unit,
    onEditPost: (PostItem) -> Unit = { /* 编辑功能已移除 */ },
    onTailOrderClick: (PostItem) -> Unit,
    viewModel: MyPublishedTailsViewModel = viewModel()
) {
    var searchText by remember { mutableStateOf("") }
    
    // 使用ViewModel获取真实数据
    val uiState by viewModel.uiState.collectAsState()
    
    // 根据搜索文本筛选发布项目
    val filteredPostItems = uiState.publishedTails.filter { postItem ->
        if (searchText.isEmpty()) {
            true // 如果搜索文本为空，显示所有项目
        } else {
            // 在标题、日期或特色中搜索
            postItem.title.contains(searchText, ignoreCase = true) ||
            postItem.dates.contains(searchText, ignoreCase = true) ||
            postItem.feature.contains(searchText, ignoreCase = true)
        }
    }
    
    // 长按菜单回调函数
    // val handleRepost = { post: PostItem ->
    //     // 导航到编辑界面 - 功能已移除
    //     onEditPost(post)
    // }
    
    val handleRefresh = {
        // 刷新数据
        viewModel.loadUserPublishedTails()
    }
    
    // 尾单点击处理函数
    val handleItemClick: (PostItem) -> Unit = { post ->
        // 调用导航回调
        onTailOrderClick(post)
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
            
            // 显示加载状态、错误信息或列表
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    // 加载中
                    uiState.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = PrimaryColor
                        )
                    }
                    
                    // 出现错误
                    uiState.errorMessage != null -> {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "错误",
                                tint = Color.Red,
                                modifier = Modifier.size(48.dp)
                            )
                            
                            Text(
                                text = uiState.errorMessage ?: "加载失败",
                                color = Color.Gray,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            
                            Button(
                                onClick = { viewModel.loadUserPublishedTails() },
                                modifier = Modifier.padding(top = 16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryColor
                                )
                            ) {
                                Text("重新加载")
                            }
                        }
                    }
                    
                    // 列表为空
                    filteredPostItems.isEmpty() -> {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "暂无发布记录",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                            
                            Button(
                                onClick = onPublishNewClick,
                                modifier = Modifier.padding(top = 16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryColor
                                )
                            ) {
                                Text("去发布")
                            }
                        }
                    }
                    
                    // 显示数据列表
                    else -> {
                        // 发布列表
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filteredPostItems) { postItem ->
                                PostItemCard(
                                    post = postItem,
                                    onItemClick = handleItemClick,
                                    onRefresh = { handleRefresh() },
                                    onDelete = { post -> 
                                        // 执行删除操作
                                        viewModel.deleteTailOrder(post.id.toLong())
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItemCard(
    post: PostItem, 
    onItemClick: (PostItem) -> Unit = {},
    onRefresh: (PostItem) -> Unit = {},
    onDelete: (PostItem) -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }
    // 添加删除确认对话框状态
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { showMenu = true },
                    onTap = { onItemClick(post) }
                )
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box {
            // 长按菜单
            if (showMenu) {
                Dialog(
                    onDismissRequest = { showMenu = false }
                ) {
                    Surface(
                        modifier = Modifier.width(280.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF3B4045)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            TextButton(
                                onClick = {
                                    onRefresh(post)
                                    showMenu = false
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Color.White
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Refresh,
                                        contentDescription = "刷新",
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "刷新",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            
                            Divider(color = Color(0xFF4E5359))
                            
                            TextButton(
                                onClick = {
                                    showMenu = false
                                    showDeleteConfirmDialog = true // 显示删除确认对话框
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Color.White
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "删除",
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "删除",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        
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
                
                // 显示实际的行程特色内容
                Text(
                    text = post.feature,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                
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
                        text = "${post.daysExpired}天",
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
                    post.publishLocations.forEach { location ->
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
                                text = CityNameMapping.pinyinToChineseName(location),
                                fontSize = 12.sp,
                                color = PrimaryColor
                            )
                        }
                    }
                }
            }
        }
    }
    
    // 删除确认对话框
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("删除尾单") },
            text = { 
                Text("确定要删除这条尾单吗？此操作将从系统和各种发布渠道中彻底删除该信息，无法恢复。") 
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(post)
                        showDeleteConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("确认删除")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}