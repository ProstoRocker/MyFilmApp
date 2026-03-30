package com.ilyadev.moviesearch.di

import com.ilyadev.moviesearch.network.MoviesApiService
import com.ilyadev.moviesearch.network.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class) // Живёт всю жизнь приложения
object NetworkModule {

    @Provides
    fun provideRetrofit(): Retrofit = RetrofitClient.retrofit

    @Provides
    fun provideApiService(retrofit: Retrofit): MoviesApiService =
        retrofit.create(MoviesApiService::class.java)
}