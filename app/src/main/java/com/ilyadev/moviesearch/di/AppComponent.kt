package com.ilyadev.moviesearch.di

import com.ilyadev.moviesearch.network.MoviesApiService
import com.ilyadev.moviesearch.ui.home.PagingHomeViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {
    fun provideApiService(): MoviesApiService
    fun inject(viewModel: PagingHomeViewModel)
}