package com.ilyadev.moviesearch.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull

/**
 * Проверяет, первый ли это запуск приложения.
 *
 * Используется для показа онбординга.
 *
 * Хранит состояние в DataStore:
 * - has_launched = true → уже запускался
 */
val Context.firstLaunchDataStore: DataStore<Preferences> by preferencesDataStore(name = "first_launch_prefs")

object FirstLaunchManager {

    private val LAUNCHED_KEY = booleanPreferencesKey("has_launched")

    /**
     * Отмечает, что приложение уже запускалось.
     */
    suspend fun markAsLaunched(context: Context) {
        context.firstLaunchDataStore.edit { prefs -> prefs[LAUNCHED_KEY] = true }
    }

    /**
     * Проверяет, является ли текущий запуск первым.
     *
     * @return true — первый запуск, false — уже был
     */
    suspend fun isFirstLaunch(context: Context): Boolean {
        val prefs = context.firstLaunchDataStore.data.firstOrNull() ?: return true
        return prefs[LAUNCHED_KEY] != true
    }
}