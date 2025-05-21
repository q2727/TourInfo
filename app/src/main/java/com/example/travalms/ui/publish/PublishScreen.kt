package com.example.travalms.ui.publish

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.DatePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.unit.Dp
import com.example.travalms.ui.theme.PrimaryColor
import com.example.travalms.ui.theme.PrimaryLight
import androidx.navigation.NavHostController
import com.example.travalms.ui.navigation.AppRoutes
import androidx.compose.foundation.shape.CircleShape
import java.util.*
import com.example.travalms.ui.viewmodels.TailListItem
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Check
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travalms.ui.viewmodels.PublishViewModel
import android.util.Log
import androidx.compose.ui.focus.onFocusChanged
import com.example.travalms.data.remote.ConnectionState
import kotlinx.coroutines.launch
import com.example.travalms.data.api.NetworkModule
import com.example.travalms.data.api.ProductResponseItem
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.style.TextAlign

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
    var title by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }
    var validUntilDate by rememberSaveable { mutableStateOf(LocalDate.now().plusDays(15).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))) }
    var publishNode by rememberSaveable { mutableStateOf("点击选择") }
    
    // 产品列表状态
    var products by remember { mutableStateOf<List<ProductResponseItem>>(emptyList()) }
    var loadingProducts by remember { mutableStateOf(true) }
    var errorProducts by remember { mutableStateOf<String?>(null) }
    
    // 使用remember而不是rememberSaveable来存储产品对象
    var selectedProduct by remember { mutableStateOf<ProductResponseItem?>(null) }
    // 使用rememberSaveable存储产品标题
    var selectedProductTitle by rememberSaveable { mutableStateOf<String?>(null) }

    // 当selectedProductTitle改变时，更新selectedProduct
    LaunchedEffect(selectedProductTitle, products) {
        if (selectedProductTitle != null) {
            selectedProduct = products.find { it.title == selectedProductTitle }
        }
    }

    // 产品选择时同时更新两个状态
    fun updateSelectedProduct(product: ProductResponseItem) {
        selectedProduct = product
        selectedProductTitle = product.title
    }

    // 日期选择器状态
    var showValidUntilDatePicker by remember { mutableStateOf(false) }

    // 产品下拉菜单控制
    var showProductDropdown by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    // 解析日期的格式
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // 获取当前日期作为默认值
    val currentDate = LocalDate.now()

    // 获取ViewModel实例
    val viewModel: PublishViewModel = viewModel()
    // 监听发布状态
    val publishState by viewModel.uiState.collectAsState()

    // 添加这段代码，以便从节点选择器返回时获取选定的节点
    val selectedNodesResult = navController.currentBackStackEntry?.savedStateHandle?.get<String>("selected_nodes")
    val selectedNodeIdsResult = navController.currentBackStackEntry?.savedStateHandle?.get<ArrayList<String>>("selected_node_ids")

    // 当获取结果时更新publishNode
    LaunchedEffect(selectedNodesResult, selectedNodeIdsResult) {
        if (selectedNodesResult != null && selectedNodeIdsResult != null) {
            publishNode = selectedNodesResult
            // 更新ViewModel中的节点ID列表
            viewModel.setSelectedNodes(
                nodeIds = selectedNodeIdsResult,
                nodeNames = listOf(selectedNodesResult)
            )
            // 清除保存的状态以避免重复处理
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("selected_nodes")
            navController.currentBackStackEntry?.savedStateHandle?.remove<List<String>>("selected_node_ids")
        }
    }

    // 使用LaunchedEffect清除之前的发布状态
    LaunchedEffect(Unit) {
        viewModel.resetPublishState()

        // 添加日志显示当前连接状态
        val currentState = viewModel.connectionState.value
        Log.d("PublishScreen", "当前XMPP连接状态: $currentState, 是否已认证: ${currentState == ConnectionState.AUTHENTICATED}")

        // 强制刷新登录状态，确保与XMPPManager同步
        viewModel.refreshLoginState()
    }

    // 获取产品列表
    LaunchedEffect(Unit) {
        try {
            val api = NetworkModule.productApiService
            products = api.getProducts()
            loadingProducts = false
        } catch (e: Exception) {
            errorProducts = e.message
            loadingProducts = false
        }
    }

    // 发布完成后显示成功消息并导航
    LaunchedEffect(publishState.publishSuccess) {
        if (publishState.publishSuccess) {
            // 短暂延迟后导航，让用户看到成功消息
            kotlinx.coroutines.delay(1500)
            navController.navigate(AppRoutes.MY_POSTS)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("发布尾单", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                // 显示错误消息（如果有）
                publishState.errorMessage?.let { errorMessage ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "错误",
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }

                // 标题
                FormTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = "输入标题",
                    defaultContent = selectedProduct?.title ?: "",
                    label = "标题",
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 行程内容
                FormTextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = "输入行程内容（每条特色用换行分隔）",
                    defaultContent = selectedProduct?.description ?: "",
                    label = "行程内容:",
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    singleLine = false,
                    maxLines = 5,
                    height = 120.dp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 有效期至
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "有效期至:",
                        modifier = Modifier.width(80.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .clickable { showValidUntilDatePicker = true }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = validUntilDate,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "选择日期",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 发布节点
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "发布节点:",
                        modifier = Modifier.width(80.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clickable {
                                // 检查XMPP连接状态
                                if (viewModel.connectionState.value == ConnectionState.AUTHENTICATED) {
                                    // 已认证，导航到节点选择器
                                    navController.navigate(AppRoutes.PUBLISH_NODE_SELECTOR)
                                } else {
                                    // 未认证，先尝试刷新连接状态
                                    viewModel.refreshLoginState()
                                }
                            },
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ) {
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxSize()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = publishNode,
                                    color = if (publishNode == "点击选择")
                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                )

                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "选择发布节点",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 产品
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "产品:",
                        modifier = Modifier.width(80.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .clickable { showProductDropdown = true }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedProduct?.title ?: "未选择",
                                fontSize = 14.sp,
                                color = if (selectedProduct == null)
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )

                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "选择产品",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                DropdownMenu(
                    expanded = showProductDropdown,
                    onDismissRequest = { showProductDropdown = false },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .heightIn(max = 300.dp)
                ) {
                    if (loadingProducts) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else if (errorProducts != null) {
                        Text(
                            text = "加载失败: $errorProducts",
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        products.forEach { product ->
                            DropdownMenuItem(
                                text = { Text(product.title) },
                                onClick = {
                                    updateSelectedProduct(product)
                                    showProductDropdown = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 提交按钮
                Button(
                    onClick = {
                        // 创建尾单数据并发布
                        val tailListItem = TailListItem(
                            title = if (title.isNotEmpty()) title else selectedProduct?.title ?: "未选择产品",
                            description = if (content.isNotEmpty()) content else selectedProduct?.description ?: "",
                            price = selectedProduct?.price ?: 0.0,
                            originalPrice = selectedProduct?.price?.times(1.2) ?: 0.0,
                            startDate = LocalDate.now().plusDays(7).let { 
                                Date.from(it.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()) 
                            },
                            endDate = LocalDate.parse(validUntilDate, dateFormatter).let { 
                                Date.from(it.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()) 
                            },
                            contactPerson = "客服中心",
                            contactPhone = "13800138000",
                            location = "上海",
                            tags = listOf(selectedProduct?.title ?: "未知产品"),
                            productId = selectedProduct?.productId?.toLong(),
                            productTitle = selectedProduct?.title ?: ""
                        )
                        
                        // 调用ViewModel发布尾单
                        viewModel.publishTailList(tailListItem)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = selectedProduct != null // 必须选择产品才能发布
                ) {
                    Text("发布")
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // 发布进度指示器
            if (publishState.isPublishing) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .width(200.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "正在发布...",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // 发布成功提示
            if (publishState.publishSuccess) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .width(200.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "成功",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "发布成功！",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }

    // 有效期至日期选择器对话框
    if (showValidUntilDatePicker) {
        DatePickerDialog(
            onDismiss = { showValidUntilDatePicker = false },
            onDateSelected = { selectedDate ->
                validUntilDate = selectedDate.format(dateFormatter)
                showValidUntilDatePicker = false
            },
            initialDate = LocalDate.parse(validUntilDate, dateFormatter)
        )
    }
}

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    defaultContent: String = "",
    label: String,
    backgroundColor: Color = Color.White,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    height: Dp = 48.dp
) {
    var isFocused by remember { mutableStateOf(false) }
    var showDefaultContent by remember { mutableStateOf(true) }

    LaunchedEffect(defaultContent) {
        showDefaultContent = true
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            modifier = Modifier.width(80.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                onValueChange = { newValue ->
                    if (newValue.isNotEmpty()) {
                        showDefaultContent = false
                    }
                    else if (newValue.isEmpty() && !isFocused) {
                        showDefaultContent = true
                    }
                    onValueChange(newValue)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                        if (focusState.isFocused) {
                            showDefaultContent = false
                        }
                        else if (!focusState.isFocused && value.isEmpty()) {
                            showDefaultContent = true
                        }
                    },
                singleLine = singleLine,
                maxLines = maxLines,
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (value.isEmpty() && showDefaultContent && defaultContent.isNotEmpty()) {
                            Text(
                                text = defaultContent,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                                fontSize = 14.sp
                            )
                        } else if (value.isEmpty() && !showDefaultContent) {
                            Text(
                                text = placeholder,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
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
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "选择日期",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )

                // 自定义日历显示，替代DatePicker组件
                CustomDatePicker(
                    datePickerState = datePickerState,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            "取消",
                            color = MaterialTheme.colorScheme.primary
                        )
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
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            "确认",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    datePickerState: DatePickerState,
    modifier: Modifier = Modifier
) {
    val currentMonth = remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    
    // 获取当前月份的LocalDate对象
    val displayMonth = remember(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { millis ->
            LocalDate.ofEpochDay(millis / (24 * 60 * 60 * 1000))
                .withDayOfMonth(1)
        } ?: currentMonth.value
    }

    Column(modifier = modifier) {
        // 月份选择器
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${displayMonth.year}年${displayMonth.monthValue}月",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Row {
                IconButton(
                    onClick = {
                        val prevMonth = displayMonth.minusMonths(1)
                        datePickerState.setSelection(
                            prevMonth.atStartOfDay().toEpochSecond(java.time.ZoneOffset.UTC) * 1000
                        )
                    }
                ) {
                    Text("<", fontSize = 18.sp)
                }
                
                IconButton(
                    onClick = {
                        val nextMonth = displayMonth.plusMonths(1)
                        datePickerState.setSelection(
                            nextMonth.atStartOfDay().toEpochSecond(java.time.ZoneOffset.UTC) * 1000
                        )
                    }
                ) {
                    Text(">", fontSize = 18.sp)
                }
            }
        }

        // 星期表头
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("一", "二", "三", "四", "五", "六", "日").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 日历网格
        val firstDayOfMonth = displayMonth.dayOfWeek.value // 1 (Monday) to 7 (Sunday)
        val lastDayOfMonth = displayMonth.lengthOfMonth()
        val selectedDay = datePickerState.selectedDateMillis?.let {
            LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000)).dayOfMonth
        }

        // 按周显示日期
        val weeks = (0 until 6).map { weekIndex ->
            (1..7).map { dayIndex ->
                val day = weekIndex * 7 + dayIndex - firstDayOfMonth + 1
                if (day in 1..lastDayOfMonth) day else null
            }
        }

        // 渲染日历
        weeks.forEach { week ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                week.forEach { day ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(
                                if (day == selectedDay) MaterialTheme.colorScheme.primary
                                else Color.Transparent
                            )
                            .clickable(enabled = day != null) {
                                day?.let {
                                    val newDate = displayMonth.withDayOfMonth(it)
                                    datePickerState.setSelection(
                                        newDate.atStartOfDay().toEpochSecond(java.time.ZoneOffset.UTC) * 1000
                                    )
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        day?.let {
                            Text(
                                text = it.toString(),
                                color = if (day == selectedDay) 
                                    MaterialTheme.colorScheme.onPrimary
                                else 
                                    MaterialTheme.colorScheme.onSurface,
                                fontSize = 16.sp,
                                fontWeight = if (day == selectedDay) FontWeight.Bold else FontWeight.Normal
                            )
                        }
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
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 标题
            Text(
                text = title.ifEmpty { "上海外国语大学体验+迪士尼6日夏令营 ❤" },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            // 行程特色内容
            features.forEach { feature ->
                Text(
                    text = feature,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = validPeriod,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Medium
                )
            }
            
            // 底部分隔线
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            
            // 发布节点
            Text(
                text = "发布节点：",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = location,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

// 在PublishScreen中添加预览部分，在提交前预览
@Composable
fun MessagePreviewSection(
    title: String,
    content: String
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
            modifier = Modifier.padding(bottom = 8.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
        
        // 将描述转换为行程特色列表
        val features = if (content.isNotEmpty()) {
            content.split("\n").filter { it.isNotBlank() }
        } else {
            listOf(
                "1.在上海外国语大学浸入式英语环境中学习英语，培养孩子良好的英语语感及口语运用能力。",
                "2.上海淮景点畅游、博物馆、知名大学参访，展开真正的上海文化寻根游学之旅。",
                "3.上海迪斯尼乐园畅游，学习游乐两不误。"
            )
        }
        
        MessagePreviewCard(
            title = title,
            features = features
        )
    }
}
