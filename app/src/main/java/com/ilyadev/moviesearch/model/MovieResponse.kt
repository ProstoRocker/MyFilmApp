package com.ilyadev.moviesearch.model

import com.squareup.moshi.Json

//Это ответ от API — Moshi автоматически распарсит его в List<MovieDto>
data class MovieResponse(
    @Json(name = "results") val results: List<MovieDto>,
    @Json(name = "total_pages") val total_pages: Int,
    @Json(name = "page") val page: Int
)