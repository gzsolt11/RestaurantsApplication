package com.example.restaurant.data.repositories

import androidx.lifecycle.LiveData
import com.example.restaurant.data.daos.ProfileImageDao
import com.example.restaurant.data.daos.RestaurantImageDao
import com.example.restaurant.data.imageEntity.ProfileImage
import com.example.restaurant.data.imageEntity.RestaurantImage

class RestaurantImageRepository(private val restaurantImageDao: RestaurantImageDao) {

    /**
     * Gets all restaurant images from restaurant image table
     */
    val readAllRestaurantImage: LiveData<List<RestaurantImage>> = restaurantImageDao.readAllRestaurantImages()

    /**
     * Add new element into the restaurant image table
     * @param restaurantImage - this element will be added into the table
     */
    suspend fun addRestaurantImage(restaurantImage: RestaurantImage){
        restaurantImageDao.addRestaurantImage(restaurantImage)
    }

    /**
     * Deletes restaurant image from restaurant image table
     * @param restaurantImage - this element will be removed from the table
     */
    suspend fun deleteRestaurantImage(restaurantImage: RestaurantImage){
        restaurantImageDao.deleteRestaurantImage(restaurantImage)
    }

    /**
     * Gets restaurant image from restaurant image table based on restaurant id
     * @param restaurantId - search parameter for restaurant ID
     */
    fun readRestaurantImageById(restaurantId: Int): LiveData<List<RestaurantImage>> {
        return restaurantImageDao.readRestaurantImageById(restaurantId)
    }


}