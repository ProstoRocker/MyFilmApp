package com.ilyadev.moviesearch.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


// @Json(name = "...") — автоматически маппит поля из JSON
// Больше не нужен Converter.fromMap() или fromJson()

/**
 * DTO-объект фильма.
 */

@Entity(tableName = "movies")
data class MovieDto(
    @PrimaryKey val id: Int,
    val title: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("genre_ids") val genreIds: List<Int>,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    var isFavorite: Boolean = false
)