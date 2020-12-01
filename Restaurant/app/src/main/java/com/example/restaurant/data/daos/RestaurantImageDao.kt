package com.example.restaurant.data.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.restaurant.data.imageEntity.ProfileImage
import com.example.restaurant.data.imageEntity.RestaurantImage

@Dao
interface RestaurantImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRestaurantImage(restaurantImage: RestaurantImage)

    @Query("SELECT * FROM restaurant_image_table ORDER BY id ASC")
    fun readAllRestaurantImages(): LiveData<List<RestaurantImage>>

    @Query("SELECT * FROM restaurant_image_table WHERE restaurantId=:restaurantId")
    fun readRestaurantImageById(restaurantId: Int): LiveData<List<RestaurantImage>>

    @Delete()
    suspend fun deleteRestaurantImage(profileImage: RestaurantImage)

}