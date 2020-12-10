package com.example.restaurant

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.restaurant.data.MyDatabase
import com.example.restaurant.data.repositories.RestaurantAppRepository
import com.example.restaurant.data.viewmodels.RestaurantViewModel
import com.example.restaurant.data.viewmodels.RestaurantViewModelFactory
import com.example.restaurant.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    lateinit var sp: SharedPreferences
    lateinit var bottomNavigation: BottomNavigationView

    lateinit var viewModel: RestaurantViewModel

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val repository = RestaurantAppRepository(MyDatabase(this))
        val restaurantViewModelProviderFactory =  RestaurantViewModelFactory(repository)
        viewModel = ViewModelProvider(this, restaurantViewModelProviderFactory).get(RestaurantViewModel::class.java)


        val navController = findNavController(R.id.navigationHostFragment)
        bottomNavigation = binding.bottomNavMenu
        bottomNavigation?.setupWithNavController(navController)

        window.statusBarColor = ContextCompat.getColor(this,R.color.backgroundColor3)


    }
}

