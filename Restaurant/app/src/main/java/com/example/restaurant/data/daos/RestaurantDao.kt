package com.example.restaurant.data.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.restaurant.data.restaurantEntityAndResponse.Restaurant

@Dao
interface  RestaurantDao {

    /**
     * Add new element into the restaurant table
     * @param restaurant - input element, this will be added to the table
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(restaurant: Restaurant):Long

    /**
     * Gets all the restaurant element from the restaurant table
     */
    @Query("SELECT * FROM restaurant_table")
    fun getAllRestaurants() : LiveData<List<Restaurant>>

    /**
     * Delete element from the restaurant table
     * @param restaurant - this element will be deleted from the table
     */
    @Delete
    suspend fun delete(restaurant: Restaurant)
}