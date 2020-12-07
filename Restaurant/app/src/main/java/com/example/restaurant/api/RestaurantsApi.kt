package com.example.restaurant.api

import com.example.restaurant.data.restaurantEntityAndResponse.Restaurants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RestaurantsApi {

    @GET("restaurants")
    suspend fun getRestaurants(
        @Query("country")
        countryCode: String = "US",
        @Query("page")
        pageNumber: Int = 1,
    ): Response<Restaurants>

    @GET("restaurants")
    suspend fun searchRestaurantsByCountry(
            @Query("country")
            searchQuery: String = "us",
            @Query("page")
            pageNumber: Int = 1,
    ): Response<Restaurants>

    @GET("restaurants")
    suspend fun searchRestaurantsByName(
            @Query("name")
            searchQuery: String,
            @Query("page")
            pageNumber: Int = 1,
    ): Response<Restaurants>

    @GET("restaurants")
    suspend fun searchRestaurantsByPrice(
            @Query("country")
            countryQuery: String = "us",
            @Query("price")
            searchQuery: String,
            @Query("page")
            pageNumber: Int = 1,
    ): Response<Restaurants>

    @GET("restaurants")
    suspend fun searchRestaurantsByAddress(
            @Query("address")
            searchQuery: String,
            @Query("page")
            pageNumber: Int = 1,
    ): Response<Restaurants>

    @GET("restaurants")
    suspend fun searchRestaurantsByCity(
            @Query("city")
            searchQuery: String,
            @Query("page")
            pageNumber: Int = 1,
    ): Response<Restaurants>



    @GET("restaurants")
    suspend fun searchRestaurantsByState(
            @Query("state")
            searchQuery: String,
            @Query("page")
            pageNumber: Int = 1,
    ): Response<Restaurants>

}