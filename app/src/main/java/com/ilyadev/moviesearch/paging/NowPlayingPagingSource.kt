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
 * PagingSource для фильмов, которые сейчас в кино.
 *
 * Важно:
 * - Paging 3 требует suspend-функций, поэтому сетевые запросы через RxJava обёрнуты в runBlocking + blockingGet()
 * - Все операции с БД — через suspend-методы Room (например, getAllMoviesSync())
 * - Кэш используется при отсутствии сети
 */
class NowPlayingPagingSource(
    private val apiService: MoviesApiService,
    private val movieDao: MovieDao
) : PagingSource<Int, MovieDto>() {

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> {
        return try {
            val page = params.key ?: STARTING_PAGE_INDEX

            // 🔁 Запрос к API через RxJava → блокирующий вызов в корутине
            val response = runBlocking {
                apiService.getNowPlayingMovies(API_KEY.KEY, page)
                    .subscribeOn(Schedulers.io())
                    .blockingGet()  // Возвращает MovieResponse
            }

            // ✅ Теперь `response.results` доступен — потому что MovieResponse объявлен правильно
            val movies = response.results

            // Сохраняем в БД (все фильмы — не избранные)
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

    /**
     * Пытается загрузить данные из локальной БД, если сеть недоступна.
     */
    private suspend fun getCachedData(): LoadResult<Int, MovieDto> {
        return try {
            // Читую из БД (синхронно в suspend)
            val cached = movieDao.getAllMoviesSync()
            if (cached.isNotEmpty()) {
                LoadResult.Page(data = cached, prevKey = null, nextKey = null)
            } else {
                LoadResult.Error(Exception("No data in cache"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    /**
     * Определяет ключ для обновления пагинации (обязательно в Paging 3.2+).
     * Используется при свайпе вверх/вниз для подгрузки соседних страниц.
     */
    override fun getRefreshKey(state: PagingState<Int, MovieDto>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}