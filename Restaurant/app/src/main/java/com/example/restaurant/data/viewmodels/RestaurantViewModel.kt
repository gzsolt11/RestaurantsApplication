package com.example.restaurant.data.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.R
import com.example.restaurant.data.Resource
import com.example.restaurant.data.repositories.RestaurantAppRepository
import com.example.restaurant.data.restaurantEntityAndResponse.Restaurant
import com.example.restaurant.data.restaurantEntityAndResponse.Restaurants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStreamReader

class RestaurantViewModel(val repository: RestaurantAppRepository): ViewModel(){

    val TAG = "RestaurantViewModel"

    val restaurants: MutableLiveData<Resource<Restaurants>> = MutableLiveData()
    var restaurantsPage = 1
    var restaurantResponse: Restaurants? = null

    val searchRestaurants: MutableLiveData<Resource<Restaurants>> = MutableLiveData()
    var searchRestaurantsPage = 1
    var searchRestaurantResponse: Restaurants? = null

    var databaseRestaurants: LiveData<List<Restaurant>> = repository.allRestaurant

    init{
        databaseRestaurants = repository.db.restaurantDao().getAllRestaurants()
        getRestaurants("US")
    }

    fun addRestaurant(restaurant: Restaurant){
        viewModelScope.launch(Dispatchers.IO){
            repository.addRestaurant(restaurant)
        }
    }

    fun getRestaurants(countryCode: String) = viewModelScope.launch{
        restaurants.postValue(Resource.Loading())
        val response = repository.getRestaurants(countryCode, restaurantsPage)
        restaurants.postValue(handleRestaurantResponse(response))
    }

    fun searchRestaurantsByCountry(searchQuery: String) = viewModelScope.launch{
        searchRestaurants.postValue(Resource.Loading())
        val response = repository.searchRestaurantsByCountry(searchQuery, searchRestaurantsPage)
        searchRestaurants.postValue(handleSearchRestaurantResponse(response))
    }

    fun searchRestaurantsByName(searchQuery: String) = viewModelScope.launch{
        searchRestaurants.postValue(Resource.Loading())
        val response = repository.searchRestaurantsByName(searchQuery, searchRestaurantsPage)
        searchRestaurants.postValue(handleSearchRestaurantResponse(response))
    }

    fun searchRestaurantsByAddress(searchQuery: String) = viewModelScope.launch{
        searchRestaurants.postValue(Resource.Loading())
        val response = repository.searchRestaurantsByAddress(searchQuery, searchRestaurantsPage)
        searchRestaurants.postValue(handleSearchRestaurantResponse(response))
    }

    fun searchRestaurantsByState(searchQuery: String) = viewModelScope.launch{
        searchRestaurants.postValue(Resource.Loading())
        val response = repository.searchRestaurantsByState(searchQuery, searchRestaurantsPage)
        searchRestaurants.postValue(handleSearchRestaurantResponse(response))
    }

    fun searchRestaurantsByCity(searchQuery: String) = viewModelScope.launch{
        searchRestaurants.postValue(Resource.Loading())
        val response = repository.searchRestaurantsByCity(searchQuery, searchRestaurantsPage)
        searchRestaurants.postValue(handleSearchRestaurantResponse(response))
    }


    fun searchRestaurantsByPrice(countryQuery: String, searchQuery: String) = viewModelScope.launch{
        searchRestaurants.postValue(Resource.Loading())
        val response = repository.searchRestaurantsByPrice(countryQuery, searchQuery, searchRestaurantsPage)
        searchRestaurants.postValue(handleSearchRestaurantResponse(response))
    }

    /**
     * Handles the response when no search parameter is added for the restaurants
     */
    private fun handleRestaurantResponse(response: Response<Restaurants>): Resource<Restaurants>{
        if(response.isSuccessful){
            response.body()?.let{ resultResponse ->
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
            response.body()?.let{ resultResponse ->
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

    /**
     * Load in the local restaurants json file and returns a jsonarray of that
     */
    private fun loadJSONArray(context: Context?): JSONArray?{
        val builder = StringBuilder()
        val inputStream = context?.resources?.openRawResource(R.raw.restaurants)
        val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))

        do{
            var jsonString = reader.readLine();
            if(jsonString == null){
                break
            }
            builder.append(jsonString)
        }while(true)

        if(inputStream != null){
            inputStream.close()
        }

        return JSONArray(String(builder))

    }

    /**
     * Read in the local restaurants json file and add the elements to the local database
     */
    fun addJsonToDatabase(context: Context?): Unit{
        val restaurants = loadJSONArray(context)
        Log.v("LISTA", restaurants?.getJSONObject(0).toString() + " " + restaurants?.length().toString())

        if(restaurants != null){
            for (i in 0..(restaurants.length() -1) ){
                val restaurant = restaurants.getJSONObject(i)
                val address = restaurant.getString("address")
                val area = restaurant.getString("area")
                val city = restaurant.getString("city")
                val country = restaurant.getString("country")
                val id = restaurant.getInt("id")
                val imageUrl = restaurant.getString("image_url")
                val lat = restaurant.getDouble("lat")
                val lng = restaurant.getDouble("lng")
                val mobileReserveUrl = restaurant.getString("mobile_reserve_url")
                val name = restaurant.getString("name")
                val phone = restaurant.getString("phone")
                val postalCode = restaurant.getString("postal_code")
                val price = restaurant.getInt("price")
                val reserveUrl = restaurant.getString("reserve_url")
                val state = restaurant.getString("state")


                addRestaurant(Restaurant(0, address, area, city, country, id, imageUrl, lat, lng, mobileReserveUrl, name, phone, postalCode, price, reserveUrl, state))
            }
        } else{
            Log.e(TAG, "Reading JSON failed")
        }
    }


}