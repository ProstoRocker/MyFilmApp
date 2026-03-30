package com.ilyadev.moviesearch.di

import com.ilyadev.moviesearch.network.MoviesApiService
import com.ilyadev.moviesearch.network.RetrofitClient
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = RetrofitClient.retrofit

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): MoviesApiService {
        return retrofit.create(MoviesApiService::class.java)
    }
}