package com.example.travalms.data.repository

import com.example.travalms.ui.screens.TailOrder
import com.example.travalms.data.remote.XMPPManager
import kotlin.random.Random

/**
 * TailOrder仓库实现，使用单例模式
 */
class TailOrderRepositoryImpl private constructor() : TailOrderRepository {
    
    companion object {
        @Volatile
        private var INSTANCE: TailOrderRepositoryImpl? = null
        
        fun getInstance(): TailOrderRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TailOrderRepositoryImpl().also { INSTANCE = it }
            }
        }
    }
    
    private val xmppManager = XMPPManager.getInstance()
    private val mockTailOrders = createMockTailOrders()
    private val favoriteTailOrders = mutableSetOf<String>()
    
    /**
     * 获取所有尾单列表
     */
    override suspend fun getTailOrders(): List<TailOrder> {
        // 在实际应用中，可能从XMPP服务器或本地数据库获取
        return mockTailOrders
    }
    
    /**
     * 根据ID获取尾单详情
     */
    override suspend fun getTailOrderById(id: String): TailOrder? {
        return mockTailOrders.find { it.id.toString() == id }
    }
    
    /**
     * 更新尾单收藏状态
     */
    override suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean): Boolean {
        if (isFavorite) {
            favoriteTailOrders.add(id)
        } else {
            favoriteTailOrders.remove(id)
        }
        
        // 更新本地模拟数据
        val index = mockTailOrders.indexOfFirst { it.id.toString() == id }
        if (index != -1) {
            val tailOrder = mockTailOrders[index]
            mockTailOrders[index] = tailOrder.copy(isFavorite = isFavorite)
        }
        
        return true
    }
    
    // 创建模拟数据
    private fun createMockTailOrders(): MutableList<TailOrder> {
        return mutableListOf(
            TailOrder(
                id = 1, 
                title = "上海外国语大学体验+迪士尼6日夏令营", 
                company = "上海旅行社",
                companyId = "company_1",
                contactPerson = "张伟",
                contactPersonId = "person_1",
                contactPhone = "13912345678",
                price = "¥5980", 
                remainingDays = "3", 
                remainingHours = "6:00",
                content = listOf("在上海外国语大学浸入式英语环境中学习英语，培养孩子良好的英语语感及口语运用能力。", 
                               "上海滩景点畅游、博物馆、知名大学参访，展开真正的上海文化寻根游学之旅。", 
                               "上海迪士尼乐园畅游，学习游乐两不误。"),
                summary = "行程特色：1.在上海外国语大学浸入式英语环境中学习英语，培养孩子良好的英语语感及口语运用能力。\n2.上海滩景点畅游、博物馆、知名大学参访。\n3.上海迪士尼乐园畅游，学习游乐两不误。",
                isFavorite = true
            ),
            TailOrder(
                id = 2, 
                title = "北京清华北大文化探访3日游", 
                company = "北京导游协会",
                companyId = "company_2",
                contactPerson = "李明",
                contactPersonId = "person_2",
                contactPhone = "13887654321",
                price = "¥2380", 
                remainingDays = "2", 
                remainingHours = "12:00",
                content = listOf("参访中国顶尖学府清华大学和北京大学，感受百年学府的文化底蕴和学术氛围。", 
                               "游览北京故宫、长城等著名景点，深入了解中国传统文化与历史。", 
                               "特别安排与在校学生交流互动环节，开拓视野。"),
                summary = "行程特色：1.参访中国顶尖学府清华大学和北京大学，感受百年学府的文化底蕴和学术氛围。\n2.游览北京故宫、长城等著名景点。\n3.特别安排与在校学生交流互动环节。",
                isFavorite = false
            ),
            TailOrder(
                id = 3, 
                title = "杭州西湖+乌镇4日文化之旅", 
                company = "杭州旅游公司",
                companyId = "company_3",
                contactPerson = "王芳",
                contactPersonId = "person_3",
                contactPhone = "13566778899",
                price = "¥3280", 
                remainingDays = "1", 
                remainingHours = "8:00",
                content = listOf("游览西湖十景，欣赏上有天堂，下有苏杭的美景，体验杭州的自然与人文之美。", 
                               "探访千年水乡乌镇，感受江南水乡的古朴风情与悠久历史。", 
                               "品尝地道杭帮菜，体验当地茶文化，深度感受江南水乡生活。"),
                summary = "行程特色：1.游览西湖十景，欣赏上有天堂，下有苏杭的美景。\n2.探访千年水乡乌镇，感受江南水乡风情。\n3.品尝地道杭帮菜，体验当地茶文化。",
                isFavorite = true
            )
        )
    }
} 