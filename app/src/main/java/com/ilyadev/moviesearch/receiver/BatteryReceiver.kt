package com.ilyadev.moviesearch.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.ilyadev.moviesearch.utils.ThemeManager

class BatteryReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        when (action) {
            Intent.ACTION_BATTERY_LOW -> {
                showToast(context, "⚠️ Низкий заряд батареи! Рекомендуется подключить зарядку.")
                // ✅ Передаём context и enabled
                ThemeManager.setNightThemeEnabled(context, true)
            }

            Intent.ACTION_POWER_CONNECTED -> {
                showToast(context, "🔌 Устройство подключено к зарядке.")
                // ✅ Передаём context и enabled
                ThemeManager.setNightThemeEnabled(context, false)
            }

            Intent.ACTION_POWER_DISCONNECTED -> {
                showToast(context, "🔋 Зарядка отключена.")
                // Можно оставить текущую тему
            }
        }
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}