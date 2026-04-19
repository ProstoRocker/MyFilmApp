package com.ilyadev.moviesearch.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ilyadev.moviesearch.R
import com.ilyadev.moviesearch.detail.DetailActivity

/**
 * Помощник по работе с уведомлениями.
 *
 * Отвечает за:
 * - Создание канала (обязательно для Android 8+)
 * - Показ уведомлений о напоминаниях
 *
 * Используется в MovieReminderWorker.
 */
object NotificationHelper {

    private const val CHANNEL_ID = "movie_reminder_channel"
    private const val NOTIFICATION_ID_BASE = 1001

    /**
     * Создаёт канал уведомлений.
     * Нужно вызвать один раз при старте приложения.
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Напоминания о фильмах",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Уведомления о фильмах, которые нужно посмотреть"
            }

            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }

    /**
     * Показывает уведомление-напоминание.
     *
     * При клике открывает DetailActivity.
     */
    fun showMovieReminderNotification(context: Context, movieId: Int, title: String) {
        val intent = Intent(context, DetailActivity::class.java).apply {
            putExtra("movie_id", movieId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            movieId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_movie)
            .setContentTitle("Напоминание")
            .setContentText("Не забудьте посмотреть: $title")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Фильм \"$title\" всё ещё ждёт вас. Приятного просмотра!")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(NOTIFICATION_ID_BASE + movieId, notification)
    }
}