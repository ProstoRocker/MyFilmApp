package com.ilyadev.moviesearch.model

/**
 * Простая модель фильма для демо-данных.
 *
 * Используется в:
 * - MovieRepository (мок-репозиторий)
 * - PosterAdapter
 * - Тестовых экранах
 *
 * Поля:
 * - id: Int — уникальный ID
 * - title: String — название
 * - year: String — год выпуска
 * - rating: Double — рейтинг
 * - genre: String — жанр
 * - posterResId: Int — ссылка на локальный ресурс (R.drawable.*)
 * - backdropResId: Int — фоновое изображение
 * - description: String — описание
 *
 * ⚠️ Не используется в сетевых запросах.
 */
data class Movie(
    val id: Int,
    val title: String,
    val year: String,
    val rating: Double,
    val genre: String,
    val posterResId: Int,
    val backdropResId: Int,
    val description: String
)