package com.example.restaurant.data.repositories

import androidx.lifecycle.LiveData
import com.example.restaurant.data.daos.FavouriteDao
import com.example.restaurant.data.favouriteEntity.Favourite

class FavouriteRepository(private val favouriteDao: FavouriteDao) {

    /**
     * Gets a list of favourites from the favourite table based on search parameters
     * @param userId - search parameter for user ID
     * @param restaurantId - search parameter for restaurant ID
     */
    fun readFavouriteById(userId: Int, restaurantId: Int): LiveData<List<Favourite>> {
        return favouriteDao.readFavouriteById(userId,restaurantId)
    }

    /**
     * Gets all favourites from favourite table based on search parameter
     * @param userId - search parameter for user ID
     */
    fun readFavouriteByUserId(userId: Int): LiveData<List<Favourite>> {
        return favouriteDao.readFavouriteByUserId(userId)
    }

    /**
     * Deletes an element from the favourite table
     * @param favourite - input parameter , this element will be deleted from the favourite table
     */
    suspend fun deleteFavourite(favourite: Favourite){
        favouriteDao.deleteFavourite(favourite)
    }

    /**
     * Add new element to the favourite table
     * @param favourite - this element will be added into the favourite table
     */
    suspend fun addFavourite(favourite: Favourite){
        favouriteDao.addFavourite(favourite)
    }
}