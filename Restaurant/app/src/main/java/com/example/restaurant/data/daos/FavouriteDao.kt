package com.example.restaurant.data.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.restaurant.data.favouriteEntity.Favourite

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavourite(favourite: Favourite)

    @Query("SELECT * FROM favourite_table WHERE userId=:userId AND restaurantId=:restaurantId")
    fun readFavouriteById(userId: Int, restaurantId: Int): LiveData<List<Favourite>>

    @Query("SELECT * FROM favourite_table WHERE userId=:userId")
    fun readFavouriteByUserId(userId: Int): LiveData<List<Favourite>>

    @Delete
    suspend fun deleteFavourite(favourite: Favourite)
}