package com.example.restaurant.data.converters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class Converters {

    /**
     * Converts a bitmap to ByteArray
     * @param bitmap - input bitmap that will be converted to bytearray
     */
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray{
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream)
        return outputStream.toByteArray()
    }

    /**
     * Converts a ByteArray to Bitmap
     * @param byteArray - input bytearray that will be converted to bitmap
     */
    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap{
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }

}