package com.ilyadev.moviesearch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

/**
 * Экран-заставка (Splash Screen), отображается при запуске приложения.
 *
 * Отвечает за:
 * - Показ анимации или логотипа при старте
 * - Инициализацию начального UI
 * - Переход в MainActivity после задержки
 *
 * Использует современный Android API: `androidx.core.splashscreen`
 */
class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Устанавливаем SplashScreen до вызова super.onCreate()
        // → автоматически показывает тему из @style/SplashTheme
        installSplashScreen()

        // Вызов родительского метода
        super.onCreate(savedInstanceState)

        // Загружаем пустую разметку (может быть не нужна — зависит от стиля)
        setContentView(R.layout.activity_splash)

        // Задержка 2 секунды перед переходом
        Handler(Looper.getMainLooper()).postDelayed({
            // Переключение на основной экран
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Убираем SplashActivity из стека навигации
        }, 2000) // ⚠️ В реальном приложении можно заменить на проверку инициализации
    }
}