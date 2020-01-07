package com.dicoding.made.data.local.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converter {

    private val gson = Gson()

    @TypeConverter
    @JvmStatic
    fun fromGenresList(genres: List<Genre>?): String? {
        return gson.toJson(genres)
    }

    @TypeConverter
    @JvmStatic
    fun toGenresList(genres: String?): List<Genre>? {
        val listType = object : TypeToken<List<Genre?>?>() {}.type
        return gson.fromJson(genres, listType)
    }

    @TypeConverter
    @JvmStatic
    fun fromStringList(list: List<String>?): String? {
        return gson.toJson(list)
    }

    @TypeConverter
    @JvmStatic
    fun toStringList(list: String?): List<String>? {
        val listType = object : TypeToken<List<String?>?>() {}.type
        return gson.fromJson(list, listType)
    }
}