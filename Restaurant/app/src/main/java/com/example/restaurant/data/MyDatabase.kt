package com.example.restaurant.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.restaurant.data.User.User
import com.example.restaurant.data.converters.Converters
import com.example.restaurant.data.daos.ProfileImageDao
import com.example.restaurant.data.daos.RestaurantDao
import com.example.restaurant.data.daos.UserDao
import com.example.restaurant.data.imageEntity.ProfileImage
import com.example.restaurant.data.restaurantEntityAndResponse.Restaurant

@Database(entities=[User::class, Restaurant::class, ProfileImage::class],version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MyDatabase: RoomDatabase() {

    abstract fun getUserDao(): UserDao

    abstract  fun restaurantDao(): RestaurantDao

    abstract  fun profileImageDao(): ProfileImageDao

    companion object{
        @Volatile
        private var instance: MyDatabase? = null
        private val LOCK = Any()

        // at constructor call this will be called
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also{ instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MyDatabase::class.java,
                "my_database"
            ).build()
    }

}