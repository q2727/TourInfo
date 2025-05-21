package com.example.travalms.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.travalms.data.model.TravelItem
import com.example.travalms.ui.theme.PrimaryColor
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Surface
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.travalms.data.api.NetworkModule
import com.example.travalms.data.api.ProductResponseItem
import kotlinx.coroutines.launch
import androidx.compose.ui.layout.ContentScale
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onItemClick: (Int) -> Unit,
    onPublishClick: () -> Unit,
    onMessageClick: () -> Unit,
    onProfileClick: () -> Unit,
    onTailListClick: () -> Unit,
    onCompanyClick: (String) -> Unit,
    navController: NavController
) {
    // 定义筛选选项
    val filterOptions = listOf("宾馆", "车队", "招聘", "签证", "票务", "其他")
    var showFilterOptions by remember { mutableStateOf(false) }
    
    // 顶部标签
    val tabs = listOf("同业社", "地接社", "组团社", "景区", "筛选")
    var selectedTabIndex by remember { mutableStateOf(0) }

    // 修改为多选支持 - 使用Set存储已选中的筛选项
    var selectedFilters by remember { mutableStateOf(setOf<String>()) }
    
    // 添加全部产品/我的产品选择
    var showAllProducts by remember { mutableStateOf(true) }
    
    // 搜索框状态
    var searchQuery by remember { mutableStateOf("") }

    // 产品数据状态
    var travelItems by remember { mutableStateOf<List<TravelItem>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // 新增：产品详情WebView页面
    var showWebView by remember { mutableStateOf<Pair<Boolean, Int>>(false to 0) }

    // 拉取产品数据
    LaunchedEffect(Unit) {
        loading = true
        error = null
        try {
            val api = NetworkModule.productApiService
            val products = api.getProducts()
            travelItems = products.map { it.toTravelItem() }
        } catch (e: Exception) {
            error = e.message ?: "加载失败"
        } finally {
            loading = false
        }
    }

    // 优先判断是否显示WebView，直接return页面
    if (showWebView.first) {
        ProductWebViewScreen(productId = showWebView.second) {
            showWebView = false to 0
        }
        return
    }

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = { Text("首页", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = PrimaryColor,
                        titleContentColor = Color.White
                    )
                )
                
                // 添加搜索栏 - 使用基本TextField和Surface
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "搜索",
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { newQuery ->
                                searchQuery = newQuery
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(24.dp),
                            decorationBox = { innerTextField ->
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            text = "搜索产品",
                                            color = Color.Gray,
                                            fontSize = 14.sp
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )
                        if (searchQuery.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = { searchQuery = "" },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "清除",
                                    tint = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            BottomNavigation(
                backgroundColor = Color.White,
                modifier = Modifier.height(56.dp)
            ) {
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "产品") },
                    label = { Text("产品", fontSize = 12.sp) },
                    selected = true,
                    onClick = { /* 已在产品页 */ },
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = Color.Gray
                )

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Add, contentDescription = "发布") },
                    label = { Text("发布", fontSize = 12.sp) },
                    selected = false,
                    onClick = onPublishClick,
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = Color.Gray
                )

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = "尾单") },
                    label = { Text("尾单", fontSize = 12.sp) },
                    selected = false,
                    onClick = onTailListClick,
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
                .background(Color(0xFFF5F5F5))
        ) {
            // 添加全部产品/我的产品选择器
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                TabRow(
                    selectedTabIndex = if (showAllProducts) 0 else 1,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color.White,
                    contentColor = PrimaryColor,
                    indicator = { tabPositions ->
                        Box {}  // 不显示指示器
                    },
                    divider = { }  // 不显示分隔线
                ) {
                    Tab(
                        selected = showAllProducts,
                        onClick = { showAllProducts = true },
                        modifier = Modifier
                            .weight(1f)
                            .background(if (showAllProducts) Color.White else Color(0xFFF5F5F5))
                    ) {
                        Text(
                            text = "全部产品",
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = if (showAllProducts) Color.Black else Color.Gray
                        )
                    }
                    
                    Tab(
                        selected = !showAllProducts,
                        onClick = { showAllProducts = false },
                        modifier = Modifier
                            .weight(1f)
                            .background(if (!showAllProducts) Color.White else Color(0xFFF5F5F5))
                    ) {
                        Text(
                            text = "我的产品",
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = if (!showAllProducts) Color.Black else Color.Gray
                        )
                    }
                }
            }
            
            // 主标签行
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                tabs.forEach { tab ->
                    Text(
                        text = tab,
                        modifier = Modifier
                            .clickable {
                                if (tab == "筛选") {
                                    showFilterOptions = !showFilterOptions
                                } else {
                                    // 多选实现 - 切换选中状态
                                    selectedFilters = if (selectedFilters.contains(tab)) {
                                        selectedFilters - tab
                                    } else {
                                        selectedFilters + tab
                                    }
                                }
                            }
                            .padding(horizontal = 12.dp),
                        color = if (selectedFilters.contains(tab)) PrimaryColor else Color.Gray,
                        fontWeight = if (selectedFilters.contains(tab)) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
            
            // 筛选选项展示区域
            AnimatedVisibility(
                visible = showFilterOptions,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5))
                        .padding(vertical = 8.dp)
                ) {
                    // 显示筛选选项
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        filterOptions.take(3).forEach { option ->
                            Text(
                                text = option,
                                modifier = Modifier
                                    .clickable { 
                                        selectedFilters = if (selectedFilters.contains(option)) {
                                            selectedFilters - option
                                        } else {
                                            selectedFilters + option
                                        }
                                    }
                                    .padding(horizontal = 16.dp),
                                color = if (selectedFilters.contains(option)) PrimaryColor else Color.Gray,
                                fontWeight = if (selectedFilters.contains(option)) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        filterOptions.takeLast(3).forEach { option ->
                            Text(
                                text = option,
                                modifier = Modifier
                                    .clickable { 
                                        selectedFilters = if (selectedFilters.contains(option)) {
                                            selectedFilters - option
                                        } else {
                                            selectedFilters + option
                                        }
                                    }
                                    .padding(horizontal = 16.dp),
                                color = if (selectedFilters.contains(option)) PrimaryColor else Color.Gray,
                                fontWeight = if (selectedFilters.contains(option)) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
            
            // 内容列表部分
            if (loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("加载失败: $error", color = Color.Red)
                }
            } else {
                // 显示产品列表
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 过滤产品列表
                    val filteredItems = travelItems
                        .filter { item ->
                            // 首先根据搜索关键词过滤
                            (searchQuery.isEmpty() || item.title.contains(searchQuery, ignoreCase = true)) &&
                            // 然后根据是否显示全部产品过滤
                            (showAllProducts || item.isFavorite) &&
                            // 最后根据选中的标签过滤
                            (selectedFilters.isEmpty() || selectedFilters.contains(item.agency))
                        }
                    
                    items(filteredItems) { item ->
                        TravelItemCard(
                            item = item,
                            onItemClick = { id -> showWebView = true to id },
                            onCompanyClick = { onCompanyClick(item.agency) }
                        )
                    }
                }
            }
        }
    }
}

// API数据转TravelItem
fun ProductResponseItem.toTravelItem(): TravelItem {
    // duration: "6天5晚" -> days=6, nights=5
    val regex = Regex("(\\d+)天(\\d+)晚")
    val match = regex.find(duration)
    val days = match?.groupValues?.getOrNull(1)?.toIntOrNull() ?: 0
    val nights = match?.groupValues?.getOrNull(2)?.toIntOrNull() ?: 0
    return TravelItem(
        id = productId,
        title = title,
        agency = provider,
        price = "¥${price}",
        hasImage = image.isNotBlank(),
        isFavorite = false,
        imageUrl = getImageUrl(),
        days = days,
        nights = nights
    )
}

@Composable
fun TravelItemCard(
    item: TravelItem,
    onItemClick: (Int) -> Unit,
    onCompanyClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onItemClick(item.id) },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.5.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 图片自适应填充且无灰色底框，加载失败才显示灰色+icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (item.imageUrl != null) {
                    SubcomposeAsyncImage(
                        model = item.imageUrl,
                        contentDescription = item.title,
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop,
                        loading = {
                            Box(
                                Modifier
                                    .matchParentSize()
                                    .background(Color.LightGray)
                            )
                        },
                        error = {
                            Box(
                                Modifier
                                    .matchParentSize()
                                    .background(Color.LightGray),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Face, contentDescription = "无图片", tint = Color.White)
                            }
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Face, contentDescription = "无图片", tint = Color.White)
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            // 右侧信息
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // 标题
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                // 天数晚数
                Text(
                    text = "${item.days}天${item.nights}晚",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                // 价格
                Text(
                    text = item.price,
                    fontSize = 16.sp,
                    color = Color(0xFFE91E63),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun BottomNavigationItem(
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit,
    selectedContentColor: Color,
    unselectedContentColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxHeight()
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(
                LocalContentColor provides if (selected) selectedContentColor else unselectedContentColor
            ) {
                icon()
            }
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        CompositionLocalProvider(
            LocalContentColor provides if (selected) selectedContentColor else unselectedContentColor
        ) {
            label()
        }
    }
}

@Composable
fun BottomNavigation(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = backgroundColor,
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            content = content
        )
    }
}

@Composable
fun FlowRow(
    maxItemsInEachRow: Int,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val rows = mutableListOf<List<Placeable>>()
        val itemWidth = (constraints.maxWidth / maxItemsInEachRow) - 
                        (horizontalArrangement.spacing.roundToPx() * (maxItemsInEachRow - 1) / maxItemsInEachRow)
        val itemConstraints = constraints.copy(minWidth = itemWidth, maxWidth = itemWidth)
        
        val placeables = measurables.map { it.measure(itemConstraints) }
        var currentRow = mutableListOf<Placeable>()
        var currentRowWidth = 0
        
        placeables.forEach { placeable ->
            if (currentRow.size >= maxItemsInEachRow) {
                rows.add(currentRow)
                currentRow = mutableListOf()
                currentRowWidth = 0
            }
            
            currentRow.add(placeable)
            currentRowWidth += placeable.width
        }
        
        if (currentRow.isNotEmpty()) {
            rows.add(currentRow)
        }
        
        val height = rows.sumOf { row -> row.maxOfOrNull { it.height } ?: 0 } +
                    (verticalArrangement.spacing.roundToPx() * (rows.size - 1)).coerceAtLeast(0)
        
        layout(constraints.maxWidth, height) {
            var y = 0
            
            rows.forEach { row ->
                var x = 0
                val rowHeight = row.maxOfOrNull { it.height } ?: 0
                
                row.forEach { placeable ->
                    placeable.placeRelative(x, y)
                    x += placeable.width + horizontalArrangement.spacing.roundToPx()
                }
                
                y += rowHeight + verticalArrangement.spacing.roundToPx()
            }
        }
    }
}

// 新增：产品详情WebView页面
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductWebViewScreen(productId: Int, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("产品详情", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        val url = "http://42.193.112.197/#/pages/user/singleProductPage?productId=$productId"
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    loadUrl(url)
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
} 