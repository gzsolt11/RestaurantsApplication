package com.example.restaurant.data.repositories

import androidx.lifecycle.LiveData
import com.example.restaurant.data.daos.ProfileImageDao
import com.example.restaurant.data.imageEntity.ProfileImage

class ProfileImageRepository(private val profileImageDao: ProfileImageDao) {

    val readAllProfileImage: LiveData<List<ProfileImage>> = profileImageDao.readAllProfileImages()

    suspend fun addProfileImage(profileImage: ProfileImage){
        profileImageDao.addProfileImage(profileImage)
    }

    suspend fun deleteProfileImage(profileImage: ProfileImage){
        profileImageDao.deleteProfileImage(profileImage)
    }

    fun readProfileImageById(imageId: Int): LiveData<List<ProfileImage>> {
        return profileImageDao.readProfileImageById(imageId)
    }


}