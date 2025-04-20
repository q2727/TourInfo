package com.example.travalms.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.travalms.data.model.ContactItem
import com.example.travalms.data.remote.XMPPManager
import com.example.travalms.ui.navigation.AppRoutes
import com.example.travalms.ui.theme.PrimaryColor
import com.example.travalms.ui.viewmodels.GroupViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jxmpp.jid.BareJid
import org.jxmpp.jid.impl.JidCreate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen(
    navController: NavController,
    groupViewModel: GroupViewModel = viewModel()
) {
    val TAG = "CreateGroupScreen"
    val context = LocalContext.current

    // 状态变量
    var groupName by remember { mutableStateOf("") }
    var groupDescription by remember { mutableStateOf("") }
    var isPrivate by remember { mutableStateOf(false) }
    var friendsList by remember { mutableStateOf<List<Pair<BareJid?, String?>>>(emptyList()) }
    var selectedFriends by remember { mutableStateOf<Set<BareJid>>(emptySet()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    
    // 加载好友列表
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val result = XMPPManager.getInstance().getFriends()
            if (result.isSuccess) {
                val allFriends = result.getOrDefault(emptyList())
                // Get current user's JID to filter it out
                val currentUserJid = XMPPManager.getInstance().currentConnection?.user?.asBareJid()
                Log.d(TAG, "当前用户JID: $currentUserJid，将从好友列表中过滤掉")
                
                // Filter out the current user from the friend list
                friendsList = allFriends.filter { friend ->
                    val friendJid = friend.first
                    friendJid != currentUserJid // Only include friends that are not the current user
                }
                
                Log.d(TAG, "获取到 ${allFriends.size} 个好友，过滤掉自己后剩余 ${friendsList.size} 个")
            } else {
                errorMessage = "获取好友列表失败: ${result.exceptionOrNull()?.message}"
                Log.e(TAG, "获取好友列表失败", result.exceptionOrNull())
            }
        } catch (e: Exception) {
            errorMessage = "加载好友异常: ${e.message}"
            Log.e(TAG, "加载好友异常", e)
        } finally {
            isLoading = false
        }
    }
    
    // 创建群聊函数
    fun createGroup() {
        if (groupName.isBlank()) {
            errorMessage = "请输入群聊名称"
            return
        }
        
        if (selectedFriends.isEmpty()) {
            errorMessage = "请至少选择一个好友"
            return
        }
        
        val currentUser = XMPPManager.getInstance().currentConnection?.user?.localpart?.toString() ?: ""
        
        isLoading = true
        coroutineScope.launch {
            try {
                // 使用增强版本的群聊创建方法
                val result = XMPPManager.getInstance().groupChatManager.createGroupRoomEnhanced(
                    roomName = groupName,
                    nickname = currentUser,
                    description = groupDescription,
                    membersOnly = isPrivate
                )
                
                if (result.isSuccess) {
                    val room = result.getOrNull()
                    if (room != null) {
                        // 1. 先加入房间，确保可以邀请其他人
                        try {
                            Log.d(TAG, "成功创建群聊: ${room.roomJid}, 确保已加入房间...")
                            val joinResult = XMPPManager.getInstance().groupChatManager.joinRoom(
                                roomJid = room.roomJid,
                                nickname = currentUser
                            )
                            
                            if (joinResult.isSuccess) {
                                Log.d(TAG, "已确认加入房间: ${room.roomJid}")
                            } else {
                                Log.w(TAG, "无法确认加入房间，但将继续尝试邀请好友", joinResult.exceptionOrNull())
                            }
                        } catch (e: Exception) {
                            Log.w(TAG, "确认加入房间时出现异常，但将继续尝试邀请好友", e)
                        }
                        
                        // 2. 邀请所有选定的好友
                        Log.d(TAG, "【群聊创建】开始邀请好友，共 ${selectedFriends.size} 人...")
                        var inviteSuccessCount = 0
                        
                        // 创建一个列表来存储所有邀请的结果
                        val invitationResults = mutableListOf<Pair<BareJid, Result<Unit>>>()
                        
                        // 记录每个好友的邀请开始时间，以便计算耗时
                        val startTimes = mutableMapOf<BareJid, Long>()
                        
                        // 整个邀请流程的开始时间
                        val invitationStartTime = System.currentTimeMillis()
                        
                        // 一个一个地邀请好友，确保顺序执行
                        for (friend in selectedFriends) {
                            try {
                                startTimes[friend] = System.currentTimeMillis()
                                Log.d(TAG, "【群聊创建】正在邀请好友 ${friend.toString()}...")
                                
                                // 邀请每个好友
                                val inviteResult = XMPPManager.getInstance().groupChatManager.inviteUserToRoom(
                                    roomJid = room.roomJid,
                                    userJid = friend.toString(),
                                    reason = "邀请您加入群聊 $groupName"
                                )
                                
                                val elapsed = System.currentTimeMillis() - (startTimes[friend] ?: 0)
                                invitationResults.add(Pair(friend, inviteResult))
                                
                                if (inviteResult.isSuccess) {
                                    Log.d(TAG, "【群聊创建】成功邀请好友: $friend (耗时: ${elapsed}ms)")
                                    inviteSuccessCount++
                                } else {
                                    Log.e(TAG, "【群聊创建】邀请好友失败: $friend (耗时: ${elapsed}ms)", inviteResult.exceptionOrNull())
                                }
                                
                                // 在邀请之间添加短暂延迟，避免服务器负载过大
                                kotlinx.coroutines.delay(200)
                            } catch (e: Exception) {
                                Log.e(TAG, "【群聊创建】邀请好友异常: $friend", e)
                                invitationResults.add(Pair(friend, Result.failure(e)))
                            }
                        }
                        
                        val totalInvitationTime = System.currentTimeMillis() - invitationStartTime
                        
                        // 详细报告邀请结果
                        Log.d(TAG, "【群聊创建】邀请好友完成，成功: $inviteSuccessCount/${selectedFriends.size} 人，总耗时: ${totalInvitationTime}ms")
                        invitationResults.forEachIndexed { index, pair ->
                            val (friend, result) = pair
                            val status = if (result.isSuccess) "成功" else "失败: ${result.exceptionOrNull()?.message}"
                            Log.d(TAG, "【群聊创建】邀请[$index] - $friend: $status")
                        }
                        
                        // 如果没有一个邀请成功且选择了好友，显示警告
                        if (inviteSuccessCount == 0 && selectedFriends.isNotEmpty()) {
                            Log.w(TAG, "【群聊创建】警告: 没有一个邀请成功，请检查群聊权限和网络状态")
                            // 延迟到主线程显示Toast
                            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                                Toast.makeText(context, "邀请好友失败，请检查网络和权限", Toast.LENGTH_SHORT).show()
                            }
                        }
                        
                        // 即使邀请失败也继续，只显示一个Toast提示
                        if (inviteSuccessCount < selectedFriends.size && inviteSuccessCount > 0) {
                            // 部分成功，显示提示
                            val successRate = inviteSuccessCount * 100 / selectedFriends.size
                            Log.w(TAG, "【群聊创建】部分邀请失败，成功率: $successRate%")
                            
                            // 延迟到主线程显示Toast
                            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                                Toast.makeText(
                                    context, 
                                    "已邀请 $inviteSuccessCount/${selectedFriends.size} 位好友加入群聊", 
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else if (inviteSuccessCount == selectedFriends.size && selectedFriends.isNotEmpty()) {
                            // 全部成功
                            Log.d(TAG, "【群聊创建】所有邀请均已成功")
                            
                            // 延迟到主线程显示Toast
                            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                                Toast.makeText(
                                    context, 
                                    "已成功邀请全部好友加入群聊", 
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        
                        // 确保所有邀请处理完毕后再导航
                        try {
                            // 等待一点时间，确保所有邀请的请求都已经发送到服务器
                            kotlinx.coroutines.delay(500)
                            
                            // 3. 创建成功后导航到新群聊界面
                            Log.d(TAG, "【群聊创建】正在导航到群聊界面: ${room.roomJid}")
                            navController.navigate(AppRoutes.GROUP_CHAT.replace("{roomJid}", room.roomJid)) {
                                // 删除之前的创建界面，防止返回
                                popUpTo(AppRoutes.CREATE_GROUP) { inclusive = true }
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "【群聊创建】导航到群聊界面时发生错误", e)
                            errorMessage = "导航到群聊界面失败: ${e.message}"
                            isLoading = false
                        }
                    } else {
                        errorMessage = "创建群聊成功，但未能获取房间信息"
                        isLoading = false
                    }
                } else {
                    errorMessage = "创建群聊失败: ${result.exceptionOrNull()?.message ?: "未知错误"}"
                    isLoading = false
                }
            } catch (e: Exception) {
                Log.e(TAG, "创建群聊过程发生异常", e)
                errorMessage = "创建群聊异常: ${e.message}"
                isLoading = false
            }
        }
    }
    
    // UI
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("创建群聊", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    // 创建按钮
                    IconButton(
                        onClick = { createGroup() },
                        enabled = !isLoading && groupName.isNotBlank() && selectedFriends.isNotEmpty()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "创建群聊",
                            tint = if (!isLoading && groupName.isNotBlank() && selectedFriends.isNotEmpty()) 
                                   Color.White else Color.Gray
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // 群聊名称输入
                OutlinedTextField(
                    value = groupName,
                    onValueChange = { groupName = it },
                    label = { Text("群聊名称 (必填)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { /* 移动到下一个输入框 */ }),
                    trailingIcon = {
                        if (groupName.isNotEmpty()) {
                            IconButton(onClick = { groupName = "" }) {
                                Icon(Icons.Filled.Clear, contentDescription = "清除")
                            }
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 群聊描述输入
                OutlinedTextField(
                    value = groupDescription,
                    onValueChange = { groupDescription = it },
                    label = { Text("群聊描述 (选填)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    trailingIcon = {
                        if (groupDescription.isNotEmpty()) {
                            IconButton(onClick = { groupDescription = "" }) {
                                Icon(Icons.Filled.Clear, contentDescription = "清除")
                            }
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 私密群聊开关
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "私密群聊",
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = isPrivate,
                        onCheckedChange = { isPrivate = it }
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 已选择的好友数量
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "选择好友",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "已选择 ${selectedFriends.size} 人",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 好友列表
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PrimaryColor)
                    }
                } else if (friendsList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "暂无好友，请先添加好友",
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(friendsList) { friend ->
                            val jid = friend.first
                            val name = friend.second ?: jid?.toString()?.substringBefore('@') ?: "未知好友"
                            val isSelected = jid != null && selectedFriends.contains(jid)
                            
                            FriendItem(
                                name = name,
                                isSelected = isSelected,
                                onClick = {
                                    if (jid != null) {
                                        selectedFriends = if (isSelected) {
                                            selectedFriends - jid
                                        } else {
                                            selectedFriends + jid
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
                
                // 显示错误信息
                errorMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            // 全屏加载指示器
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }
    }
}

@Composable
fun FriendItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 头像
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (isSelected) PrimaryColor else Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "已选择",
                    tint = Color.White
                )
            } else {
                Text(
                    text = name.first().toString(),
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // 名字
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // 选择框
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .then(
                    if (isSelected) {
                        Modifier.background(PrimaryColor)
                    } else {
                        Modifier
                            .border(
                                width = 2.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .background(Color.White)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "已选择",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
} 