package com.ilyadev.moviesearch.di

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class AppApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        // ✅ Инициализация темы при старте
        val prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val isNightEnabled = prefs.getBoolean("night_mode_enabled", false)

        AppCompatDelegate.setDefaultNightMode(
            if (isNightEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        // Создание Dagger-компонента
        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()
    }
}