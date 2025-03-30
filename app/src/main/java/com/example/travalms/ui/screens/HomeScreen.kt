package com.example.travalms.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.travalms.R
import com.example.travalms.data.model.TravelItem
import com.example.travalms.data.repository.FavoriteManager
import com.example.travalms.ui.navigation.AppRoutes
import com.example.travalms.ui.theme.PrimaryColor
import com.example.travalms.ui.theme.AccentColor
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Surface
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onItemClick: (Int) -> Unit,
    onPublishClick: () -> Unit,
    onMessageClick: () -> Unit,
    onProfileClick: () -> Unit,
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
            CenterAlignedTopAppBar(
                title = { Text("首页", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PrimaryColor,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomNavigation(
                backgroundColor = Color.White,
                modifier = Modifier.height(56.dp)
            ) {
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "首页") },
                    label = { Text("首页", fontSize = 12.sp) },
                    selected = true,
                    onClick = { /* 已在首页 */ },
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
        ) {
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
                    // 显示筛选选项，两行显示，每行3个
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // 第一行: 宾馆, 车队, 招聘
                        filterOptions.take(3).forEach { option ->
                            Text(
                                text = option,
                                modifier = Modifier
                                    .clickable { 
                                        // 多选实现 - 切换选中状态
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
                        // 第二行: 签证, 公司名, 其他
                        filterOptions.takeLast(3).forEach { option ->
                            Text(
                                text = option,
                                modifier = Modifier
                                    .clickable { 
                                        // 多选实现 - 切换选中状态
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
                modifier = Modifier.fillMaxSize()
            ) {
                // 根据筛选条件过滤项目
                val filteredItems = if (selectedFilters.isEmpty()) {
                    travelItems
                } else {
                    // 假设每个TravelItem有tags属性，包含适用的筛选标签
                    // 如果没有tags属性，可以基于其他属性创建筛选逻辑
                    travelItems.filter { item ->
                        // 这里简化处理，在实际应用中可以根据具体筛选条件进行更复杂的匹配
                        selectedFilters.contains(item.agency) || 
                        (item.isFavorite && selectedFilters.contains("关注")) ||
                        // 可以添加其他筛选逻辑
                        false
                    }
                }
                
                items(filteredItems) { item ->
                    TravelItemCard(item, onItemClick)
                }
            }
        }
    }
}

@Composable
fun TravelItemCard(item: TravelItem, onItemClick: (Int) -> Unit) {
    // 使用remember跟踪当前收藏状态
    var isFavorite by remember { mutableStateOf(item.isFavorite) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(item.id) },
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 可选的左侧图片
            if (item.hasImage) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("图片", color = Color.White)
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            
            // 右侧信息
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 标题
                    Text(
                        text = item.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    
                    // 收藏星标 - 添加点击事件
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "收藏",
                        tint = if (isFavorite) Color(0xFFFFC107) else Color.LightGray,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                // 切换收藏状态并保存到FavoriteManager
                                isFavorite = !isFavorite
                                if (isFavorite) {
                                    FavoriteManager.addFavorite(
                                        item.id.toString(), 
                                        item.title,
                                        item.agency,
                                        item.price
                                    )
                                } else {
                                    FavoriteManager.removeFavorite(item.id.toString())
                                }
                            }
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 旅行社信息
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "旅行社",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = item.agency,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 价格信息
                Text(
                    text = item.price,
                    fontSize = 16.sp,
                    color = Color(0xFFE91E63),
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
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