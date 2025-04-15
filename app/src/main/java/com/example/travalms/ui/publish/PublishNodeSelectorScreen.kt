package com.example.travalms.ui.publish

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
import androidx.navigation.NavController
import com.example.travalms.ui.theme.PrimaryColor

// 节点类型枚举
enum class NodeType {
    PROVINCE, CITY, ATTRACTION, COMPANY
}

// 树形结构的节点数据类
data class NodeItem(
    val id: String,
    val name: String,
    val type: NodeType,
    val children: List<NodeItem> = emptyList(),
    val parent: NodeItem? = null
)

/**
 * 发布节点选择器独立页面，用于选择发布内容的节点，采用树形结构
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishNodeSelectorScreen(
    navController: NavController,
    onBackClick: () -> Unit
) {
    // 模拟层级数据
    val rootNodes = remember {
        listOf(
            NodeItem(
                id = "guangdong",
                name = "广东",
                type = NodeType.PROVINCE,
                children = listOf(
                    NodeItem(
                        id = "guangzhou",
                        name = "广州",
                        type = NodeType.CITY,
                        children = listOf(
                            NodeItem(id = "gzatt1", name = "长隆欢乐世界", type = NodeType.ATTRACTION),
                            NodeItem(id = "gzatt2", name = "白云山", type = NodeType.ATTRACTION),
                            NodeItem(id = "gzcom1", name = "广州旅游集团", type = NodeType.COMPANY),
                            NodeItem(id = "gzcom2", name = "南湖国旅", type = NodeType.COMPANY)
                        )
                    ),
                    NodeItem(
                        id = "shenzhen",
                        name = "深圳",
                        type = NodeType.CITY,
                        children = listOf(
                            NodeItem(id = "szatt1", name = "深圳欢乐谷", type = NodeType.ATTRACTION),
                            NodeItem(id = "szatt2", name = "世界之窗", type = NodeType.ATTRACTION),
                            NodeItem(id = "szcom1", name = "深圳康辉旅行社", type = NodeType.COMPANY)
                        )
                    )
                )
            ),
            NodeItem(
                id = "beijing",
                name = "北京",
                type = NodeType.PROVINCE,
                children = listOf(
                    NodeItem(
                        id = "haidian",
                        name = "海淀区",
                        type = NodeType.CITY,
                        children = listOf(
                            NodeItem(id = "hdatt1", name = "颐和园", type = NodeType.ATTRACTION),
                            NodeItem(id = "hdatt2", name = "圆明园", type = NodeType.ATTRACTION),
                            NodeItem(id = "hdcom1", name = "北京中青旅", type = NodeType.COMPANY)
                        )
                    ),
                    NodeItem(
                        id = "dongcheng",
                        name = "东城区",
                        type = NodeType.CITY,
                        children = listOf(
                            NodeItem(id = "dcatt1", name = "故宫", type = NodeType.ATTRACTION),
                            NodeItem(id = "dcatt2", name = "天安门", type = NodeType.ATTRACTION),
                            NodeItem(id = "dccom1", name = "东城区旅行社", type = NodeType.COMPANY)
                        )
                    )
                )
            ),
            NodeItem(
                id = "shanghai",
                name = "上海",
                type = NodeType.PROVINCE,
                children = listOf(
                    NodeItem(
                        id = "pudong",
                        name = "浦东新区",
                        type = NodeType.CITY,
                        children = listOf(
                            NodeItem(id = "pdatt1", name = "迪士尼乐园", type = NodeType.ATTRACTION),
                            NodeItem(id = "pdatt2", name = "东方明珠", type = NodeType.ATTRACTION),
                            NodeItem(id = "pdcom1", name = "携程旅游", type = NodeType.COMPANY)
                        )
                    ),
                    NodeItem(
                        id = "huangpu",
                        name = "黄浦区",
                        type = NodeType.CITY,
                        children = listOf(
                            NodeItem(id = "hpatt1", name = "外滩", type = NodeType.ATTRACTION),
                            NodeItem(id = "hpatt2", name = "豫园", type = NodeType.ATTRACTION),
                            NodeItem(id = "hpcom1", name = "上海国旅", type = NodeType.COMPANY)
                        )
                    )
                )
            ),
            NodeItem(
                id = "jiangsu",
                name = "江苏",
                type = NodeType.PROVINCE,
                children = listOf(
                    NodeItem(
                        id = "nanjing",
                        name = "南京",
                        type = NodeType.CITY,
                        children = listOf(
                            NodeItem(id = "njatt1", name = "夫子庙", type = NodeType.ATTRACTION),
                            NodeItem(id = "njatt2", name = "中山陵", type = NodeType.ATTRACTION),
                            NodeItem(id = "njcom1", name = "南京旅游公司", type = NodeType.COMPANY)
                        )
                    ),
                    NodeItem(
                        id = "suzhou",
                        name = "苏州",
                        type = NodeType.CITY,
                        children = listOf(
                            NodeItem(id = "szatt1", name = "拙政园", type = NodeType.ATTRACTION),
                            NodeItem(id = "szatt2", name = "金鸡湖", type = NodeType.ATTRACTION),
                            NodeItem(id = "szcom1", name = "苏州旅行社", type = NodeType.COMPANY)
                        )
                    )
                )
            ),
            NodeItem(
                id = "zhejiang",
                name = "浙江",
                type = NodeType.PROVINCE,
                children = listOf(
                    NodeItem(
                        id = "hangzhou",
                        name = "杭州",
                        type = NodeType.CITY,
                        children = listOf(
                            NodeItem(id = "hzatt1", name = "西湖", type = NodeType.ATTRACTION),
                            NodeItem(id = "hzatt2", name = "灵隐寺", type = NodeType.ATTRACTION),
                            NodeItem(id = "hzcom1", name = "飞猪旅行", type = NodeType.COMPANY)
                        )
                    ),
                    NodeItem(
                        id = "ningbo",
                        name = "宁波",
                        type = NodeType.CITY,
                        children = listOf(
                            NodeItem(id = "nbatt1", name = "天一阁", type = NodeType.ATTRACTION),
                            NodeItem(id = "nbatt2", name = "溪口", type = NodeType.ATTRACTION),
                            NodeItem(id = "nbcom1", name = "宁波旅游集团", type = NodeType.COMPANY)
                        )
                    )
                )
            )
        )
    }

    // 搜索查询状态
    var searchQuery by remember { mutableStateOf("") }
    
    // 保存已展开的节点ID列表
    val expandedNodes = remember { mutableStateMapOf<String, Boolean>() }
    
    // 保存已选择的节点ID列表
    val selectedNodes = remember { mutableStateListOf<String>() }

    // 辅助函数：获取节点及其所有子节点的ID
    fun getAllChildrenIds(node: NodeItem): List<String> {
        val result = mutableListOf(node.id)
        for (child in node.children) {
            result.addAll(getAllChildrenIds(child))
        }
        return result
    }

    // 辅助函数：检查节点的所有子节点是否已选中
    fun areAllChildrenSelected(node: NodeItem): Boolean {
        return node.children.all { child ->
            selectedNodes.contains(child.id) && (child.children.isEmpty() || areAllChildrenSelected(child))
        }
    }

    // 辅助函数：处理节点的选择状态改变
    fun toggleNodeSelection(node: NodeItem, selected: Boolean) {
        val childrenIds = getAllChildrenIds(node)
        if (selected) {
            childrenIds.forEach { id ->
                if (!selectedNodes.contains(id)) {
                    selectedNodes.add(id)
                }
            }
        } else {
            childrenIds.forEach { id ->
                selectedNodes.remove(id)
            }
        }
    }

    // 递归函数：渲染树节点
    @Composable
    fun TreeNodeItem(node: NodeItem, level: Int = 0) {
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
            Text(
                text = node.name,
                modifier = Modifier
                    .weight(1f)
                    .clickable { expandedNodes[node.id] = !isExpanded },
                fontWeight = if (isSelected || allChildrenSelected || someChildrenSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected || allChildrenSelected) PrimaryColor else Color.Black
            )
            
            // 显示节点类型标签
            val typeText = when (node.type) {
                NodeType.PROVINCE -> "省份"
                NodeType.CITY -> "城市"
                NodeType.ATTRACTION -> "景点"
                NodeType.COMPANY -> "公司"
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
                                }
                            } else {
                                selectedNodes.remove(node.id)
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 取消按钮
                Button(
                    onClick = onBackClick,
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
                        // 获取选中的节点名称列表
                        val selectedNodeNames = mutableListOf<String>()
                        
                        // 辅助函数查找指定ID的节点
                        fun findNode(nodeId: String, nodes: List<NodeItem>): NodeItem? {
                            for (node in nodes) {
                                if (node.id == nodeId) return node
                                val found = findNode(nodeId, node.children)
                                if (found != null) return found
                            }
                            return null
                        }
                        
                        // 辅助函数获取所有节点
                        fun findAllNodes(nodes: List<NodeItem>): List<NodeItem> {
                            val result = mutableListOf<NodeItem>()
                            for (node in nodes) {
                                result.add(node)
                                result.addAll(findAllNodes(node.children))
                            }
                            return result
                        }
                        
                        // 获取所有节点
                        val allNodes = findAllNodes(rootNodes)
                        
                        // 找出已选择的节点名称
                        selectedNodes.forEach { nodeId ->
                            allNodes.find { it.id == nodeId }?.name?.let {
                                selectedNodeNames.add(it)
                            }
                        }
                        
                        // 如果有选中的节点，将节点名称传递回上一个界面
                        if (selectedNodeNames.isNotEmpty()) {
                            navController.previousBackStackEntry?.savedStateHandle?.set(
                                "selected_nodes", selectedNodeNames.joinToString(", ")
                            )
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryColor
                    ),
                    shape = RoundedCornerShape(8.dp),
                    enabled = selectedNodes.isNotEmpty()
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