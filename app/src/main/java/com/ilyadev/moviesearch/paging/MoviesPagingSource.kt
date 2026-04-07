package com.ilyadev.moviesearch.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ilyadev.moviesearch.API_KEY
import com.ilyadev.moviesearch.db.MovieDao
import com.ilyadev.moviesearch.model.MovieDto
import com.ilyadev.moviesearch.network.MoviesApiService
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.io.IOException

/**
 * PagingSource для популярных фильмов.
 *
 * Важно:
 * - Использует RxJava + runBlocking для запросов к API
 * - Читает из БД через suspend-метод getAllMoviesSync()
 * - Поддерживает fallback на кэш при отсутствии сети
 */
class MoviesPagingSource(
    private val apiService: MoviesApiService,
    private val movieDao: MovieDao
) : PagingSource<Int, MovieDto>() {

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> {
        return try {
            val page = params.key ?: STARTING_PAGE_INDEX

            // 🔁 Запрос через RxJava → блокирующий вызов в корутине
            val response = runBlocking {
                apiService.getPopularMovies(API_KEY.KEY, page)
                    .subscribeOn(Schedulers.io())
                    .blockingGet()  // ← Возвращает MovieResponse
            }

            // ✅ Теперь response.results доступен (если MovieResponse правильно объявлен)
            val movies = response.results

            // Сохраняем в БД
            movieDao.insertAll(movies.map { it.copy(isFavorite = false) })

            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page < response.total_pages) page + 1 else null
            )
        } catch (e: IOException) {
            getCachedData()
        } catch (e: HttpException) {
            getCachedData()
        } catch (e: Exception) {
            getCachedData()
        }
    }

    private suspend fun getCachedData(): LoadResult<Int, MovieDto> {
        return try {
            val cached = movieDao.getAllMoviesSync()  // ✅ suspend-метод, возвращает List<MovieDto>
            if (cached.isNotEmpty()) {
                LoadResult.Page(data = cached, prevKey = null, nextKey = null)
            } else {
                LoadResult.Error(Exception("No data in cache"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieDto>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}