package com.example.ECommerceApp.data.source.Database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class converters {


    @TypeConverter
    fun fromImagesList(imagesList: List<String>): String {
        return Gson().toJson(imagesList)
    }

    @TypeConverter
    fun toImagesList(imagesString: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(imagesString, type)
    }

}