package com.ilyadev.moviesearch.db

import androidx.room.*
import com.ilyadev.moviesearch.model.MovieDto
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

/**
 * Data Access Object (DAO) для работы с фильмами.
 *
 * Определяет операции с таблицей `movies`:
 * - Чтение: Flowable (реактивное), suspend (корутины)
 * - Запись: insert, update, delete, insertAll
 * - Поиск: по названию, избранные
 *
 * Используется:
 * - RxJava: Flowable + Completable → для реактивной подписки в ViewModel
 * - Suspend функции → для корутин
 */
@Dao
interface MovieDao {

    // === 🔍 ЧТЕНИЕ ДАННЫХ ===

    /**
     * Получить все фильмы как поток.
     * Обновления отправляются автоматически при изменении БД.
     */
    @Query("SELECT * FROM movies ORDER BY voteAverage DESC")
    fun getAllMovies(): Flowable<List<MovieDto>>

    /**
     * Получить только избранные фильмы.
     */
    @Query("SELECT * FROM movies WHERE isFavorite = 1")
    fun getFavorites(): Flowable<List<MovieDto>>

    /**
     * Поиск по подстроке в названии.
     */
    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%'")
    fun searchMovies(query: String): Flowable<List<MovieDto>>

    /**
     * Синхронное получение всех фильмов (для suspend функций).
     * Используется при первоначальной загрузке.
     */
    @Query("SELECT * FROM movies ORDER BY voteAverage DESC")
    suspend fun getAllMoviesSync(): List<MovieDto>

    // === ✏️ ЗАПИСЬ / ОБНОВЛЕНИЕ / УДАЛЕНИЕ ===

    /**
     * Вставить фильм.
     * Если уже есть — заменяет (REPLACE).
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: MovieDto): Completable

    /**
     * Обновить существующий фильм.
     */
    @Update
    fun update(movie: MovieDto): Completable

    /**
     * Удалить фильм.
     */
    @Delete
    fun delete(movie: MovieDto): Completable

    /**
     * Вставить список фильмов.
     * Используется при обновлении кэша.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<MovieDto>): Completable

    /**
     * Очистить всю таблицу.
     * Используется перед полной перезагрузкой из сети.
     */
    @Query("DELETE FROM movies")
    fun deleteAll(): Completable
}