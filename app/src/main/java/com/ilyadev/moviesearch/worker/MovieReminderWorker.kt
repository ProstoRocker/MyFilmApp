package com.ilyadev.moviesearch.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ilyadev.moviesearch.utils.NotificationHelper
import com.ilyadev.moviesearch.data.repository.ReminderRepository

/**
 * Работник (Worker) для показа уведомлений о фильмах.
 *
 * Выполняется один раз в указанное время через WorkManager.
 *
 * Отвечает за:
 * - Получение данных: ID и название фильма
 * - Показ уведомления через NotificationHelper
 * - Отметку напоминания как "доставленного" (чтобы не дублировать)
 *
 * Использует CoroutineWorker — современный способ асинхронных задач.
 */
class MovieReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    /**
     * Основной метод, выполняемый в фоне.
     *
     * @return Result.success() — задача выполнена
     *         Result.failure() — ошибка (например, нет данных)
     */
    override suspend fun doWork(): Result {
        // Получаем данные из входного набора
        val movieId = inputData.getInt("movie_id", -1)
        val title = inputData.getString("movie_title") ?: return Result.failure()

        // Проверка корректности данных
        if (movieId == -1) {
            return Result.failure()
        }

        // Показываем уведомление
        NotificationHelper.showMovieReminderNotification(applicationContext, movieId, title)

        // Отмечаем, что уведомление доставлено
        ReminderRepository.markAsDelivered(applicationContext, movieId)

        // Возвращаем успех
        return Result.success()
    }
}