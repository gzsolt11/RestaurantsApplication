package com.example.restaurant.data.repositories

import androidx.lifecycle.LiveData
import com.example.restaurant.data.daos.FavouriteDao
import com.example.restaurant.data.favouriteEntity.Favourite

class FavouriteRepository(private val favouriteDao: FavouriteDao) {

    fun readFavouriteById(userId: Int, restaurantId: Int): LiveData<List<Favourite>> {
        return favouriteDao.readFavouriteById(userId,restaurantId)
    }

    fun readFavouriteByUserId(userId: Int): LiveData<List<Favourite>> {
        return favouriteDao.readFavouriteByUserId(userId)
    }

    suspend fun deleteFavourite(favourite: Favourite){
        favouriteDao.deleteFavourite(favourite)
    }

    suspend fun addFavourite(favourite: Favourite){
        favouriteDao.addFavourite(favourite)
    }
}