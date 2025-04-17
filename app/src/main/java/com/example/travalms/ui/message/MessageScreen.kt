package com.example.travalms.ui.message

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.travalms.data.model.ContactItem
import com.example.travalms.data.remote.XMPPManager
import com.example.travalms.ui.components.LetterIndex
import com.example.travalms.ui.navigation.AppRoutes
import com.example.travalms.ui.theme.PrimaryColor
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.jxmpp.jid.BareJid
// 添加下拉刷新相关导入
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import com.example.travalms.ui.screens.BottomNavigation
import com.example.travalms.ui.screens.BottomNavigationItem

// 添加自定义BottomNavigation导入，与HomeScreen保持一致

/**
 * 消息列表屏幕
 * 
 * 注：此实现使用了实验性API，添加注解以抑制警告
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(
    navController: NavController,
    onHomeClick: () -> Unit,
    onPublishClick: () -> Unit,
    onTailListClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    // 状态管理
    var selectedTab by remember { mutableStateOf(1) } // 默认选中"好友"选项卡
    var searchText by remember { mutableStateOf("") }

    // State for Add Friend Dialog
    var showAddFriendDialog by remember { mutableStateOf(false) }
    var friendJidInput by remember { mutableStateOf("") }
    var addFriendStatus by remember { mutableStateOf<String?>(null) } // To show feedback

    // Snackbar state for feedback
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 字母索引状态
    var selectedLetter by remember { mutableStateOf<String?>(null) }

    // 创建LazyListState用于控制滚动
    val allMessagesListState = rememberLazyListState()
    val friendsListState = rememberLazyListState()
    val groupChatsListState = rememberLazyListState()
    val companyDirectoryListState = rememberLazyListState()

    // State for fetched friends and directory users
    var friendsJidSet by remember { mutableStateOf<Set<BareJid>>(emptySet()) }
    var companyDirectoryUsers by remember { mutableStateOf<List<ContactItem>>(emptyList()) }
    var directoryLoadingState by remember { mutableStateOf<String?>(null) } // null=idle, "Loading...", "Error..."

    // 新增：真实好友列表和加载状态
    var realFriendsList by remember { mutableStateOf<List<ContactItem>>(emptyList()) }
    var friendsLoadingState by remember { mutableStateOf<String?>(null) }

    // Function to load directory users
    fun loadDirectoryUsers() {
        // 先清空之前的用户列表
        directoryLoadingState = "正在加载用户列表，请稍候..."

        scope.launch {
            try {
                // Fetch friends
                val friendsResult = XMPPManager.getInstance().getFriendsJids()
                if (friendsResult.isSuccess) {
                    friendsJidSet = friendsResult.getOrDefault(emptySet())
                    Log.d("MessageScreen", "成功获取 ${friendsJidSet.size} 个好友")
                } else {
                    Log.e("MessageScreen", "获取好友列表失败", friendsResult.exceptionOrNull())
                    snackbarHostState.showSnackbar("获取好友列表失败")
                }
            } catch (e: Exception) {
                Log.e("MessageScreen", "获取好友列表过程中发生异常", e)
            }

            try {
                // Fetch all users
                Log.d("MessageScreen", "[A] 开始获取服务器用户列表")
                val usersResult = XMPPManager.getInstance().getServerUsers()
                Log.d("MessageScreen", "[B] 获取服务器用户列表调用完成, isSuccess=${usersResult.isSuccess}")

                if (usersResult.isSuccess) {
                    val fetchedUsers = usersResult.getOrDefault(emptyList())
                    Log.d("MessageScreen", "[C] 成功获取 ${fetchedUsers.size} 个用户")
                    Log.d("MessageScreen", "用户数据预览: ${fetchedUsers.take(3).joinToString { "${it.first}:${it.second}" }}...")

                    // 添加内部 try-catch 来捕获映射或状态更新中的错误
                    try {
                        // 首先确保协程仍然活跃
                        if (!isActive) {
                            Log.w("MessageScreen", "协程在映射用户数据前已不再活跃，中止更新")
                            directoryLoadingState = "操作已取消"
                            return@launch
                        }

                        Log.d("MessageScreen", "[D] 开始映射用户数据到 ContactItem")
                        val mappedUsers = fetchedUsers.mapIndexed { index, (jid, nickname) ->
                            val localPart = jid.localpartOrNull?.toString() ?: ""
                            val name = nickname ?: localPart.takeIf { it.isNotEmpty() } ?: jid.toString()

                            if (index < 3 || index >= fetchedUsers.size - 3) {
                                Log.d("MessageScreen", "  映射用户 $index: jid=$jid, nickname=$nickname, 最终name=$name")
                            }

                            ContactItem(
                                id = jid.hashCode() + index,
                                name = name,
                                status = jid.toString(), // Store JID in status
                                jid = jid
                            )
                        }.sortedBy { it.name }
                        Log.d("MessageScreen", "[E] 用户映射完成，共 ${mappedUsers.size} 个 ContactItem")

                        // 再次确保协程仍然活跃
                        if (!isActive) {
                            Log.w("MessageScreen", "协程在更新状态前已不再活跃，中止更新")
                            directoryLoadingState = "操作已取消"
                            return@launch
                        }

                        // 更新状态 - 重要修复：先将列表更新，然后清除加载状态
                        Log.d("MessageScreen", "[F] 开始更新 UI 状态")

                        // 先更新列表
                        companyDirectoryUsers = mappedUsers
                        Log.d("MessageScreen", "[G] 成功更新用户列表，${companyDirectoryUsers.size} 个用户")

                        // 关键修复：确保清除加载状态
                        delay(300) // 给UI一些时间更新列表
                        directoryLoadingState = null
                        Log.d("MessageScreen", "[H] 成功更新加载状态，设置为: null")
                        Log.d("MessageScreen", "[I] 完整流程成功，已加载 ${companyDirectoryUsers.size} 个用户，加载状态为null")

                    } catch (t: Throwable) {
                        if (t is CancellationException) {
                            Log.w("MessageScreen", "用户数据处理协程被取消", t)
                            directoryLoadingState = "操作已取消"
                        } else {
                            Log.e("MessageScreen", "处理用户数据时出错", t)
                            Log.e("MessageScreen", "错误堆栈: ${t.stackTraceToString()}")
                            directoryLoadingState = "处理用户数据失败: ${t.message}"
                        }
                    }
                } else {
                    val errorEx = usersResult.exceptionOrNull()
                    Log.e("MessageScreen", "[ERR] 获取用户列表失败: ${errorEx?.message}", errorEx)
                    directoryLoadingState = "加载用户列表失败: ${errorEx?.message ?: "未知错误"}"
                }
            } catch (e: Exception) {
                Log.e("MessageScreen", "获取服务器用户列表过程中发生异常", e)
                Log.e("MessageScreen", "错误堆栈: ${e.stackTraceToString()}")
                directoryLoadingState = "加载用户列表时发生错误: ${e.message}"
            }
        }
    }

    // Function to load real friends list
    fun loadFriendsList() {
        friendsLoadingState = "正在加载好友列表..."

        scope.launch {
            try {
                Log.d("MessageScreen", "开始加载好友列表")
                val friendsResult = XMPPManager.getInstance().getFriends()

                if (friendsResult.isSuccess) {
                    val fetchedFriends = friendsResult.getOrDefault(emptyList())
                    Log.d("MessageScreen", "成功获取 ${fetchedFriends.size} 个好友")

                    // 更新好友JID集合用于公司黄页的"已添加"状态
                    friendsJidSet = fetchedFriends.mapNotNull { pair -> pair.first }.toSet()

                    // 转换为ContactItem
                    val friendItems = fetchedFriends.mapIndexed { index, pair ->
                        val jid = pair.first
                        val name = pair.second ?: jid?.localpartOrNull?.toString() ?: jid?.toString() ?: "未知用户"
                        ContactItem(
                            id = jid?.hashCode()?.plus(index) ?: index,
                            name = name,
                            status = "在线", // 默认状态，实际应该从presence信息获取
                            jid = jid
                        )
                    }.sortedBy { item -> item.name }

                    realFriendsList = friendItems
                    Log.d("MessageScreen", "好友列表加载完成，共 ${realFriendsList.size} 个好友")

                    // 延迟清除加载状态，确保UI有时间更新
                    delay(300)
                    friendsLoadingState = if (realFriendsList.isEmpty()) "暂无好友" else null

                } else {
                    val errorEx = friendsResult.exceptionOrNull()
                    Log.e("MessageScreen", "获取好友列表失败: ${errorEx?.message}", errorEx)
                    friendsLoadingState = "加载好友列表失败: ${errorEx?.message ?: "未知错误"}"
                }
            } catch (e: Exception) {
                Log.e("MessageScreen", "获取好友列表过程中发生异常", e)
                Log.e("MessageScreen", "错误堆栈: ${e.stackTraceToString()}")
                friendsLoadingState = "加载好友列表时发生错误: ${e.message}"
            }
        }
    }

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
        1 -> realFriendsList // 使用真实好友列表
        2 -> groups
        3 -> companyDirectoryUsers // Use fetched directory users
        else -> messages
    }

    // 获取当前活动的LazyListState
    val currentListState = when (selectedTab) {
        0 -> allMessagesListState
        1 -> friendsListState
        2 -> groupChatsListState
        3 -> companyDirectoryListState // Use dedicated state for directory
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
    val filteredList = remember(searchText, currentList, selectedTab) {
        // 添加更多日志来诊断问题
        Log.d("MessageScreen", "重新计算filteredList: selectedTab=$selectedTab, currentList大小=${currentList.size}, searchText='$searchText'")

        // 公司黄页显示日志
        if (selectedTab == 3) {
            Log.d("MessageScreen", "公司黄页Tab: 总计${companyDirectoryUsers.size}个用户, 当前列表${currentList.size}个项目")
        }

        if (searchText.isBlank()) {
            // 特殊情况处理：公司黄页，确保始终使用最新的用户列表
            if (selectedTab == 3) {
                val result = companyDirectoryUsers
                Log.d("MessageScreen", "公司黄页: 直接显示所有 ${result.size} 个用户")
                if (result.isNotEmpty()) {
                    Log.d("MessageScreen", "  前3个用户: ${result.take(3).map { it.name }}...")
                }
                result
            } else {
                currentList
            }
        } else {
            val filtered = if (selectedTab == 3) {
                companyDirectoryUsers.filter { it.name.contains(searchText, ignoreCase = true) }
            } else {
                currentList.filter { it.name.contains(searchText, ignoreCase = true) }
            }
            Log.d("MessageScreen", "搜索结果: 找到 ${filtered.size} 个匹配 '$searchText' 的项")
            filtered
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

    // Fetch friends and directory users when tab is selected
    LaunchedEffect(selectedTab) {
        if (selectedTab == 1) {
            // 切换到好友标签时
            Log.d("MessageScreen", "切换到好友标签，当前状态：realFriendsList.size=${realFriendsList.size}, friendsLoadingState=$friendsLoadingState")

            // 清除卡住的加载状态
            if (friendsLoadingState?.startsWith("正在加载") == true) {
                Log.d("MessageScreen", "清除卡住的加载状态: $friendsLoadingState -> null")
                friendsLoadingState = null
            }

            // 如果列表为空才加载
            if (realFriendsList.isEmpty()) {
                Log.d("MessageScreen", "好友列表为空，开始加载")
                delay(300)  // 给UI一些时间渲染
                loadFriendsList()
            } else {
                Log.d("MessageScreen", "已有 ${realFriendsList.size} 个好友，无需重新加载")
            }
        }
        else if (selectedTab == 3) {
            Log.d("MessageScreen", "切换到公司黄页标签，当前状态：companyDirectoryUsers.size=${companyDirectoryUsers.size}, directoryLoadingState=$directoryLoadingState")

            // 切换到公司黄页标签时，重置搜索文本
            searchText = ""

            // 关键修复：始终重置加载状态，防止卡在"正在加载"
            if (directoryLoadingState?.contains("正在加载") == true) {
                Log.d("MessageScreen", "清除卡住的加载状态: $directoryLoadingState -> null")
                directoryLoadingState = null
            }

            // 如果当前列表为空，重新加载
            if (companyDirectoryUsers.isEmpty()) {
                Log.d("MessageScreen", "公司黄页用户列表为空，开始加载")
                // 设置一个延迟，确保UI先完成渲染再开始加载
                delay(300)
                loadDirectoryUsers()
            } else {
                Log.d("MessageScreen", "已有 ${companyDirectoryUsers.size} 个用户，无需重新加载")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(screenTitle, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryColor,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    // 移除这里的刷新按钮
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        bottomBar = {
            BottomNavigation(
                backgroundColor = Color.White,
                modifier = Modifier.height(56.dp)
            ) {
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "产品") },
                    label = { Text("产品", fontSize = 12.sp) },
                    selected = true,
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
                    selected = false,
                    onClick = onTailListClick,
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = Color.Gray
                )

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Email, contentDescription = "消息") },
                    label = { Text("消息", fontSize = 12.sp) },
                    selected = false,
                    onClick = {},
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
    ){ paddingValues ->
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
                val refreshing = remember { mutableStateOf(false) }
                val pullRefreshState = rememberPullRefreshState(
                    refreshing = refreshing.value,
                    onRefresh = { 
                        refreshing.value = true
                        // 执行刷新操作
                        if (selectedTab == 3) {
                            loadDirectoryUsers()
                        } else if (selectedTab == 1) {
                            loadFriendsList()
                        }
                        // 延迟后重置刷新状态
                        scope.launch {
                            delay(1000)
                            refreshing.value = false
                        }
                    }
                )
                
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pullRefresh(pullRefreshState)
                ) {
                    LazyColumn(
                        state = currentListState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Show loading or error state for directory
                        if (selectedTab == 3) {
                            // Add a header showing the number of users found
                            if (companyDirectoryUsers.isNotEmpty()) {
                                Log.d("MessageScreen", "渲染公司黄页: 找到 ${companyDirectoryUsers.size} 个用户")
                                item {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "找到 ${companyDirectoryUsers.size} 个用户",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    }
                                    Divider()
                                }
                            } else if (directoryLoadingState != null) {
                                // 仅当列表为空且有加载状态时，显示加载状态
                                Log.d("MessageScreen", "渲染公司黄页加载状态: $directoryLoadingState")
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            if (directoryLoadingState?.startsWith("正在加载") == true) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(24.dp),
                                                    strokeWidth = 2.dp
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                            }
                                            Text(directoryLoadingState ?: "")
                                        }
                                    }
                                }
                            }
                        }

                        // 好友标签页加载状态
                        if (selectedTab == 1) {
                            if (friendsLoadingState != null && realFriendsList.isEmpty()) {
                                // 仅当列表为空且有加载状态时，显示加载状态
                                Log.d("MessageScreen", "渲染好友列表加载状态: $friendsLoadingState")
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            if (friendsLoadingState?.startsWith("正在加载") == true) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(24.dp),
                                                    strokeWidth = 2.dp
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                            }
                                            Text(friendsLoadingState ?: "")
                                        }
                                    }
                                }
                            }
                        }

                        items(filteredList, key = { it.id }) { contact ->
                            ContactListItem(
                                friend = contact,
                                isCompanyTab = selectedTab == 3,
                                isAdded = if (selectedTab == 3 && contact.jid != null) {
                                    friendsJidSet.contains(contact.jid)
                                } else {
                                    false
                                },
                                onAddFriend = {
                                    if (contact.jid != null) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("正在发送请求给 ${contact.name}...")
                                            val result = XMPPManager.getInstance().sendFriendRequest(contact.jid.toString())
                                            if (result.isSuccess) {
                                                snackbarHostState.showSnackbar("好友请求已发送")
                                                val refreshResult = XMPPManager.getInstance().getFriendsJids()
                                                if (refreshResult.isSuccess) {
                                                    friendsJidSet = refreshResult.getOrDefault(emptySet())
                                                }
                                            } else {
                                                snackbarHostState.showSnackbar("发送请求失败: ${result.exceptionOrNull()?.message ?: "未知错误"}")
                                            }
                                        }
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("无法添加好友: JID 无效")
                                        }
                                    }
                                },
                                onClick = {
                                    when (selectedTab) {
                                        0, 2 -> {
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
                                            navController.navigate(
                                                AppRoutes.PERSON_DETAIL
                                                    .replace("{personId}", contact.id.toString())
                                            )
                                        }
                                        3 -> {
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
                                letterToIndexMap[letter]?.let { index ->
                                    scope.launch {
                                        currentListState.animateScrollToItem(index)
                                    }
                                }
                            }
                        )
                    }

                    // Add pull-to-refresh indicator
                    PullRefreshIndicator(
                        refreshing = refreshing.value,
                        state = pullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter),
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        contentColor = PrimaryColor
                    )
                }
            }
        }

        // Add Friend Dialog
        if (showAddFriendDialog) {
            AlertDialog(
                onDismissRequest = { showAddFriendDialog = false },
                title = { Text("添加好友 (通过 JID)") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = friendJidInput,
                            onValueChange = { friendJidInput = it },
                            label = { Text("好友 JID (例: user@domain)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        addFriendStatus?.let {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(it, color = if (it.startsWith("错误")) MaterialTheme.colorScheme.error else LocalContentColor.current)
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (!friendJidInput.contains('@')) {
                                addFriendStatus = "错误：JID 格式无效"
                                return@Button
                            }
                            addFriendStatus = "正在发送请求..."
                            scope.launch {
                                val result = XMPPManager.getInstance().sendFriendRequest(friendJidInput)
                                if (result.isSuccess) {
                                    addFriendStatus = null
                                    showAddFriendDialog = false
                                    snackbarHostState.showSnackbar("好友请求已发送至 $friendJidInput")
                                } else {
                                    val errorMsg = result.exceptionOrNull()?.message ?: "未知错误"
                                    addFriendStatus = "错误: $errorMsg"
                                }
                            }
                        }
                    ) {
                        Text("发送请求")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddFriendDialog = false }) {
                        Text("取消")
                    }
                }
            )
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
                text = if (isCompanyTab && friend.jid != null) friend.status else friend.status,
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
