package com.example.travalms.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travalms.ui.theme.PrimaryColor
import com.example.travalms.ui.theme.TextOnPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribeSettingScreen(
    onBackClick: () -> Unit = {}
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    
    // 选项卡标题
    val tabTitles = listOf("订阅类型", "层级总览", "省份/城市", "景区", "公司")
    
    // 订阅类型选项及是否有特殊标记
    val subscriptionTypes = listOf(
        SubscriptionTypeItem("全部", false),
        SubscriptionTypeItem("同业社", false),
        SubscriptionTypeItem("地接社", false),
        SubscriptionTypeItem("组团社", false),
        SubscriptionTypeItem("景区", false),
        SubscriptionTypeItem("宾馆", false),
        SubscriptionTypeItem("车队", false),
        SubscriptionTypeItem("招聘", true),
        SubscriptionTypeItem("签证", false),
        SubscriptionTypeItem("票务", false),
        SubscriptionTypeItem("其他", false)
    )
    
    // 省份选项
    val provinces = listOf(
        "北京", "上海", "广东", "江苏", "浙江", "山东", 
        "四川", "河南", "湖北", "湖南", "河北", "安徽",
        "福建", "甘肃", "广西", "贵州", "海南", "黑龙江",
        "吉林", "江西", "辽宁", "内蒙古", "宁夏", "青海"
    )
    
    // 景区选项
    val scenicAreas = listOf(
        "黄山", "张家界", "九寨沟", "西湖", "故宫", "长城",
        "三亚", "桂林", "敦煌", "丽江", "泰山", "峨眉山",
        "武夷山", "张掖丹霞", "喀纳斯", "洱海", "香格里拉", "鼓浪屿"
    )
    
    // 公司选项
    val companies = listOf(
        "中国国旅", "春秋旅行社", "凯撒旅游", "携程旅行", "途牛旅游",
        "驴妈妈旅游", "同程旅游", "青年旅行社", "中青旅", "首旅集团",
        "广之旅", "众信旅游", "东航旅游", "南航旅游", "锦江旅游"
    )

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("订阅节点") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = PrimaryColor,
                        titleContentColor = TextOnPrimary,
                        navigationIconContentColor = TextOnPrimary
                    )
                )
                
                // 添加Tab导航条 - 使用PrimaryColor主题色
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.White,
                    contentColor = PrimaryColor,  // 使用主题色
                    edgePadding = 0.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            height = 2.dp,
                            color = PrimaryColor  // 指示器使用主题色
                        )
                    }
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { 
                                Text(
                                    text = title,
                                    color = if (selectedTabIndex == index) PrimaryColor else Color.Gray,
                                    fontSize = 14.sp
                                ) 
                            }
                        )
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Button(
                    onClick = { onBackClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryColor
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "确认订阅",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 根据选中的选项卡显示不同的内容
            when (selectedTabIndex) {
                0 -> SubscriptionTypeList(subscriptionTypes)
                1 -> CategoryList("层级总览", listOf("全部", "一级", "二级", "三级", "四级", "五级"))
                2 -> CategoryList("省份/城市", provinces)
                3 -> CategoryList("景区", scenicAreas)
                4 -> CategoryList("公司", companies)
            }
        }
    }
}

data class SubscriptionTypeItem(
    val name: String,
    val hasHighlight: Boolean
)

@Composable
fun SubscriptionTypeList(items: List<SubscriptionTypeItem>) {
    val selectedItems = remember { mutableStateListOf<String>() }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(items) { item ->
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* 切换选中状态 */ }
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedItems.contains(item.name),
                        onCheckedChange = { isChecked ->
                            if (isChecked) selectedItems.add(item.name)
                            else selectedItems.remove(item.name)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = PrimaryColor,
                            checkmarkColor = Color.White
                        )
                    )
                    
                    Text(
                        text = item.name,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (item.hasHighlight) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(20.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(20.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                
                Divider(color = Color(0xFFD7EAD3), thickness = 1.dp)
            }
        }
    }
}

@Composable
fun CategoryList(categoryTitle: String, items: List<String>) {
    val selectedItems = remember { mutableStateListOf<String>() }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(items) { item ->
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* 切换选中状态 */ }
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedItems.contains(item),
                        onCheckedChange = { isChecked ->
                            if (isChecked) selectedItems.add(item)
                            else selectedItems.remove(item)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = PrimaryColor,
                            checkmarkColor = Color.White
                        )
                    )
                    
                    Text(
                        text = item,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Divider(color = Color(0xFFD7EAD3), thickness = 1.dp)
            }
        }
    }
} 