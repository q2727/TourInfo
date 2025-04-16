package com.example.travalms.ui.publish

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.travalms.data.remote.XMPPManager
import com.example.travalms.ui.navigation.AppRoutes
import com.example.travalms.ui.screens.SubscriptionNodeItem
import com.example.travalms.ui.screens.SubscriptionNodeType
import com.example.travalms.ui.theme.PrimaryColor
import com.example.travalms.ui.viewmodels.PublishViewModel
import kotlinx.coroutines.delay

/**
 * 选择发布节点的界面，采用树形结构展示
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishNodeSelectorScreen(
    navController: NavController,
    onBackClick: () -> Unit = {},
    viewModel: PublishViewModel = viewModel()
) {
    // 模拟层级数据
    val rootNodes = remember {
        listOf(
            SubscriptionNodeItem(
                id = "guangdong",
                name = "广东",
                type = SubscriptionNodeType.PROVINCE,
                children = listOf(
                    SubscriptionNodeItem(
                        id = "guangzhou",
                        name = "广州",
                        type = SubscriptionNodeType.CITY,
                        children = listOf(
                            SubscriptionNodeItem(id = "gzatt1", name = "长隆欢乐世界", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "gzatt2", name = "白云山", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "gzcom1", name = "广州旅游集团", type = SubscriptionNodeType.COMPANY),
                            SubscriptionNodeItem(id = "gzcom2", name = "南湖国旅", type = SubscriptionNodeType.COMPANY)
                        )
                    ),
                    SubscriptionNodeItem(
                        id = "shenzhen",
                        name = "深圳",
                        type = SubscriptionNodeType.CITY,
                        children = listOf(
                            SubscriptionNodeItem(id = "szatt1", name = "深圳欢乐谷", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "szatt2", name = "世界之窗", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "szcom1", name = "深圳康辉旅行社", type = SubscriptionNodeType.COMPANY)
                        )
                    ),
                    SubscriptionNodeItem(
                        id = "zhuhai",
                        name = "珠海",
                        type = SubscriptionNodeType.CITY,
                        children = listOf(
                            SubscriptionNodeItem(id = "zhatt1", name = "珠海长隆海洋王国", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "zhatt2", name = "情侣路", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "zhcom1", name = "珠海旅游集团", type = SubscriptionNodeType.COMPANY)
                        )
                    )
                )
            ),
            SubscriptionNodeItem(
                id = "beijing",
                name = "北京",
                type = SubscriptionNodeType.PROVINCE,
                children = listOf(
                    SubscriptionNodeItem(
                        id = "haidian",
                        name = "海淀区",
                        type = SubscriptionNodeType.CITY,
                        children = listOf(
                            SubscriptionNodeItem(id = "hdatt1", name = "颐和园", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "hdatt2", name = "圆明园", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "hdcom1", name = "北京中青旅", type = SubscriptionNodeType.COMPANY)
                        )
                    ),
                    SubscriptionNodeItem(
                        id = "dongcheng",
                        name = "东城区",
                        type = SubscriptionNodeType.CITY,
                        children = listOf(
                            SubscriptionNodeItem(id = "dcatt1", name = "故宫", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "dcatt2", name = "天安门", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "dccom1", name = "东城区旅行社", type = SubscriptionNodeType.COMPANY)
                        )
                    ),
                    SubscriptionNodeItem(
                        id = "chaoyang",
                        name = "朝阳区",
                        type = SubscriptionNodeType.CITY,
                        children = listOf(
                            SubscriptionNodeItem(id = "cyatt1", name = "798艺术区", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "cyatt2", name = "奥林匹克公园", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "cycom1", name = "朝阳区旅游公司", type = SubscriptionNodeType.COMPANY)
                        )
                    )
                )
            ),
            SubscriptionNodeItem(
                id = "shanghai",
                name = "上海",
                type = SubscriptionNodeType.PROVINCE,
                children = listOf(
                    SubscriptionNodeItem(
                        id = "pudong",
                        name = "浦东新区",
                        type = SubscriptionNodeType.CITY,
                        children = listOf(
                            SubscriptionNodeItem(id = "pdatt1", name = "迪士尼乐园", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "pdatt2", name = "东方明珠", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "pdcom1", name = "携程旅游", type = SubscriptionNodeType.COMPANY)
                        )
                    ),
                    SubscriptionNodeItem(
                        id = "huangpu",
                        name = "黄浦区",
                        type = SubscriptionNodeType.CITY,
                        children = listOf(
                            SubscriptionNodeItem(id = "hpatt1", name = "外滩", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "hpatt2", name = "豫园", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "hpcom1", name = "上海国旅", type = SubscriptionNodeType.COMPANY)
                        )
                    ),
                    SubscriptionNodeItem(
                        id = "jingan",
                        name = "静安区",
                        type = SubscriptionNodeType.CITY,
                        children = listOf(
                            SubscriptionNodeItem(id = "jaatt1", name = "静安寺", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "jaatt2", name = "南京西路", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "jacom1", name = "静安区旅游公司", type = SubscriptionNodeType.COMPANY)
                        )
                    )
                )
            ),
            SubscriptionNodeItem(
                id = "zhejiang",
                name = "浙江",
                type = SubscriptionNodeType.PROVINCE,
                children = listOf(
                    SubscriptionNodeItem(
                        id = "hangzhou",
                        name = "杭州",
                        type = SubscriptionNodeType.CITY,
                        children = listOf(
                            SubscriptionNodeItem(id = "hzatt1", name = "西湖", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "hzatt2", name = "灵隐寺", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "hzcom1", name = "杭州旅游集团", type = SubscriptionNodeType.COMPANY)
                        )
                    ),
                    SubscriptionNodeItem(
                        id = "ningbo",
                        name = "宁波",
                        type = SubscriptionNodeType.CITY,
                        children = listOf(
                            SubscriptionNodeItem(id = "nbatt1", name = "天一阁", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "nbatt2", name = "东钱湖", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "nbcom1", name = "宁波旅游公司", type = SubscriptionNodeType.COMPANY)
                        )
                    )
                )
            ),
            SubscriptionNodeItem(
                id = "jiangsu",
                name = "江苏",
                type = SubscriptionNodeType.PROVINCE,
                children = listOf(
                    SubscriptionNodeItem(
                        id = "nanjing",
                        name = "南京",
                        type = SubscriptionNodeType.CITY,
                        children = listOf(
                            SubscriptionNodeItem(id = "njatt1", name = "中山陵", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "njatt2", name = "夫子庙", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "njcom1", name = "南京旅游集团", type = SubscriptionNodeType.COMPANY)
                        )
                    ),
                    SubscriptionNodeItem(
                        id = "suzhou",
                        name = "苏州",
                        type = SubscriptionNodeType.CITY,
                        children = listOf(
                            SubscriptionNodeItem(id = "szatt1", name = "拙政园", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "szatt2", name = "虎丘", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "szcom1", name = "苏州旅游公司", type = SubscriptionNodeType.COMPANY)
                        )
                    )
                )
            ),
            SubscriptionNodeItem(
                id = "sichuan",
                name = "四川",
                type = SubscriptionNodeType.PROVINCE,
                children = listOf(
                    SubscriptionNodeItem(
                        id = "chengdu",
                        name = "成都",
                        type = SubscriptionNodeType.CITY,
                        children = listOf(
                            SubscriptionNodeItem(id = "cdatt1", name = "宽窄巷子", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "cdatt2", name = "锦里", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "cdcom1", name = "成都旅游集团", type = SubscriptionNodeType.COMPANY)
                        )
                    ),
                    SubscriptionNodeItem(
                        id = "leshan",
                        name = "乐山",
                        type = SubscriptionNodeType.CITY,
                        children = listOf(
                            SubscriptionNodeItem(id = "lsatt1", name = "乐山大佛", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "lsatt2", name = "峨眉山", type = SubscriptionNodeType.ATTRACTION),
                            SubscriptionNodeItem(id = "lscom1", name = "乐山旅游公司", type = SubscriptionNodeType.COMPANY)
                        )
                    )
                )
            )
        )
    }

    // 状态变量
    var searchQuery by remember { mutableStateOf("") }
    val expandedNodes = remember { mutableStateMapOf<String, Boolean>() }
    val selectedNodes = remember { mutableStateListOf<String>() }
    val selectedNodeNames = remember { mutableStateListOf<String>() }
    
    // 监听ViewModel状态
    val publishState by viewModel.uiState.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    // 新增：获取连接状态
    val connectionState by viewModel.connectionState.collectAsState()
    
    // 使用单一LaunchedEffect进行串行处理
    LaunchedEffect(Unit) {
        Log.d(TAG, "初始化发布节点选择器")
        
        // 注释掉连接相关操作
        /*
        // 1. 检查登录状态
        if (connectionState != com.example.travalms.data.remote.ConnectionState.AUTHENTICATED) {
            Log.d(TAG, "未认证，尝试登录")
            // 2. 执行登录
            val loginResult = viewModel.loginToXMPP("qinchong", "qc@BIT")
            
            // 3. 等待登录结果
            if (loginResult) {
                Log.d(TAG, "登录成功")
                // 给系统一些时间完成认证流程
                delay(500)
            } else {
                Log.d(TAG, "登录失败，无法创建节点")
                return@LaunchedEffect
            }
        } else {
            Log.d(TAG, "已经认证，无需登录")
        }
        
        // 4. 创建节点
        Log.d(TAG, "开始创建节点层次结构")
        // viewModel.createNodesForHierarchy(rootNodes) // 暂时注释掉创建节点的逻辑
        Log.d(TAG, "节点创建流程已启动")
        */
    }

    // 辅助函数：获取节点及其所有子节点的ID
    fun getAllChildrenIds(node: SubscriptionNodeItem): List<String> {
        val result = mutableListOf(node.id)
        for (child in node.children) {
            result.addAll(getAllChildrenIds(child))
        }
        return result
    }
    
    // 辅助函数：获取节点及其所有子节点的名称
    fun getAllChildrenNames(node: SubscriptionNodeItem): List<String> {
        val result = mutableListOf(node.name)
        for (child in node.children) {
            result.addAll(getAllChildrenNames(child))
        }
        return result
    }

    // 辅助函数：检查节点的所有子节点是否已选中
    fun areAllChildrenSelected(node: SubscriptionNodeItem): Boolean {
        return node.children.all { child ->
            selectedNodes.contains(child.id) && (child.children.isEmpty() || areAllChildrenSelected(child))
        }
    }

    // 辅助函数：处理节点的选择状态改变
    fun toggleNodeSelection(node: SubscriptionNodeItem, selected: Boolean) {
        val childrenIds = getAllChildrenIds(node)
        val childrenNames = getAllChildrenNames(node)
        
        if (selected) {
            childrenIds.forEachIndexed { index, id ->
                if (!selectedNodes.contains(id)) {
                    selectedNodes.add(id)
                    if (index < childrenNames.size && !selectedNodeNames.contains(childrenNames[index])) {
                        selectedNodeNames.add(childrenNames[index])
                    }
                }
            }
        } else {
            childrenIds.forEachIndexed { index, id ->
                selectedNodes.remove(id)
                if (index < childrenNames.size) {
                    selectedNodeNames.remove(childrenNames[index])
                }
            }
        }
        
        // 更新ViewModel中的选择
        viewModel.setSelectedNodes(selectedNodes.toList(), selectedNodeNames.toList())
    }

    // 递归函数：渲染树节点
    @Composable
    fun TreeNodeItem(node: SubscriptionNodeItem, level: Int = 0) {
        val isExpanded = expandedNodes[node.id] ?: false
        val isSelected = selectedNodes.contains(node.id)
        val hasChildren = node.children.isNotEmpty()
        val allChildrenSelected = hasChildren && areAllChildrenSelected(node)
        val someChildrenSelected = hasChildren && !allChildrenSelected && 
                                   node.children.any { child -> selectedNodes.contains(child.id) }
        
        // 过滤功能：如果有搜索内容且不匹配，则不显示
        if (searchQuery.isNotEmpty() && !node.name.contains(searchQuery, ignoreCase = true)) {
            return
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = (level * 20).dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 展开/折叠图标
            if (hasChildren) {
                IconButton(
                    onClick = { expandedNodes[node.id] = !isExpanded },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ArrowDropDown else Icons.Default.KeyboardArrowRight,
                        contentDescription = if (isExpanded) "折叠" else "展开",
                        tint = PrimaryColor
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(32.dp))
            }
            
            // 复选框
            Checkbox(
                checked = isSelected || allChildrenSelected,
                onCheckedChange = { checked ->
                    toggleNodeSelection(node, checked)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = PrimaryColor,
                    uncheckedColor = Color.Gray
                ),
                modifier = Modifier.padding(end = 8.dp)
            )
            
            // 节点名称
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable { expandedNodes[node.id] = !isExpanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = node.name,
                    fontWeight = if (isSelected || allChildrenSelected || someChildrenSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected || allChildrenSelected) PrimaryColor else Color.Black
                )
                
                if (node.hasHighlight) {
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(8.dp)
                            .background(color = Color.Red, shape = androidx.compose.foundation.shape.CircleShape)
                    )
                }
            }
            
            // 显示节点类型标签
            val typeText = when (node.type) {
                SubscriptionNodeType.CATEGORY -> "类别"
                SubscriptionNodeType.PROVINCE -> "省份"
                SubscriptionNodeType.CITY -> "城市"
                SubscriptionNodeType.ATTRACTION -> "景点"
                SubscriptionNodeType.COMPANY -> "公司"
            }
            
            Text(
                text = typeText,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        
        // 显示子节点，如果当前节点已展开
        if (isExpanded) {
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                // 全选按钮 (只有当有子节点时才显示)
                if (hasChildren) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = (level * 20 + 32).dp, top = 4.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = allChildrenSelected,
                            onCheckedChange = { checked ->
                                node.children.forEach { child ->
                                    toggleNodeSelection(child, checked)
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = PrimaryColor,
                                uncheckedColor = Color.Gray
                            ),
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        
                        Text(
                            text = "全选",
                            fontWeight = FontWeight.Medium,
                            color = if (allChildrenSelected) PrimaryColor else Color.Gray
                        )
                    }
                }
                
                // 显示节点本身作为一个可选项
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = (level * 20 + 32).dp, top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = { checked ->
                            if (checked) {
                                if (!selectedNodes.contains(node.id)) {
                                    selectedNodes.add(node.id)
                                    selectedNodeNames.add(node.name)
                                    viewModel.setSelectedNodes(selectedNodes.toList(), selectedNodeNames.toList())
                                }
                            } else {
                                selectedNodes.remove(node.id)
                                selectedNodeNames.remove(node.name)
                                viewModel.setSelectedNodes(selectedNodes.toList(), selectedNodeNames.toList())
                            }
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = PrimaryColor,
                            uncheckedColor = Color.Gray
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    
                    Text(
                        text = node.name,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) PrimaryColor else Color.Black
                    )
                }
                
                Divider(modifier = Modifier.padding(start = (level * 20 + 32).dp, end = 16.dp, top = 4.dp, bottom = 4.dp))
                
                // 渲染子节点
                node.children.forEach { child ->
                    TreeNodeItem(child, level + 1)
                }
            }
        }
    }

    // 显示错误消息的组件
    @Composable
    fun ErrorMessage(message: String) {
        if (message.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        color = Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = message,
                    color = Color(0xFFB71C1C)
                )
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("选择发布节点", fontWeight = FontWeight.Bold) },
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                placeholder = { Text("搜索节点", color = Color.Gray) },
                leadingIcon = { 
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "搜索",
                        tint = PrimaryColor
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(28.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                )
            )
            
            // 显示错误消息
            publishState.errorMessage?.let { ErrorMessage(it) }
            
            // 当前选中的节点
            if (selectedNodes.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .background(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "已选择 ${selectedNodes.size} 个节点",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = selectedNodeNames.joinToString(", "),
                        color = Color(0xFF388E3C)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // 树形节点列表
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                items(rootNodes) { rootNode ->
                    TreeNodeItem(rootNode)
                    Divider(modifier = Modifier.padding(start = 32.dp, end = 16.dp))
                }
            }
            
            // 底部按钮
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        // 将节点信息存储到导航参数中
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "selected_nodes", 
                            selectedNodeNames.joinToString(", ")
                        )
                        // 同时传递节点ID列表用于实际发布
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "selected_node_ids", 
                            ArrayList(selectedNodes.toList())
                        )
                        // 导航到发布页面
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryColor,
                        disabledContainerColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(8.dp),
                    // 注释掉连接状态检查，只检查是否有选择节点
                    enabled = selectedNodes.isNotEmpty() // && isLoggedIn
                ) {
                    Text(
                        // 修改按钮文本，移除连接状态提示
                        text = "确认选择 (${selectedNodes.size})",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
} 