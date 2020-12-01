package com.example.restaurant.data.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.restaurant.data.MyDatabase
import com.example.restaurant.data.favouriteEntity.Favourite
import com.example.restaurant.data.repositories.FavouriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteViewModel(application: Application): AndroidViewModel(application) {

        private val repository: FavouriteRepository

        init{
            val favouriteDao = MyDatabase.invoke(application.applicationContext).favouriteDao()
            repository = FavouriteRepository(favouriteDao)
        }

        fun addFavourite(favourite: Favourite){
            viewModelScope.launch(Dispatchers.IO){
                repository.addFavourite(favourite)
            }
        }

        fun readFavouriteById(userId: Int,restaurantId: Int): LiveData<List<Favourite>> {
            return repository.readFavouriteById(userId, restaurantId)
        }

        fun readFavouriteByUserId(userId: Int): LiveData<List<Favourite>> {
            return repository.readFavouriteByUserId(userId)
        }

        fun deleteFavourite(favourite: Favourite){
            viewModelScope.launch(Dispatchers.IO){
                repository.deleteFavourite(favourite)
            }
        }

}