package com.example.travalms.ui.message

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text as MaterialText
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
import androidx.navigation.NavController
import com.example.travalms.ui.navigation.AppRoutes
import com.example.travalms.ui.theme.PrimaryColor
import com.example.travalms.data.model.ContactItem
import com.example.travalms.ui.components.LetterIndex
import kotlinx.coroutines.launch

/**
 * 消息界面 - 按照图片样式重新实现
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(
    navController: NavController,
    onHomeClick: () -> Unit,
    onPublishClick: () -> Unit,
    onProfileClick: () -> Unit,
    onTailListClick: () -> Unit
) {
    // 状态管理
    var selectedTab by remember { mutableStateOf(1) } // 默认选中"好友"选项卡
    var searchText by remember { mutableStateOf("") }
    
    // 字母索引状态
    var selectedLetter by remember { mutableStateOf<String?>(null) }
    
    // 创建LazyListState用于控制滚动
    val allMessagesListState = rememberLazyListState()
    val friendsListState = rememberLazyListState()
    val groupChatsListState = rememberLazyListState()
    val companiesListState = rememberLazyListState()
    
    // 协程作用域，用于执行滚动操作
    val coroutineScope = rememberCoroutineScope()
    
    // 跟踪已添加的好友/公司
    var addedFriends by remember { mutableStateOf(setOf(1, 201)) } // 示例：默认已添加的ID
    
    // 分类的联系人数据 - 确保每个字母都有对应的联系人
    val friends = remember {
        listOf(
            ContactItem(id = 1, name = "Alice Johnson", status = "在线"),
            ContactItem(id = 2, name = "Bob Smith", status = "离线 - 30分钟前"),
            ContactItem(id = 3, name = "陈明 (Chen Ming)", status = "忙碌"),
            ContactItem(id = 4, name = "David Lee", status = "在线"),
            ContactItem(id = 5, name = "Emma Davis", status = "离线 - 1小时前"),
            ContactItem(id = 6, name = "冯华 (Feng Hua)", status = "在线"),
            ContactItem(id = 7, name = "Grace Kim", status = "在线"),
            ContactItem(id = 8, name = "Henry Park", status = "忙碌"),
            ContactItem(id = 9, name = "Irene Chen", status = "离线 - 2小时前"),
            ContactItem(id = 10, name = "Jack Wilson", status = "在线"),
            ContactItem(id = 11, name = "柯南 (Ke Nan)", status = "离线 - 昨天"),
            ContactItem(id = 12, name = "李阳 (Li Yang)", status = "在线"),
            ContactItem(id = 13, name = "马云 (Ma Yun)", status = "忙碌"),
            ContactItem(id = 14, name = "Nathan Adams", status = "在线"),
            ContactItem(id = 15, name = "Olivia Roberts", status = "离线 - 3小时前"),
            ContactItem(id = 16, name = "庞大 (Pang Da)", status = "在线"),
            ContactItem(id = 17, name = "钱多 (Qian Duo)", status = "忙碌"),
            ContactItem(id = 18, name = "Rachel Green", status = "在线"),
            ContactItem(id = 19, name = "宋江 (Song Jiang)", status = "离线 - 4小时前"),
            ContactItem(id = 20, name = "Tina Rodriguez", status = "在线"),
            ContactItem(id = 21, name = "Uma Patel", status = "忙碌"),
            ContactItem(id = 22, name = "Victor Nguyen", status = "在线"),
            ContactItem(id = 23, name = "王五 (Wang Wu)", status = "离线 - 5小时前"),
            ContactItem(id = 24, name = "夏雨 (Xia Yu)", status = "在线"),
            ContactItem(id = 25, name = "杨过 (Yang Guo)", status = "忙碌"),
            ContactItem(id = 26, name = "张三 (Zhang San)", status = "在线"),
            ContactItem(id = 27, name = "赵四 (Zhao Si)", status = "离线 - 6小时前")
        )
    }
    
    val groups = remember {
        listOf(
            ContactItem(id = 101, name = "安徽旅游团 (Anhui Travel)", status = "5人在线"),
            ContactItem(id = 102, name = "Business Strategy Team", status = "12人在线"),
            ContactItem(id = 103, name = "Creative Design Forum", status = "3人在线"),
            ContactItem(id = 104, name = "大连海鲜爱好者 (Dalian Seafood)", status = "8人在线"),
            ContactItem(id = 105, name = "Engineering Community", status = "15人在线"),
            ContactItem(id = 106, name = "Finance Discussion Group", status = "2人在线"),
            ContactItem(id = 107, name = "广州美食探索 (Guangzhou Food)", status = "10人在线"),
            ContactItem(id = 108, name = "HR Professional Network", status = "6人在线"),
            ContactItem(id = 109, name = "International Travel Club", status = "4人在线"),
            ContactItem(id = 110, name = "Java Programming", status = "9人在线"),
            ContactItem(id = 111, name = "昆明旅游同行 (Kunming Travel)", status = "7人在线"),
            ContactItem(id = 112, name = "Language Exchange Group", status = "11人在线"),
            ContactItem(id = 113, name = "Music Lovers Community", status = "14人在线"),
            ContactItem(id = 114, name = "南京历史文化 (Nanjing History)", status = "3人在线"),
            ContactItem(id = 115, name = "Outdoor Activities Club", status = "8人在线"),
            ContactItem(id = 116, name = "Photography Discussion", status = "5人在线"),
            ContactItem(id = 117, name = "青岛啤酒爱好者 (Qingdao Beer)", status = "7人在线"),
            ContactItem(id = 118, name = "Remote Work Community", status = "16人在线"),
            ContactItem(id = 119, name = "Software Development", status = "9人在线"),
            ContactItem(id = 120, name = "天津本地游 (Tianjin Tour)", status = "4人在线"),
            ContactItem(id = 121, name = "UI/UX Design Group", status = "6人在线"),
            ContactItem(id = 122, name = "Virtual Reality Fans", status = "3人在线"),
            ContactItem(id = 123, name = "武汉美景分享 (Wuhan Scenery)", status = "11人在线"),
            ContactItem(id = 124, name = "西安历史遗迹 (Xi'an Heritage)", status = "2人在线"),
            ContactItem(id = 125, name = "扬州慢生活 (Yangzhou Lifestyle)", status = "8人在线"),
            ContactItem(id = 126, name = "珠海海岛游 (Zhuhai Islands)", status = "5人在线")
        )
    }
    
    val companies = remember {
        listOf(
            ContactItem(id = 201, name = "Acme Corporation", status = "科技 - 全球领先"),
            ContactItem(id = 202, name = "Bright Solutions Inc", status = "咨询 - 专业服务"),
            ContactItem(id = 203, name = "Cloud Technologies", status = "IT服务 - 云计算专家"),
            ContactItem(id = 204, name = "Dynamic Systems", status = "软件 - 企业解决方案"),
            ContactItem(id = 205, name = "Echo Innovations", status = "研发 - 前沿技术"),
            ContactItem(id = 206, name = "Future Networks", status = "通信 - 5G技术"),
            ContactItem(id = 207, name = "Global Data Services", status = "数据 - 大数据分析"),
            ContactItem(id = 208, name = "Harmony Design Studio", status = "设计 - UI/UX专家"),
            ContactItem(id = 209, name = "Insight Analytics", status = "分析 - 商业智能"),
            ContactItem(id = 210, name = "Junction Partners", status = "金融 - 风险投资"),
            ContactItem(id = 211, name = "Knowledge Base Systems", status = "教育 - 在线学习"),
            ContactItem(id = 212, name = "Logic AI Solutions", status = "人工智能 - 机器学习"),
            ContactItem(id = 213, name = "Mobile App Developers", status = "移动开发 - 跨平台专家"),
            ContactItem(id = 214, name = "Next Generation Tech", status = "创新 - 未来科技"),
            ContactItem(id = 215, name = "Optimum Security", status = "网络安全 - 防护专家"),
            ContactItem(id = 216, name = "Premier Hardware", status = "硬件 - 设备制造"),
            ContactItem(id = 217, name = "Quality Testing Labs", status = "测试 - 质量保证"),
            ContactItem(id = 218, name = "Reliable Infrastructure", status = "基础设施 - 云服务"),
            ContactItem(id = 219, name = "Smart IoT Solutions", status = "物联网 - 智能家居"),
            ContactItem(id = 220, name = "Tech Support Services", status = "客服 - 24/7支持"),
            ContactItem(id = 221, name = "Universal Web Design", status = "网页设计 - 响应式专家"),
            ContactItem(id = 222, name = "Virtual Reality Labs", status = "VR/AR - 沉浸式体验"),
            ContactItem(id = 223, name = "Web Hosting Solutions", status = "托管 - 高性能服务器"),
            ContactItem(id = 224, name = "Xcel Digital Marketing", status = "营销 - 数字策略"),
            ContactItem(id = 225, name = "Yield Optimization", status = "优化 - 性能提升"),
            ContactItem(id = 226, name = "Zenith IT Consulting", status = "咨询 - IT战略")
        )
    }
    
    val messages = remember {
        listOf(
            ContactItem(id = 301, name = "Alice Johnson", status = "刚刚"),
            ContactItem(id = 302, name = "Bright Solutions", status = "5分钟前"),
            ContactItem(id = 303, name = "Cloud Technologies", status = "10分钟前"),
            ContactItem(id = 304, name = "David Lee", status = "15分钟前"),
            ContactItem(id = 305, name = "Echo Innovations", status = "30分钟前"),
            ContactItem(id = 306, name = "Future Networks", status = "1小时前"),
            ContactItem(id = 307, name = "Grace Kim", status = "2小时前"),
            ContactItem(id = 308, name = "Harmony Design", status = "3小时前"),
            ContactItem(id = 309, name = "Insight Analytics", status = "昨天"),
            ContactItem(id = 310, name = "Junction Partners", status = "昨天"),
            ContactItem(id = 311, name = "Knowledge Base", status = "前天"),
            ContactItem(id = 312, name = "Logic AI Solutions", status = "前天"),
            ContactItem(id = 313, name = "Mobile App Developers", status = "3天前"),
            ContactItem(id = 314, name = "Next Generation", status = "4天前"),
            ContactItem(id = 315, name = "Optimum Security", status = "5天前"),
            ContactItem(id = 316, name = "Premier Hardware", status = "6天前"),
            ContactItem(id = 317, name = "Quality Testing", status = "一周前"),
            ContactItem(id = 318, name = "Reliable Infrastructure", status = "一周前"),
            ContactItem(id = 319, name = "Smart IoT Solutions", status = "一周前"),
            ContactItem(id = 320, name = "Tech Support", status = "2023-06-15"),
            ContactItem(id = 321, name = "Universal Web Design", status = "2023-06-10"),
            ContactItem(id = 322, name = "Virtual Reality Labs", status = "2023-06-05"),
            ContactItem(id = 323, name = "Web Hosting", status = "2023-06-01"),
            ContactItem(id = 324, name = "Xcel Digital", status = "2023-05-25"),
            ContactItem(id = 325, name = "Yield Optimization", status = "2023-05-20"),
            ContactItem(id = 326, name = "Zenith IT Consulting", status = "2023-05-15")
        )
    }
    
    // 根据选中的选项卡显示不同的联系人列表
    val currentList = when (selectedTab) {
        0 -> messages
        1 -> friends
        2 -> groups
        3 -> companies
        else -> messages
    }
    
    // 获取当前活动的LazyListState
    val currentListState = when (selectedTab) {
        0 -> allMessagesListState
        1 -> friendsListState
        2 -> groupChatsListState
        3 -> companiesListState
        else -> allMessagesListState
    }

    // 确保包含所有字母 A-Z
    val allLetters = ('A'..'Z').map { it.toString() }.toList()
    
    // 获取当前的所有索引字母 - 从联系人列表中提取
    val contactLetters = currentList.mapNotNull { contact -> 
        // 对于中文名称，提取括号中的拼音首字母
        val name = contact.name
        if (name.contains('(') && name.contains(')')) {
            // 提取括号中的第一个字母
            val pinyinPart = name.substringAfter('(').substringBefore(')')
            pinyinPart.firstOrNull()?.uppercaseChar()?.toString()
        } else {
            // 对于英文名，直接取首字母
            name.firstOrNull()?.uppercaseChar()?.toString()
        }
    }.distinct().sorted()
    
    // 确保所有字母都显示
    val indexLetters = (contactLetters + allLetters).distinct().sorted()
    
    // 创建字母到索引的映射
    val letterToIndexMap = remember(currentList) {
        val map = mutableMapOf<String, Int>()
        currentList.forEachIndexed { index, contact ->
            val name = contact.name
            val firstLetter = if (name.contains('(') && name.contains(')')) {
                // 中文名，提取拼音首字母
                val pinyinPart = name.substringAfter('(').substringBefore(')')
                pinyinPart.firstOrNull()?.uppercaseChar()?.toString()
            } else {
                // 英文名，直接取首字母
                name.firstOrNull()?.uppercaseChar()?.toString()
            }
            
            if (firstLetter != null && !map.containsKey(firstLetter)) {
                map[firstLetter] = index
            }
        }
        map
    }
    
    // 过滤搜索结果
    val filteredList = remember(searchText, currentList) {
        if (searchText.isBlank()) {
            currentList
        } else {
            currentList.filter { 
                it.name.contains(searchText, ignoreCase = true) 
            }
        }
    }
    
    // 头部标题根据选中的选项卡动态变化
    val screenTitle = when (selectedTab) {
        0 -> "全部消息"
        1 -> "好友"
        2 -> "群聊"
        3 -> "公司黄页"
        else -> "消息"
    }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(screenTitle, fontWeight = FontWeight.Bold) },
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
                    label = { MaterialText("产品", fontSize = 12.sp) },
                    selected = false,
                    onClick = onHomeClick,
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = Color.Gray
                )
                
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Add, contentDescription = "发布") },
                    label = { MaterialText("发布", fontSize = 12.sp) },
                    selected = false,
                    onClick = onPublishClick,
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = Color.Gray
                )
                
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = "尾单") },
                    label = { MaterialText("尾单", fontSize = 12.sp) },
                    selected = false,
                    onClick = onTailListClick,
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = Color.Gray
                )
                
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Email, contentDescription = "消息") },
                    label = { MaterialText("消息", fontSize = 12.sp) },
                    selected = true,
                    onClick = { /* Already on 消息 screen */ },
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = Color.Gray
                )
                
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "我的") },
                    label = { MaterialText("我的", fontSize = 12.sp) },
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
            // 搜索框和搜索按钮
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
                
                // 搜索按钮
                Button(
                    onClick = { /* 执行搜索 */ },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "搜索",
                        tint = Color.White
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
                    onClick = { selectedTab = 0 },
                    modifier = Modifier.weight(1f)
                )
                
                TabItem(
                    title = "好友",
                    isSelected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    modifier = Modifier.weight(1f)
                )
                
                TabItem(
                    title = "群聊",
                    isSelected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    modifier = Modifier.weight(1f)
                )
                
                TabItem(
                    title = "公司黄页",
                    isSelected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    modifier = Modifier.weight(1f)
                )
            }
            
            // 主内容区域
            Box(modifier = Modifier.weight(1f)) {
                // 联系人列表
                LazyColumn(
                    state = currentListState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredList) { contact ->
                        ContactListItem(
                            friend = contact,
                            isCompanyTab = selectedTab == 3,
                            isAdded = addedFriends.contains(contact.id),
                            onAddFriend = { 
                                addedFriends = addedFriends + contact.id
                            },
                            onClick = {
                                // 导航到聊天界面或详情页
                                when (selectedTab) {
                                    0, 2 -> {
                                        // 消息、群聊 -> 聊天室
                                        val targetType = when (selectedTab) {
                                            0 -> "message"
                                            2 -> "group"
                                            else -> "message"
                                        }
                                        navController.navigate(
                                            AppRoutes.CHAT_ROOM
                                                .replace("{sessionId}", contact.id.toString())
                                                .replace("{targetName}", contact.name)
                                                .replace("{targetType}", targetType)
                                        )
                                    }
                                    1 -> {
                                        // 好友 -> 个人详情页
                                        navController.navigate(
                                            AppRoutes.PERSON_DETAIL
                                                .replace("{personId}", contact.id.toString())
                                        )
                                    }
                                    3 -> {
                                        // 公司黄页 -> 公司详情
                                        navController.navigate(
                                            AppRoutes.COMPANY_DETAIL
                                                .replace("{companyId}", contact.id.toString())
                                        )
                                    }
                                }
                            }
                        )
                        Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                    }
                }
                
                // 使用LetterIndex组件
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                        .padding(end = 2.dp)
                ) {
                    LetterIndex(
                        selectedLetter = selectedLetter,
                        onLetterSelected = { letter ->
                            selectedLetter = letter
                            // 滚动到对应字母的第一个联系人
                            letterToIndexMap[letter]?.let { index ->
                                coroutineScope.launch {
                                    currentListState.animateScrollToItem(index)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TabItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
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
    isCompanyTab: Boolean = false,
    isAdded: Boolean = false,
    onAddFriend: () -> Unit = {},
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 头像
            Box(
                modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            // 如果没有头像图片就显示首字母
            val firstLetter = friend.name.firstOrNull()?.toString() ?: ""
            Text(
                text = firstLetter,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        // 名称和状态
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = friend.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = friend.status,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        
        // 公司黄页中显示添加好友按钮
        if (isCompanyTab) {
            if (isAdded) {
                // 已添加
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .background(
                            color = Color.LightGray,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "已添加",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            } else {
                // 添加好友按钮
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .background(
                            color = Color(0xFF2196F3),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable(onClick = onAddFriend)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "加为好友",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
