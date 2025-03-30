package com.example.travalms.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.travalms.ui.screens.PostItem
import com.example.travalms.ui.theme.PrimaryColor
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

// 导入已存在的日期选择器
// 1. 确保从PublishScreen导入DatePickerDialog
import com.example.travalms.ui.screens.DatePickerDialog as ExistingDatePickerDialog

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostEditScreen(
    postId: String?,
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    // 这里应该根据postId获取帖子数据
    // 以下是示例数据，实际应用中应该从数据源获取
    val post = remember {
        // 假设PostItem.id是Int类型
        val id = postId?.toIntOrNull() ?: 0
        PostItem(
            id = id,
            title = "上海外国语大学体验+迪士尼6日夏令营",
            dates = "7月25日8月7、9、11、13、15、17、19、21、23、25日",
            feature = "1.在上海外国语大学浸入式英语环境中",
            remainingSlots = 10,
            price = 100,
            daysExpired = 15
        )
    }

    // 状态管理
    var title by remember { mutableStateOf(post.title) }
    var adultPrice by remember { mutableStateOf(post.price.toString()) }
    var remainingSlots by remember { mutableStateOf(post.remainingSlots.toString()) }
    var startLocation by remember { mutableStateOf("江苏 - 南京") }
    var tourType by remember { mutableStateOf("纯玩团") }
    var startDate by remember { mutableStateOf("2025-03-11") }
    var endDate by remember { mutableStateOf("2025-03-21") }
    var description by remember { mutableStateOf("发团日期:7月25日8月7,9,11,13,15,17,1...") }
    var detailLink by remember { mutableStateOf("www.detai-link.com") }
    var publishNode by remember { mutableStateOf("南京, 南京旅游社") }
    var expiryDays by remember { mutableStateOf("15") }
    var seniorPrice by remember { mutableStateOf("") }
    var childPrice by remember { mutableStateOf("") }

    // 是否展开团型选择菜单
    var showTourTypeDropdown by remember { mutableStateOf(false) }

    // 添加日期选择器控制状态
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    // 日期格式化器
    val dateFormatter = remember { DateTimeFormatter.ofPattern("yyyy-MM-dd") }

    // 在函数末尾添加这个部分，在return前
    // 节点选择对话框
    var showNodeSelector by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("修改信息", fontWeight = FontWeight.Bold) },
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
                .background(Color(0xFFF5F5F5))
        ) {
            // 表单区域
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 路线名称
                FormField(
                    label = "路线名称",
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth()
                )

                // 同业价格
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "同业价格",
                        modifier = Modifier.width(80.dp),
                        color = Color.Gray
                    )
                    
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        // 成人价格
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "成人：",
                                fontSize = 14.sp
                            )
                            OutlinedTextField(
                                value = adultPrice,
                                onValueChange = { adultPrice = it },
                                placeholder = { Text("输入价格") },
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(52.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "元/人",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // 老人价格
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "老人：",
                                fontSize = 14.sp
                            )
                            OutlinedTextField(
                                value = seniorPrice,
                                onValueChange = { seniorPrice = it },
                                placeholder = { Text("输入价格") },
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(52.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "元/人",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // 儿童价格
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "儿童：",
                                fontSize = 14.sp
                            )
                            OutlinedTextField(
                                value = childPrice,
                                onValueChange = { childPrice = it },
                                placeholder = { Text("输入价格") },
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(52.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "元/人",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Divider(color = Color(0xFFEEEEEE))

                // 剩余名额
                FormField(
                    label = "剩余名额",
                    value = remainingSlots,
                    onValueChange = { remainingSlots = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Divider(color = Color(0xFFEEEEEE))

                // 起止地点
                FormField(
                    label = "起止地点",
                    value = startLocation,
                    onValueChange = { startLocation = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Divider(color = Color(0xFFEEEEEE))

                // 团型选择
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "团型",
                        modifier = Modifier.width(80.dp),
                        color = Color.Gray
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .clickable { showTourTypeDropdown = true }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = tourType,
                                fontSize = 14.sp
                            )

                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "选择团型",
                                tint = Color.Gray
                            )
                        }
                    }
                }

                // 添加团型下拉菜单
                DropdownMenu(
                    expanded = showTourTypeDropdown,
                    onDismissRequest = { showTourTypeDropdown = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    listOf("纯玩团", "半自助", "自由行", "跟团游").forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                tourType = type
                                showTourTypeDropdown = false
                            }
                        )
                    }
                }

                Divider(color = Color(0xFFEEEEEE))

                // 行程时间
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "团期选择:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("开始日期", modifier = Modifier.width(80.dp))

                        Text(
                            text = startDate,
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                                .padding(vertical = 12.dp, horizontal = 16.dp)
                        )

                        IconButton(
                            onClick = { showStartDatePicker = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "选择日期",
                                tint = Color.Gray
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("结束日期", modifier = Modifier.width(80.dp))

                        Text(
                            text = endDate,
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                                .padding(vertical = 12.dp, horizontal = 16.dp)
                        )

                        IconButton(
                            onClick = { showEndDatePicker = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "选择日期",
                                tint = Color.Gray
                            )
                        }
                    }
                }

                Divider(color = Color(0xFFEEEEEE))

                // 行程描述
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "行程描述",
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryColor,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                        )
                    )
                }

                Divider(color = Color(0xFFEEEEEE))

                // 详情链接
                FormField(
                    label = "详情链接",
                    value = detailLink,
                    onValueChange = { detailLink = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Divider(color = Color(0xFFEEEEEE))

                // 发布节点
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "发布节点",
                        modifier = Modifier.width(80.dp),
                        color = Color.Gray
                    )

                    // 使用与团型选择相同的Box结构
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .clickable { showNodeSelector = true }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = publishNode,
                                fontSize = 14.sp
                            )

                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "选择发布节点",
                                tint = Color.Gray
                            )
                        }
                    }
                }

                Divider(color = Color(0xFFEEEEEE))

                // 有效期
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "有效期",
                        modifier = Modifier.width(80.dp),
                        color = Color.Gray
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color(0xFFFFF9C4),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(24.dp)
                        ) {
                            Text(
                                text = "⚡",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Text("$expiryDays 天")
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 底部按钮区域
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 取消发布按钮
                Button(
                    onClick = onBackClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = ButtonDefaults.outlinedButtonBorder
                ) {
                    Text(
                        text = "取消发布",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }

                // 确认发布按钮 - 修改为使用PrimaryColor而不是硬编码的绿色
                Button(
                    onClick = onSaveSuccess,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryColor  // 使用应用主题色
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "确认发布",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }

    // 显示开始日期选择器
    if (showStartDatePicker) {
        ExistingDatePickerDialog(
            onDismiss = { showStartDatePicker = false },
            onDateSelected = { selectedDate ->
                startDate = selectedDate.format(dateFormatter)
                showStartDatePicker = false
            },
            initialDate = try {
                LocalDate.parse(startDate, dateFormatter)
            } catch (e: Exception) {
                LocalDate.now()
            }
        )
    }

    // 显示结束日期选择器
    if (showEndDatePicker) {
        ExistingDatePickerDialog(
            onDismiss = { showEndDatePicker = false },
            onDateSelected = { selectedDate ->
                endDate = selectedDate.format(dateFormatter)
                showEndDatePicker = false
            },
            initialDate = try {
                LocalDate.parse(endDate, dateFormatter)
            } catch (e: Exception) {
                LocalDate.now().plusDays(10)
            }
        )
    }

    // 节点选择对话框
    if (showNodeSelector) {
        PublishNodeSelector(
            onDismiss = { showNodeSelector = false },
            onNodeSelected = {
                publishNode = it
                showNodeSelector = false
            }
        )
    }
}

@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
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
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryColor,
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
            )
        )
    }
}