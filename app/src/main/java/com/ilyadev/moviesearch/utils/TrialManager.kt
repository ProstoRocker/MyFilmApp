package com.ilyadev.moviesearch.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ilyadev.moviesearch.di.AppApplication
import kotlinx.coroutines.flow.firstOrNull
import java.util.concurrent.TimeUnit

// DataStore для хранения начала пробного периода
val Context.trialDataStore: DataStore<Preferences> by preferencesDataStore(name = "trial_prefs")

object TrialManager {

    private val START_TIME_KEY = longPreferencesKey("trial_start_time")

    /**
     * Запускает пробный период (один раз).
     */
    suspend fun startTrial(context: Context) {
        val dataStore = context.trialDataStore
        val prefs = dataStore.data.firstOrNull()
        if (prefs?.get(START_TIME_KEY) == null) {
            dataStore.edit { it[START_TIME_KEY] = System.currentTimeMillis() }
        }
    }

    /**
     * Проверяет, активен ли пробный период (меньше 14 дней).
     */
    suspend fun isTrialActive(): Boolean {
        val context = AppApplication.appContext
        val dataStore = context.trialDataStore
        val prefs = dataStore.data.firstOrNull() ?: return true

        val startTime = prefs[START_TIME_KEY] ?: return true
        val elapsedDays = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - startTime)
        return elapsedDays < 14
    }
}