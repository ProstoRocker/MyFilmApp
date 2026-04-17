package com.ilyadev.moviesearch.network

import com.ilyadev.moviesearch.model.MovieDto
import com.ilyadev.moviesearch.model.MovieResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Интерфейс для взаимодействия с TMDb API.
 *
 * Все методы возвращают `Single<T>`:
 * - Одно успешное значение или ошибка
 * - Подходит для HTTP-запросов: один вызов → один ответ
 *
 * Используется в:
 * - ViewModel (например, DetailViewModel)
 * - PagingSource (для пагинации)
 * - SearchRepository
 */
interface MoviesApiService {

    /**
     * Получить популярные фильмы.
     *
     * @param apiKey Ключ API от themoviedb.org
     * @param page Номер страницы (по умолчанию = 1)
     * @return MovieResponse — обёртка с пагинацией
     */
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): Single<MovieResponse>

    /**
     * Получить фильмы с высоким рейтингом.
     */
    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): Single<MovieResponse>

    /**
     * Получить фильмы, которые сейчас в кино.
     */
    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): Single<MovieResponse>

    /**
     * Поиск фильмов по названию.
     *
     * @param query Строка запроса (например, "Inception")
     * @param apiKey Ключ API
     * @param page Номер страницы
     * @return Ответ с результатами
     */
    @GET("search/movie")
    fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): Single<MovieResponse>

    /**
     * Получить детали одного фильма.
     *
     * @param id ID фильма
     * @param apiKey Ключ API
     * @return Полная информация о фильме
     */
    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String
    ): Single<MovieDto>
}