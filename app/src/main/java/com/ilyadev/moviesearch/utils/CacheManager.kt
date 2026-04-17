package com.ilyadev.moviesearch.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import java.util.concurrent.TimeUnit

/**
 * Менеджер кэширования для управления временем последней загрузки данных.
 * Используется для определения, нужно ли обновлять данные из сети или можно использовать локальный кэш (Room).
 *
 * Логика:
 * - Если с последней загрузки прошло >10 минут → считаем кэш устаревшим
 * - Иначе — используем локальные данные
 *
 * @param context Контекст приложения (для доступа к SharedPreferences)
 */
class CacheManager(private val context: Context) {

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences("cache_prefs", Context.MODE_PRIVATE)

    private val LAST_UPDATED_KEY = "last_updated_timestamp"

    /**
     * Получает время последней успешной загрузки данных (в миллисекундах).
     * Возвращает 0L, если метка не установлена (например, впервые запуск).
     */
    fun getLastUpdatedTime(): Long {
        return sharedPrefs.getLong(LAST_UPDATED_KEY, 0L)
    }

    /**
     * Обновляет временную метку.
     * Устанавливает текущее время как метку последней загрузки.
     * Вызывается после успешного получения данных из сети.
     *
     * @param time Время в миллисекундах (по умолчанию — текущее время)
     * Вызывается после успешной загрузки из API.
     */
    fun setLastUpdatedTime(time: Long = System.currentTimeMillis()) {
        sharedPrefs.edit {
            putLong(LAST_UPDATED_KEY, time)
        }
    }

    /**
     * Проверяет, истёк ли срок действия кэша.
     * Кэш считается устаревшим, если прошло **10 минут или больше** с момента последней загрузки.
     *
     * @return true — кэш устарел (нужно обновить из сети), false — кэш актуален
     */
    fun isCacheExpired(): Boolean {
        val lastUpdated = getLastUpdatedTime()
        if (lastUpdated == 0L) return true // первая загрузка → считаем устаревшим

        val now = System.currentTimeMillis()
        val tenMinutesInMillis = TimeUnit.MINUTES.toMillis(10)
        return now - lastUpdated >= tenMinutesInMillis
    }

    /**
     * Принудительно очищает метку времени (для тестирования или ручного обновления).
     * После вызова `isCacheExpired()` вернёт `true`.
     * Полезно для тестирования.
     */
    fun clearCache() {
        sharedPrefs.edit {
            putLong(LAST_UPDATED_KEY, 0L)
        }
    }
}