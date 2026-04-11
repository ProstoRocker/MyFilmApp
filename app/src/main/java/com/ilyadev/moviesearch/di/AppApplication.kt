package com.ilyadev.moviesearch.di

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.ilyadev.moviesearch.utils.NotificationHelper

class AppApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        // Восстановление темы
        val prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val isNightEnabled = prefs.getBoolean("night_mode_enabled", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isNightEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        // Dagger
        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()

        // Уведомления
        NotificationHelper.createNotificationChannel(this)
    }
}