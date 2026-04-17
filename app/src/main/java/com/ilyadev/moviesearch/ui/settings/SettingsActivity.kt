package com.ilyadev.moviesearch.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.ilyadev.moviesearch.R

/**
 * Экран настроек приложения.
 *
 * Использует стандартный подход Android:
 * - AppCompatActivity
 * - PreferenceFragmentCompat для декларативной верстки
 *
 * Настройки хранятся в SharedPreferences через <preferenceScreen>.
 */
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Заменяем контейнер на фрагмент с настройками
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.settings_container, SettingsFragment())
                .commit()
        }

        // Кнопка "Назад" в ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Фрагмент с настройками, загруженными из XML.
     *
     * Преимущества:
     * - Быстро реализовать
     * - Поддержка категорий, переключателей, списков
     * - Автоматическое хранение в SharedPreferences
     *
     * Недостаток:
     * - Меньше контроля над UI
     */
    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
        }
    }
}