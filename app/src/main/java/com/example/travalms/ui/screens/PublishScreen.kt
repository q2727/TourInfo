package com.example.travalms.ui.screens

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import com.example.travalms.ui.theme.PrimaryColor
import com.example.travalms.ui.theme.PrimaryLight
import androidx.navigation.NavHostController
import com.example.travalms.ui.navigation.AppRoutes

/**
 * 发布页面
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishScreen(
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onMessageClick: () -> Unit,
    onProfileClick: () -> Unit,
    navController: NavHostController
) {
    // 表单状态
    var selectedCategory by remember { mutableStateOf("同业社") }
    var routeName by remember { mutableStateOf("") }
    var adultPrice by remember { mutableStateOf("") }
    var remainingSlots by remember { mutableStateOf("") }
    var startLocation by remember { mutableStateOf("") }
    var groupType by remember { mutableStateOf("纯玩团") }
    var startDate by remember { mutableStateOf("2025-03-11") }
    var endDate by remember { mutableStateOf("2025-03-21") }
    var description by remember { mutableStateOf("") }
    var detailLink by remember { mutableStateOf("选填") }
    var publishNode by remember { mutableStateOf("点击选择") }
    var validDays by remember { mutableStateOf("15") }
    
    // 添加搜索状态 (简单添加变量，但不修改UI)
    var searchText by remember { mutableStateOf("") }

    // 添加地点选择对话框控制
    var showLocationSelector by remember { mutableStateOf(false) }

    // 添加日期选择器控制状态
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    // 添加团型下拉菜单控制
    var showGroupTypeDropdown by remember { mutableStateOf(false) }

    // 节点选择对话框控制
    var showNodeSelector by remember { mutableStateOf(false) }

    // 发布类别选项
    val categories = listOf("同业社", "地接社", "组团社", "景区", "宾馆", "车队", "招聘", "签证", "票务", "其他")
    
    // 类别颜色映射
    val categoryColors = mapOf(
        "同业社" to PrimaryLight,
        "地接社" to PrimaryLight,
        "组团社" to PrimaryLight,
        "景区" to PrimaryLight,
        "宾馆" to PrimaryLight,
        "车队" to PrimaryLight,
        "招聘" to PrimaryLight,
        "签证" to PrimaryLight,
        "票务" to PrimaryLight,
        "其他" to PrimaryLight
    )

    // 类别边框颜色
    val categoryBorderColors = mapOf(
        "同业社" to PrimaryColor.copy(alpha = 0.4f),
        "地接社" to PrimaryColor.copy(alpha = 0.4f),
        "组团社" to PrimaryColor.copy(alpha = 0.4f),
        "景区" to PrimaryColor.copy(alpha = 0.4f),
        "宾馆" to PrimaryColor.copy(alpha = 0.4f),
        "车队" to PrimaryColor.copy(alpha = 0.4f),
        "招聘" to PrimaryColor.copy(alpha = 0.4f),
        "签证" to PrimaryColor.copy(alpha = 0.4f),
        "票务" to PrimaryColor.copy(alpha = 0.4f),
        "其他" to PrimaryColor.copy(alpha = 0.4f)
    )

    val scrollState = rememberScrollState()

    // 解析日期的格式
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // 获取当前日期作为默认值
    val currentDate = LocalDate.now()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("创建发布", fontWeight = FontWeight.Bold) },
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
        bottomBar = {
            BottomNavigation(
                backgroundColor = Color.White,
                modifier = Modifier.height(56.dp)
            ) {
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "产品") },
                    label = { Text("产品", fontSize = 12.sp) },
                    selected = false,
                    onClick = onHomeClick,
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = Color.Gray
                )

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Add, contentDescription = "发布") },
                    label = { Text("发布", fontSize = 12.sp) },
                    selected = true,
                    onClick = {
                        // 如果在发布表单页，点击应导航到我的发布页面
                        navController.navigate(AppRoutes.MY_POSTS) {
                            popUpTo(AppRoutes.PUBLISH) { inclusive = true }
                        }
                    },
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = Color.Gray
                )

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Email, contentDescription = "消息") },
                    label = { Text("消息", fontSize = 12.sp) },
                    selected = false,
                    onClick = onMessageClick,
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = Color.Gray
                )

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "我的") },
                    label = { Text("我的", fontSize = 12.sp) },
                    selected = false,
                    onClick = onProfileClick,
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = Color.Gray
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // 发布表单界面
            // 发布类别
            Text(
                text = "发布类别:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // 类别选择器 - 使用固定高度的Row代替LazyGrid
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CategoryButton(
                    text = "同业社",
                    selected = selectedCategory == "同业社",
                    backgroundColor = categoryColors["同业社"] ?: Color.LightGray,
                    borderColor = categoryBorderColors["同业社"] ?: Color.Gray,
                    onClick = { selectedCategory = "同业社" },
                    modifier = Modifier.weight(1f)
                )

                CategoryButton(
                    text = "地接社",
                    selected = selectedCategory == "地接社",
                    backgroundColor = categoryColors["地接社"] ?: Color.LightGray,
                    borderColor = categoryBorderColors["地接社"] ?: Color.Gray,
                    onClick = { selectedCategory = "地接社" },
                    modifier = Modifier.weight(1f)
                )

                CategoryButton(
                    text = "组团社",
                    selected = selectedCategory == "组团社",
                    backgroundColor = categoryColors["组团社"] ?: Color.LightGray,
                    borderColor = categoryBorderColors["组团社"] ?: Color.Gray,
                    onClick = { selectedCategory = "组团社" },
                    modifier = Modifier.weight(1f)
                )

                CategoryButton(
                    text = "景区",
                    selected = selectedCategory == "景区",
                    backgroundColor = categoryColors["景区"] ?: Color.LightGray,
                    borderColor = categoryBorderColors["景区"] ?: Color.Gray,
                    onClick = { selectedCategory = "景区" },
                    modifier = Modifier.weight(1f)
                )
            }

            // 第二行类别
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CategoryButton(
                    text = "宾馆",
                    selected = selectedCategory == "宾馆",
                    backgroundColor = categoryColors["宾馆"] ?: Color.LightGray,
                    borderColor = categoryBorderColors["宾馆"] ?: Color.Gray,
                    onClick = { selectedCategory = "宾馆" },
                    modifier = Modifier.weight(1f)
                )

                CategoryButton(
                    text = "车队",
                    selected = selectedCategory == "车队",
                    backgroundColor = categoryColors["车队"] ?: Color.LightGray,
                    borderColor = categoryBorderColors["车队"] ?: Color.Gray,
                    onClick = { selectedCategory = "车队" },
                    modifier = Modifier.weight(1f)
                )

                CategoryButton(
                    text = "招聘",
                    selected = selectedCategory == "招聘",
                    backgroundColor = categoryColors["招聘"] ?: Color.LightGray,
                    borderColor = categoryBorderColors["招聘"] ?: Color.Gray,
                    onClick = { selectedCategory = "招聘" },
                    modifier = Modifier.weight(1f)
                )

                CategoryButton(
                    text = "签证",
                    selected = selectedCategory == "签证",
                    backgroundColor = categoryColors["签证"] ?: Color.LightGray,
                    borderColor = categoryBorderColors["签证"] ?: Color.Gray,
                    onClick = { selectedCategory = "签证" },
                    modifier = Modifier.weight(1f)
                )
            }

            // 第三行类别
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CategoryButton(
                    text = "票务",
                    selected = selectedCategory == "票务",
                    backgroundColor = categoryColors["票务"] ?: Color.LightGray,
                    borderColor = categoryBorderColors["票务"] ?: Color.Gray,
                    onClick = { selectedCategory = "票务" },
                    modifier = Modifier.weight(1f)
                )

                CategoryButton(
                    text = "其他",
                    selected = selectedCategory == "其他",
                    backgroundColor = categoryColors["其他"] ?: Color.LightGray,
                    borderColor = categoryBorderColors["其他"] ?: Color.Gray,
                    onClick = { selectedCategory = "其他" },
                    modifier = Modifier.weight(1f)
                )

                // 添加两个空的权重，保持布局一致
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.weight(1f))
            }

            // 路线名称
            FormTextField(
                value = routeName,
                onValueChange = { routeName = it },
                placeholder = "输入路线名称",
                label = "路线名称",
                backgroundColor = Color(0xFFF5F5F5)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 同业价格
            FormTextField(
                value = adultPrice,
                onValueChange = { adultPrice = it },
                placeholder = "输入价格",
                label = "同业价格 成人:",
                backgroundColor = Color(0xFFF5F5F5)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 剩余名额
            FormTextField(
                value = remainingSlots,
                onValueChange = { remainingSlots = it },
                placeholder = "输入剩余名额",
                label = "剩余名额",
                backgroundColor = Color(0xFFF5F5F5)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 起始地点
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "起始地点",
                    modifier = Modifier.width(80.dp),
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .clickable { showLocationSelector = true }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = startLocation.ifEmpty { "请选择起始地点" },
                            color = if (startLocation.isEmpty()) Color.Gray else Color.Black,
                            fontSize = 14.sp
                        )

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "选择起始地点",
                            tint = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 团型
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "团型",
                    modifier = Modifier.width(80.dp),
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .clickable { showGroupTypeDropdown = true }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = groupType,
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

            DropdownMenu(
                expanded = showGroupTypeDropdown,
                onDismissRequest = { showGroupTypeDropdown = false },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                listOf("纯玩团", "半自助", "自由行", "跟团游").forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            groupType = type
                            showGroupTypeDropdown = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 团期选择
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "团期选择:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // 开始日期选择
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "开始日期",
                        modifier = Modifier.width(80.dp),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .clickable { showStartDatePicker = true }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = startDate,
                                fontSize = 14.sp
                            )

                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "选择日期",
                                tint = Color.Gray
                            )
                        }
                    }
                }

                // 结束日期选择
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "结束日期",
                        modifier = Modifier.width(80.dp),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .clickable { showEndDatePicker = true }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = endDate,
                                fontSize = 14.sp
                            )

                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "选择日期",
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 添加描述
            FormTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = "输入行程描述（每条特色用换行分隔）",
                label = "行程描述",
                backgroundColor = Color(0xFFF5F5F5),
                singleLine = false,
                maxLines = 5,
                height = 120.dp
            )

            Spacer(modifier = Modifier.height(24.dp))
            
            // 消息预览部分
            MessagePreviewSection(routeName, description)
            
            Spacer(modifier = Modifier.height(24.dp))

            // 详情链接
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "详情链接",
                    modifier = Modifier.width(80.dp),
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                OutlinedTextField(
                    value = detailLink,
                    onValueChange = { detailLink = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("填写详情链接", color = Color.Gray) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 发布节点
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "发布节点",
                    modifier = Modifier.width(80.dp),
                    color = Color.Gray
                )

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clickable { showNodeSelector = true },
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFF5F5F5)
                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .fillMaxSize()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                publishNode,
                                color = if (publishNode == "点击选择") Color.Gray else Color.Black
                            )

                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "选择发布节点",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 有效期
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "有效期",
                    modifier = Modifier.width(80.dp),
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                TextField(
                    value = validDays,
                    onValueChange = { validDays = it },
                    modifier = Modifier.width(80.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFF3F51B5)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "天",
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 黄色提示图标
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(PrimaryColor, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "!",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "1",
                        fontSize = 14.sp,
                        color = PrimaryColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 提交按钮
            Button(
                onClick = {
                    // 处理提交逻辑
                    navController.navigate(AppRoutes.MY_POSTS)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "发布信息",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // 开始日期选择器对话框
    if (showStartDatePicker) {
        DatePickerDialog(
            onDismiss = { showStartDatePicker = false },
            onDateSelected = { selectedDate ->
                startDate = selectedDate.format(dateFormatter)
                showStartDatePicker = false
            },
            initialDate = LocalDate.parse(startDate, dateFormatter)
        )
    }

    // 结束日期选择器对话框
    if (showEndDatePicker) {
        DatePickerDialog(
            onDismiss = { showEndDatePicker = false },
            onDateSelected = { selectedDate ->
                endDate = selectedDate.format(dateFormatter)
                showEndDatePicker = false
            },
            initialDate = LocalDate.parse(endDate, dateFormatter)
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

    // 地点选择对话框
    if (showLocationSelector) {
        LocationSelector(
            onDismiss = { showLocationSelector = false },
            onLocationSelected = { location ->
                startLocation = location
                showLocationSelector = false
            }
        )
    }
}

@Composable
fun CategoryButton(
    text: String,
    selected: Boolean,
    backgroundColor: Color,
    borderColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) Color(0xFF3F51B5) else Color.Gray,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    label: String,
    backgroundColor: Color = Color.White,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    height: Dp = 48.dp
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            modifier = Modifier.width(80.dp),
            color = Color.Gray,
            fontSize = 14.sp
        )
        
        Box(
            modifier = Modifier
                .weight(1f)
                .height(height)
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = singleLine,
                maxLines = maxLines,
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishNodeSelector(
    onDismiss: () -> Unit,
    onNodeSelected: (String) -> Unit
) {
    // 模拟数据
    val provinces = listOf("广东", "广西", "北京", "上海", "江苏", "浙江", "四川", "湖南", "福建", "山东")
    val cities = listOf("广州", "深圳", "珠海", "佛山", "东莞", "惠州", "中山", "江门", "肇庆", "清远")
    val attractions = listOf("长隆欢乐世界", "白云山", "珠江夜游", "小洲村", "陈家祠", "沙面", "广州塔", "宝墨园", "黄埔军校", "南沙湿地")
    val companies = listOf("广州旅游集团", "南湖国旅", "广之旅", "中青旅", "携程旅游", "飞猪旅行", "广东旅游", "康辉旅行社", "广州中旅", "白云国际旅行社")

    var selectedTabIndex by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedItem by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 顶部栏
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PrimaryColor)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = "选择发布节点",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )

                    // 右侧空白平衡布局
                    Spacer(modifier = Modifier.width(48.dp))
                }

                // 搜索框
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { newValue -> searchQuery = newValue },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        placeholder = { 
                            Text("搜索", color = Color.Gray) 
                        },
                        leadingIcon = { 
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "搜索",
                                tint = Color(0xFF2196F3)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        )
                    )
                }

                // 选项卡
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier.fillMaxWidth(),
                    contentColor = PrimaryColor,
                    indicator = { tabPositions ->
                        Box(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                .height(3.dp)
                                .background(
                                    color = PrimaryColor,
                                    shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)
                                )
                        )
                    },
                    divider = {}
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = { Text("省市") }
                    )

                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = { Text("景区") }
                    )

                    Tab(
                        selected = selectedTabIndex == 2,
                        onClick = { selectedTabIndex = 2 },
                        text = { Text("公司") }
                    )
                }

                // 内容区域
                Box(modifier = Modifier.weight(1f)) {
                    when (selectedTabIndex) {
                        0 -> {
                            // 省市选项
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                provinces.forEach { province ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { selectedItem = province }
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = selectedItem == province,
                                            onClick = { selectedItem = province }
                                        )
                                        Text(
                                            text = province,
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                                    Divider()
                                }
                            }
                        }
                        1 -> {
                            // 景区选项
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                attractions.forEach { attraction ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { selectedItem = attraction }
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = selectedItem == attraction,
                                            onClick = { selectedItem = attraction }
                                        )
                                        Text(
                                            text = attraction,
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                                    Divider()
                                }
                            }
                        }
                        2 -> {
                            // 公司选项
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                companies.forEach { company ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { selectedItem = company }
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = selectedItem == company,
                                            onClick = { selectedItem = company }
                                        )
                                        Text(
                                            text = company,
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                                    Divider()
                                }
                            }
                        }
                    }
                }

                // 底部按钮
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 取消按钮
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE0F2F1)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "取消",
                            fontSize = 16.sp,
                            color = PrimaryColor
                        )
                    }
                    
                    // 确认按钮
                    Button(
                        onClick = {
                            selectedItem?.let {
                                onNodeSelected(it)
                                onDismiss()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryColor
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = selectedItem != null
                    ) {
                        Text(
                            text = "确认",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSelector(
    onDismiss: () -> Unit,
    onLocationSelected: (String) -> Unit
) {
    // 按字母分组的地点数据
    val locationGroups = mapOf(
        "A" to listOf(
            LocationItem("安徽", ""),
            LocationItem("澳门", ""),
            LocationItem("阿勒泰", "新疆维吾尔自治区"),
            LocationItem("安庆", "安徽省"),
            LocationItem("阿里山", "台湾省")
        ),
        "B" to listOf(
            LocationItem("北京", ""),
            LocationItem("包头", "内蒙古自治区"),
            LocationItem("白洋淀", "河北省"),
            LocationItem("蚌埠", "安徽省"),
            LocationItem("滨海新区", "天津省"),
            LocationItem("白城", "吉林省")
        )
        // 其他字母组可以按需添加
    )

    var searchQuery by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf<LocationItem?>(null) }
    var selectedTabIndex by remember { mutableStateOf(0) }

    // 字母导航列表
    val alphabet = ('A'..'Z').filter {
        locationGroups.containsKey(it.toString())
    }.map { it.toString() }

    // 当前选中的字母索引，用于滚动定位
    var currentLetterIndex by remember { mutableStateOf(0) }

    // 创建字母对应的滚动位置
    val letterScrollStates = remember {
        alphabet.associateWith { mutableStateOf(0f) }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 顶部栏
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PrimaryColor)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = "检索",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.width(48.dp))
                }

                // 搜索框
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { newValue -> searchQuery = newValue },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        placeholder = { 
                            Text("搜索", color = Color.Gray) 
                        },
                        leadingIcon = { 
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "搜索",
                                tint = Color(0xFF2196F3)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        )
                    )
                }

                // 选项卡
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier.fillMaxWidth(),
                    contentColor = PrimaryColor,
                    indicator = { tabPositions ->
                        Box(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                .height(3.dp)
                                .background(
                                    color = PrimaryColor,
                                    shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)
                                )
                        )
                    },
                    divider = {}
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = { Text("省市") }
                    )

                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = { Text("景区") }
                    )

                    Tab(
                        selected = selectedTabIndex == 2,
                        onClick = { selectedTabIndex = 2 },
                        text = { Text("公司") }
                    )
                }

                // 内容区域
                Box(modifier = Modifier.weight(1f)) {
                    // 主内容列表
                    val scrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        // 显示分组的地点
                        locationGroups.forEach { (letter, locations) ->
                            // 过滤搜索结果
                            val filteredLocations = if (searchQuery.isEmpty()) {
                                locations
                            } else {
                                locations.filter {
                                    it.name.contains(searchQuery, ignoreCase = true) ||
                                    it.province.contains(searchQuery, ignoreCase = true)
                                }
                            }

                            if (filteredLocations.isNotEmpty()) {
                                // 字母标题
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFEEEEEE))
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = letter,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }

                                // 地点列表
                                filteredLocations.forEach { location ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                selectedLocation = location
                                                onLocationSelected(location.name)
                                            }
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = selectedLocation == location,
                                            onClick = {
                                                selectedLocation = location
                                                onLocationSelected(location.name)
                                            }
                                        )

                                        Column(
                                            modifier = Modifier.padding(start = 8.dp)
                                        ) {
                                            Text(
                                                text = location.name,
                                                fontSize = 16.sp
                                            )

                                            if (location.province.isNotEmpty()) {
                                                Text(
                                                    text = location.province,
                                                    fontSize = 14.sp,
                                                    color = Color.Gray
                                                )
                                            }
                                        }
                                    }

                                    Divider()
                                }
                            }
                        }
                    }

                    // 字母导航栏
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        alphabet.forEachIndexed { index, letter ->
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        currentLetterIndex = index
                                        // 这里可以添加滚动到对应字母的逻辑
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = letter,
                                    fontSize = 12.sp,
                                    color = if (currentLetterIndex == index)
                                        Color(0xFF00B894) else Color.Gray
                                )
                            }
                        }
                    }
                }

                // 底部按钮
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 取消按钮
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE0F2F1)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "取消",
                            fontSize = 16.sp,
                            color = PrimaryColor
                        )
                    }
                    
                    // 确认按钮
                    Button(
                        onClick = {
                            selectedLocation?.let {
                                onLocationSelected(it.name)
                                onDismiss()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryColor
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = selectedLocation != null
                    ) {
                        Text(
                            text = "确认",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// 地点数据类
data class LocationItem(
    val name: String,
    val province: String
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    initialDate: LocalDate
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.atStartOfDay().toEpochSecond(java.time.ZoneOffset.UTC) * 1000
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "选择日期",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                DatePicker(
                    state = datePickerState,
                    title = null,
                    headline = null,
                    showModeToggle = false
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("取消")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val selectedDate = java.time.Instant.ofEpochMilli(millis)
                                    .atZone(java.time.ZoneId.systemDefault())
                                    .toLocalDate()
                                onDateSelected(selectedDate)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00B894)
                        )
                    ) {
                        Text("确认")
                    }
                }
            }
        }
    }
}

/**
 * 消息预览卡片，用于显示发布后的消息预览
 */
@Composable
fun MessagePreviewCard(
    title: String,
    features: List<String>,
    validPeriod: String = "3天6:30",
    publishLocations: List<String> = listOf("北京", "上海", "海淀")
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 标题
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            
            // 行程特色内容
            features.forEach { feature ->
                Text(
                    text = feature,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
            
            // 有效期
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                
                Text(
                    text = "有效期：",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                
                Text(
                    text = validPeriod,
                    fontSize = 14.sp,
                    color = Color(0xFFFF6E40),
                    fontWeight = FontWeight.Medium
                )
            }
            
            // 底部分隔线
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.LightGray
            )
            
            // 发布节点
            Text(
                text = "发布节点：",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            // 发布地点标签
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                publishLocations.forEach { location ->
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .background(
                                color = PrimaryColor.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = location,
                            fontSize = 12.sp,
                            color = PrimaryColor
                        )
                    }
                }
            }
        }
    }
}

// 在PublishScreen中添加预览部分，在提交前预览
// 你可以在表单完成后添加以下代码来展示预览
@Composable
fun MessagePreviewSection(
    routeName: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "发布预览",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // 将描述转换为行程特色列表
        val features = if (description.isNotEmpty()) {
            description.split("\n").filter { it.isNotBlank() }
        } else {
            listOf(
                "1.在上海外国语大学浸入式英语环境中学习英语，培养孩子良好的英语语感及口语运用能力。",
                "2.上海淮景点畅游、博物馆、知名大学参访，展开真正的上海文化寻根游学之旅。",
                "3.上海迪斯尼乐园畅游，学习游乐两不误。"
            )
        }
        
        MessagePreviewCard(
            title = routeName.ifEmpty { "上海外国语大学体验+迪士尼6日夏令营 ❤" },
            features = features
        )
    }
}
