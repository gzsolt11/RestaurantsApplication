package com.example.restaurant.data.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.restaurant.data.responses.Restaurant

@Dao
interface RestaurantDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(restaurant: Restaurant):Long

    @Query("SELECT * FROM restaurant_table")
    fun getAllRestaurants() : LiveData<List<Restaurant>>

    @Delete
    suspend fun delete(restaurant: Restaurant)
}