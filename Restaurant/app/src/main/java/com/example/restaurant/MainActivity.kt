package com.example.restaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.restaurant.data.MyDatabase
import com.example.restaurant.data.repositories.RestaurantAppRepository
import com.example.restaurant.data.viewmodels.RestaurantViewModel
import com.example.restaurant.data.viewmodels.RestaurantViewModelFactory
import com.example.restaurant.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    lateinit var viewModel: RestaurantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)

        val repository = RestaurantAppRepository(MyDatabase(this))
        val restaurantViewModelProviderFactory =  RestaurantViewModelFactory(repository)
        viewModel = ViewModelProvider(this, restaurantViewModelProviderFactory).get(RestaurantViewModel::class.java)



        val navController = findNavController(R.id.navigationHostFragment)
        val bottomNavigation = binding.bottomNavMenu
        bottomNavigation?.setupWithNavController(navController)

    }
}