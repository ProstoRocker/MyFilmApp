package com.ilyadev.moviesearch.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ilyadev.moviesearch.model.MovieDto
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    // ==============================
    // 🔹 Запросы через Flow (реактивные)
    // ==============================

    /**
     * Получить все фильмы (для отображения в списке)
     */
    @Query("SELECT * FROM movies ORDER BY voteAverage DESC")
    fun getAllMovies(): Flow<List<MovieDto>>

    /**
     * Получить избранные фильмы
     */
    @Query("SELECT * FROM movies WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<MovieDto>>

    /**
     * Фильмы с рейтингом выше N
     */
    @Query("SELECT * FROM movies WHERE voteAverage > :minRating")
    fun getMoviesWithMinRating(minRating: Double): Flow<List<MovieDto>>

    /**
     * Поиск по названию
     */
    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%'")
    fun searchMovies(query: String): Flow<List<MovieDto>>

    // ==============================
    // 🔹 Запросы для Paging 3 (из кэша)
    // ==============================

    /**
     * Получить все фильмы как PagingSource — для использования в Pager
     * Используется при загрузке из локального кэша (когда сеть недоступна или кэш актуален)
     */
    @Query("SELECT * FROM movies ORDER BY voteAverage DESC")
    fun getAllMoviesAsPagingSource(): PagingSource<Int, MovieDto>

    // ==============================
    // 🔹 CRUD-операции
    // ==============================

    /**
     * Вставить или обновить фильм
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieDto)

    /**
     * Обновить фильм (например, пометить как любимый)
     */
    @Update
    suspend fun update(movie: MovieDto)

    /**
     * Удалить фильм
     */
    @Delete
    suspend fun delete(movie: MovieDto)

    /**
     * Вставить список фильмов
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieDto>)

    /**
     * Очистить всю таблицу (для обновления кэша при истечении 10 минут)
     */
    @Query("DELETE FROM movies")
    suspend fun deleteAll()
}