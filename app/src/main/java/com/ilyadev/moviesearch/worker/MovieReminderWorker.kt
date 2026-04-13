package com.ilyadev.moviesearch.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ilyadev.moviesearch.utils.NotificationHelper
import com.ilyadev.moviesearch.data.repository.ReminderRepository

class MovieReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val movieId = inputData.getInt("movie_id", -1)
        val title = inputData.getString("movie_title") ?: return Result.failure()

        // Показываем уведомление
        NotificationHelper.showMovieReminderNotification(applicationContext, movieId, title)

        // Отмечаем как доставленное
        ReminderRepository.markAsDelivered(applicationContext, movieId)

        return Result.success()
    }
}