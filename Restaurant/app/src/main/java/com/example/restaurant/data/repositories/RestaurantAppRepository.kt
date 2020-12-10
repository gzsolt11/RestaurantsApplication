package com.example.restaurant.data.repositories

import com.example.restaurant.api.RetrofitInstance
import com.example.restaurant.data.MyDatabase

class RestaurantAppRepository(
    val db: MyDatabase
) {

    /**
     * Get all restaurants for a certain country
     */
    suspend fun getRestaurants(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getRestaurants(countryCode, pageNumber)

    /**
     * Get restaurants for a certain country based on searchQuery
     * @param searchQuery - search query for country
     * @param pageNumber - pagination number
     */
    suspend fun searchRestaurantsByCountry(searchQuery: String, pageNumber: Int)=
            RetrofitInstance.api.searchRestaurantsByCountry(searchQuery,pageNumber)

    /**
     * Get all restaurants for a certain name
     * @param searchQuery - search query for name
     * @param pageNumber - pagination number
     */
    suspend fun searchRestaurantsByName(searchQuery: String, pageNumber: Int)=
            RetrofitInstance.api.searchRestaurantsByName(searchQuery,pageNumber)

    /**
     * Get all restaurants for a certain address
     * @param searchQuery - search query for address
     * @param pageNumber - pagination number
     */
    suspend fun searchRestaurantsByAddress(searchQuery: String, pageNumber: Int)=
            RetrofitInstance.api.searchRestaurantsByAddress(searchQuery,pageNumber)

    /**
     * Get all restaurants for a certain state
     * @param searchQuery - search query for state
     * @param pageNumber - pagination number
     */
    suspend fun searchRestaurantsByState(searchQuery: String, pageNumber: Int)=
            RetrofitInstance.api.searchRestaurantsByState(searchQuery,pageNumber)

    /**
     * Get all restaurants for a certain city
     * @param searchQuery - search query for city
     * @param pageNumber - pagination number
     */
    suspend fun searchRestaurantsByCity(searchQuery: String, pageNumber: Int)=
            RetrofitInstance.api.searchRestaurantsByCity(searchQuery,pageNumber)

    /**
     * Get all restaurants for a certain price
     * @param searchQuery - search query for price
     * @param pageNumber - pagination number
     */
    suspend fun searchRestaurantsByPrice(countryQuery:String, searchQuery: String, pageNumber: Int)=
            RetrofitInstance.api.searchRestaurantsByPrice(countryQuery,searchQuery,pageNumber)

}