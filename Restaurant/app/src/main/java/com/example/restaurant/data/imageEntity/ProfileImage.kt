package com.example.restaurant.data.imageEntity

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey

@Entity(tableName="profile_image_table")
data class ProfileImage(
    @PrimaryKey()
    val id: Int,
    val userId: Int,
    val photo: Bitmap
)