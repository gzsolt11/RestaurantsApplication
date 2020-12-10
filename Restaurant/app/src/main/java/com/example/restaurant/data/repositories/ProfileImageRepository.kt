package com.example.restaurant.data.repositories

import androidx.lifecycle.LiveData
import com.example.restaurant.data.daos.ProfileImageDao
import com.example.restaurant.data.imageEntity.ProfileImage

class ProfileImageRepository(private val profileImageDao: ProfileImageDao) {

    /**
     * Gets all profile images from the profile image table
     */
    val readAllProfileImage: LiveData<List<ProfileImage>> = profileImageDao.readAllProfileImages()

    /**
     * Add new element into the profile image table
     * @param profileImage - this element will be added into the profile image table
     */
    suspend fun addProfileImage(profileImage: ProfileImage){
        profileImageDao.addProfileImage(profileImage)
    }

    /**
     * Deletes element from the profile image table
     * @param profileImage - this elem will be deleted from the table
     */
    suspend fun deleteProfileImage(profileImage: ProfileImage){
        profileImageDao.deleteProfileImage(profileImage)
    }

    /**
     * Gets all elements from profile images table based on search parameter
     * @param imageId - search parameter for image ID
     */
    fun readProfileImageById(imageId: Int): LiveData<List<ProfileImage>> {
        return profileImageDao.readProfileImageById(imageId)
    }


}