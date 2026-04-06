package com.ilyadev.moviesearch.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json


// @Json(name = "...") — автоматически маппит поля из JSON
// Больше не нужен Converter.fromMap() или fromJson()

/**
 * DTO-объект фильма.
 */

@Entity(tableName = "movies")
data class MovieDto(
    @PrimaryKey val id: Int,
    val title: String,
    @Json(name = "release_date") val releaseDate: String,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "genre_ids") val genreIds: List<Int>,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    var isFavorite: Boolean = false
)