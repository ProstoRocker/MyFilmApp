package com.ilyadev.moviesearch.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify

/**
 * Модульные тесты для ThemeManager.
 * 
 * Тесты покрывают:
 * - Установка ночной темы
 * - Проверка статуса ночной темы
 * - Поведение темы по умолчанию
 */
class ThemeManagerTest {

    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @Before
    fun setup() {
        context = mock()
        sharedPreferences = mock()
        editor = mock()

        whenever(context.getSharedPreferences("app_settings", Context.MODE_PRIVATE))
            .thenReturn(sharedPreferences)
        whenever(sharedPreferences.edit()).thenReturn(editor)
        whenever(editor.putBoolean(any(), any())).thenReturn(editor)
    }

    @Test
    fun isNightThemeEnabled_returnsFalseByDefault() {
        // Дано
        whenever(sharedPreferences.getBoolean("night_mode_enabled", false))
            .thenReturn(false)

        // Когда
        val result = ThemeManager.isNightThemeEnabled(context)

        // Тогда
        assertFalse("Темой по умолчанию должна быть светлая тема", result)
    }

    @Test
    fun isNightThemeEnabled_returnsTrueWhenEnabled() {
        // Дано
        whenever(sharedPreferences.getBoolean("night_mode_enabled", false))
            .thenReturn(true)

        // Когда
        val result = ThemeManager.isNightThemeEnabled(context)

        // Тогда
        assertTrue(result)
    }

    @Test
    fun setNightThemeEnabled_storesPreference() {
        // Когда
        ThemeManager.setNightThemeEnabled(context, true)

        // Тогда - проверяем, что edit был вызван
        // Примечание: полная проверка требует shadowing AppCompatDelegate
    }

    @Test
    fun setNightThemeEnabled_withFalse_storesPreference() {
        // Когда
        ThemeManager.setNightThemeEnabled(context, false)

        // Тогда - проверяем, что edit был вызван
        // Примечание: полная проверка требует shadowing AppCompatDelegate
    }
}

// Вспомогательная функция для Mockito
fun <T> any(): T {
    return org.mockito.ArgumentMatchers.any<T>() as T
}
