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
}