package com.ilyadev.moviesearch.di

import android.content.Context
import com.ilyadev.moviesearch.db.MovieDao
import com.ilyadev.moviesearch.network.MoviesApiService
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Главный компонент Dagger 2.
 *
 * Отвечает за внедрение зависимостей во всём приложении.
 */
@Singleton
@Component(
    modules = [
        NetworkModule::class,
        DatabaseModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }

    // Экспорт зависимостей для внедрения в ViewModel
    fun apiService(): MoviesApiService
    fun movieDao(): MovieDao

    // Можно добавить инжекторы, если нужно
    // fun inject(viewModel: PagingHomeViewModel)
}