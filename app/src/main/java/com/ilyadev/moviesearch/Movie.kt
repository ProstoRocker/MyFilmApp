package com.ilyadev.moviesearch

data class Movie(
    val id: Int,
    val title: String,
    val year: String,
    val rating: Double,
    val genre: String,
    val posterResId: Int,       // временно из drawable
    val backdropResId: Int,     // фон для CollapsingToolbar
    val description: String
)