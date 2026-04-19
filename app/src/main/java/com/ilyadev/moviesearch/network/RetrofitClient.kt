package com.ilyadev.moviesearch.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Устаревший клиент.
 *
 * ❌ Не используется в проекте.
 *
 * Оставлен для примера.
 *
 * Современный подход:
 * - Создание Retrofit через Dagger (в NetworkModule)
 * - А не как объект-синглтон
 *
 * Причины:
 * - Проблемы с DI
 * - Сложно тестировать
 * - Gson создаётся заново при каждом lazy-get
 */
object RetrofitClient {
    private const val BASE_URL = "https://api.themoviedb.org/3/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private val gson: Gson by lazy {
        GsonBuilder().setLenient().create()
    }
}