package com.ilyadev.moviesearch.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ilyadev.moviesearch.API_KEY
import com.ilyadev.moviesearch.model.MovieDto
import com.ilyadev.moviesearch.network.MoviesApiService
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

/**
 * PagingSource для поиска фильмов по названию.
 *
 * Используется в SearchViewModel для пагинации результатов поиска.
 */
class SearchPagingSource(
    private val apiService: MoviesApiService,
    private val query: String
) : PagingSource<Int, MovieDto>() {

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, MovieDto>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> {
        return try {
            val page = params.key ?: STARTING_PAGE_INDEX

            // Вызов RxJava из suspend через withContext(Dispatchers.IO)
            val response = withContext(Dispatchers.IO) {
                apiService.searchMovies(query, API_KEY.KEY, page)
                    .subscribeOn(Schedulers.io())
                    .blockingGet()  // Возвращает MovieResponse
            }

            LoadResult.Page(
                data = response.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page < response.total_pages) page + 1 else null
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}