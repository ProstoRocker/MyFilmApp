package com.ilyadev.moviesearch.model

import com.google.gson.annotations.SerializedName

/**
 * Ответ от TMDb API для списка фильмов.
 */
data class MovieResponse(
    @SerializedName("results") val results: List<MovieDto>,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("page") val page: Int
)