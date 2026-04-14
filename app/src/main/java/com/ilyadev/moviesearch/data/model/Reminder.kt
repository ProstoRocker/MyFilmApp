package com.ilyadev.moviesearch.data.model

data class Reminder(
    val id: Int,
    val movieId: Int,
    val movieTitle: String,
    val reminderTimeMillis: Long,
    val isScheduled: Boolean = true
)