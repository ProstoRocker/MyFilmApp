package com.ilyadev.moviesearch.di

import android.content.Context
import androidx.room.Room
import com.ilyadev.moviesearch.db.AppDatabase
import com.ilyadev.moviesearch.db.MovieDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Модуль Dagger 2 для предоставления компонентов базы данных.
 */
@Module
class DatabaseModule {

    private val DATABASE_NAME = "movies_database"

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(database: AppDatabase): MovieDao {
        return database.movieDao()
    }
}