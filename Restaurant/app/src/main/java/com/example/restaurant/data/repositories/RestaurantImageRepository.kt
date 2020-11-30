package com.example.restaurant.data.repositories

import androidx.lifecycle.LiveData
import com.example.restaurant.data.daos.ProfileImageDao
import com.example.restaurant.data.daos.RestaurantImageDao
import com.example.restaurant.data.imageEntity.ProfileImage
import com.example.restaurant.data.imageEntity.RestaurantImage

class RestaurantImageRepository(private val restaurantImageDao: RestaurantImageDao) {

    val readAllRestaurantImage: LiveData<List<RestaurantImage>> = restaurantImageDao.readAllRestaurantImages()

    suspend fun addRestaurantImage(restaurantImage: RestaurantImage){
        restaurantImageDao.addRestaurantImage(restaurantImage)
    }

    suspend fun deleteRestaurantImage(restaurantImage: RestaurantImage){
        restaurantImageDao.deleteRestaurantImage(restaurantImage)
    }

    fun readRestaurantImageById(restaurantId: Int): LiveData<List<RestaurantImage>> {
        return restaurantImageDao.readRestaurantImageById(restaurantId)
    }


}