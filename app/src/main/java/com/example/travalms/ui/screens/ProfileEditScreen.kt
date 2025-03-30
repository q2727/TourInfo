package com.example.travalms.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travalms.ui.theme.PrimaryColor
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onBindCompanyClick: () -> Unit,
    onVerificationClick: () -> Unit
) {
    var nickname by remember { mutableStateOf("曾圆圆") }
    var phone by remember { mutableStateOf("17722222333") }
    var qq by remember { mutableStateOf("17722222333") }
    var wechat by remember { mutableStateOf("11122233") }
    var introduction by remember { mutableStateOf("为旅客提供个性化旅游服务。欢迎合作") }
    
    // 添加密码修改对话框状态
    var showPasswordDialog by remember { mutableStateOf(false) }
    
    val scrollState = rememberScrollState()
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("编辑个人资料", fontWeight = FontWeight.Bold) },
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
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // 用户ID (不可编辑)
            ProfileInfoRow(
                label = "用户ID",
                value = "0748123",
                editable = false
            )
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            // 昵称
            ProfileEditableRow(
                label = "昵称",
                value = nickname,
                onValueChange = { nickname = it }
            )
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            // 实名认证
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "实名认证",
                    modifier = Modifier.width(80.dp),
                    color = Color.Gray
                )
                
                Text(
                    text = "未认证",
                    modifier = Modifier.weight(1f)
                )
                
                Button(
                    onClick = { onVerificationClick() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryColor
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("开始认证")
                }
            }
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            // 密码 - 修改为可点击打开对话框
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "密码",
                    modifier = Modifier.width(80.dp),
                    color = Color.Gray
                )
                
                Text(
                    text = "***",
                    modifier = Modifier.weight(1f)
                )
                
                IconButton(onClick = { showPasswordDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "修改密码",
                        tint = PrimaryColor
                    )
                }
            }
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            // 手机
            ProfileEditableRow(
                label = "手机",
                value = phone,
                onValueChange = { phone = it }
            )
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            // QQ
            ProfileEditableRow(
                label = "QQ",
                value = qq,
                onValueChange = { qq = it }
            )
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            // 微信
            ProfileEditableRow(
                label = "微信",
                value = wechat,
                onValueChange = { wechat = it }
            )
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            // 所属公司
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "所属公司",
                    modifier = Modifier.width(80.dp),
                    color = Color.Gray
                )
                
                Text(
                    text = "未绑定",
                    modifier = Modifier.weight(1f)
                )
                
                Button(
                    onClick = { onBindCompanyClick() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryColor
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("去绑定")
                }
            }
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            // 简介
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "简介",
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                OutlinedTextField(
                    value = introduction,
                    onValueChange = { introduction = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(8.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 保存按钮
            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "保存修改",
                    fontSize = 16.sp
                )
            }
        }
        
        // 添加密码修改对话框
        if (showPasswordDialog) {
            SimplePasswordChangeDialog(
                onDismiss = { showPasswordDialog = false },
                onConfirm = { currentPassword, newPassword ->
                    // 这里应该添加更改密码的逻辑
                    // 例如调用API或本地数据更新
                    showPasswordDialog = false
                }
            )
        }
    }
}

@Composable
fun ProfileInfoRow(
    label: String,
    value: String,
    editable: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.width(80.dp),
            color = Color.Gray
        )
        
        Text(
            text = value,
            modifier = Modifier.weight(1f)
        )
        
        if (editable) {
            IconButton(onClick = { /* 编辑 */ }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "编辑$label",
                    tint = PrimaryColor
                )
            }
        }
    }
}

@Composable
fun ProfileEditableRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.width(80.dp),
            color = Color.Gray
        )
        
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryColor,
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimplePasswordChangeDialog(
    onDismiss: () -> Unit,
    onConfirm: (currentPassword: String, newPassword: String) -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("修改密码") },
        text = {
            Column {
                // 错误消息显示
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                
                // 当前密码
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = { Text("当前密码") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = { 
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "密码图标"
                        ) 
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                
                // 新密码
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("新密码 (8-20位)") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = { 
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "密码图标"
                        ) 
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                
                // 确认新密码
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("确认新密码") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = { 
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "密码图标"
                        ) 
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // 验证密码
                    when {
                        currentPassword.isEmpty() -> {
                            errorMessage = "请输入当前密码"
                        }
                        newPassword.length < 8 || newPassword.length > 20 -> {
                            errorMessage = "新密码长度必须在8-20位之间"
                        }
                        newPassword != confirmPassword -> {
                            errorMessage = "两次输入的新密码不一致"
                        }
                        else -> {
                            errorMessage = null
                            onConfirm(currentPassword, newPassword)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
            ) {
                Text("确认修改")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
} 