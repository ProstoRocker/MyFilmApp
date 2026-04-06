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

//Все методы записи — suspend → работают в корутинах
@Dao
interface MovieDao {

    // Получить все фильмы (реактивно)
    @Query("SELECT * FROM movies ORDER BY voteAverage DESC")
    fun getAllMovies(): Flow<List<MovieDto>>

    // Получить избранные
    @Query("SELECT * FROM movies WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<MovieDto>>

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

    // Получить все как PagingSource (для кэша)
    @Query("SELECT * FROM movies ORDER BY voteAverage DESC")
    fun getAllMoviesAsPagingSource(): PagingSource<Int, MovieDto>

    // Очистить всё
    @Query("DELETE FROM movies")
    suspend fun deleteAll()

    // Получить фильмы с минимальным рейтингом
    @Query("SELECT * FROM movies WHERE voteAverage >= :minRating")
    fun getMoviesWithMinRating(minRating: Double): Flow<List<MovieDto>>
}