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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    sessionId: String,
    targetName: String,
    targetType: String,
    onBackClick: () -> Unit
) {
    // 创建消息列表状态
    val messagesState = remember { mutableStateListOf<ChatMessage>() }
    var inputText by remember { mutableStateOf("") }
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val lazyListState = rememberLazyListState()
    
    // 加载历史消息并订阅新消息
    LaunchedEffect(key1 = sessionId) {
        // 获取聊天对象的JID
        val targetJid = sessionId.takeIf { it.contains('@') } ?: "$sessionId@${XMPPManager.SERVER_DOMAIN}"
        
        Log.d("ChatRoom", "初始化聊天: targetJid=$targetJid, targetName=$targetName")
        
        // 加载历史聊天记录
        coroutineScope.launch {
            try {
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
                    
                    // 检查消息是否来自当前聊天对象或当前用户自己发送的
                    if (newMessage.senderId == targetJid || 
                        (newMessage.senderName == "我" && 
                         newMessage.content !in messagesState.map { it.content })) {
                        messagesState.add(newMessage)
                        
                        // 自动滚动到底部
                        lazyListState.animateScrollToItem(messagesState.size - 1)
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
            CenterAlignedTopAppBar(
                title = { Text(targetName, fontWeight = FontWeight.Bold) },
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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 聊天消息列表
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                reverseLayout = false,
                state = lazyListState
            ) {
                items(messagesState) { message ->
                    ChatMessageItem(
                        message = message,
                        isFromCurrentUser = message.senderName == "我",
                        timeFormatter = timeFormatter
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            
            // 底部输入框
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 4.dp,
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 使用Material3的OutlinedTextField替代TextField
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("输入消息...") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            unfocusedContainerColor = Color.LightGray.copy(alpha = 0.2f),
                            focusedContainerColor = Color.LightGray.copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(24.dp),
                        singleLine = true
                    )
                    
                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                // 获取聊天对象的JID
                                val targetJid = sessionId.takeIf { it.contains('@') } ?: "$sessionId@${XMPPManager.SERVER_DOMAIN}"
                                
                                // 发送消息
                                coroutineScope.launch {
                                    try {
                                        val result = XMPPManager.getInstance().sendMessage(targetJid, inputText)
                                        if (result.isSuccess) {
                                            // 消息已由messageFlow自动添加
                                            inputText = ""
                                        } else {
                                            val error = result.exceptionOrNull()
                                            Log.e("ChatRoom", "发送消息失败: ${error?.message}")
                                            snackbarHostState.showSnackbar("发送失败: ${error?.message ?: "未知错误"}")
                                        }
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
                            Icons.Filled.Send,
                            contentDescription = "发送",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    isFromCurrentUser: Boolean,
    timeFormatter: DateTimeFormatter
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isFromCurrentUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!isFromCurrentUser) {
            // 头像
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message.senderName.first().toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        Column(
            horizontalAlignment = if (isFromCurrentUser) Alignment.End else Alignment.Start
        ) {
            if (!isFromCurrentUser) {
                Text(
                    text = message.senderName,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )
            }
            
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                if (isFromCurrentUser) {
                    Text(
                        text = message.timestamp.format(timeFormatter),
                        fontSize = 10.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                
                Box(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = if (isFromCurrentUser) 16.dp else 4.dp,
                                bottomEnd = if (isFromCurrentUser) 4.dp else 16.dp
                            )
                        )
                        .background(
                            if (isFromCurrentUser) PrimaryColor else Color.LightGray.copy(alpha = 0.3f)
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = message.content,
                        color = if (isFromCurrentUser) Color.White else Color.Black
                    )
                }
                
                if (!isFromCurrentUser) {
                    Text(
                        text = message.timestamp.format(timeFormatter),
                        fontSize = 10.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
} 