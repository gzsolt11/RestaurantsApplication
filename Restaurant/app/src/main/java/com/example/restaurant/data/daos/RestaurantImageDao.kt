package com.example.restaurant.data.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.restaurant.data.imageEntity.ProfileImage
import com.example.restaurant.data.imageEntity.RestaurantImage

@Dao
interface RestaurantImageDao {

    /**
     * Insert new element into the restaurant image table
     * @param restaurantImage - this element will be added into the table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRestaurantImage(restaurantImage: RestaurantImage)

    /**
     * Gets all the restaurant images in ascension order of ID's
     */
    @Query("SELECT * FROM restaurant_image_table ORDER BY id ASC")
    fun readAllRestaurantImages(): LiveData<List<RestaurantImage>>

    /**
     * Gets a list of restaurant images to a specific restaurant
     * @param restaurantId - finds images for restaurant which has this ID
     */
    @Query("SELECT * FROM restaurant_image_table WHERE restaurantId=:restaurantId")
    fun readRestaurantImageById(restaurantId: Int): LiveData<List<RestaurantImage>>

    /**
     * Deletes an element from the table
     * @param profileImage - this element will be removed from the table
     */
    @Delete()
    suspend fun deleteRestaurantImage(profileImage: RestaurantImage)

}