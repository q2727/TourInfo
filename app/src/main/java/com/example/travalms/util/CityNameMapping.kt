package com.example.travalms.util

/**
 * 提供城市和地区名称拼音与中文之间的映射
 */
object CityNameMapping {
    
    /**
     * 拼音到中文的映射表
     */
    private val pinyinToChineseMap = mapOf(
        // 直辖市
        "beijing" to "北京",
        "shanghai" to "上海",
        "tianjin" to "天津",
        "chongqing" to "重庆",
        
        // 特别行政区
        "xianggang" to "香港",
        "aomen" to "澳门",
        
        // 省会城市
        "shijiazhuang" to "石家庄",
        "taiyuan" to "太原",
        "huhehaote" to "呼和浩特",
        "shenyang" to "沈阳",
        "changchun" to "长春",
        "haerbin" to "哈尔滨",
        "nanjing" to "南京",
        "hangzhou" to "杭州",
        "hefei" to "合肥",
        "fuzhou" to "福州",
        "nanchang" to "南昌",
        "jinan" to "济南",
        "zhengzhou" to "郑州",
        "wuhan" to "武汉",
        "changsha" to "长沙",
        "guangzhou" to "广州",
        "nanning" to "南宁",
        "haikou" to "海口",
        "chengdu" to "成都",
        "guiyang" to "贵阳",
        "kunming" to "昆明",
        "lasa" to "拉萨",
        "xian" to "西安",
        "lanzhou" to "兰州",
        "xining" to "西宁",
        "yinchuan" to "银川",
        "wulumuqi" to "乌鲁木齐",
        
        // 主要旅游城市
        "suzhou" to "苏州",
        "hangzhou" to "杭州",
        "xiamen" to "厦门",
        "qingdao" to "青岛",
        "dalian" to "大连",
        "sanya" to "三亚",
        "zhuhai" to "珠海",
        "guilin" to "桂林",
        "lijiang" to "丽江",
        "dali" to "大理",
        "yangshuo" to "阳朔",
        "huangshan" to "黄山",
        "zhangjiajie" to "张家界",
        "jiuzhaigou" to "九寨沟",
        "leshan" to "乐山",
        "emeishan" to "峨眉山",
        "wuxi" to "无锡",
        "ningbo" to "宁波",
        "wuhu" to "芜湖",
        "shaoxing" to "绍兴",
        "shantou" to "汕头",
        "zhuhai" to "珠海",
        "shenzhen" to "深圳",
        "foshan" to "佛山",
        "dongguan" to "东莞",
        "zhongshan" to "中山",
        "huizhou" to "惠州",
        "jiangmen" to "江门",
        
        // 北京区县
        "haidian" to "海淀",
        "chaoyang" to "朝阳",
        "dongcheng" to "东城",
        "xicheng" to "西城",
        "fengtai" to "丰台",
        "shijingshan" to "石景山",
        "tongzhou" to "通州",
        "changping" to "昌平",
        "daxing" to "大兴",
        "mentougou" to "门头沟",
        "fangshan" to "房山",
        "shunyi" to "顺义",
        "huairou" to "怀柔",
        "pinggu" to "平谷",
        "miyun" to "密云",
        "yanqing" to "延庆",
        
        // 新增其他知名旅游目的地
        "jiuzhaigou" to "九寨沟",
        "hainan" to "海南",
        "yunnan" to "云南",
        "xizang" to "西藏",
        "xinjiang" to "新疆",
        "huangshan" to "黄山",
        "taishan" to "泰山",
        "zhangjiajie" to "张家界",
        "wudangshan" to "武当山",
        "gulangyu" to "鼓浪屿",
        "qinhuangdao" to "秦皇岛",
        "beidaihe" to "北戴河",
        "dunhuang" to "敦煌",
        "pingyao" to "平遥",
        "wuzhen" to "乌镇",
        "xitang" to "西塘",
        "zhouzhuang" to "周庄",
        "tongli" to "同里",
        "lushan" to "庐山",
        "xianggelila" to "香格里拉",
        
        // 其他
        "other" to "其他"
    )
    
    /**
     * 将拼音转换为中文名称
     * @param pinyin 拼音名称
     * @return 中文名称，如果没有对应中文则返回原拼音
     */
    fun pinyinToChineseName(pinyin: String): String {
        return pinyinToChineseMap[pinyin.lowercase()] ?: pinyin
    }
    
    /**
     * 批量将拼音转换为中文名称
     * @param pinyinList 拼音名称列表
     * @return 中文名称列表
     */
    fun pinyinListToChineseNames(pinyinList: List<String>): List<String> {
        return pinyinList.map { pinyinToChineseName(it) }
    }
} 