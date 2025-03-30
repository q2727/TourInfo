package com.example.travalms.data.repository

import androidx.compose.runtime.mutableStateListOf
import com.example.travalms.data.model.Favorite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

/**
 * 管理收藏功能的单例类
 */
object FavoriteManager {
    // 使用StateFlow存储收藏列表，便于在Compose中收集和观察
    private val _favorites = MutableStateFlow<List<Favorite>>(listOf())
    val favorites: StateFlow<List<Favorite>> = _favorites.asStateFlow()

    // 添加收藏
    fun addFavorite(postId: String, postTitle: String, agency: String, price: String) {
        val favorite = Favorite(postId, postTitle, agency, price)
        if (!isFavorite(postId)) {
            _favorites.update { currentList ->
                currentList + favorite
            }
        }
        println("已添加收藏: $postTitle") // 调试用
    }

    // 移除收藏
    fun removeFavorite(postId: String) {
        _favorites.update { currentList ->
            currentList.filter { it.postId != postId }
        }
        println("已移除收藏ID: $postId") // 调试用
    }

    // 切换收藏状态
    fun toggleFavorite(postId: String, postTitle: String, agency: String, price: String) {
        if (isFavorite(postId)) {
            removeFavorite(postId)
        } else {
            addFavorite(postId, postTitle, agency, price)
        }
    }

    // 检查是否已收藏
    fun isFavorite(postId: String): Boolean {
        return favorites.value.any { it.postId == postId }
    }

    // 获取所有收藏
    fun getAllFavorites(): List<Favorite> {
        return favorites.value
    }
} 