package com.ilyadev.moviesearch.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Конвертеры для Room.
 *
 * Позволяют хранить сложные типы (например, List<Int>) в виде строки JSON.
 *
 * Например:
 *   genreIds: List<Int> → сохраняется как "[28, 12, 18]" → строка
 *
 * Без конвертеров Room не может работать с коллекциями.
 */
class Converters {
    private val gson = Gson()

    /**
     * Преобразует List<Int> → String (JSON).
     * Используется при записи в БД.
     */
    @TypeConverter
    fun fromGenreIds(genreIds: List<Int>): String {
        return gson.toJson(genreIds)
    }

    /**
     * Преобразует String (JSON) → List<Int>.
     * Используется при чтении из БД.
     *
     * Если ошибка — возвращает пустой список.
     */
    @TypeConverter
    fun toGenreIds(genreIdsString: String): List<Int> {
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(genreIdsString, type) ?: emptyList()
    }
}