package com.example.restaurant.data.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.restaurant.data.MyDatabase
import com.example.restaurant.data.imageEntity.ProfileImage
import com.example.restaurant.data.imageEntity.RestaurantImage
import com.example.restaurant.data.repositories.ProfileImageRepository
import com.example.restaurant.data.repositories.RestaurantImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RestaurantImageViewModel(application: Application): AndroidViewModel(application) {

    val readAllRestaurantImages: LiveData<List<RestaurantImage>>
    private val repository: RestaurantImageRepository

    init{
        val profileImageDao = MyDatabase.invoke(application.applicationContext).restaurantImageDao()
        repository = RestaurantImageRepository(profileImageDao)
        readAllRestaurantImages = repository.readAllRestaurantImage
    }

    fun addRestaurantImage(restaurantImage: RestaurantImage){
        viewModelScope.launch(Dispatchers.IO){
            repository.addRestaurantImage(restaurantImage)
        }
    }

    fun readRestaurantImageById(profileId: Int): LiveData<List<RestaurantImage>> {
        return repository.readRestaurantImageById(profileId)
    }

}