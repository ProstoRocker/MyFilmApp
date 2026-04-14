package com.ilyadev.moviesearch.di

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.ilyadev.moviesearch.utils.NotificationHelper
import com.ilyadev.moviesearch.utils.TrialManager
import kotlinx.coroutines.launch

class AppApplication : Application() {

    // Глобальный контекст для использования в TrialManager и других утилитах
    companion object {
        lateinit var appContext: Context
    }

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        // Сохраняем контекст приложения
        appContext = this

        // === 1. Восстановление ночной темы ===
        val prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val isNightEnabled = prefs.getBoolean("night_mode_enabled", false)

        AppCompatDelegate.setDefaultNightMode(
            if (isNightEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        // === 2. Инициализация Dagger-компонента ===
        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()

        // === 3. Создание канала уведомлений ===
        NotificationHelper.createNotificationChannel(this)

        // === 4. Запуск пробного периода (если ещё не запускался) ===
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            TrialManager.startTrial(this@AppApplication)
        }
    }
}