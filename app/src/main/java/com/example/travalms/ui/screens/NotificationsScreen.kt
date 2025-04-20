package com.example.travalms.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import com.example.travalms.data.model.ChatInvitation

/**
 * 通知屏幕，显示群聊邀请通知
 * 
 * @param invitations 邀请列表
 * @param onClose 关闭通知的回调
 * @param onAccept 接受邀请的回调
 * @param onReject 拒绝邀请的回调
 */
@Composable
fun NotificationsScreen(
    invitations: List<ChatInvitation>,
    onClose: () -> Unit,
    onAccept: (ChatInvitation) -> Unit,
    onReject: (ChatInvitation) -> Unit
) {
    // 记录当前显示的邀请数量
    val logTag = "NotificationsScreen"
    Log.d(logTag, "显示通知屏幕，邀请数量: ${invitations.size}")
    invitations.forEachIndexed { index, invitation ->
        Log.d(logTag, "邀请 #$index: ${invitation.shortDescription}, roomJid=${invitation.roomJid}")
    }
    
    val context = LocalContext.current
    
    // 当组件显示时记录日志
    DisposableEffect(Unit) {
        Log.d(logTag, "通知屏幕已显示")
        onDispose {
            Log.d(logTag, "通知屏幕已关闭")
        }
    }

    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // 头部
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF2196F3))
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "消息通知",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            
                            IconButton(onClick = onClose) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "关闭",
                                    tint = Color.White
                                )
                            }
                        }
                        
                        // 内容
                        if (invitations.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "暂无通知",
                                    color = Color.Gray
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(350.dp)
                                    .padding(horizontal = 8.dp)
                            ) {
                                items(invitations) { invitation ->
                                    InvitationItem(
                                        invitation = invitation,
                                        onAccept = { 
                                            Log.d(logTag, "接受邀请: ${invitation.roomJid}")
                                            onAccept(invitation)
                                            Toast.makeText(context, "接受邀请: ${invitation.roomName}", Toast.LENGTH_SHORT).show()
                                        },
                                        onReject = { 
                                            Log.d(logTag, "拒绝邀请: ${invitation.roomJid}")
                                            onReject(invitation)
                                            Toast.makeText(context, "已拒绝邀请", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                    
                                    Divider(color = Color.LightGray, thickness = 0.5.dp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 邀请项，显示单个邀请的信息和操作按钮
 */
@Composable
fun InvitationItem(
    invitation: ChatInvitation,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 8.dp)
    ) {
        // 邀请信息
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 群组图标
            Icon(
                imageVector = Icons.Default.AccountBox,
                contentDescription = "群聊",
                tint = Color(0xFF2196F3),
                modifier = Modifier.padding(end = 12.dp)
            )
            
            // 邀请信息
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = invitation.shortDescription,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "房间: ${invitation.roomName}",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Text(
                    text = "JID: ${invitation.roomJid}",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (invitation.reason.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "原因: ${invitation.reason}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "邀请时间: ${invitation.formattedTime}",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
            
            // 删除按钮
            IconButton(onClick = onReject) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "拒绝",
                    tint = Color.Red
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 操作按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = onReject,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray
                ),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("拒绝")
            }
            
            Button(
                onClick = onAccept,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
                )
            ) {
                Text("接受")
            }
        }
    }
} 