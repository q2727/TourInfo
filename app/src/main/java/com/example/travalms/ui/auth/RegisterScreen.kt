package com.example.travalms.ui.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.CircularProgressIndicator

// Sample data for provinces and cities (add more as needed)
// In a real app, consider loading this from a resource file (e.g., JSON)
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
    "山东省" to listOf("济南市", "青岛市", "淄博市", "枣庄市", "东营市", "烟台市", "潍坊市", "济宁市", "泰安市", "威海市", "日照市", "临沂市", "德州市", "聊城市", "滨州市", "菏泽市"), // Corrected from Rizhao
    "河南省" to listOf("郑州市", "开封市", "洛阳市", "平顶山市", "安阳市", "鹤壁市", "新乡市", "焦作市", "濮阳市", "许昌市", "漯河市", "三门峡市", "南阳市", "商丘市", "信阳市", "周口市", "驻马店市", "济源市"),
    "湖北省" to listOf("武汉市", "黄石市", "十堰市", "宜昌市", "襄阳市", "鄂州市", "荆门市", "孝感市", "荆州市", "黄冈市", "咸宁市", "随州市", "恩施土家族苗族自治州", "仙桃市", "潜江市", "天门市", "神农架林区"),
    "湖南省" to listOf("长沙市", "株洲市", "湘潭市", "衡阳市", "邵阳市", "岳阳市", "常德市", "张家界市", "益阳市", "郴州市", "永州市", "怀化市", "娄底市", "湘西土家族苗族自治州"),
    "广东省" to listOf("广州市", "韶关市", "深圳市", "珠海市", "汕头市", "佛山市", "江门市", "湛江市", "茂名市", "肇庆市", "惠州市", "梅州市", "汕尾市", "河源市", "阳江市", "清远市", "东莞市", "中山市", "潮州市", "揭阳市", "云浮市"),
    "海南省" to listOf("海口市", "三亚市", "三沙市", "儋州市", "五指山市", "琼海市", "文昌市", "万宁市", "东方市", "定安县", "屯昌县", "澄迈县", "临高县", "白沙黎族自治县", "昌江黎族自治县", "乐东黎族自治县", "陵水黎族自治县", "保亭黎族苗族自治县", "琼中黎族苗族自治县"),
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
    "新疆维吾尔自治区" to listOf("乌鲁木齐市", "克拉玛依市", "吐鲁番市", "哈密市", "昌吉回族自治州", "博尔塔拉蒙古自治州", "巴音郭楞蒙古自治州", "阿克苏地区", "克孜勒苏柯尔克孜自治州", "喀什地区", "和田地区", "伊犁哈萨克自治州", "塔城地区", "阿勒泰地区", "石河子市", "阿拉尔市", "图木舒克市", "五家渠市", "北屯市", "铁门关市", "双河市", "可克达拉市", "昆玉市"),

    // Special Administrative Regions
    "香港特别行政区" to listOf("香港岛", "九龙", "新界"),
    "澳门特别行政区" to listOf("澳门半岛", "氹仔", "路氹", "路环")
    // Taiwan data can be added if needed, depending on requirements
    // "台湾省" to listOf(...) 
)

val provinces = provinceCityData.keys.toList()

/**
 * 注册屏幕组件
 * 提供用户注册功能
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    registerViewModel: RegisterViewModel = viewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    
    // Basic account info
    var username by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    // Company info
    var companyName by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    
    // Location info
    var selectedProvince by remember { mutableStateOf<String?>(null) }
    var selectedCity by remember { mutableStateOf<String?>(null) }
    var citiesForSelectedProvince by remember { mutableStateOf<List<String>>(emptyList()) }
    var provinceDropdownExpanded by remember { mutableStateOf(false) }
    var cityDropdownExpanded by remember { mutableStateOf(false) }
    
    // File paths (in a real app, these would be set after upload)
    var businessLicensePath by remember { mutableStateOf<String?>(null) }
    var idCardFrontPath by remember { mutableStateOf<String?>(null) }
    var idCardBackPath by remember { mutableStateOf<String?>(null) }
    
    val uiState by registerViewModel.uiState.collectAsState()

    // Update cities when province changes
    LaunchedEffect(selectedProvince) {
        citiesForSelectedProvince = selectedProvince?.let { provinceCityData[it] } ?: emptyList()
        if (selectedProvince != null && selectedCity !in citiesForSelectedProvince) {
            selectedCity = null // Reset city if it's not valid for the new province
        }
    }

    LaunchedEffect(key1 = uiState) {
        when (val state = uiState) {
            is RegisterUiState.Success -> {
                Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show()
                onLoginClick()
            }
            is RegisterUiState.Error -> {
                Toast.makeText(context, "注册失败: ${state.message}", Toast.LENGTH_LONG).show()
                registerViewModel.resetState()
            }
            else -> {
                // Idle or Loading
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                }
                Text(
                    text = "注册",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(48.dp))
            }
            
            // Registration form card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                ) {
                    // Username
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("用户名") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Nickname
                    OutlinedTextField(
                        value = nickname,
                        onValueChange = { nickname = it },
                        label = { Text("昵称") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("电子邮箱") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("设置密码 (8-20位)") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Confirm Password
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("确认密码") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Company Name
                    OutlinedTextField(
                        value = companyName,
                        onValueChange = { companyName = it },
                        label = { Text("公司名称") }, // Removed *
                        leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Mobile Number
                    OutlinedTextField(
                        value = mobileNumber,
                        onValueChange = { mobileNumber = it },
                        label = { Text("手机号") }, // Removed *
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Location Selection
                    Text(
                        text = "所在地", // Removed *
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Province Dropdown
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
                                            selectedProvince = province
                                            selectedCity = null // Reset city when province changes
                                            provinceDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                        
                        // City Dropdown
                        ExposedDropdownMenuBox(
                            expanded = cityDropdownExpanded && citiesForSelectedProvince.isNotEmpty(),
                            onExpandedChange = { cityDropdownExpanded = !cityDropdownExpanded },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedCity ?: "选择城市",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("城市") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityDropdownExpanded) },
                                enabled = selectedProvince != null, // Enable only when province is selected
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
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Business License Upload Button
                    Button(
                        onClick = { /* Implement upload logic */ },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("上传营业执照")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // ID Card Front Upload Button
                    Button(
                        onClick = { /* Implement upload logic */ },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("上传身份证正面")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // ID Card Back Upload Button
                    Button(
                        onClick = { /* Implement upload logic */ },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("上传身份证反面")
                    }
                }
            }
            
            // Register Button
            Button(
                onClick = {
                    val currentProvince = selectedProvince
                    val currentCity = selectedCity
                    
                    if (password != confirmPassword) {
                        Toast.makeText(context, "两次输入的密码不一致", Toast.LENGTH_SHORT).show()
                    } else if (companyName.isBlank()) {
                        Toast.makeText(context, "公司名称不能为空", Toast.LENGTH_SHORT).show()
                    } else if (mobileNumber.isBlank()) {
                        Toast.makeText(context, "手机号不能为空", Toast.LENGTH_SHORT).show()
                    } else if (currentProvince == null || currentCity == null) {
                        Toast.makeText(context, "请选择所在地", Toast.LENGTH_SHORT).show()
                    } else {
                        registerViewModel.performRegister(
                            username = username,
                            password = password,
                            nickname = nickname.takeIf { it.isNotBlank() },
                            email = email.takeIf { it.isNotBlank() },
                            companyName = companyName,
                            mobileNumber = mobileNumber,
                            province = currentProvince, // Pass selected province
                            city = currentCity,       // Pass selected city
                            businessLicensePath = businessLicensePath,
                            idCardFrontPath = idCardFrontPath,
                            idCardBackPath = idCardBackPath
                        )
                    }
                },
                enabled = uiState != RegisterUiState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                if (uiState == RegisterUiState.Loading) {
                    CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                } else {
                    Text("立即注册", fontSize = 16.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            // Go to Login prompt
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("已有账号? ")
                TextButton(onClick = onLoginClick) {
                    Text("去登录")
                }
            }
            Spacer(modifier = Modifier.height(24.dp)) // Bottom padding for scroll
        }
    }
} 