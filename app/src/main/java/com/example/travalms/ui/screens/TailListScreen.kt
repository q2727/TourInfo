package com.example.travalms.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.travalms.ui.theme.PrimaryColor
import com.example.travalms.ui.theme.BackgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TailListScreen(
    onItemClick: (Int) -> Unit,
    onHomeClick: () -> Unit,
    onPublishClick: () -> Unit,
    onMessageClick: () -> Unit,
    onProfileClick: () -> Unit,
    onCompanyClick: (String) -> Unit,
    onContactClick: (String) -> Unit,
    onPersonClick: (String) -> Unit,
    onReportItem: (Int, String) -> Unit,
    onDeleteItem: (Int) -> Unit,
    navController: NavController
) {
    // 模拟尾单数据
    var tailOrders by remember { mutableStateOf(
        listOf(
            TailOrder(
                id = 1,
                title = "上海外国语大学体验+迪士尼6日夏令营",
                company = "上海旅行社",
                companyId = "sh_travel",
                contactPerson = "张伟",
                contactPersonId = "1001",
                contactPhone = "13912345678",
                price = "¥5980",
                remainingDays = "3",
                remainingHours = "6:30",
                content = listOf(
                    "在上海外国语大学浸入式英语环境中学习英语，培养孩子良好的英语语感及口语运用能力。",
                    "上海滩景点畅游、博物馆、知名大学参访，展开真正的上海文化寻根游学之旅。",
                    "上海迪士尼乐园畅游，学习游乐两不误。"
                ),
                summary = "行程特色：1.在上海外国语大学浸入式英语环境中学习英语，培养孩子良好的英语语感及口语运用能力。\n2.上海滩景点畅游、博物馆、知名大学参访，展开真正的上海文化寻根游学之旅。\n3.上海迪士尼乐园畅游，学习游乐两不误。",
                isFavorite = true
            ),
            TailOrder(
                id = 2,
                title = "北京清华北大文化探访3日游",
                company = "北京导游协会",
                companyId = "bj_guide",
                contactPerson = "李明",
                contactPersonId = "1002",
                contactPhone = "13887654321",
                price = "¥2380",
                remainingDays = "2",
                remainingHours = "12:45",
                content = listOf(
                    "参访中国顶尖学府清华大学和北京大学，感受百年学府的文化底蕴和学术氛围。",
                    "游览北京故宫、长城等著名景点，深入了解中国传统文化与历史。",
                    "特别安排与在校学生交流互动环节，开拓视野。"
                ),
                summary = "行程特色：1.参访中国顶尖学府清华大学和北京大学，感受百年学府的文化底蕴和学术氛围。\n2.游览北京故宫、长城等著名景点，深入了解中国传统文化与历史。\n3.特别安排与在校学生交流互动环节，开拓视野。",
                isFavorite = false
            ),
            TailOrder(
                id = 3,
                title = "杭州西湖+乌镇4日文化之旅",
                company = "杭州旅游公司",
                companyId = "hz_travel",
                contactPerson = "王芳",
                contactPersonId = "1003",
                contactPhone = "13566778899",
                price = "¥3280",
                remainingDays = "1",
                remainingHours = "8:20",
                content = listOf(
                    "游览西湖十景，欣赏上有天堂，下有苏杭的美景，体验杭州的自然与人文之美。",
                    "探访千年水乡乌镇，感受江南水乡的古朴风情与悠久历史。",
                    "品尝地道杭帮菜，体验当地茶文化，深度感受江南水乡生活。"
                ),
                summary = "行程特色：1.游览西湖十景，欣赏上有天堂，下有苏杭的美景，体验杭州的自然与人文之美。\n2.探访千年水乡乌镇，感受江南水乡的古朴风情与悠久历史。\n3.品尝地道杭帮菜，体验当地茶文化，深度感受江南水乡生活。",
                isFavorite = true
            ),
            TailOrder(
                id = 4,
                title = "厦门鼓浪屿海景2日休闲游",
                company = "厦门旅行社",
                companyId = "xm_travel",
                contactPerson = "陈小玲",
                contactPersonId = "1004",
                contactPhone = "13277889900",
                price = "¥1680",
                remainingDays = "4",
                remainingHours = "15:10",
                content = listOf(
                    "漫步鼓浪屿，探访万国建筑博物馆，聆听钢琴之岛的动人旋律。",
                    "游览厦门大学，感受中国最美大学校园的独特魅力。",
                    "品尝正宗闽南美食，尽享海岛休闲时光。"
                ),
                summary = "行程特色：1.漫步鼓浪屿，探访万国建筑博物馆，聆听钢琴之岛的动人旋律。\n2.游览厦门大学，感受中国最美大学校园的独特魅力。\n3.品尝正宗闽南美食，尽享海岛休闲时光。",
                isFavorite = false
            ),
            TailOrder(
                id = 5,
                title = "成都熊猫基地+都江堰3日游",
                company = "四川旅游集团",
                companyId = "sc_travel",
                contactPerson = "赵刚",
                contactPersonId = "1005",
                contactPhone = "13588990011",
                price = "¥2580",
                remainingDays = "2",
                remainingHours = "10:30",
                content = listOf(
                    "近距离观赏国宝大熊猫，了解熊猫保护与繁育知识。",
                    "参观都江堰水利工程，领略古代科技智慧的瑰宝。",
                    "品尝成都地道川菜，感受天府之国的美食文化。"
                ),
                summary = "行程特色：1.近距离观赏国宝大熊猫，了解熊猫保护与繁育知识。\n2.参观都江堰水利工程，领略古代科技智慧的瑰宝。\n3.品尝成都地道川菜，感受天府之国的美食文化。",
                isFavorite = true
            ),
            TailOrder(
                id = 6,
                title = "青岛海滨度假4日游",
                company = "山东旅行社",
                companyId = "sd_travel",
                contactPerson = "孙媛",
                contactPersonId = "1006",
                contactPhone = "13644556677",
                price = "¥2980",
                remainingDays = "5",
                remainingHours = "9:15",
                content = listOf(
                    "漫步青岛栈桥和八大关，欣赏德国风情建筑与碧海蓝天。",
                    "参观青岛啤酒博物馆，了解百年啤酒历史与文化。",
                    "品尝新鲜海鲜，尽享滨海城市的休闲生活。"
                ),
                summary = "行程特色：1.漫步青岛栈桥和八大关，欣赏德国风情建筑与碧海蓝天。\n2.参观青岛啤酒博物馆，了解百年啤酒历史与文化。\n3.品尝新鲜海鲜，尽享滨海城市的休闲生活。",
                isFavorite = false
            ),
            TailOrder(
                id = 7,
                title = "云南丽江大理5日民族风情游",
                company = "云南旅游公司",
                companyId = "yn_travel",
                contactPerson = "王小明",
                contactPersonId = "1007",
                contactPhone = "13712345678",
                price = "¥4580",
                remainingDays = "3",
                remainingHours = "14:25",
                content = listOf(
                    "探访丽江古城和大理古城，感受纳西族与白族文化的独特魅力。",
                    "游览洱海和玉龙雪山，欣赏云南多样自然风光。",
                    "体验当地民族特色活动，品尝云南特色美食。"
                ),
                summary = "行程特色：1.探访丽江古城和大理古城，感受纳西族与白族文化的独特魅力。\n2.游览洱海和玉龙雪山，欣赏云南多样自然风光。\n3.体验当地民族特色活动，品尝云南特色美食。"
            ),
            TailOrder(
                id = 8,
                title = "西安兵马俑+华山4日历史探索",
                company = "陕西文化旅游",
                companyId = "sx_travel",
                contactPerson = "李小红",
                contactPersonId = "1008",
                contactPhone = "13987654321",
                price = "¥3180",
                remainingDays = "2",
                remainingHours = "11:40",
                content = listOf(
                    "探访世界第八大奇迹兵马俑，领略秦始皇陵的宏伟气势。",
                    "登临华山，体验中国五岳之一的险峻壮美。",
                    "品尝陕西特色小吃，漫步回民街，感受古都文化。"
                ),
                summary = "行程特色：1.探访世界第八大奇迹兵马俑，领略秦始皇陵的宏伟气势。\n2.登临华山，体验中国五岳之一的险峻壮美。\n3.品尝陕西特色小吃，漫步回民街，感受古都文化。"
            )
        )
    )}

    // 添加状态跟踪当前选择的标签
    var selectedTab by remember { mutableStateOf(0) }

    // 根据selectedTab筛选要显示的尾单
    val displayedTailOrders = if (selectedTab == 0) {
        // 显示所有尾单
        tailOrders
    } else {
        // 只显示已收藏的尾单
        tailOrders.filter { it.isFavorite }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("我的尾单", fontWeight = FontWeight.Bold) },
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
                    selected = false,
                    onClick = onPublishClick,
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = Color.Gray
                )

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = "尾单") },
                    label = { Text("尾单", fontSize = 12.sp) },
                    selected = true,
                    onClick = { /* 已在尾单页面 */ },
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
                .background(BackgroundColor)
        ) {
            // 搜索框
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp)),
                    placeholder = { Text("搜索尾单") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "搜索") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        unfocusedContainerColor = Color.White
                    )
                )
            }

            // 标签选择器
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color(0xFFF5F5F5),
                    contentColor = PrimaryColor,
                    indicator = { tabPositions ->
                        Box {}  // 不显示指示器
                    },
                    divider = { }  // 不显示分隔线
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        modifier = Modifier
                            .weight(1f)
                            .background(if (selectedTab == 0) Color.White else Color(0xFFF5F5F5))
                    ) {
                        Text(
                            text = "全部信息",
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = if (selectedTab == 0) Color.Black else Color.Gray
                        )
                    }

                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        modifier = Modifier
                            .weight(1f)
                            .background(if (selectedTab == 1) Color.White else Color(0xFFF5F5F5))
                    ) {
                        Text(
                            text = "我的收藏",
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = if (selectedTab == 1) Color.Black else Color.Gray
                        )
                    }
                }
            }

            // 尾单列表 - 使用过滤后的列表
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(displayedTailOrders) { tailOrder ->
                    TailOrderItem(
                        tailOrder = tailOrder,
                        onClick = { onItemClick(tailOrder.id) },
                        onCompanyClick = { onCompanyClick(tailOrder.companyId) },
                        onContactClick = { onContactClick(tailOrder.contactPhone) },
                        onPersonClick = { onPersonClick(tailOrder.contactPersonId) },
                        onReportItem = { reason -> onReportItem(tailOrder.id, reason) },
                        onDeleteItem = {
                            tailOrders = tailOrders.filter { it.id != tailOrder.id }
                            onDeleteItem(tailOrder.id)
                        },
                        onFavoriteClick = { isFavorite ->
                            // 更新尾单的收藏状态
                            tailOrders = tailOrders.map {
                                if (it.id == tailOrder.id) it.copy(isFavorite = isFavorite) else it
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TailOrderItem(
    tailOrder: TailOrder,
    onClick: () -> Unit,
    onCompanyClick: () -> Unit,
    onContactClick: () -> Unit,
    onPersonClick: () -> Unit,
    onReportItem: (String) -> Unit,
    onDeleteItem: () -> Unit,
    onFavoriteClick: (Boolean) -> Unit
) {
    var isFavorite by remember { mutableStateOf(tailOrder.isFavorite) }
    var showMenu by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    var reportReason by remember { mutableStateOf("") }

    // 举报对话框
    if (showReportDialog) {
        Dialog(
            onDismissRequest = { showReportDialog = false }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "输入举报原因",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = reportReason,
                        onValueChange = { reportReason = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.LightGray,
                            focusedBorderColor = PrimaryColor
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            onReportItem(reportReason)
                            reportReason = ""
                            showReportDialog = false
                        },
                        modifier = Modifier
                            .width(120.dp)
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryColor
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("确定")
                    }
                }
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        showMenu = true
                    },
                    onTap = { onClick() }
                )
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box {
            // 长按菜单 - 使用AlertDialog替代DropdownMenu
            if (showMenu) {
                AlertDialog(
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.width(280.dp),
                    content = {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(
                                onClick = {
                                    showMenu = false
                                    showReportDialog = true
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Color.White
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Warning,
                                        contentDescription = "举报",
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "举报",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Divider()

                            TextButton(
                                onClick = {
                                    onDeleteItem()
                                    showMenu = false
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Color.White
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "删除",
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "删除",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Divider()

                            TextButton(
                                onClick = { showMenu = false },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Color.White
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "取消",
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "取消",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // 标题和收藏按钮
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = tailOrder.title,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryColor,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {
                            isFavorite = !isFavorite
                            onFavoriteClick(isFavorite)
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "收藏",
                            tint = if (isFavorite) Color.Red else Color.Gray
                        )
                    }
                }

                // 分隔线
                Divider(
                    color = Color(0xFFE0E0E0),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // 行程内容
                Text(
                    text = "行程内容：",
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 显示行程内容，以编号列表形式展示
                tailOrder.content.forEachIndexed { index, content ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "${index + 1}.",
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.width(20.dp)
                        )

                        Text(
                            text = content,
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // 有效期和价格
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = tailOrder.price,
                        fontSize = 16.sp,
                        color = Color(0xFFE53935),
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "有效期: ${tailOrder.remainingDays}天${tailOrder.remainingHours}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                // 分隔线
                Divider(
                    color = Color(0xFFE0E0E0),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // 底部信息区域 - 公司信息和联系人头像
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 公司信息（可点击）
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable(onClick = onCompanyClick)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "公司",
                            tint = PrimaryColor,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = tailOrder.company,
                            color = PrimaryColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 联系人头像（可点击跳转到个人界面）
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(PrimaryColor.copy(alpha = 0.1f))
                                .border(1.dp, PrimaryColor, CircleShape)
                                .clickable(onClick = onPersonClick),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "联系人头像",
                                tint = PrimaryColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // 电话图标（可点击拨打电话）
                        IconButton(
                            onClick = onContactClick,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Phone,
                                contentDescription = "联系电话",
                                tint = PrimaryColor
                            )
                        }
                    }
                }
            }
        }
    }
}