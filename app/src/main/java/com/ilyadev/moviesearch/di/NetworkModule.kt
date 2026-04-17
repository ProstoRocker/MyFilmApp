package com.ilyadev.moviesearch.di

import com.ilyadev.moviesearch.network.MoviesApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Модуль Dagger для сетевых компонентов.
 *
 * Отвечает за:
 * - Создание Retrofit с baseUrl
 * - Добавление адаптера для RxJava 3
 * - Добавление конвертера JSON (Gson)
 * - Управление HTTP-клиентом (OkHttp)
 */
@Module
class NetworkModule {

    private val baseUrl = "https://api.themoviedb.org/3/"

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()) // JSON ↔ объекты
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // Поддержка Single, Observable
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): MoviesApiService {
        return retrofit.create(MoviesApiService::class.java)
    }
}