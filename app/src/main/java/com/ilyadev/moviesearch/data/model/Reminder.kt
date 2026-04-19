package com.ilyadev.moviesearch.data.model

/**
 * Модель напоминания о фильме.
 *
 * Используется для:
 * - Хранения в DataStore (через ReminderRepository)
 * - Передачи в WorkManager (для отложенных уведомлений)
 * - Отображения в списке "Посмотреть позже"
 *
 * Поля:
 * - id — уникальный ID напоминания (генерируется при создании)
 * - movieId — ID фильма (для связи с MovieDto)
 * - movieTitle — название фильма (чтобы не загружать его повторно)
 * - reminderTimeMillis — время в миллисекундах (System.currentTimeMillis())
 * - isScheduled — флаг: активно ли напоминание (true = показать, false = уже показано/отменено)
 */
data class Reminder(
    val id: Int,
    val movieId: Int,
    val movieTitle: String,
    val reminderTimeMillis: Long,
    val isScheduled: Boolean = true
)