package com.example.restaurant

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.restaurant.data.MyDatabase
import com.example.restaurant.data.repositories.RestaurantAppRepository
import com.example.restaurant.data.viewmodels.RestaurantViewModel
import com.example.restaurant.data.viewmodels.RestaurantViewModelFactory
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.statusBarColor = ContextCompat.getColor(this, R.color.backgroundColor)

        Handler().postDelayed({
            val intent = Intent(this@SplashScreen,MainActivity::class.java)
            startActivity(intent)
        },3000)
    }
}