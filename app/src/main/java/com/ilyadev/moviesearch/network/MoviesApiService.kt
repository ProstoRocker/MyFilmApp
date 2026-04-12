package com.ilyadev.moviesearch.network

import com.ilyadev.moviesearch.model.MovieDto
import com.ilyadev.moviesearch.model.MovieResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Все методы теперь возвращают Single<T> → один результат или ошибка
 *  Подходит для запросов к API
 */

interface MoviesApiService {

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): Single<MovieResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): Single<MovieResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): Single<MovieResponse>

    /**
     * Поиск фильма по названию.
     *
     * @param query Запрос (например, "Inception")
     * @param apiKey Ключ API
     * @param page Номер страницы (пагинация)
     * @return Ответ с фильмами
     */

    @GET("search/movie")
    fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String
    ): Single<MovieDto>

}