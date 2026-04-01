package com.ilyadev.moviesearch.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromGenreIds(genreIds: List<Int>): String {
        return gson.toJson(genreIds)
    }

    @TypeConverter
    fun toGenreIds(genreIdsString: String): List<Int> {
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(genreIdsString, type) ?: emptyList()
    }
}