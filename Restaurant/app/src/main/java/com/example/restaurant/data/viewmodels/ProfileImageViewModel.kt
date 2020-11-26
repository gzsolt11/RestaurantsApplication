package com.example.restaurant.data.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.restaurant.data.MyDatabase
import com.example.restaurant.data.imageEntity.ProfileImage
import com.example.restaurant.data.repositories.ProfileImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileImageViewModel(application: Application): AndroidViewModel(application) {

    val readAllProfileImages: LiveData<List<ProfileImage>>
    private val repository: ProfileImageRepository

    init{
        val profileImageDao = MyDatabase.invoke(application.applicationContext).profileImageDao()
        repository = ProfileImageRepository(profileImageDao)
        readAllProfileImages = repository.readAllProfileImage
    }

    fun addProfileImage(profileImage: ProfileImage){
        viewModelScope.launch(Dispatchers.IO){
            repository.addProfileImage(profileImage)
        }
    }

    fun readProfileImageById(profileId: Int): LiveData<List<ProfileImage>> {
        return repository.readProfileImageById(profileId)
    }

}