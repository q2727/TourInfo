package com.example.travalms.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.travalms.ui.theme.PrimaryColor
import com.example.travalms.ui.theme.BackgroundColor
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travalms.ui.viewmodels.TailListViewModel
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import com.example.travalms.ui.navigation.AppRoutes
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun TailListScreen(
    onTailOrderClick: (TailOrder) -> Unit,
    onHomeClick: () -> Unit,
    onPublishClick: () -> Unit,
    onMessageClick: () -> Unit,
    onProfileClick: () -> Unit,
    onCompanyClick: (String) -> Unit,
    onContactClick: (String) -> Unit,
    onPersonClick: (String) -> Unit,
    onReportItem: (String, String) -> Unit,
    onDeleteItem: (String) -> Unit,
    navController: NavController,
    viewModel: TailListViewModel = viewModel(factory = TailListViewModel.Factory())
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    
    // 在组件挂载时设置Application
    LaunchedEffect(Unit) {
        viewModel.setApplication(context.applicationContext as android.app.Application)
    }
    
    val refreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = {
            // 直接调用刷新方法，不需要额外的协程作用域
            viewModel.refreshTailLists()
            
            // 添加提示信息
            Toast.makeText(context, "正在刷新数据...", Toast.LENGTH_SHORT).show()
        }
    )
    // 添加焦点管理器，用于关闭键盘
    val focusManager = LocalFocusManager.current

    // 添加搜索关键词状态
    var searchText by remember { mutableStateOf("") }

    // 添加状态跟踪当前选择的标签
    var selectedTab by remember { mutableStateOf(0) }

    // 根据selectedTab和搜索关键词筛选要显示的尾单
    val displayedTailOrders = if (searchText.isEmpty()) {
        // 无搜索关键词时，根据标签筛选
        if (selectedTab == 0) {
            // 显示所有尾单
            state.tailOrders
        } else {
            // 只显示已收藏的尾单
            state.tailOrders.filter { it.isFavorite }
        }
    } else {
        // 有搜索关键词时，先按搜索关键词筛选，再根据标签筛选
        val searchFiltered = state.tailOrders.filter {
            it.title.contains(searchText, ignoreCase = true)
        }

        if (selectedTab == 0) {
            // 显示所有匹配搜索关键词的尾单
            searchFiltered
        } else {
            // 只显示已收藏且匹配搜索关键词的尾单
            searchFiltered.filter { it.isFavorite }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("我的尾单", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PrimaryColor,
                    titleContentColor = Color.White
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
                    selected = false,
                    onClick = onPublishClick,
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = Color.Gray
                )

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = "尾单") },
                    label = { Text("尾单", fontSize = 12.sp) },
                    selected = true,
                    onClick = { /* 已在尾单页面 */ },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(refreshState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundColor)
            ) {
                // 搜索框
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp)),
                        placeholder = { Text("搜索尾单") },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "搜索") },
                        trailingIcon = {
                            if (searchText.isNotEmpty()) {
                                IconButton(
                                    onClick = {
                                        searchText = ""
                                    }
                                ) {
                                    Icon(Icons.Filled.Clear, contentDescription = "清除")
                                }
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.LightGray,
                            unfocusedContainerColor = Color.White
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                // 关闭键盘
                                focusManager.clearFocus()
                            }
                        )
                    )
                }

                // 标签选择器
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = Color(0xFFF5F5F5),
                        contentColor = PrimaryColor,
                        indicator = { tabPositions ->
                            Box {}  // 不显示指示器
                        },
                        divider = { }  // 不显示分隔线
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            modifier = Modifier
                                .weight(1f)
                                .background(if (selectedTab == 0) Color.White else Color(0xFFF5F5F5))
                        ) {
                            Text(
                                text = "全部信息",
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = if (selectedTab == 0) Color.Black else Color.Gray
                            )
                        }

                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            modifier = Modifier
                                .weight(1f)
                                .background(if (selectedTab == 1) Color.White else Color(0xFFF5F5F5))
                        ) {
                            Text(
                                text = "我的收藏",
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = if (selectedTab == 1) Color.Black else Color.Gray
                            )
                        }
                    }
                }

                // 显示搜索结果数量
                if (searchText.isNotEmpty() && displayedTailOrders.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "找到 ${displayedTailOrders.size} 个结果",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                if (displayedTailOrders.isEmpty() && !state.isLoading) {
                    // 显示空状态
                    Box(
                        modifier = Modifier.fillMaxSize().weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (searchText.isNotEmpty()) {
                                // 无搜索结果
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "没有找到匹配\"$searchText\"的尾单",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.Gray
                                )
                            } else {
                                // 无尾单数据
                                Text("没有可用的尾单", style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                } else {
                    // 尾单列表 - 使用过滤后的列表
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().weight(1f),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(displayedTailOrders) { tailOrder ->
                            TailOrderItem(
                                tailOrder = tailOrder,
                                onClick = {
                                    // 添加日志记录尾单信息
                                    android.util.Log.d("TailListScreen", "点击尾单: ID=${tailOrder.id}, 标题=${tailOrder.title}, 发布者JID=${tailOrder.publisherJid}")
                                    // 使用传入的回调函数处理尾单点击
                                    onTailOrderClick(tailOrder)
                                },
                                onCompanyClick = { onCompanyClick(tailOrder.companyId) },
                                onContactClick = {
                                    // 获取发布者信息并显示拨号对话框
                                    viewModel.viewModelScope.launch {
                                        val username = tailOrder.publisherJid.substringBefore("@")
                                        val publisherInfo = viewModel.getUserInfo(username)
                                        val phoneNumber = publisherInfo?.get("phoneNumber")?.toString()
                                        val publisherName = publisherInfo?.get("nickname")?.toString() 
                                            ?: publisherInfo?.get("username")?.toString()
                                            ?: "未知用户"

                                        if (phoneNumber != null && phoneNumber.isNotEmpty()) {
                                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                                data = Uri.parse("tel:${phoneNumber}")
                                            }
                                            context.startActivity(intent)
                                        } else {
                                            // 如果没有电话号码，可以显示一个Toast提示
                                            Toast.makeText(context, "${publisherName}未设置联系电话", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                onPersonClick = { onPersonClick(tailOrder.contactPersonId) },
                                onReportItem = { reason -> onReportItem(tailOrder.id.toString(), reason) },
                                onDeleteItem = { onDeleteItem(tailOrder.id.toString()) },
                                onFavoriteClick = { isFavorite ->
                                    viewModel.toggleFavorite(tailOrder.id)
                                },
                                navController = navController,
                                viewModel = viewModel
                            )
                        }
                    }
                }
                
                // 显示错误消息
                state.error?.let { errorMsg ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = errorMsg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            
            // 显示下拉刷新指示器
            PullRefreshIndicator(
                refreshing = state.isLoading,
                state = refreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TailOrderItem(
    tailOrder: TailOrder,
    onClick: () -> Unit,
    onCompanyClick: () -> Unit,
    onContactClick: () -> Unit,
    onPersonClick: () -> Unit,
    onReportItem: (String) -> Unit,
    onDeleteItem: () -> Unit,
    onFavoriteClick: (Boolean) -> Unit,
    navController: NavController,
    viewModel: TailListViewModel
) {
    var isFavorite by remember { mutableStateOf(tailOrder.isFavorite) }
    var showMenu by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    var reportReason by remember { mutableStateOf("") }
    
    // 添加拨号对话框状态
    var showPhoneDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    
    // 获取发布者信息
    var publisherInfo by remember { mutableStateOf<Map<String, Any>?>(null) }
    LaunchedEffect(tailOrder.publisherJid) {
        val username = tailOrder.publisherJid.substringBefore("@")
        publisherInfo = viewModel.getUserInfo(username)
    }

    // 举报对话框
    if (showReportDialog) {
        Dialog(
            onDismissRequest = { showReportDialog = false }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "输入举报原因",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = reportReason,
                        onValueChange = { reportReason = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.LightGray,
                            focusedBorderColor = PrimaryColor
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            onReportItem(reportReason)
                            reportReason = ""
                            showReportDialog = false
                        },
                        modifier = Modifier
                            .width(120.dp)
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryColor
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("确定")
                    }
                }
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        showMenu = true
                    },
                    onTap = { onClick() }
                )
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box {
            // 长按菜单 - 使用AlertDialog替代DropdownMenu
            if (showMenu) {
                AlertDialog(
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.width(280.dp),
                    content = {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(
                                onClick = {
                                    showMenu = false
                                    showReportDialog = true
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
                                        imageVector = Icons.Filled.Warning,
                                        contentDescription = "举报",
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "举报",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Divider()

                            TextButton(
                                onClick = {
                                    onDeleteItem()
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

                            Divider()

                            TextButton(
                                onClick = { showMenu = false },
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
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "取消",
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "取消",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // 标题和收藏按钮
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = tailOrder.title,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryColor,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {
                            isFavorite = !isFavorite
                            onFavoriteClick(isFavorite)
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "收藏",
                            tint = if (isFavorite) Color.Red else Color.Gray
                        )
                    }
                }

                // 分隔线
                Divider(
                    color = Color(0xFFE0E0E0),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // 行程内容
                Text(
                    text = "行程内容：",
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 显示行程内容，以编号列表形式展示
                tailOrder.content.forEachIndexed { index, content ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "${index + 1}.",
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.width(20.dp)
                        )

                        Text(
                            text = content,
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // 有效期和价格
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = tailOrder.price,
                        fontSize = 16.sp,
                        color = Color(0xFFE53935),
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "有效期: ${tailOrder.remainingDays}天",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                // 分隔线
                Divider(
                    color = Color(0xFFE0E0E0),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // 底部信息区域 - 公司信息和联系人头像
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 公司信息（可点击）
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            // 提取用户名并导航到用户详情页
                            val username = tailOrder.publisherJid.substringBefore("@")
                            navController.navigate(AppRoutes.FRIEND_DETAIL.replace("{username}", username))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "发布者",
                            tint = PrimaryColor,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            // 显示发布者用户名
                            text = tailOrder.publisherJid.substringBefore("@"),
                            color = PrimaryColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 联系人头像（可点击跳转到个人界面）
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(PrimaryColor.copy(alpha = 0.1f))
                                .border(1.dp, PrimaryColor, CircleShape)
                                .clickable {
                                    // 使用发布者的JID作为聊天对象
                                    val username = tailOrder.publisherJid.substringBefore("@")
                                    navController.navigate(
                                        AppRoutes.CHAT_ROOM
                                            .replace("{sessionId}", username)  // 使用纯用户名作为sessionId
                                            .replace("{targetName}", username) // 使用纯用户名作为targetName
                                            .replace("{targetType}", "chat")  // 类型改为chat
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.MailOutline,
                                contentDescription = "发送消息",
                                tint = PrimaryColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // 电话图标（可点击拨打电话）
                        IconButton(
                            onClick = onContactClick,  // 直接使用传入的 onContactClick 回调
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Phone,
                                contentDescription = "联系电话",
                                tint = PrimaryColor
                            )
                        }
                    }
                }
            }
        }
    }

    // 添加拨号确认对话框
    if (showPhoneDialog && publisherInfo != null) {
        val phoneNumber = publisherInfo?.get("phoneNumber")?.toString()
        val publisherName = publisherInfo?.get("nickname")?.toString() 
            ?: publisherInfo?.get("username")?.toString()
            ?: "未知用户"
            
        if (phoneNumber != null && phoneNumber.isNotEmpty()) {
            AlertDialog(
                onDismissRequest = { showPhoneDialog = false },
                title = null,
                text = {
                    Text(
                        text = "${phoneNumber}可能是一个电话号码，你可以",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showPhoneDialog = false
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${phoneNumber}")
                            }
                            context.startActivity(intent)
                        }
                    ) {
                        Text("呼叫", color = MaterialTheme.colorScheme.primary)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showPhoneDialog = false }) {
                        Text("取消")
                    }
                }
            )
        } else {
            // 如果没有电话号码，显示提示对话框
            AlertDialog(
                onDismissRequest = { showPhoneDialog = false },
                title = null,
                text = {
                    Text(
                        text = "${publisherName}未设置联系电话",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                confirmButton = {
                    TextButton(onClick = { showPhoneDialog = false }) {
                        Text("确定")
                    }
                }
            )
        }
    }
}