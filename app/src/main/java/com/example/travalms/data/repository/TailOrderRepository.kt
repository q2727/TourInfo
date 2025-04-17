package com.example.travalms.data.repository

import com.example.travalms.ui.screens.TailOrder

interface TailOrderRepository {
    /**
     * 获取所有尾单列表
     */
    suspend fun getTailOrders(): List<TailOrder>
    
    /**
     * 根据ID获取尾单详情
     */
    suspend fun getTailOrderById(id: String): TailOrder?
    
    /**
     * 更新尾单收藏状态
     */
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean): Boolean
} 