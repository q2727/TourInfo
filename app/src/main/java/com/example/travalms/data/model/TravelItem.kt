package com.example.travalms.data.model

data class TravelItem(
    val id: Int,
    val title: String,
    val agency: String,
    val price: String,
    val hasImage: Boolean = false,
    val isFavorite: Boolean = false
) 