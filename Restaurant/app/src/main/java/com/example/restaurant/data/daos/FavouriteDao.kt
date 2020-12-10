package com.example.restaurant.data.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.restaurant.data.favouriteEntity.Favourite

@Dao
interface FavouriteDao {

    /**
     * Add a favourite element to the favourite table
     * @param favourite - this element will be added to the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavourite(favourite: Favourite)

    /**
     * Get a favourite element from the favourite table by user and restaurant id
     * @param userId - search parameter for the id of the user
     * @param restaurantId - search parameter for the id of the restaurant
     */
    @Query("SELECT * FROM favourite_table WHERE userId=:userId AND restaurantId=:restaurantId")
    fun readFavouriteById(userId: Int, restaurantId: Int): LiveData<List<Favourite>>

    /**
     * Get a favourite element from the favourite table by user id
     * @param userId - search parameter for the id of the user
     */
    @Query("SELECT * FROM favourite_table WHERE userId=:userId")
    fun readFavouriteByUserId(userId: Int): LiveData<List<Favourite>>

    /**
     * Deletes favourite from favourite table
     * @param favourite - this element will be deleted from the favourite table
     */
    @Delete
    suspend fun deleteFavourite(favourite: Favourite)
}