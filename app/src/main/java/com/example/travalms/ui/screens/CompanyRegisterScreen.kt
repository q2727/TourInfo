package com.example.travalms.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travalms.ui.theme.PrimaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyRegisterScreen(
    onBackClick: () -> Unit,
    onRegisterSubmit: () -> Unit
) {
    var companyName by remember { mutableStateOf("") }
    var companyType by remember { mutableStateOf("同业社") }
    var adminId by remember { mutableStateOf("") }
    var contactName by remember { mutableStateOf("") }
    var contactPhone by remember { mutableStateOf("") }
    var creditCode by remember { mutableStateOf("") }
    var companyDescription by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    
    val companyTypes = listOf("同业社", "地接社", "组团社", "景区", "宾馆", "车队", "票务公司")
    val scrollState = rememberScrollState()
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("企业注册", fontWeight = FontWeight.Bold) },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 企业名称
            FormField(label = "企业名称") {
                OutlinedTextField(
                    value = companyName,
                    onValueChange = { companyName = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = PrimaryColor
                    ),
                    shape = RoundedCornerShape(4.dp)
                )
            }
            
            // 企业类型
            FormField(label = "企业类型") {
                Box {
                    OutlinedTextField(
                        value = companyType,
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "选择类型",
                                Modifier.clickable { expanded = true }
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.LightGray,
                            focusedBorderColor = PrimaryColor
                        ),
                        shape = RoundedCornerShape(4.dp)
                    )
                    
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        companyTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type) },
                                onClick = {
                                    companyType = type
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
            
            // 初始管理员ID
            FormField(label = "初始管理员ID") {
                OutlinedTextField(
                    value = adminId,
                    onValueChange = { adminId = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = PrimaryColor
                    ),
                    shape = RoundedCornerShape(4.dp)
                )
            }
            
            // 联系人姓名
            FormField(label = "联系人姓名") {
                OutlinedTextField(
                    value = contactName,
                    onValueChange = { contactName = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = PrimaryColor
                    ),
                    shape = RoundedCornerShape(4.dp)
                )
            }
            
            // 联系人电话
            FormField(label = "联系人电话") {
                OutlinedTextField(
                    value = contactPhone,
                    onValueChange = { contactPhone = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = PrimaryColor
                    ),
                    shape = RoundedCornerShape(4.dp)
                )
            }
            
            // 企业统一社会信用代码
            FormField(label = "企业统一\n社会信用代码") {
                OutlinedTextField(
                    value = creditCode,
                    onValueChange = { creditCode = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = PrimaryColor
                    ),
                    shape = RoundedCornerShape(4.dp)
                )
            }
            
            // 企业营业执照
            FormField(label = "企业营业执照") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFF5F5F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier
                                .size(36.dp)
                                .rotate(45f)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "点击上传图片",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            
            // 企业介绍
            FormField(label = "企业介绍") {
                OutlinedTextField(
                    value = companyDescription,
                    onValueChange = { companyDescription = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = PrimaryColor
                    ),
                    shape = RoundedCornerShape(4.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 提交注册按钮
            Button(
                onClick = onRegisterSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "提交注册",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun FormField(
    label: String,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            modifier = Modifier
                .width(100.dp)
                .padding(top = 8.dp),
            fontSize = 15.sp,
            color = Color.DarkGray
        )
        
        Box(modifier = Modifier.weight(1f)) {
            content()
        }
    }
} 