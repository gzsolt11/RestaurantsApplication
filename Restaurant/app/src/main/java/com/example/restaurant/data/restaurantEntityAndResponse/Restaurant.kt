package com.example.restaurant.data.restaurantEntityAndResponse

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="restaurant_table")
data class Restaurant(
    @PrimaryKey(autoGenerate = true)
    val roomId: Int,
    val address: String,
    val area: String,
    val city: String,
    val country: String,
    val id: Int,
    val image_url: String,
    val lat: Double,
    val lng: Double,
    val mobile_reserve_url: String,
    val name: String,
    val phone: String,
    val postal_code: String,
    val price: Int,
    val reserve_url: String,
    val state: String
)