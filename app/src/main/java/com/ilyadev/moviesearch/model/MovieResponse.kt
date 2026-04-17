package com.ilyadev.moviesearch.model

import com.google.gson.annotations.SerializedName

/**
 * Обёртка ответа от TMDb API.
 *
 * Приходит при запросах к endpoint'ам:
 * - /movie/popular
 * - /movie/top_rated
 * - /movie/now_playing
 *
 * Содержит:
 * - results: List<MovieDto> — список фильмов
 * - total_pages: общее количество страниц
 * - page: текущая страница
 *
 * Используется в Paging 3 и SearchPagingSource.
 */
data class MovieResponse(
    @SerializedName("results") val results: List<MovieDto>,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("page") val page: Int
)