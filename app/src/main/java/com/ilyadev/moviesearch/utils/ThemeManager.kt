package com.ilyadev.moviesearch.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

object ThemeManager {

    private const val PREFS_NAME = "app_settings"
    private const val KEY_NIGHT_MODE = "night_mode_enabled"

    fun setNightThemeEnabled(context: Context, enabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (enabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
            putBoolean(KEY_NIGHT_MODE, enabled)
        }
    }

    fun isNightThemeEnabled(context: Context): Boolean {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_NIGHT_MODE, false)
    }
}