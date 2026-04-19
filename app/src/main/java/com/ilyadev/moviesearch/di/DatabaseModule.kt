package com.ilyadev.moviesearch.di

import android.content.Context
import androidx.room.Room
import com.ilyadev.moviesearch.db.AppDatabase
import com.ilyadev.moviesearch.db.MovieDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Модуль Dagger для предоставления компонентов Room.
 *
 * Отвечает за:
 * - Создание экземпляра AppDatabase
 * - Предоставление DAO
 *
 * @Provides — сообщает Dagger, как создать объект
 * @Singleton — гарантирует один экземпляр
 */
@Module
class DatabaseModule {

    private val DATABASE_NAME = "movies_database"

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration() // Пересоздаёт БД при изменении схемы (для разработки)
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(database: AppDatabase): MovieDao {
        return database.movieDao()
    }
}