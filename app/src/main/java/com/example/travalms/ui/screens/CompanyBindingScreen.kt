package com.example.travalms.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travalms.ui.theme.PrimaryColor
import com.example.travalms.ui.theme.PrimaryLight
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyBindingScreen(
    onBackClick: () -> Unit,
    onCompanySelect: (String) -> Unit,
    onRegisterCompany: () -> Unit
) {
    // 企业数据，按字母分组
    val companies = remember {
        mapOf(
            "A" to listOf("爱游旅行社", "安驰车队", "安信旅游联盟", "艾达同业合作", "安悦酒店"),
            "B" to listOf("百川旅行社", "博联同业社", "白云酒店", "博途旅游巴士", "北斗旅游", "贝斯特旅行"),
            "C" to listOf("长城旅行社", "创新出行"),
            "D" to listOf("东方航旅", "大地旅游"),
            "E" to listOf("恩泽旅游"),
            "F" to listOf("飞龙旅行社", "富华酒店"),
            "G" to listOf("光明旅游", "国际旅行社"),
            "H" to listOf("海洋旅游", "华美酒店"),
            "J" to listOf("金马旅行社", "锦江酒店"),
            "K" to listOf("康辉旅游", "凯莱酒店"),
            "L" to listOf("蓝天旅行社", "龙腾国旅"),
            "M" to listOf("美景旅游", "明珠航旅"),
            "N" to listOf("南方旅游", "宁波假期"),
            "P" to listOf("品质旅游", "朋友国旅"),
            "Q" to listOf("青年旅行社", "千里马旅游"),
            "R" to listOf("瑞安旅游", "日月旅行社"),
            "S" to listOf("神州旅行社", "四季旅游"),
            "T" to listOf("天马旅游", "泰达国旅"),
            "W" to listOf("文化旅行社", "万里行旅游"),
            "X" to listOf("新东方旅游", "祥和假期"),
            "Y" to listOf("远方旅行社", "雅典旅游"),
            "Z" to listOf("中青旅", "中华国旅")
        )
    }
    
    // 搜索关键词
    var searchQuery by remember { mutableStateOf("") }
    
    // 选中的企业
    var selectedCompany by remember { mutableStateOf<String?>(null) }
    
    // 过滤后的企业列表
    val filteredCompanies = remember(searchQuery) {
        if (searchQuery.isEmpty()) {
            companies
        } else {
            companies.mapValues { (_, companyList) ->
                companyList.filter { it.contains(searchQuery, ignoreCase = true) }
            }.filter { it.value.isNotEmpty() }
        }
    }
    
    // 用于控制列表滚动的状态
    val listState = rememberLazyListState()
    
    // 用于跳转到特定位置的协程作用域
    val coroutineScope = rememberCoroutineScope()
    
    // 构建字母到列表项索引的映射
    val letterToIndexMap = remember(filteredCompanies) {
        val map = mutableMapOf<String, Int>()
        var index = 0
        
        filteredCompanies.forEach { (letter, companies) ->
            map[letter] = index
            index += companies.size + 1 // +1 是因为每个字母区域还有一个标题项
        }
        
        map
    }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("绑定企业", fontWeight = FontWeight.Bold) },
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
        ) {
            // 搜索框
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("搜索") },
                leadingIcon = { 
                    Icon(
                        Icons.Default.Search, 
                        contentDescription = "搜索",
                        tint = PrimaryColor.copy(alpha = 0.6f)
                    ) 
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = PrimaryColor,
                    unfocusedBorderColor = Color.LightGray
                ),
                singleLine = true
            )
            
            // 企业列表
            Box(modifier = Modifier.fillMaxSize()) {
                // 主列表
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState
                ) {
                    filteredCompanies.forEach { (letter, companyList) ->
                        item {
                            // 字母标题
                            Text(
                                text = letter,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.LightGray.copy(alpha = 0.3f))
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                fontSize = 18.sp,
                                color = PrimaryColor
                            )
                        }
                        
                        items(companyList) { company ->
                            // 企业项
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { 
                                        selectedCompany = company
                                        onCompanySelect(company)
                                    }
                                    .background(
                                        if (selectedCompany == company) 
                                            PrimaryLight else Color.White
                                    )
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 选择圆圈
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (selectedCompany == company) 
                                                PrimaryColor else Color.LightGray.copy(alpha = 0.3f)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = if (selectedCompany == company) 
                                                PrimaryColor else Color.Gray.copy(alpha = 0.6f),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (selectedCompany == company) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .clip(CircleShape)
                                                .background(Color.White)
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.width(16.dp))
                                
                                // 企业名称
                                Text(
                                    text = company,
                                    fontSize = 16.sp,
                                    color = if (selectedCompany == company) 
                                        PrimaryColor else Color.Black
                                )
                            }
                            
                            Divider(
                                color = Color.LightGray.copy(alpha = 0.5f),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                    
                    // 底部注册选项
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "企业未注册？",
                                color = Color.Gray
                            )
                            Text(
                                text = "去注册",
                                color = PrimaryColor,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { onRegisterCompany() }
                            )
                        }
                    }
                }
                
                // 右侧字母索引
                AlphabetSideIndex(
                    letters = filteredCompanies.keys.toList(),
                    allLetters = ('A'..'Z').map { it.toString() },
                    onLetterClick = { letter ->
                        // 点击字母时，滚动到对应位置
                        letterToIndexMap[letter]?.let { index ->
                            coroutineScope.launch {
                                listState.animateScrollToItem(index)
                            }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                )
            }
        }
    }
}

@Composable
fun AlphabetSideIndex(
    letters: List<String>,
    allLetters: List<String>,
    onLetterClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        allLetters.forEach { letter ->
            val isActive = letters.contains(letter)
            val letterColor = if (isActive) PrimaryColor else Color.Gray.copy(alpha = 0.5f)
            
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clickable(enabled = isActive) {
                        if (isActive) {
                            onLetterClick(letter)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = letter,
                    fontSize = 12.sp,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                    color = letterColor
                )
            }
        }
    }
} 