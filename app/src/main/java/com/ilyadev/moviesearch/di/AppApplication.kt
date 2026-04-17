package com.ilyadev.moviesearch.di

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.ilyadev.moviesearch.utils.NotificationHelper
import com.ilyadev.moviesearch.utils.TrialManager
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch

/**
 * Главный класс приложения.
 *
 * Отвечает за:
 * - Инициализацию глобальных компонентов
 * - Настройку темы (день/ночь)
 * - Создание Dagger-компонента
 * - Запуск пробного периода
 * - Подготовку уведомлений
 * - Инициализацию Firebase
 */
class AppApplication : Application() {

    companion object {
        lateinit var appContext: Context // Глобальный контекст для доступа из любого места
    }

    lateinit var appComponent: AppComponent // Компонент Dagger

    override fun onCreate() {
        super.onCreate()

        // Сохраняем контекст для использования в других модулях
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

        // === 3. Создание канала уведомлений (Android 8+) ===
        NotificationHelper.createNotificationChannel(this)

        // === 4. Запуск пробного периода (если первый запуск) ===
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            TrialManager.startTrial(this@AppApplication)
        }

        // === 5. Инициализация Firebase для Remote Config ===
        FirebaseApp.initializeApp(this)
        // Теперь можно использовать FirebaseRemoteConfig в любом экране
    }
}