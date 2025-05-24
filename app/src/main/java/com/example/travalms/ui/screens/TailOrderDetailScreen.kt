package com.example.travalms.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travalms.ui.components.LoadingIndicator
import com.example.travalms.ui.components.UserAvatar
import com.example.travalms.ui.viewmodels.TailOrderDetailViewModel
import com.example.travalms.ui.viewmodels.TailListViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.ui.viewinterop.AndroidView
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.travalms.ui.navigation.AppRoutes

/**
 * 尾单详情屏幕
 */
@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TailOrderDetailScreen(
    tailOrderId: String,
    onBack: () -> Unit,
    onChatRoomClick: (String) -> Unit,
    onFriendDetailClick: (String) -> Unit
) {
    val TAG = "TailOrderDetailScreen"
    Log.d(TAG, "Enter TailOrderDetailScreen with tailOrderId: $tailOrderId")
    
    // 尝试直接从TailListViewModel获取尾单信息并打印
    try {
        val tailListViewModel = TailListViewModel.getInstance()
        val tailOrders = tailListViewModel.state.value.tailOrders
        Log.d(TAG, "TailListViewModel中的尾单数量: ${tailOrders.size}")
        
        val tailOrderFromList = tailOrders.find { it.id.toString() == tailOrderId }
        if (tailOrderFromList != null) {
            Log.d(TAG, "直接从TailListViewModel找到尾单: ${tailOrderFromList.title}, 发布者JID: ${tailOrderFromList.publisherJid}")
        } else {
            Log.d(TAG, "在TailListViewModel中找不到ID为 $tailOrderId 的尾单")
            // 尝试打印所有尾单ID进行对比
            tailOrders.forEach { 
                Log.d(TAG, "尾单ID: ${it.id}, 标题: ${it.title}")
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "直接访问TailListViewModel失败", e)
    }
    
    val context = LocalContext.current
    val viewModel: TailOrderDetailViewModel = viewModel(
        factory = TailOrderDetailViewModel.Factory(tailOrderId, context.applicationContext as android.app.Application)
    )
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    var reportReason by remember { mutableStateOf("") }
    var showPhoneDialog by remember { mutableStateOf(false) }
    
    // 添加WebView状态
    var showProductWebView by remember { mutableStateOf(false) }
    var productIdToShow by remember { mutableStateOf<Long?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (!showProductWebView) "尾单详情" else "产品详情") },
                navigationIcon = {
                    IconButton(onClick = { 
                        if (showProductWebView) {
                            showProductWebView = false
                        } else {
                            onBack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (showProductWebView && productIdToShow != null) {
            // 显示产品WebView
            val url = "http://42.193.112.197/#/pages/user/singleProductPage?productId=${productIdToShow}"
            AndroidView(
                factory = { ctx ->
                    WebView(ctx).apply {
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                        loadUrl(url)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else {
            // 尾单内容展示
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    state.isLoading -> {
                        LoadingIndicator(modifier = Modifier.fillMaxSize())
                    }
                    state.error != null -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = state.error ?: "未知错误",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = onBack) {
                                Text("返回")
                            }
                        }
                    }
                    state.tailOrder == null -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "尾单不存在或已被删除",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = onBack) {
                                Text("返回")
                            }
                        }
                    }
                    else -> {
                        // 尾单详情内容
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            // 发布者卡片
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .clickable {
                                        // 提取用户名并导航到用户详情页
                                        val publisherJidFromTailOrder = state.tailOrder?.publisherJid
                                        val usernameFromTailOrderJid = if (publisherJidFromTailOrder?.contains("@") == true) {
                                            publisherJidFromTailOrder.substringBefore("@")
                                        } else {
                                            publisherJidFromTailOrder
                                        }

                                        val userToNavigate = state.publisherInfo?.get("username")?.toString()
                                                             ?: usernameFromTailOrderJid
                                                             ?: "unknown_user"
                                        if (userToNavigate != "unknown_user" && userToNavigate.isNotBlank()) {
                                            onFriendDetailClick(userToNavigate)
                                        } else {
                                            Log.w(TAG, "无法获取有效的用户名进行导航。 publisherInfo: ${state.publisherInfo}, tailOrder.publisherJid: ${state.tailOrder?.publisherJid}")
                                            coroutineScope.launch { snackbarHostState.showSnackbar("无法获取用户信息") }
                                        }
                                    },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 2.dp
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // 发布者头像
                                    if (state.publisherInfo != null) {
                                        UserAvatar(
                                            username = state.publisherInfo?.get("username")?.toString() ?: "",
                                            size = 50.dp
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .size(50.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.primaryContainer),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = state.tailOrder?.publisherJid?.firstOrNull()?.uppercase() ?: "?",
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.width(16.dp))
                                    
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        // 联系人姓名
                                        Text(
                                            text = if (state.publisherInfo != null) {
                                                state.publisherInfo?.get("nickname")?.toString() 
                                                    ?: state.publisherInfo?.get("username")?.toString() 
                                                    ?: "未知用户"
                                            } else {
                                                val publisherJid = state.tailOrder?.publisherJid ?: ""
                                                if (publisherJid.isNotEmpty()) {
                                                    if (publisherJid.contains("@")) {
                                                        Log.d(TAG, "使用JID用户名部分: ${publisherJid.substringBefore("@")}")
                                                        publisherJid.substringBefore("@")
                                                    } else {
                                                        Log.d(TAG, "使用原始JID: $publisherJid")
                                                        publisherJid
                                                    }
                                                } else {
                                                    "未知用户"
                                                }
                                            },
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        
                                        Spacer(modifier = Modifier.height(4.dp))
                                        
                                        // 联系电话
                                        Box(
                                            modifier = Modifier.clickable(
                                                enabled = true,
                                                onClick = { showPhoneDialog = true }
                                            )
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Phone,
                                                    contentDescription = "电话",
                                                    tint = Color.Gray,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = state.publisherInfo?.get("phoneNumber")?.toString() ?: "未设置电话",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = Color.Gray
                                                )
                                            }
                                        }
                                    }
                                    
                                    // 收藏按钮
                                    IconButton(
                                        onClick = { /* 收藏功能 */ }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Favorite,
                                            contentDescription = "收藏",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                            
                            // 尾单标题
                            Text(
                                text = state.tailOrder?.title ?: "",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            
                            // 产品信息
                            state.tailOrder?.productTitle?.let { productTitle ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                        .clickable {
                                            state.tailOrder?.productId?.let { productId ->
                                                // 使用WebView显示产品
                                                productIdToShow = productId
                                                showProductWebView = true
                                                Log.d(TAG, "打开产品详情WebView: productId=$productId")
                                            }
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "产品链接",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = productTitle,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            
                            // 尾单详情内容卡片
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    state.tailOrder?.content?.forEach { contentItem ->
                                        Text(
                                            text = contentItem,
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                            
                            // 有效期
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = "截至日期: ${state.tailOrder?.remainingDays}天后",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // 底部按钮区域
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                // 删除按钮
                                Button(
                                    onClick = { showDeleteDialog = true },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFF8A5C2)
                                    ),
                                    contentPadding = PaddingValues(vertical = 12.dp)
                                ) {
                                    Text("删除")
                                }
                                
                                Spacer(modifier = Modifier.width(16.dp))
                                
                                // 举报按钮
                                Button(
                                    onClick = { showReportDialog = true },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFFF8A80)
                                    ),
                                    contentPadding = PaddingValues(vertical = 12.dp)
                                ) {
                                    Text("举报")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    // 举报对话框
    if (showReportDialog) {
        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            title = { Text("举报尾单") },
            text = {
                Column {
                    Text("请输入举报原因:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = reportReason,
                        onValueChange = { reportReason = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("举报原因") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (reportReason.isNotEmpty()) {
                            viewModel.reportTailOrder(reportReason)
                            showReportDialog = false
                            reportReason = ""
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("举报成功，我们会尽快处理")
                            }
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("请输入举报原因")
                            }
                        }
                    }
                ) {
                    Text("提交")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReportDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
    
    // 删除确认对话框
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("删除尾单") },
            text = { Text("确定要删除此尾单吗？此操作无法撤销。") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteTailOrder()
                        showDeleteDialog = false
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("尾单已删除")
                            // 短暂延迟后返回
                            kotlinx.coroutines.delay(500)
                            onBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("删除")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
    
    // 添加拨号确认对话框
    if (showPhoneDialog) {
        val phoneNumber = state.publisherInfo?.get("phoneNumber")?.toString()
        val publisherName = state.publisherInfo?.get("nickname")?.toString() 
            ?: state.publisherInfo?.get("username")?.toString()
            ?: "未知用户"
            
        if (phoneNumber != null) {
            AlertDialog(
                onDismissRequest = { showPhoneDialog = false },
                title = null,
                text = {
                    Text(
                        text = "${phoneNumber}可能是一个电话号码，你可以",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showPhoneDialog = false
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${phoneNumber}")
                            }
                            context.startActivity(intent)
                        }
                    ) {
                        Text("呼叫", color = MaterialTheme.colorScheme.primary)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showPhoneDialog = false }) {
                        Text("取消")
                    }
                }
            )
        } else {
            // 如果没有电话号码，显示提示对话框
            AlertDialog(
                onDismissRequest = { showPhoneDialog = false },
                title = null,
                text = {
                    Text(
                        text = "${publisherName}未设置联系电话",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                confirmButton = {
                    TextButton(onClick = { showPhoneDialog = false }) {
                        Text("确定")
                    }
                }
            )
        }
    }
} 