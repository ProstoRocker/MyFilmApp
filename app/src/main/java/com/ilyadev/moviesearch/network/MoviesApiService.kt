package com.ilyadev.moviesearch.network

// API интерфейс

import com.google.gson.annotations.SerializedName
import com.ilyadev.moviesearch.API_KEY
import com.ilyadev.moviesearch.model.MovieDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = API_KEY.KEY,
        @Query("page") page: Int = 1
    ): MovieResponse
}

// Обёртка для списка фильмов
data class MovieResponse(
    val page: Int,
    val results: List<MovieDto>,
    @SerializedName("total_pages") val totalPages: Int
)