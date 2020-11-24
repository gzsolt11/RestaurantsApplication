package com.example.restaurant.data.repositories

import com.example.restaurant.api.RetrofitInstance
import com.example.restaurant.data.MyDatabase

class RestaurantAppRepository(
    val db: MyDatabase
) {

    suspend fun getRestaurants(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getRestaurants(countryCode, pageNumber)

}