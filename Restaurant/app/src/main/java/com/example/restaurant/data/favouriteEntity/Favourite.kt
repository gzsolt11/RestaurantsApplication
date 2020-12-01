package com.example.restaurant.data.favouriteEntity

import androidx.room.Entity

@Entity(tableName="favourite_table", primaryKeys = ["userId", "restaurantId"])
data class Favourite (
        val userId: Int,
        val restaurantId: Int
)