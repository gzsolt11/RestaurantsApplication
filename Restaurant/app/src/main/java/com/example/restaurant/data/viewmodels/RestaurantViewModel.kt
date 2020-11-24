package com.example.restaurant.data.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.data.Resource
import com.example.restaurant.data.repositories.RestaurantAppRepository
import com.example.restaurant.data.responses.Restaurants
import kotlinx.coroutines.launch
import retrofit2.Response

class RestaurantViewModel(val repository: RestaurantAppRepository): ViewModel(){

    val restaurants: MutableLiveData<Resource<Restaurants>> = MutableLiveData()
    val restaurantsPage = 1

    init{
        getRestaurants("US")
    }

    fun getRestaurants(countryCode: String) = viewModelScope.launch{
        restaurants.postValue(Resource.Loading())
        val response = repository.getRestaurants(countryCode,restaurantsPage)
        restaurants.postValue(handleRestaurantResponse(response))
    }

    private fun handleRestaurantResponse(response: Response<Restaurants>): Resource<Restaurants>{
        if(response.isSuccessful){
            response.body()?.let{resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


}