package com.ilyadev.moviesearch.db

import androidx.room.*
import com.ilyadev.moviesearch.model.MovieDto
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    // Получить все фильмы
    @Query("SELECT * FROM movies ORDER BY voteAverage DESC")
    fun getAllMovies(): Flow<List<MovieDto>>

    // Получить избранные
    @Query("SELECT * FROM movies WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<MovieDto>>

    // Получить фильмы с рейтингом выше N
    @Query("SELECT * FROM movies WHERE voteAverage > :minRating")
    fun getMoviesWithMinRating(minRating: Double): Flow<List<MovieDto>>

    // Поиск по названию
    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%'")
    fun searchMovies(query: String): Flow<List<MovieDto>>

    // Вставить или обновить
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieDto)

    // Обновить (например, пометить как любимый)
    @Update
    suspend fun update(movie: MovieDto)

    // Удалить
    @Delete
    suspend fun delete(movie: MovieDto)

    // Вставить список
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieDto>)
}