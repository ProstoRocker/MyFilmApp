package com.ilyadev.moviesearch.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.ilyadev.moviesearch.utils.ThemeManager

/**
 * BroadcastReceiver для отслеживания состояния батареи.
 *
 * Слушает системные события:
 * - ACTION_BATTERY_LOW — низкий заряд (менее 15%)
 * - ACTION_POWER_CONNECTED — устройство подключено к зарядке
 * - ACTION_POWER_DISCONNECTED — зарядка отключена
 *
 * Реагирует автоматически:
 * - При низком заряде → включает ночную тему
 * - При подключении → выключает ночную тему
 *
 * 🔔 Особенность: работает даже когда приложение закрыто (если не убито системой).
 */
class BatteryReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        when (action) {
            // === 1. Низкий заряд батареи ===
            Intent.ACTION_BATTERY_LOW -> {
                showToast(context, "⚠️ Низкий заряд батареи! Рекомендуется подключить зарядку.")
                ThemeManager.setNightThemeEnabled(context, true) // Включаем тёмную тему
            }

            // === 2. Подключение к зарядке ===
            Intent.ACTION_POWER_CONNECTED -> {
                showToast(context, "🔌 Устройство подключено к зарядке.")
                ThemeManager.setNightThemeEnabled(context, false) // Выключаем тёмную тему
            }

            // === 3. Отключение зарядки ===
            Intent.ACTION_POWER_DISCONNECTED -> {
                showToast(context, "🔋 Зарядка отключена.")
                // Не меняем тему — оставляем как есть (можно добавить логику по предыдущему состоянию)
            }
        }
    }

    /**
     * Показывает Toast-сообщение.
     */
    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}