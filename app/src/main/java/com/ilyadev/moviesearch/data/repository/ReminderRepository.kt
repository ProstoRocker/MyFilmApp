package com.ilyadev.moviesearch.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ilyadev.moviesearch.data.model.Reminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object ReminderRepository {
    private const val REMINDER_PREFIX = "reminder_"

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "reminders_prefs")

    // Ключи для хранения
    private fun idKey(movieId: Int) = intPreferencesKey("${REMINDER_PREFIX}${movieId}_id")
    private fun titleKey(movieId: Int) = stringPreferencesKey("${REMINDER_PREFIX}${movieId}_title")
    private fun timeKey(movieId: Int) = longPreferencesKey("${REMINDER_PREFIX}${movieId}_time")
    private fun scheduledKey(movieId: Int) =
        intPreferencesKey("${REMINDER_PREFIX}${movieId}_scheduled")

    suspend fun addReminder(context: Context, reminder: Reminder) {
        context.dataStore.edit { prefs ->
            prefs[idKey(reminder.movieId)] = reminder.id
            prefs[titleKey(reminder.movieId)] = reminder.movieTitle
            prefs[timeKey(reminder.movieId)] = reminder.reminderTimeMillis
            prefs[scheduledKey(reminder.movieId)] = if (reminder.isScheduled) 1 else 0
        }
    }

    fun getAllRemindersFlow(context: Context): Flow<List<Reminder>> {
        return context.dataStore.data.map { prefs ->
            prefs.asMap().keys
                .filter { it.name.startsWith(REMINDER_PREFIX) && it.name.endsWith("_time") }
                .mapNotNull { key ->
                    val movieIdStr =
                        key.name.substringAfter("${REMINDER_PREFIX}").substringBefore("_time")
                    val movieId = movieIdStr.toIntOrNull() ?: return@mapNotNull null

                    val title = prefs[titleKey(movieId)] ?: "Фильм"
                    val time = prefs[timeKey(movieId)] ?: return@mapNotNull null
                    val scheduled = prefs[scheduledKey(movieId)] == 1

                    if (scheduled) {
                        Reminder(id = movieId.hashCode(), movieId, title, time, scheduled)
                    } else null
                }
                .filterNotNull()
        }
    }

    suspend fun removeReminder(context: Context, movieId: Int) {
        context.dataStore.edit { prefs ->
            prefs.remove(idKey(movieId))
            prefs.remove(titleKey(movieId))
            prefs.remove(timeKey(movieId))
            prefs.remove(scheduledKey(movieId))
        }
    }

    suspend fun markAsDelivered(context: Context, movieId: Int) {
        context.dataStore.edit { prefs ->
            prefs[scheduledKey(movieId)] = 0
        }
    }
}