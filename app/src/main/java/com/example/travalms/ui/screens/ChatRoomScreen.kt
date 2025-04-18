package com.example.travalms.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travalms.data.model.ChatMessage
import com.example.travalms.data.remote.XMPPManager
import com.example.travalms.ui.theme.PrimaryColor
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travalms.ui.viewmodels.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    sessionId: String,
    targetName: String,
    targetType: String,
    onBackClick: () -> Unit,
    chatViewModel: ChatViewModel = viewModel() // 添加ViewModel
) {
    // 创建消息列表状态
    val messagesState = remember { mutableStateListOf<ChatMessage>() }
    var inputText by remember { mutableStateOf("") }
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val lazyListState = rememberLazyListState()
    
    // 获取当前用户的JID
    val currentUserJid = remember { 
        XMPPManager.getInstance().currentConnection?.user?.asEntityBareJidString() ?: ""
    }
    
    // 加载历史消息并订阅新消息
    LaunchedEffect(key1 = sessionId) {
        // 获取聊天对象的JID
        val targetJid = sessionId.takeIf { it.contains('@') } ?: "$sessionId@${XMPPManager.SERVER_DOMAIN}"
        
        Log.d("ChatRoom", "初始化聊天: targetJid=$targetJid, targetName=$targetName")
        Log.d("ChatRoom", "当前用户JID: $currentUserJid")
        
        // 加载历史聊天记录
        coroutineScope.launch {
            try {
                val chatHistory = chatViewModel.getMessagesForSession(targetJid)
                if (chatHistory.isNotEmpty()) {
                    Log.d("ChatRoom", "从ViewModel加载了 ${chatHistory.size} 条消息")
                    messagesState.addAll(chatHistory)
                } else {
                    Log.d("ChatRoom", "ViewModel中没有消息，从服务器加载历史记录")
                    val chatHistoryResult = XMPPManager.getInstance().getChatHistory(targetJid)
                    if (chatHistoryResult.isSuccess) {
                        val history = chatHistoryResult.getOrDefault(emptyList())
                        Log.d("ChatRoom", "已加载 ${history.size} 条历史消息")
                        messagesState.addAll(history)
                    } else {
                        val error = chatHistoryResult.exceptionOrNull()
                        Log.e("ChatRoom", "加载历史消息失败: ${error?.message}")
                        snackbarHostState.showSnackbar("无法加载聊天记录: ${error?.message ?: "未知错误"}")
                    }
                }

                // 标记会话为已读
                chatViewModel.markSessionAsRead(targetJid)
            } catch (e: Exception) {
                Log.e("ChatRoom", "加载历史消息异常", e)
                snackbarHostState.showSnackbar("无法加载聊天记录: ${e.message ?: "未知错误"}")
            }
        }
        
        // 监听新消息
        coroutineScope.launch {
            try {
                XMPPManager.getInstance().messageFlow.collectLatest { newMessage ->
                    Log.d("ChatRoom", "收到消息: ${newMessage.content} 从 ${newMessage.senderId}")
                    
                    // 增强去重逻辑：使用消息ID作为唯一标识，避免重复处理同一消息
                    val isDuplicate = messagesState.any { existingMsg -> 
                        existingMsg.id == newMessage.id ||
                        (existingMsg.content == newMessage.content && 
                         existingMsg.senderId == newMessage.senderId &&
                         existingMsg.timestamp.isEqual(newMessage.timestamp))
                    }
                    
                    // 检查消息是否来自当前聊天对象或当前用户自己发送的
                    if (!isDuplicate && (newMessage.senderId == targetJid || 
                        (newMessage.senderId == currentUserJid && 
                         newMessage.recipientId == targetJid))) {
                        Log.d("ChatRoom", "添加新消息到UI: ${newMessage.id}")
                        messagesState.add(newMessage)
                        
                        // 自动滚动到底部
                        lazyListState.animateScrollToItem(messagesState.size - 1)

                        // 如果是接收到的消息，标记为已读
                        if (newMessage.senderId == targetJid) {
                            chatViewModel.markSessionAsRead(targetJid)
                        }
                    } else if (isDuplicate) {
                        Log.d("ChatRoom", "忽略重复消息: ${newMessage.id}")
                    }
                }
            } catch (e: Exception) {
                Log.e("ChatRoom", "收集消息异常", e)
                snackbarHostState.showSnackbar("接收消息出错: ${e.message ?: "未知错误"}")
            }
        }
    }
    
    // 当有新消息时自动滚动到底部
    LaunchedEffect(messagesState.size) {
        if (messagesState.isNotEmpty()) {
            lazyListState.animateScrollToItem(messagesState.size - 1)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(targetName) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 消息列表
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                state = lazyListState
            ) {
                items(messagesState) { message ->
                    // 添加更明确的调试日志来确认修复
                    Log.d("ChatRoom", "** 判断消息位置 ** " +
                          "消息内容: ${message.content}, " +
                          "发送者: ${message.senderId}, " + 
                          "当前用户: $currentUserJid, " +
                          "显示在: ${if (message.senderId == currentUserJid) "右侧(自己)" else "左侧(对方)"}")
                          
                    MessageItem(
                        message = message,
                        isFromCurrentUser = message.senderId == currentUserJid, // 正确的逻辑：自己发的消息senderId应等于当前用户JID
                        timeFormatter = timeFormatter
                    )
                }
            }
            
            // 输入框区域
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 文本输入框
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("输入消息") },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    maxLines = 3
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // 发送按钮
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            // 获取聊天对象的JID
                            val targetJid = sessionId.takeIf { it.contains('@') } ?: "$sessionId@${XMPPManager.SERVER_DOMAIN}"
                            
                            // 发送消息
                            coroutineScope.launch {
                                try {
                                    // 使用ViewModel发送消息
                                    chatViewModel.sendMessage(targetJid, inputText)
                                    inputText = "" // 清空输入框
                                } catch (e: Exception) {
                                    Log.e("ChatRoom", "发送消息异常", e)
                                    snackbarHostState.showSnackbar("发送失败: ${e.message ?: "未知错误"}")
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(PrimaryColor, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "发送",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun MessageItem(
    message: ChatMessage,
    isFromCurrentUser: Boolean,
    timeFormatter: DateTimeFormatter
) {
    // 调试日志，帮助追踪消息显示位置问题
    LaunchedEffect(message.id) {
        Log.d("MessageItem", 
            "显示消息: ID=${message.id}, 内容=${message.content}, " +
            "发送者=${message.senderId}, isFromCurrentUser=$isFromCurrentUser"
        )
    }

    // 是否显示技术细节(长按消息时)
    var showDetails by remember { mutableStateOf(false) }

    Column {
        // 如果显示详情，添加一个调试信息框
        if (showDetails) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF0F0F0)
                )
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("消息ID: ${message.id}", fontSize = 10.sp)
                    Text("发送者: ${message.senderId}", fontSize = 10.sp)
                    Text("接收者: ${message.recipientId ?: "未知"}", fontSize = 10.sp)
                    Text("isFromCurrentUser: $isFromCurrentUser", fontSize = 10.sp)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable(
                    indication = null, // 移除点击效果
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { showDetails = !showDetails } // 切换显示详情
                ),
            horizontalArrangement = if (isFromCurrentUser) Arrangement.End else Arrangement.Start
        ) {
            Column(
                horizontalAlignment = if (isFromCurrentUser) Alignment.End else Alignment.Start
            ) {
                // 发送者名称
                if (!isFromCurrentUser && message.senderName != "我") {
                    Text(
                        text = message.senderName,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                    )
                }
                
                // 消息气泡
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = if (isFromCurrentUser) Arrangement.End else Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isFromCurrentUser) {
                        // 时间戳
                        Text(
                            text = timeFormatter.format(message.timestamp),
                            fontSize = 10.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                    
                    // 消息内容
                    Box(
                        modifier = Modifier
                            .widthIn(max = 280.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 12.dp,
                                    topEnd = 12.dp,
                                    bottomStart = if (isFromCurrentUser) 12.dp else 4.dp,
                                    bottomEnd = if (isFromCurrentUser) 4.dp else 12.dp
                                )
                            )
                            .background(if (isFromCurrentUser) PrimaryColor else Color.White)
                            .border(
                                width = 1.dp,
                                color = if (isFromCurrentUser) PrimaryColor else Color.LightGray,
                                shape = RoundedCornerShape(
                                    topStart = 12.dp,
                                    topEnd = 12.dp,
                                    bottomStart = if (isFromCurrentUser) 12.dp else 4.dp,
                                    bottomEnd = if (isFromCurrentUser) 4.dp else 12.dp
                                )
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = message.content,
                            color = if (isFromCurrentUser) Color.White else Color.Black,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    
                    if (!isFromCurrentUser) {
                        // 时间戳
                        Text(
                            text = timeFormatter.format(message.timestamp),
                            fontSize = 10.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }
    }
} 
