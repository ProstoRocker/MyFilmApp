package com.ilyadev.moviesearch

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