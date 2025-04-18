package com.example.travalms.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.travalms.data.model.ChatMessage
import com.example.travalms.data.model.ChatSession
import com.example.travalms.data.model.NotificationMessage
import com.example.travalms.ui.theme.BackgroundColor
import com.example.travalms.ui.theme.PrimaryColor
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageListScreen(
    onSessionClick: (String) -> Unit,
    onNotificationClick: (String) -> Unit,
    onHomeClick: () -> Unit,
    onPublishClick: () -> Unit,
    onProfileClick: () -> Unit,
    onTailListClick: () -> Unit,
    navController: NavController
) {
    // 在实际应用中，应该从数据源获取会话列表
    val chatSessions = remember {
        listOf(
            ChatSession(
                id = "1",
                targetId = "company1",
                targetName = "上海旅行社",
                targetType = "company",
                lastMessage = ChatMessage(
                    id = "msg1",
                    senderId = "company1",
                    senderName = "上海旅行社",
                    content = "行程是5天4晚，住宿都是当地四星级酒店，标准双人间。如果您需要详细行程单，我可以发给您。",
                    timestamp = LocalDateTime.now().minusMinutes(20),
                    isRead = true
                ),
                unreadCount = 0
            ),
            ChatSession(
                id = "2",
                targetId = "person1",
                targetName = "曾圆圆",
                targetType = "person",
                lastMessage = ChatMessage(
                    id = "msg2",
                    senderId = "person1",
                    senderName = "曾圆圆",
                    content = "我想问一下这个价格是否包含接送机服务？",
                    timestamp = LocalDateTime.now().minusHours(2),
                    isRead = false
                ),
                unreadCount = 1
            ),
            ChatSession(
                id = "3",
                targetId = "company2",
                targetName = "北京导游协会",
                targetType = "company",
                lastMessage = ChatMessage(
                    id = "msg3",
                    senderId = "user1",
                    senderName = "我",
                    content = "好的，谢谢，我会考虑的。",
                    timestamp = LocalDateTime.now().minusDays(1),
                    isRead = true
                ),
                unreadCount = 0
            )
        )
    }

    // 添加订阅的推送消息
    val notifications = remember {
        listOf(
            NotificationMessage(
                id = "n1",
                title = "桂林山水游推荐",
                content = "桂林山水甲天下，阳朔风光美如画。现推出特惠5日游，含漓江精华段游览。",
                timestamp = LocalDateTime.now().minusDays(1),
                isRead = false,
                relatedPostId = "post101"
            ),
            NotificationMessage(
                id = "n2",
                title = "西安历史文化之旅",
                content = "探索中国古都西安，感受千年历史文化。兵马俑、古城墙、大雁塔等景点等你来体验。",
                timestamp = LocalDateTime.now().minusDays(2),
                isRead = true,
                relatedPostId = "post102"
            ),
            NotificationMessage(
                id = "n3",
                title = "云南丽江古城旅游攻略",
                content = "丽江古城被誉为中国最美丽的古城之一，纳西族文化、玉龙雪山、泸沽湖等绝美景点推荐。",
                timestamp = LocalDateTime.now().minusDays(3),
                isRead = false,
                relatedPostId = "post103"
            )
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { androidx.compose.material3.Text("消息", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PrimaryColor,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.SUBSCRIBE_SETTING) }) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "订阅设置",
                            tint = Color.White
                        )
                    }
                }
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
                    selected = false,
                    onClick = onTailListClick,
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = Color.Gray
                )

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Email, contentDescription = "消息") },
                    label = { Text("消息", fontSize = 12.sp) },
                    selected = true,
                    onClick = { /* 当前已在消息页面 */ },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundColor)
        ) {
            item {
                androidx.compose.material3.Text(
                    text = "聊天",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            items(chatSessions) { session ->
                ChatSessionItem(
                    session = session,
                    onClick = { onSessionClick(session.id) }
                )
                Divider(color = Color.LightGray.copy(alpha = 0.5f))
            }

            item {
                androidx.compose.material3.Text(
                    text = "推送",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            items(notifications) { notification ->
                NotificationItem(
                    notification = notification,
                    onClick = { onNotificationClick(notification.relatedPostId) }
                )
                Divider(color = Color.LightGray.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
fun ChatSessionItem(
    session: ChatSession,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 头像 - 使用系统主题色
        val avatarBackgroundColor = when (session.targetType) {
            "person" -> PrimaryColor.copy(alpha = 0.7f)  // 使用系统主题色
            "company" -> PrimaryColor.copy(alpha = 0.7f) // 旅行社也使用系统主题色
            else -> PrimaryColor.copy(alpha = 0.7f)      // 默认也使用系统主题色
        }

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(avatarBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.material3.Text(
                text = session.targetName.first().toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.material3.Text(
                    text = session.targetName,
                    fontWeight = if (session.unreadCount > 0) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 16.sp
                )

                session.lastMessage?.let { lastMessage ->
                    androidx.compose.material3.Text(
                        text = formatTime(lastMessage.timestamp),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                session.lastMessage?.let { lastMessage ->
                    androidx.compose.material3.Text(
                        text = lastMessage.content,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (session.unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color.Red),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.material3.Text(
                            text = session.unreadCount.toString(),
                            color = Color.White,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: NotificationMessage,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 图标
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.Notifications,
                contentDescription = "推送",
                tint = PrimaryColor
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.material3.Text(
                    text = notification.title,
                    fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 16.sp
                )

                androidx.compose.material3.Text(
                    text = formatTime(notification.timestamp),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.material3.Text(
                    text = notification.content,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (!notification.isRead) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.Red)
                    ) {
                        // 空的Box，只是显示一个红点
                    }
                }
            }
        }
    }
}

fun formatTime(timestamp: LocalDateTime): String {
    val now = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    return when {
        ChronoUnit.DAYS.between(timestamp, now) == 0L -> {
            // 今天
            timestamp.format(formatter)
        }
        ChronoUnit.DAYS.between(timestamp, now) == 1L -> {
            // 昨天
            "昨天"
        }
        ChronoUnit.DAYS.between(timestamp, now) < 7L -> {
            // 一周内
            when (timestamp.dayOfWeek.value) {
                1 -> "周一"
                2 -> "周二"
                3 -> "周三"
                4 -> "周四"
                5 -> "周五"
                6 -> "周六"
                7 -> "周日"
                else -> ""
            }
        }
        else -> {
            // 更早
            timestamp.format(DateTimeFormatter.ofPattern("MM-dd"))
        }
    }
} 