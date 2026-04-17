package com.ilyadev.moviesearch.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ilyadev.moviesearch.model.MovieDto

/**
 * Главная база данных приложения.
 *
 * Отвечает за:
 * - Хранение фильмов (кэш из API)
 * - Управление сессиями через DAO
 * - Реализацию Singleton-паттерна для безопасного доступа
 *
 * Аннотации:
 * - @TypeConverters(Converters::class) — позволяет сохранять сложные типы (например, List<Int>)
 * - @Database(entities = [MovieDto::class], version = 2) — список таблиц и версия схемы
 */
@TypeConverters(Converters::class)
@Database(entities = [MovieDto::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Получить DAO для работы с фильмами
    abstract fun movieDao(): MovieDao

    companion object {
        private const val DATABASE_NAME = "movie_database"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Получить единственный экземпляр БД.
         * Гарантирует потокобезопасность и один экземпляр на всё приложение.
         *
         * Используется в Dagger/Hilt.
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration() // Пересоздаёт БД при изменении схемы (для разработки)
                    .build().also { INSTANCE = it }
            }
        }
    }
}