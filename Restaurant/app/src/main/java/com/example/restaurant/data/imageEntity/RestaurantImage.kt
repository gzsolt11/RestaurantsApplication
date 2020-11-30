package com.example.restaurant.data.imageEntity

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="restaurant_image_table")
data class RestaurantImage(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val restaurantId:Int,
    val userId: Int,
    val photo: Bitmap
)