package com.example.restaurant.data.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.restaurant.data.imageEntity.ProfileImage

@Dao
interface ProfileImageDao {

    /**
     * Adding profile image to profileimage table
     * @param profileImage - this element will be added to the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProfileImage(profileImage: ProfileImage)

    /**
     * reading profile images from profileimage table
     */
    @Query("SELECT * FROM profile_image_table ORDER BY id ASC")
    fun readAllProfileImages(): LiveData<List<ProfileImage>>

    /**
     * Read a list of profile images based on userId
     * @param userId - search parameter for the user ID
     */
    @Query("SELECT * FROM profile_image_table WHERE userId=:userId")
    fun readProfileImageById(userId: Int): LiveData<List<ProfileImage>>

    /**
     * Delete element from the table
     * @param profileImage - this element will be deleted from the table
     */
    @Delete()
    suspend fun deleteProfileImage(profileImage: ProfileImage)

}