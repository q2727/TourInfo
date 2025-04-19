package com.example.travalms.ui.message

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.travalms.data.remote.toBareJidOrNull
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travalms.data.model.ChatMessage
import com.example.travalms.ui.viewmodels.ChatViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberDismissState
import androidx.compose.material.Card
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.draw.scale
import com.example.travalms.utils.PinyinUtils

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
    onProfileClick: () -> Unit,
    chatViewModel: ChatViewModel = viewModel() // 添加ViewModel
) {
    // 状态管理
    val selectedTabState by chatViewModel.selectedMessageTab.collectAsState()
    var selectedTab by remember { mutableStateOf(selectedTabState) } // 使用ViewModel状态初始化
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
    
    // 获取消息会话 - 确保在刷新状态函数前获取
    val recentSessions by chatViewModel.recentSessions.collectAsState()
    
    // 获取联系人状态映射
    val contactsStatusMap by chatViewModel.contactsStatusMap.collectAsState()
    
    // 获取当前用户JID
    val currentUserJid = getCurrentUserJid()
    val currentUserLocalPart = remember(currentUserJid) {
        if (currentUserJid.contains("@")) {
            currentUserJid.substringBefore("@")
        } else {
            currentUserJid
        }
    }

    // 将会话转换为ContactItem以便显示 - 添加过滤逻辑
    val messages = recentSessions
        .filter { session ->
            // 1. 过滤掉空会话
            if (session.targetId.isEmpty() || session.targetName.isEmpty()) {
                false
            } 
            // 2. 过滤掉与自己的聊天 - 修复：使用精确匹配而非contains
            else if (session.targetId == currentUserJid || 
                    (session.targetId.contains("@") && session.targetId.substringBefore("@") == currentUserLocalPart)) {
                Log.d("MessageScreen", "过滤掉与自己的聊天: ${session.targetId}")
                false
            }
            // 3. 保留其他有效会话
            else {
                true
            }
        }
        .map { session ->
            // 获取此联系人的在线状态
            val jid = session.targetId.toBareJidOrNull()
            val statusFromMap = if (jid != null && contactsStatusMap.containsKey(jid.toString())) {
                contactsStatusMap[jid.toString()]
            } else {
                null
            }
            
            // 决定显示的状态文本：如果有消息，显示消息内容；否则显示状态
            val displayStatus = if (session.lastMessage != null) {
                formatLastMessage(session.lastMessage)
            } else {
                statusFromMap ?: "离线"
            }
            
            ContactItem(
                id = session.targetId.hashCode(),
                name = session.targetName,
                status = displayStatus,
                jid = jid,  // 存储原始的targetId(JID)
                lastMessage = session.lastMessage,
                unreadCount = session.unreadCount,
                originalId = session.targetId  // 增加一个字段保存原始ID
            )
        }
    
    // 刷新好友在线状态的函数 - 放在messages变量定义之后，避免引用错误
    fun refreshFriendsStatus() {
        scope.launch {
            try {
                Log.d("MessageScreen", "开始刷新好友在线状态")
                // 使用XMPPManager的requestAllContactsPresence()主动请求状态更新
                XMPPManager.getInstance().requestAllContactsPresence()
                
                // 不再调用getFriendsPresence()方法，而是让服务器推送到XMPPManager
                // 服务器会通过XMPPManager的presence监听器接收状态并广播给ChatViewModel
                // 视图界面会自动通过ChatViewModel的contactsStatusMap获取最新状态
                
                Log.d("MessageScreen", "已请求好友状态更新")
            } catch (e: Exception) {
                Log.e("MessageScreen", "刷新好友状态异常", e)
            }
        }
    }
    
    // 添加周期性刷新在线状态逻辑
    LaunchedEffect(Unit) {
        // 立即刷新一次状态
        refreshFriendsStatus()
        
        // 启动独立的状态更新协程，不依赖于选项卡状态
        while (isActive) {
            delay(5000) // 每5秒刷新一次
            refreshFriendsStatus()
        }
    }

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
                                status = "离线", // 设置默认状态为离线
                                jid = jid,  // 确保JID字段正确设置
                                originalId = jid.toString() // 添加原始ID字段存储完整JID字符串
                            )
                        }.sortedBy { item ->
                            // 按照拼音首字母排序
                            extractFirstLetter(item.name) ?: "?"
                        }
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

                    // 获取好友在线状态
                    val presenceResult = XMPPManager.getInstance().getFriendsPresence()
                    val statusMap = if (presenceResult.isSuccess) {
                        val resultMap = presenceResult.getOrDefault(emptyMap())
                        // 打印所有状态值，用于调试
                        Log.d("MessageScreen", "好友在线状态详情: ${resultMap.entries.joinToString { "${it.key}=${it.value}" }}")
                        // 也更新ViewModel中的状态映射
                        chatViewModel.updateContactsStatus(resultMap.map { (jid, status) ->
                            ContactItem(
                                id = jid.hashCode(),
                                name = jid.toString(),
                                status = status,
                                jid = jid,
                                originalId = jid.toString()
                            )
                        })
                        resultMap
                    } else {
                        Log.e("MessageScreen", "获取好友在线状态失败: ${presenceResult.exceptionOrNull()?.message}")
                        emptyMap()
                    }

                    // 转换为ContactItem，并更新为真实状态
                    val friendItems = fetchedFriends.mapIndexed { index, pair ->
                        val jid = pair.first
                        val name = pair.second ?: jid?.localpartOrNull?.toString() ?: jid?.toString() ?: "未知用户"
                        
                        // 获取此好友的在线状态
                        val status = if (jid != null && statusMap.containsKey(jid)) {
                            val rawStatus = statusMap[jid]
                            Log.d("MessageScreen", "获取到好友[$name]的原始状态: $rawStatus")
                            rawStatus ?: "未知状态"
                        } else {
                            Log.d("MessageScreen", "好友[$name]在状态Map中未找到，设置为离线")
                            "离线" // 默认为离线
                        }

                        ContactItem(
                            id = jid?.hashCode()?.plus(index) ?: index,
                            name = name,
                            status = status,
                            jid = jid
                        )
                    }.sortedBy { item -> 
                        // 按照拼音首字母排序
                        extractFirstLetter(item.name) ?: "?"
                    }

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
        extractFirstLetter(contact.name)
    }.distinct().sorted()

    // 确保所有字母都显示
    val indexLetters = (contactLetters + allLetters).distinct().sorted()

    // 创建字母到索引的映射
    val letterToIndexMap = remember(currentList) {
        val map = mutableMapOf<String, Int>()
        currentList.forEachIndexed { index, contact ->
            val firstLetter = extractFirstLetter(contact.name)
            
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

    // 在选择联系人时导航到聊天室
    fun onContactSelected(contact: ContactItem) {
        when (selectedTab) {
            0 -> {
                // 全部消息选项卡导航到聊天室
                val sessionId = contact.originalId ?: contact.id.toString()
                navController.navigate(
                    AppRoutes.CHAT_ROOM
                        .replace("{sessionId}", sessionId)
                        .replace("{targetName}", contact.name)
                        .replace("{targetType}", "message")
                )
            }
            1 -> {
                // 如果有JID，导航到聊天界面
                if (contact.jid != null) {
                    navController.navigate(
                        AppRoutes.CHAT_ROOM
                            .replace("{sessionId}", contact.jid.toString())
                            .replace("{targetName}", contact.name)
                            .replace("{targetType}", "message")
                    )
                }
            }
            2 -> {
                // 群聊选项卡
                val targetId = contact.jid?.toString() ?: contact.id.toString()
                navController.navigate(
                    AppRoutes.CHAT_ROOM
                        .replace("{sessionId}", targetId)
                        .replace("{targetName}", contact.name)
                        .replace("{targetType}", "group")
                )
            }
            3 -> {
                // 公司黄页选项卡 - 使用JID而不是ID
                val targetType = "message" // 修改为message类型以确保一致性
                
                // 从status中获取JID（在ContactItem映射时我们将JID存储在status中）
                val sessionId = if (contact.jid != null) {
                    contact.jid.toString()
                } else if (contact.status.contains("@")) {
                    // 备选方案：如果JID字段为空但status包含完整JID
                    contact.status
                } else {
                    // 回退到ID（不应该发生）
                    contact.id.toString()
                }
                
                Log.d("MessageScreen", "从公司黄页导航: 使用sessionId=$sessionId, targetName=${contact.name}")
                
                navController.navigate(
                    AppRoutes.CHAT_ROOM
                        .replace("{sessionId}", sessionId)
                        .replace("{targetName}", contact.name)
                        .replace("{targetType}", targetType)
                )
            }
        }
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

    // 当选项卡变化时更新ViewModel
    LaunchedEffect(selectedTab) {
        if (selectedTab != selectedTabState) {
            chatViewModel.updateSelectedMessageTab(selectedTab)
        }
    }

    // 当ViewModel状态变化时更新UI
    LaunchedEffect(selectedTabState) {
        if (selectedTab != selectedTabState) {
            selectedTab = selectedTabState
        }
    }

    // 在界面初始时确保同步选项卡状态
    LaunchedEffect(Unit) {
        // 从ViewModel获取保存的选项卡状态，这确保了应用返回后选项卡状态可以恢复
        selectedTab = selectedTabState
        
        // 根据选项卡状态加载相应数据
        when (selectedTab) {
            1 -> if (realFriendsList.isEmpty()) {
                delay(300)
                loadFriendsList()
            }
            3 -> if (companyDirectoryUsers.isEmpty()) {
                delay(300)
                loadDirectoryUsers()
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
                    selected = false,
                    onClick = onTailListClick,
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = Color.Gray
                )

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Email, contentDescription = "消息") },
                    label = { Text("消息", fontSize = 12.sp) },
                    selected = true,
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
                            // 使用左滑删除组件包装ContactListItem
                            if (selectedTab == 0) { // 只在消息列表中启用左滑删除
                                var showDeleteConfirmDialog by remember { mutableStateOf(false) }
                                
                                // 删除确认对话框
                                if (showDeleteConfirmDialog) {
                                    AlertDialog(
                                        onDismissRequest = { showDeleteConfirmDialog = false },
                                        title = { Text("确认删除") },
                                        text = { Text("确定要删除与 ${contact.name} 的聊天记录吗？\n此操作无法撤销。") },
                                        confirmButton = {
                                            Button(
                                                onClick = {
                                                    val sessionId = contact.originalId ?: contact.jid?.toString() ?: return@Button
                                                    chatViewModel.clearSessionHistory(sessionId)
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar("已删除与 ${contact.name} 的聊天记录")
                                                    }
                                                    showDeleteConfirmDialog = false
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                            ) {
                                                Text("删除")
                                            }
                                        },
                                        dismissButton = {
                                            TextButton(onClick = { showDeleteConfirmDialog = false }) {
                                                Text("取消")
                                            }
                                        }
                                    )
                                }
                                
                                SwipeToDeleteContactItem(
                                    contact = contact,
                                    onDelete = {
                                        // 显示确认对话框
                                        showDeleteConfirmDialog = true
                                    },
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
                                    onClick = { onContactSelected(contact) }
                                )
                            } else {
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
                                    onClick = { onContactSelected(contact) }
                                )
                            }
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
    // 获取当前选中的选项卡
    // 在MessageScreen中作为参数传入
    val isGroupTab = friend.status.contains("人在线") // 用于判断是否为群聊项

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
            
            // 移除头像上的状态指示器
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

            // 状态文本 - 带有颜色指示点
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 状态指示点 - 显示在所有类型的列表中
                val statusColor = when {
                    friend.status.equals("在线", ignoreCase = true) -> Color.Green
                    friend.status.equals("available", ignoreCase = true) -> Color.Green
                    friend.status.startsWith("在线") -> Color.Green
                    friend.status.contains("available") -> Color.Green
                    friend.status.contains("忙碌") || friend.status.contains("busy") -> Color(0xFFFFA500) // 橙色表示忙碌
                    friend.status.contains("离开") || friend.status.contains("away") -> Color(0xFFFFFF00) // 黄色表示离开
                    friend.status.contains("dnd") -> Color(0xFFFFA500) // 勿扰也是橙色
                    friend.status.contains("离线") || friend.status.contains("offline") || friend.status.equals("unavailable", ignoreCase = true) -> Color.Gray
                    else -> Color.Gray // 默认灰色表示离线
                }
                
                // 状态指示点始终显示
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                )
                
                Spacer(modifier = Modifier.width(4.dp))
                
                // 获取合适的状态文本，区分消息内容和状态
                val displayText = if (friend.lastMessage != null) {
                    // 消息列表项：显示最后一条消息内容，而不是状态
                    formatLastMessage(friend.lastMessage)
                } else if (isCompanyTab) {
                    // 公司黄页：简化状态显示
                    jidToUsername(friend.jid?.toString() ?: "")
                } else {
                    // 好友列表：显示状态文本
                    friend.status
                }
                
                Text(
                    text = displayText,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
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

// 添加工具函数
private fun formatLastMessage(message: ChatMessage?): String {
    if (message == null) return ""
    return "${message.content} · ${formatMessageTime(message.timestamp)}"
}

/**
 * 从名称中提取首字母，支持中文拼音
 * 
 * 优先级:
 * 1. 如果名称包含括号，从括号中提取首字母（如 "陈明 (Chen Ming)" 提取 "C"）
 * 2. 如果名称以中文开头，根据拼音提取首字母（如 "曾小美" 提取 "Z"）
 * 3. 其他情况直接提取名称首字母的大写形式
 */
private fun extractFirstLetter(name: String): String? {
    if (name.isEmpty()) return null
    
    // 1. 如果名称包含括号，从括号中提取首字母
    if (name.contains('(') && name.contains(')')) {
        val pinyinPart = name.substringAfter('(').substringBefore(')')
        if (pinyinPart.isNotEmpty()) {
            return pinyinPart.first().uppercase()
        }
    }
    
    // 2. 如果名称以中文开头，通过拼音转换获取首字母
    if (PinyinUtils.isChineseChar(name[0])) {
        return PinyinUtils.getFirstLetter(name)
    }
    
    // 3. 其他情况直接返回首字母的大写形式
    return name.first().uppercase()
}

private fun formatMessageTime(time: LocalDateTime): String {
    val now = LocalDateTime.now()
    return when {
        time.toLocalDate().isEqual(now.toLocalDate()) -> {
            // 今天，显示时间
            time.format(DateTimeFormatter.ofPattern("HH:mm"))
        }
        time.toLocalDate().isEqual(now.minusDays(1).toLocalDate()) -> {
            // 昨天
            "昨天"
        }
        time.year == now.year -> {
            // 今年，显示月日
            time.format(DateTimeFormatter.ofPattern("MM-dd"))
        }
        else -> {
            // 不是今年，显示年月日
            time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
    }
}

private fun getCurrentUserJid(): String {
    return XMPPManager.getInstance().currentConnection?.user?.asEntityBareJidString() ?: ""
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToDeleteContactItem(
    contact: ContactItem,
    onDelete: () -> Unit,
    isCompanyTab: Boolean = false,
    isAdded: Boolean = false,
    onAddFriend: () -> Unit = {},
    onClick: () -> Unit
) {
    // 创建滑动状态
    val dismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            if (dismissValue == DismissValue.DismissedToStart) {
                // 左滑显示删除按钮
                onDelete()
                // 不自动关闭，待用户确认后手动关闭
                false
            } else {
                false
            }
        }
    )

    // 计算滑动过程中的动画效果
    val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
    val isDragged = dismissState.offset.value != 0f
    val deleteIconScale = animateFloatAsState(
        targetValue = if (isDragged) 1.3f else 1.0f,
        label = "deleteIconScale"
    )

    SwipeToDismiss(
        state = dismissState,
        // 设置仅响应从左向右滑动
        directions = setOf(DismissDirection.EndToStart),
        // 设置滑动阈值
        dismissThresholds = { direction -> FractionalThreshold(0.3f) },
        // 滑动背景，显示删除按钮
        background = {
            val color = Color.Red
            val alignment = Alignment.CenterEnd

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.scale(deleteIconScale.value)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "删除",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "删除",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        },
        // 实际的联系人项目
        dismissContent = {
            // 使用Card保证背景是白色，防止滑动时看到底部红色背景
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = 0.dp,
                backgroundColor = Color.White,
                // 消除圆角
                shape = RoundedCornerShape(0.dp)
            ) {
                ContactListItem(
                    friend = contact,
                    isCompanyTab = isCompanyTab,
                    isAdded = isAdded,
                    onAddFriend = onAddFriend,
                    onClick = onClick
                )
            }
        }
    )
}

// 从JID中提取用户名部分
private fun jidToUsername(jid: String): String {
    return if (jid.contains("@")) {
        jid.substringBefore("@")
    } else {
        jid
    }
}
