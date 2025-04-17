package com.example.travalms.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travalms.ui.theme.PrimaryColor
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travalms.ui.viewmodels.ProfileEditViewModel
import com.example.travalms.ui.viewmodels.ProfileEditUiState

// 省份和城市数据 (与注册页面保持一致)
// Consider loading this from a resource file or data source in a real app
val provinceCityData = mapOf(
    // Municipalities
    "北京市" to listOf("北京市"),
    "上海市" to listOf("上海市"),
    "天津市" to listOf("天津市"),
    "重庆市" to listOf("重庆市"),
    // Provinces
    "河北省" to listOf("石家庄市", "唐山市", "秦皇岛市", "邯郸市", "邢台市", "保定市", "张家口市", "承德市", "沧州市", "廊坊市", "衡水市"),
    "山西省" to listOf("太原市", "大同市", "阳泉市", "长治市", "晋城市", "朔州市", "晋中市", "运城市", "忻州市", "临汾市", "吕梁市"),
    "辽宁省" to listOf("沈阳市", "大连市", "鞍山市", "抚顺市", "本溪市", "丹东市", "锦州市", "营口市", "阜新市", "辽阳市", "盘锦市", "铁岭市", "朝阳市", "葫芦岛市"),
    "吉林省" to listOf("长春市", "吉林市", "四平市", "辽源市", "通化市", "白山市", "松原市", "白城市", "延边朝鲜族自治州"),
    "黑龙江省" to listOf("哈尔滨市", "齐齐哈尔市", "鸡西市", "鹤岗市", "双鸭山市", "大庆市", "伊春市", "佳木斯市", "七台河市", "牡丹江市", "黑河市", "绥化市", "大兴安岭地区"),
    "江苏省" to listOf("南京市", "无锡市", "徐州市", "常州市", "苏州市", "南通市", "连云港市", "淮安市", "盐城市", "扬州市", "镇江市", "泰州市", "宿迁市"),
    "浙江省" to listOf("杭州市", "宁波市", "温州市", "嘉兴市", "湖州市", "绍兴市", "金华市", "衢州市", "舟山市", "台州市", "丽水市"),
    "安徽省" to listOf("合肥市", "芜湖市", "蚌埠市", "淮南市", "马鞍山市", "淮北市", "铜陵市", "安庆市", "黄山市", "滁州市", "阜阳市", "宿州市", "六安市", "亳州市", "池州市", "宣城市"),
    "福建省" to listOf("福州市", "厦门市", "莆田市", "三明市", "泉州市", "漳州市", "南平市", "龙岩市", "宁德市"),
    "江西省" to listOf("南昌市", "景德镇市", "萍乡市", "九江市", "新余市", "鹰潭市", "赣州市", "吉安市", "宜春市", "抚州市", "上饶市"),
    "山东省" to listOf("济南市", "青岛市", "淄博市", "枣庄市", "东营市", "烟台市", "潍坊市", "济宁市", "泰安市", "威海市", "日照市", "临沂市", "德州市", "聊城市", "滨州市", "菏泽市"),
    "河南省" to listOf("郑州市", "开封市", "洛阳市", "平顶山市", "安阳市", "鹤壁市", "新乡市", "焦作市", "濮阳市", "许昌市", "漯河市", "三门峡市", "南阳市", "商丘市", "信阳市", "周口市", "驻马店市"),
    "湖北省" to listOf("武汉市", "黄石市", "十堰市", "宜昌市", "襄阳市", "鄂州市", "荆门市", "孝感市", "荆州市", "黄冈市", "咸宁市", "随州市", "恩施土家族苗族自治州"),
    "湖南省" to listOf("长沙市", "株洲市", "湘潭市", "衡阳市", "邵阳市", "岳阳市", "常德市", "张家界市", "益阳市", "郴州市", "永州市", "怀化市", "娄底市", "湘西土家族苗族自治州"),
    "广东省" to listOf("广州市", "韶关市", "深圳市", "珠海市", "汕头市", "佛山市", "江门市", "湛江市", "茂名市", "肇庆市", "惠州市", "梅州市", "汕尾市", "河源市", "阳江市", "清远市", "东莞市", "中山市", "潮州市", "揭阳市", "云浮市"),
    "海南省" to listOf("海口市", "三亚市", "三沙市", "儋州市"),
    "四川省" to listOf("成都市", "自贡市", "攀枝花市", "泸州市", "德阳市", "绵阳市", "广元市", "遂宁市", "内江市", "乐山市", "南充市", "眉山市", "宜宾市", "广安市", "达州市", "雅安市", "巴中市", "资阳市", "阿坝藏族羌族自治州", "甘孜藏族自治州", "凉山彝族自治州"),
    "贵州省" to listOf("贵阳市", "六盘水市", "遵义市", "安顺市", "毕节市", "铜仁市", "黔西南布依族苗族自治州", "黔东南苗族侗族自治州", "黔南布依族苗族自治州"),
    "云南省" to listOf("昆明市", "曲靖市", "玉溪市", "保山市", "昭通市", "丽江市", "普洱市", "临沧市", "楚雄彝族自治州", "红河哈尼族彝族自治州", "文山壮族苗族自治州", "西双版纳傣族自治州", "大理白族自治州", "德宏傣族景颇族自治州", "怒江傈僳族自治州", "迪庆藏族自治州"),
    "陕西省" to listOf("西安市", "铜川市", "宝鸡市", "咸阳市", "渭南市", "延安市", "汉中市", "榆林市", "安康市", "商洛市"),
    "甘肃省" to listOf("兰州市", "嘉峪关市", "金昌市", "白银市", "天水市", "武威市", "张掖市", "平凉市", "酒泉市", "庆阳市", "定西市", "陇南市", "临夏回族自治州", "甘南藏族自治州"),
    "青海省" to listOf("西宁市", "海东市", "海北藏族自治州", "黄南藏族自治州", "海南藏族自治州", "果洛藏族自治州", "玉树藏族自治州", "海西蒙古族藏族自治州"),
    // Autonomous Regions
    "内蒙古自治区" to listOf("呼和浩特市", "包头市", "乌海市", "赤峰市", "通辽市", "鄂尔多斯市", "呼伦贝尔市", "巴彦淖尔市", "乌兰察布市", "兴安盟", "锡林郭勒盟", "阿拉善盟"),
    "广西壮族自治区" to listOf("南宁市", "柳州市", "桂林市", "梧州市", "北海市", "防城港市", "钦州市", "贵港市", "玉林市", "百色市", "贺州市", "河池市", "来宾市", "崇左市"),
    "西藏自治区" to listOf("拉萨市", "日喀则市", "昌都市", "林芝市", "山南市", "那曲市", "阿里地区"),
    "宁夏回族自治区" to listOf("银川市", "石嘴山市", "吴忠市", "固原市", "中卫市"),
    "新疆维吾尔自治区" to listOf("乌鲁木齐市", "克拉玛依市", "吐鲁番市", "哈密市", "昌吉回族自治州", "博尔塔拉蒙古自治州", "巴音郭楞蒙古自治州", "阿克苏地区", "克孜勒苏柯尔克孜自治州", "喀什地区", "和田地区", "伊犁哈萨克自治州", "塔城地区", "阿勒泰地区"),
    // Special Administrative Regions
    "香港特别行政区" to listOf("香港岛", "九龙", "新界"),
    "澳门特别行政区" to listOf("澳门半岛", "氹仔", "路氹", "路环")
)

val provinces = provinceCityData.keys.toList()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    onBackClick: () -> Unit,
    onSaveClick: (updatedInfo: Map<String, String?>) -> Unit,
    onBindCompanyClick: () -> Unit,
    onVerificationClick: () -> Unit,
    profileEditViewModel: ProfileEditViewModel = viewModel()
) {
    val uiState by profileEditViewModel.uiState.collectAsState()
    
    val scrollState = rememberScrollState()
    
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    LaunchedEffect(uiState) {
        errorMessage = when (uiState) {
            is ProfileEditUiState.Error -> (uiState as ProfileEditUiState.Error).message
            else -> null
        }
    }
    
    val userData = when (uiState) {
        is ProfileEditUiState.Success -> uiState as ProfileEditUiState.Success
        else -> ProfileEditUiState.Success()
    }
    
    var username by remember { mutableStateOf(userData.username) }
    var nickname by remember { mutableStateOf(userData.nickname) }
    var email by remember { mutableStateOf(userData.email) }
    var phone by remember { mutableStateOf(userData.phone) }
    var qq by remember { mutableStateOf(userData.qq) }
    var wechat by remember { mutableStateOf(userData.wechat) }
    var companyName by remember { mutableStateOf(userData.companyName) }
    var selectedProvince by remember { mutableStateOf(userData.province) }
    var selectedCity by remember { mutableStateOf(userData.city) }
    var introduction by remember { mutableStateOf(userData.introduction) }
    var isVerified by remember { mutableStateOf(userData.isVerified) }
    
    var citiesForSelectedProvince by remember { mutableStateOf<List<String>>(emptyList()) }
    var provinceDropdownExpanded by remember { mutableStateOf(false) }
    var cityDropdownExpanded by remember { mutableStateOf(false) }
    
    var showPasswordDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(selectedProvince) {
        citiesForSelectedProvince = selectedProvince?.let { provinceCityData[it] } ?: emptyList()
        if (selectedCity !in citiesForSelectedProvince && citiesForSelectedProvince.isNotEmpty()) {
            selectedCity = null
        }
    }

    LaunchedEffect(userData) {
        username = userData.username
        nickname = userData.nickname
        email = userData.email
        phone = userData.phone
        qq = userData.qq
        wechat = userData.wechat
        companyName = userData.companyName
        selectedProvince = userData.province
        selectedCity = userData.city
        introduction = userData.introduction
        isVerified = userData.isVerified
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("编辑个人资料", fontWeight = FontWeight.Bold) },
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
        Box(modifier = Modifier.fillMaxSize()) {
            when (uiState) {
                is ProfileEditUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.Center),
                        color = PrimaryColor
                    )
                }
                is ProfileEditUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = errorMessage ?: "获取用户信息失败",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { profileEditViewModel.loadUserProfile() },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
                        ) {
                            Text("重试")
                        }
                    }
                }
                is ProfileEditUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .verticalScroll(scrollState)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        ProfileInfoRow(label = "用户ID", value = userData.userId.ifEmpty { "未设置" }, editable = false)
                        ListDivider()

                        ProfileInfoRow(label = "用户名", value = username, editable = false)
                        ListDivider()

                        ProfileEditableRow(label = "昵称", value = nickname, onValueChange = { nickname = it })
                        ListDivider()

                        ProfileEditableRow(label = "电子邮箱", value = email, onValueChange = { email = it })
                        ListDivider()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "实名认证",
                                modifier = Modifier.width(100.dp),
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (isVerified) "已认证" else "未认证",
                                modifier = Modifier.weight(1f),
                                fontSize = 16.sp,
                                color = if (isVerified) Color.Green else Color.Red
                            )
                            if (!isVerified) {
                                Button(
                                    onClick = onVerificationClick,
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Text("去认证")
                                }
                            } else {
                                Spacer(modifier = Modifier.width(70.dp))
                            }
                        }
                        ListDivider()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "密码",
                                modifier = Modifier.width(100.dp),
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "********",
                                modifier = Modifier.weight(1f),
                                fontSize = 16.sp
                            )
                            IconButton(onClick = { showPasswordDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "修改密码",
                                    tint = PrimaryColor
                                )
                            }
                        }
                        ListDivider()

                        ProfileEditableRow(label = "手机", value = phone, onValueChange = { phone = it })
                        ListDivider()

                        ProfileEditableRow(label = "QQ", value = qq, onValueChange = { qq = it }, placeholder = "选填")
                        ListDivider()

                        ProfileEditableRow(label = "微信", value = wechat, onValueChange = { wechat = it }, placeholder = "选填")
                        ListDivider()

                        ProfileEditableRow(label = "公司名称", value = companyName, onValueChange = { companyName = it })
                        ListDivider()

                        Text(
                            text = "所在地",
                            modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ExposedDropdownMenuBox(
                                expanded = provinceDropdownExpanded,
                                onExpandedChange = { provinceDropdownExpanded = !provinceDropdownExpanded },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = selectedProvince ?: "选择省份",
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("省份") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = provinceDropdownExpanded) },
                                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                ExposedDropdownMenu(
                                    expanded = provinceDropdownExpanded,
                                    onDismissRequest = { provinceDropdownExpanded = false }
                                ) {
                                    provinces.forEach { province ->
                                        DropdownMenuItem(
                                            text = { Text(province) },
                                            onClick = {
                                                if (selectedProvince != province) {
                                                    selectedProvince = province
                                                    selectedCity = null
                                                }
                                                provinceDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            ExposedDropdownMenuBox(
                                expanded = cityDropdownExpanded && citiesForSelectedProvince.isNotEmpty(),
                                onExpandedChange = {
                                    if (selectedProvince != null) {
                                        cityDropdownExpanded = !cityDropdownExpanded
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = selectedCity ?: "选择城市",
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("城市") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityDropdownExpanded) },
                                    enabled = selectedProvince != null,
                                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                ExposedDropdownMenu(
                                    expanded = cityDropdownExpanded && citiesForSelectedProvince.isNotEmpty(),
                                    onDismissRequest = { cityDropdownExpanded = false }
                                ) {
                                    citiesForSelectedProvince.forEach { city ->
                                        DropdownMenuItem(
                                            text = { Text(city) },
                                            onClick = {
                                                selectedCity = city
                                                cityDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        ListDivider()

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp, bottom = 8.dp)
                        ) {
                            Text(
                                text = "简介",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            OutlinedTextField(
                                value = introduction,
                                onValueChange = { introduction = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                shape = RoundedCornerShape(8.dp),
                                placeholder = { Text("介绍一下自己或业务...") }
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = {
                                val updatedInfo = mapOf(
                                    "nickname" to nickname,
                                    "email" to email,
                                    "phone" to phone,
                                    "qq" to qq,
                                    "wechat" to wechat,
                                    "companyName" to companyName,
                                    "province" to selectedProvince,
                                    "city" to selectedCity,
                                    "introduction" to introduction
                                ).filterValues { it != null }
                                
                                profileEditViewModel.updateUserProfile(updatedInfo as Map<String, String?>)
                                
                                onSaveClick(updatedInfo as Map<String, String?>)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "保存修改",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
        
        if (showPasswordDialog) {
            SimplePasswordChangeDialog(
                onDismiss = { showPasswordDialog = false },
                onConfirm = { currentPassword, newPassword ->
                    showPasswordDialog = false
                }
            )
        }
    }
}

@Composable
fun ProfileInfoRow(
    label: String,
    value: String,
    editable: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.width(100.dp),
            color = Color.Gray,
            fontSize = 14.sp
        )
        Text(
            text = value.ifEmpty { "-" },
            modifier = Modifier.weight(1f),
            fontSize = 16.sp
        )
        if (editable && onClick != null) {
            IconButton(onClick = onClick, modifier = Modifier.size(48.dp)) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "编辑$label",
                    tint = PrimaryColor
                )
            }
        } else {
            Spacer(modifier = Modifier.width(48.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditableRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.width(100.dp),
            color = Color.Gray,
            fontSize = 14.sp
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp),
            placeholder = { Text(placeholder, fontSize = 14.sp) },
            singleLine = singleLine,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryColor,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = PrimaryColor
            ),
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation
        )
    }
}

@Composable
fun ListDivider() {
    Divider(modifier = Modifier.padding(vertical = 4.dp), color = Color.LightGray.copy(alpha = 0.5f))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimplePasswordChangeDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("修改密码") },
        text = {
            Column {
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 8.dp),
                        fontSize = 14.sp
                    )
                }

                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it; errorMessage = null },
                    label = { Text("当前密码") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    isError = errorMessage?.contains("当前密码") == true
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it; errorMessage = null },
                    label = { Text("新密码 (8-20位)") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    isError = errorMessage?.contains("新密码") == true
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it; errorMessage = null },
                    label = { Text("确认新密码") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    isError = errorMessage?.contains("不一致") == true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    when {
                        currentPassword.isBlank() -> {
                            errorMessage = "请输入当前密码"
                        }
                        newPassword.length < 8 || newPassword.length > 20 -> {
                            errorMessage = "新密码长度必须在8-20位之间"
                        }
                        newPassword != confirmPassword -> {
                            errorMessage = "两次输入的新密码不一致"
                        }
                        else -> {
                            errorMessage = null
                            onConfirm(currentPassword, newPassword)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
            ) {
                Text("确认修改", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消", color = PrimaryColor)
            }
        }
    )
}

/*
@Preview(showBackground = true)
@Composable
fun ProfileEditScreenPreview() {
    ProfileEditScreen(
        onBackClick = {},
        onSaveClick = {},
        onBindCompanyClick = {},
        onVerificationClick = {},
        initialProvince = "四川省",
        initialCity = "成都市"
    )
}

@Preview(showBackground = true)
@Composable
fun SimplePasswordChangeDialogPreview() {
    SimplePasswordChangeDialog(onDismiss = {}, onConfirm = { _, _ -> })
}
*/ 