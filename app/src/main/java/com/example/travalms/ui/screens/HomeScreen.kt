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

    // 模拟旅游项目数据 - 增加更多数据用于测试滚动
    val travelItems = listOf(
        TravelItem(1, "桂林山水5日游", "广西旅游集团", "¥2580", hasImage = true, isFavorite = false),
        TravelItem(2, "北京故宫+长城3日游", "北京旅行社", "¥1980", hasImage = true, isFavorite = true),
        TravelItem(3, "云南大理丽江7日游", "云南旅游集团", "¥3680", hasImage = true, isFavorite = false),
        TravelItem(4, "海南三亚5日度假", "海南旅游公司", "¥3280", hasImage = true, isFavorite = false),
        TravelItem(5, "上海迪士尼2日游", "上海旅游集团", "¥1280", hasImage = true, isFavorite = true),
        // 新增测试数据
        TravelItem(6, "西藏拉萨6日深度游", "西藏旅行社", "¥4980", hasImage = true, isFavorite = false),
        TravelItem(7, "成都熊猫基地3日游", "四川旅游公司", "¥1680", hasImage = true, isFavorite = true),
        TravelItem(8, "青岛海滨4日度假", "山东旅游集团", "¥2280", hasImage = true, isFavorite = false),
        TravelItem(9, "厦门鼓浪屿3日游", "福建旅游公司", "¥1980", hasImage = true, isFavorite = true),
        TravelItem(10, "哈尔滨冰雪5日游", "黑龙江旅行社", "¥3280", hasImage = true, isFavorite = false),
        TravelItem(11, "九寨沟黄龙6日游", "四川旅游集团", "¥3580", hasImage = true, isFavorite = true),
        TravelItem(12, "张家界玻璃桥4日游", "湖南旅游公司", "¥2480", hasImage = true, isFavorite = false),
        TravelItem(13, "香港迪士尼3日游", "香港旅行社", "¥3980", hasImage = true, isFavorite = true),
        TravelItem(14, "澳门威尼斯人2日游", "澳门旅游公司", "¥2380", hasImage = true, isFavorite = false),
        TravelItem(15, "内蒙古草原4日游", "内蒙古旅游集团", "¥2680", hasImage = true, isFavorite = true)
    )

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
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "搜索",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                Box {
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            text = "搜索产品、公司、地区...",
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                // 根据筛选条件和选项卡过滤项目
                val filteredItems = if (!showAllProducts) {
                    // 只显示收藏的产品
                    travelItems.filter { it.isFavorite }
                } else if (selectedFilters.isEmpty()) {
                    // 显示所有产品
                    travelItems
                } else {
                    // 根据筛选条件过滤
                    travelItems.filter { item ->
                        selectedFilters.contains(item.agency) || 
                        (item.isFavorite && selectedFilters.contains("关注")) ||
                        false
                    }
                }
                
                items(filteredItems) { item ->
                    TravelItemCard(
                        item = item, 
                        onItemClick = onItemClick,
                        onCompanyClick = { onCompanyClick(item.agency) }
                    )
                }
            }
        }
    }
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // 标题
            Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 商品图片、详情区域和价格在同一行
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 图片
                if (item.hasImage) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("图片", color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }
                
                // 中间详情
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // 旅行社信息 - 可点击
                    Text(
                        text = item.agency,
                        fontSize = 14.sp,
                        color = PrimaryColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable(onClick = onCompanyClick)
                    )
                    
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    // 电话信息
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Phone,
                            contentDescription = "电话",
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "139-1234-5678", // 模拟电话号码
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    // 发布时间
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = "发布时间",
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "2023-06-15", // 模拟发布时间
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
                
                // 价格显示在最右侧垂直居中
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item.price,
                    fontSize = 16.sp,
                    color = Color(0xFFE91E63),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically)
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