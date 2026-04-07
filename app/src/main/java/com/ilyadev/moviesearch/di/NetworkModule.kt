package com.ilyadev.moviesearch.di

import com.ilyadev.moviesearch.network.MoviesApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Модуль Dagger 2 для предоставления сетевых зависимостей.
 *
 * @Module — указывает, что это модуль для внедрения зависимостей.
 */
@Module
class NetworkModule {

    private val baseUrl = "https://api.themoviedb.org/3/"

    /**
     * Предоставляет экземпляр [MoviesApiService] через Retrofit.
     *
     * @param retrofit Настроенный экземпляр Retrofit.
     * @return Сервис для запросов к TMDb API.
     */
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): MoviesApiService {
        return retrofit.create(MoviesApiService::class.java)
    }

    /**
     * Предоставляет настроенный экземпляр [Retrofit] с Moshi конвертером.
     *
     * @param client HTTP-клиент (OkHttp).
     * @return Готовый Retrofit-бэкенд.
     */
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    /**
     * Предоставляет HTTP-клиент [OkHttpClient].
     *
     * @return Настроенный OkHttp-клиент.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }
}