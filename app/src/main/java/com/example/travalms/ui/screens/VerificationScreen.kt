package com.example.travalms.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import com.example.travalms.ui.theme.PrimaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationScreen(
    onBackClick: () -> Unit,
    onVerificationComplete: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var idNumber by remember { mutableStateOf("") }
    var agreedToTerms by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("实名认证", fontWeight = FontWeight.Bold) },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 姓名输入框
            OutlinedTextField(
                value = name,
                onValueChange = { 
                    name = it
                    isError = false 
                },
                label = { Text("姓名") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                isError = isError && name.isEmpty()
            )
            
            // 身份证号码输入框
            OutlinedTextField(
                value = idNumber,
                onValueChange = { 
                    idNumber = it
                    isError = false 
                },
                label = { Text("身份证号码") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = isError && idNumber.isEmpty()
            )
            
            // 同意用户服务协议和隐私政策
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = agreedToTerms,
                    onCheckedChange = { agreedToTerms = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = PrimaryColor
                    )
                )
                
                Text(
                    text = "同意用户服务协议和隐私政策条款",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            
            if (isError) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            
            // 确定按钮
            Button(
                onClick = {
                    when {
                        name.isEmpty() -> {
                            isError = true
                            errorMessage = "请输入姓名"
                        }
                        idNumber.isEmpty() -> {
                            isError = true
                            errorMessage = "请输入身份证号码"
                        }
                        !isValidIdNumber(idNumber) -> {
                            isError = true
                            errorMessage = "身份证号码格式不正确"
                        }
                        !agreedToTerms -> {
                            isError = true
                            errorMessage = "请同意用户服务协议和隐私政策"
                        }
                        else -> {
                            // 提交认证请求
                            onVerificationComplete()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor
                )
            ) {
                Text("确定")
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 注释信息
            Text(
                text = "(可能使用API接口？还是人工审核？)",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// 简单验证身份证号码格式 (15位或18位)
private fun isValidIdNumber(idNumber: String): Boolean {
    return idNumber.length == 18 || idNumber.length == 15
} 