package com.example.restaurant.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restaurant.data.repositories.RestaurantAppRepository

class RestaurantViewModelFactory(val restaurantAppRepository: RestaurantAppRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RestaurantViewModel(restaurantAppRepository) as T
    }


}