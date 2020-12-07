package com.example.restaurant.data.restaurantEntityAndResponse

data class Restaurants(
        val current_page: Int,
        val per_page: Int,
        val restaurants: MutableList<Restaurant>,
        val total_entries: Int
)