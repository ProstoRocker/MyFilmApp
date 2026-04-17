package com.ilyadev.moviesearch.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ilyadev.moviesearch.API_KEY
import com.ilyadev.moviesearch.db.MovieDao
import com.ilyadev.moviesearch.model.MovieDto
import com.ilyadev.moviesearch.network.MoviesApiService
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Источник пагинации для популярных фильмов.
 *
 * Загружает страницы из:
 * - Сети: /movie/popular
 * - Позже может быть дополнен кэшированием в Room (не используется напрямую)
 *
 * Используется в PagingHomeViewModel для категории "popular".
 */
class MoviesPagingSource(
    private val apiService: MoviesApiService,
    private val movieDao: MovieDao
) : PagingSource<Int, MovieDto>() {

    /**
     * Определяет ключ обновления при refresh.
     * Нужен для восстановления списка после ошибки или перезапуска экрана.
     */
    override fun getRefreshKey(state: PagingState<Int, MovieDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    /**
     * Загружает одну страницу данных.
     *
     * @param params содержит ключ (page number)
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> {
        val page = params.key ?: 1
        return try {
            // RxJava + blockingGet() — безопасно в suspend
            val response = apiService.getPopularMovies(API_KEY.KEY, page)
                .subscribeOn(Schedulers.io())
                .blockingGet()

            val movies = response.results

            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (movies.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}