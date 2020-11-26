package com.example.restaurant.data.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.restaurant.data.imageEntity.ProfileImage

@Dao
interface ProfileImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProfileImage(profileImage: ProfileImage)

    @Query("SELECT * FROM profile_image_table ORDER BY id ASC")
    fun readAllProfileImages(): LiveData<List<ProfileImage>>

    @Query("SELECT * FROM profile_image_table WHERE userId=:userId")
    fun readProfileImageById(userId: Int): LiveData<List<ProfileImage>>

    @Delete()
    suspend fun deleteProfileImage(profileImage: ProfileImage)

}