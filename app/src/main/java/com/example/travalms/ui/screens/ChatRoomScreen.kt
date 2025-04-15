package com.example.travalms.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.travalms.ui.theme.PrimaryColor
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    sessionId: String,
    targetName: String,
    targetType: String,
    onBackClick: () -> Unit
) {
    // 在实际应用中应该从数据源获取聊天消息
    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                id = "1",
                senderId = "user1",
                senderName = "我",
                content = "你好，请问这个旅游团什么时候出发？",
                timestamp = LocalDateTime.now().minusHours(2),
                isRead = true
            ),
            ChatMessage(
                id = "2",
                senderId = "company1",
                senderName = targetName,
                content = "您好，这个团预计下周二出发，目前还有3个名额。",
                timestamp = LocalDateTime.now().minusHours(1),
                isRead = true
            ),
            ChatMessage(
                id = "3",
                senderId = "user1",
                senderName = "我",
                content = "行程是几天？住宿条件怎么样？",
                timestamp = LocalDateTime.now().minusMinutes(30),
                isRead = true
            ),
            ChatMessage(
                id = "4",
                senderId = "company1",
                senderName = targetName,
                content = "行程是5天4晚，住宿都是当地四星级酒店，标准双人间。如果您需要详细行程单，我可以发给您。",
                timestamp = LocalDateTime.now().minusMinutes(20),
                isRead = true
            )
        )
    }
    
    var inputText by remember { mutableStateOf("") }
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    
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
        }
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
                reverseLayout = false
            ) {
                items(messages) { message ->
                    ChatMessageItem(
                        message = message,
                        isFromCurrentUser = message.senderId == "user1",
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
                                messages.add(
                                    ChatMessage(
                                        id = "${messages.size + 1}",
                                        senderId = "user1",
                                        senderName = "我",
                                        content = inputText,
                                        timestamp = LocalDateTime.now(),
                                        isRead = true
                                    )
                                )
                                inputText = ""
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
        
        if (isFromCurrentUser) {
            Spacer(modifier = Modifier.width(8.dp))
            // 头像
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "我",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
} 