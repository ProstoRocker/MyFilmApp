package com.ilyadev.moviesearch.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) для фильмов.
 *
 * Основная модель данных приложения.
 *
 * Отвечает за:
 * - Маппинг JSON из TMDb API
 * - Хранение в локальной БД (Room)
 * - Передачу между слоями: Network → Repository → ViewModel → UI
 *
 * Аннотации:
 * - @Entity(tableName = "movies") — указывает, что это таблица Room
 * - @PrimaryKey — первичный ключ (id)
 * - @SerializedName — соответствие полей JSON
 */
@Entity(tableName = "movies")
data class MovieDto(
    @PrimaryKey val id: Int,

    @SerializedName("title") val title: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,

    // Локальное состояние
    val isFavorite: Boolean = false
)