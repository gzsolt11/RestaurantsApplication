package com.example.restaurant.data.responses

data class Restaurants(
    val current_page: Int,
    val per_page: Int,
    val restaurants: List<Restaurant>,
    val total_entries: Int
)