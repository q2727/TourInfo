package com.example.travalms.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.travalms.data.model.GroupChatMessage
import com.example.travalms.data.model.GroupMember
import com.example.travalms.data.model.GroupRoom
import com.example.travalms.ui.viewmodels.GroupChatViewModel
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupChatScreen(navController: NavController, roomJid: String) {
    val groupChatViewModel: GroupChatViewModel = viewModel()
    val currentRoom by groupChatViewModel.currentRoom.collectAsState()
    val roomMessages = groupChatViewModel.getRoomMessages(roomJid)
    val roomMembers = groupChatViewModel.getRoomMembers(roomJid)
    val loading by groupChatViewModel.loading.collectAsState()
    val errorMessage by groupChatViewModel.errorMessage.collectAsState()
    
    var messageText by remember { mutableStateOf("") }
    val showMembersDialog = remember { mutableStateOf(false) }
    val showOptionsMenu = remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    
    // 时间格式化器
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("yyyy-MM-dd") }
    
    // 加载房间信息和消息
    LaunchedEffect(roomJid) {
        // 通过JID加载房间信息
        groupChatViewModel.joinRoom(roomJid, "用户")  // 这里可以设置默认昵称
        groupChatViewModel.loadRoomMembers(roomJid)
    }
    
    // 自动滚动到底部
    LaunchedEffect(roomMessages.size) {
        if (roomMessages.isNotEmpty()) {
            listState.animateScrollToItem(roomMessages.size - 1)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = currentRoom?.name ?: roomJid.substringBefore('@'),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${roomMembers.size} 名成员",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { showMembersDialog.value = true }) {
                        Icon(Icons.Filled.AccountBox, contentDescription = "成员")
                    }
                    IconButton(onClick = { showOptionsMenu.value = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "更多选项")
                    }
                    DropdownMenu(
                        expanded = showOptionsMenu.value,
                        onDismissRequest = { showOptionsMenu.value = false }
                    ) {
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = { Text("输入消息...") },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = {
                            if (messageText.isNotBlank()) {
                                groupChatViewModel.sendMessage(roomJid, messageText)
                                messageText = ""
                            }
                        },
                        enabled = messageText.isNotBlank()
                    ) {
                        Icon(Icons.Filled.Send, contentDescription = "发送")
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (loading && roomMessages.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (roomMessages.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "群聊开始，来说点什么吧！",
                        color = Color.Gray
                    )
                }
            } else {
                // 消息列表
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    var currentDate = ""
                    
                    items(roomMessages) { message ->
                        val messageDate = message.timestamp.format(dateFormatter)
                        
                        // 如果日期变了，显示日期分割线
                        if (messageDate != currentDate) {
                            currentDate = messageDate
                            
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = messageDate,
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    modifier = Modifier
                                        .background(
                                            color = Color.LightGray.copy(alpha = 0.3f),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }
                        }
                        
                        // 消息气泡
                        MessageBubble(
                            message = message,
                            timeFormatter = timeFormatter
                        )
                    }
                }
            }
            
            // 错误消息
            if (errorMessage != null) {
                AlertDialog(
                    onDismissRequest = { groupChatViewModel.clearError() },
                    title = { Text("提示") },
                    text = { Text(errorMessage ?: "未知错误") },
                    confirmButton = {
                        TextButton(onClick = { groupChatViewModel.clearError() }) {
                            Text("确定")
                        }
                    }
                )
            }
            
            // 成员列表对话框
            if (showMembersDialog.value) {
                GroupMembersDialog(
                    members = roomMembers,
                    onDismiss = { showMembersDialog.value = false },
                    onInvite = { userJid ->
                        groupChatViewModel.inviteUser(roomJid, userJid)
                        showMembersDialog.value = false
                    }
                )
            }
        }
    }
}

@Composable
fun MessageBubble(
    message: GroupChatMessage,
    timeFormatter: DateTimeFormatter
) {
    val isFromMe = message.isFromMe
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = if (isFromMe) Alignment.End else Alignment.Start
    ) {
        // 发送者名称（仅显示他人消息的名称）
        if (!isFromMe && message.messageType != GroupChatMessage.MessageType.SYSTEM) {
            Text(
                text = message.senderNickname,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
            )
        }
        
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = if (isFromMe) Arrangement.End else Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            // 系统消息居中显示
            if (message.messageType == GroupChatMessage.MessageType.SYSTEM) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = message.content,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .background(
                                color = Color.LightGray.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            } else {
                // 普通消息
                if (!isFromMe) {
                    // 头像
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("https://ui-avatars.com/api/?name=${message.senderNickname}&background=random")
                            .crossfade(true)
                            .build(),
                        contentDescription = "头像",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                }
                
                Column(
                    horizontalAlignment = if (isFromMe) Alignment.End else Alignment.Start
                ) {
                    // 消息气泡
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isFromMe) Color(0xFF0288D1) else Color.LightGray,
                                shape = RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = if (isFromMe) 16.dp else 4.dp,
                                    bottomEnd = if (isFromMe) 4.dp else 16.dp
                                )
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = message.content,
                            color = if (isFromMe) Color.White else Color.Black
                        )
                    }
                    
                    // 时间
                    Text(
                        text = message.timestamp.format(timeFormatter),
                        fontSize = 10.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 2.dp, start = 4.dp, end = 4.dp)
                    )
                }
                
                if (isFromMe) {
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // 自己的头像
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("https://ui-avatars.com/api/?name=我&background=random")
                            .crossfade(true)
                            .build(),
                        contentDescription = "头像",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Composable
fun GroupMembersDialog(
    members: List<GroupMember>,
    onDismiss: () -> Unit,
    onInvite: (String) -> Unit
) {
    var showInviteDialog by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("群成员 (${members.size})") },
        text = {
            Column {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    items(members) { member ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("https://ui-avatars.com/api/?name=${member.nickname}&background=random")
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "头像",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = member.nickname,
                                        fontWeight = FontWeight.Bold
                                    )
                                    
                                    if (member.isAdmin) {
                                        Text(
                                            text = if (member.affiliation == GroupMember.AFFILIATION_OWNER) " (群主)" else " (管理员)",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                                
                                Text(
                                    text = member.status,
                                    fontSize = 12.sp,
                                    color = if (member.status == "离线") Color.Gray else Color(0xFF4CAF50)
                                )
                            }
                        }
                        
                        if (members.indexOf(member) < members.size - 1) {
                            Divider(
                                modifier = Modifier.padding(start = 52.dp),
                                color = Color.LightGray.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { showInviteDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "邀请")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("邀请好友")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("关闭")
            }
        },
        dismissButton = {}
    )
    
    if (showInviteDialog) {
        InviteUserDialog(
            onDismiss = { showInviteDialog = false },
            onInvite = { userJid ->
                onInvite(userJid)
                showInviteDialog = false
            }
        )
    }
}

@Composable
fun InviteUserDialog(
    onDismiss: () -> Unit,
    onInvite: (String) -> Unit
) {
    var userJid by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("邀请好友") },
        text = {
            Column {
                TextField(
                    value = userJid,
                    onValueChange = { userJid = it },
                    label = { Text("好友JID (用户名@服务器)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                
                Text(
                    "提示: 请输入好友的完整JID地址，例如: friend@example.com",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onInvite(userJid) },
                enabled = userJid.isNotBlank() && userJid.contains('@')
            ) {
                Text("邀请")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
} 