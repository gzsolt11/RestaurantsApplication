package com.example.restaurant.data.repositories

import com.example.restaurant.api.RetrofitInstance
import com.example.restaurant.data.MyDatabase

class RestaurantAppRepository(
    val db: MyDatabase
) {

    suspend fun getRestaurants(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getRestaurants(countryCode, pageNumber)

    suspend fun searchRestaurantsByCountry(searchQuery: String, pageNumber: Int)=
            RetrofitInstance.api.searchRestaurantsByCountry(searchQuery,pageNumber)

    suspend fun searchRestaurantsByName(searchQuery: String, pageNumber: Int)=
            RetrofitInstance.api.searchRestaurantsByName(searchQuery,pageNumber)


    suspend fun searchRestaurantsByAddress(searchQuery: String, pageNumber: Int)=
            RetrofitInstance.api.searchRestaurantsByAddress(searchQuery,pageNumber)

    suspend fun searchRestaurantsByState(searchQuery: String, pageNumber: Int)=
            RetrofitInstance.api.searchRestaurantsByState(searchQuery,pageNumber)

    suspend fun searchRestaurantsByCity(searchQuery: String, pageNumber: Int)=
            RetrofitInstance.api.searchRestaurantsByCity(searchQuery,pageNumber)

    suspend fun searchRestaurantsByPrice(countryQuery:String, searchQuery: String, pageNumber: Int)=
            RetrofitInstance.api.searchRestaurantsByPrice(countryQuery,searchQuery,pageNumber)

}