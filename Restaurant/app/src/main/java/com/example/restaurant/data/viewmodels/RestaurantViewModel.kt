package com.example.restaurant.data.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.data.Resource
import com.example.restaurant.data.repositories.RestaurantAppRepository
import com.example.restaurant.data.restaurantEntityAndResponse.Restaurants
import kotlinx.coroutines.launch
import retrofit2.Response

class RestaurantViewModel(val repository: RestaurantAppRepository): ViewModel(){

    val restaurants: MutableLiveData<Resource<Restaurants>> = MutableLiveData()
    var restaurantsPage = 1
    var restaurantResponse: Restaurants? = null

    val searchRestaurants: MutableLiveData<Resource<Restaurants>> = MutableLiveData()
    var searchRestaurantsPage = 1
    var searchRestaurantResponse: Restaurants? = null

    init{
        getRestaurants("US")
    }

    fun getRestaurants(countryCode: String) = viewModelScope.launch{
        restaurants.postValue(Resource.Loading())
        val response = repository.getRestaurants(countryCode,restaurantsPage)
        restaurants.postValue(handleRestaurantResponse(response))
    }

    fun searchRestaurantsByCountry(searchQuery: String) = viewModelScope.launch{
        searchRestaurants.postValue(Resource.Loading())
        val response = repository.searchRestaurantsByCountry(searchQuery,searchRestaurantsPage)
        searchRestaurants.postValue(handleSearchRestaurantResponse(response))
    }

    fun searchRestaurantsByName(searchQuery: String) = viewModelScope.launch{
        searchRestaurants.postValue(Resource.Loading())
        val response = repository.searchRestaurantsByName(searchQuery,searchRestaurantsPage)
        searchRestaurants.postValue(handleSearchRestaurantResponse(response))
    }

    fun searchRestaurantsByAddress(searchQuery: String) = viewModelScope.launch{
        searchRestaurants.postValue(Resource.Loading())
        val response = repository.searchRestaurantsByAddress(searchQuery,searchRestaurantsPage)
        searchRestaurants.postValue(handleSearchRestaurantResponse(response))
    }

    fun searchRestaurantsByState(searchQuery: String) = viewModelScope.launch{
        searchRestaurants.postValue(Resource.Loading())
        val response = repository.searchRestaurantsByState(searchQuery,searchRestaurantsPage)
        searchRestaurants.postValue(handleSearchRestaurantResponse(response))
    }

    fun searchRestaurantsByCity(searchQuery: String) = viewModelScope.launch{
        searchRestaurants.postValue(Resource.Loading())
        val response = repository.searchRestaurantsByCity(searchQuery,searchRestaurantsPage)
        searchRestaurants.postValue(handleSearchRestaurantResponse(response))
    }


    fun searchRestaurantsByPrice(countryQuery:String, searchQuery: String) = viewModelScope.launch{
        searchRestaurants.postValue(Resource.Loading())
        val response = repository.searchRestaurantsByPrice(countryQuery, searchQuery,searchRestaurantsPage)
        searchRestaurants.postValue(handleSearchRestaurantResponse(response))
    }

    /**
     * Handles the response when no search parameter is added for the restaurants
     */
    private fun handleRestaurantResponse(response: Response<Restaurants>): Resource<Restaurants>{
        if(response.isSuccessful){
            response.body()?.let{resultResponse ->
                restaurantsPage++
                if(restaurantResponse == null){
                    restaurantResponse = resultResponse
                } else{
                    val oldRestaurants = restaurantResponse?.restaurants
                    val newRestaurants = resultResponse.restaurants
                    oldRestaurants?.addAll(newRestaurants)
                }
                return Resource.Success(restaurantResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    /**
     * handles response when search query is used for restaurants
     */
    private fun handleSearchRestaurantResponse(response: Response<Restaurants>): Resource<Restaurants>{
        if(response.isSuccessful){
            response.body()?.let{resultResponse ->
                searchRestaurantsPage++
                if(searchRestaurantResponse == null){
                    searchRestaurantResponse = resultResponse
                } else{
                    val oldRestaurants = searchRestaurantResponse?.restaurants
                    val newRestaurants = resultResponse.restaurants
                    oldRestaurants?.addAll(newRestaurants)
                }
                return Resource.Success(searchRestaurantResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


}