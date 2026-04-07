package com.ilyadev.moviesearch.db

import androidx.room.*
import com.ilyadev.moviesearch.model.MovieDto
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface MovieDao {

    // Получить все фильмы как поток (реактивно)
    @Query("SELECT * FROM movies ORDER BY voteAverage DESC")
    fun getAllMovies(): Flowable<List<MovieDto>>

    // Получить избранные
    @Query("SELECT * FROM movies WHERE isFavorite = 1")
    fun getFavorites(): Flowable<List<MovieDto>>

    // Поиск по названию
    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%'")
    fun searchMovies(query: String): Flowable<List<MovieDto>>

    //ключевой метод — он позволяет читать из БД синхронно в suspend fun.
    @Query("SELECT * FROM movies ORDER BY voteAverage DESC")
    suspend fun getAllMoviesSync(): List<MovieDto>

    // Вставить фильм
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: MovieDto): Completable

    // Обновить
    @Update
    fun update(movie: MovieDto): Completable

    // Удалить
    @Delete
    fun delete(movie: MovieDto): Completable

    // Вставить список
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<MovieDto>): Completable

    // Очистить всю таблицу
    @Query("DELETE FROM movies")
    fun deleteAll(): Completable

}