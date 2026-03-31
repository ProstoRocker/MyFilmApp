package com.ilyadev.moviesearch.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ilyadev.moviesearch.API_KEY
import com.ilyadev.moviesearch.model.MovieDto
import com.ilyadev.moviesearch.network.MoviesApiService
import retrofit2.HttpException
import java.io.IOException

class MoviesPagingSource(
    private val apiService: MoviesApiService  // Сервис для запросов к API
) : PagingSource<Int, MovieDto>() {

    companion object {
        const val STARTING_PAGE_INDEX = 1  // Начинаем с первой страницы
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> {
        return try {
            val page = params.key ?: STARTING_PAGE_INDEX  // Текущая страница

            // Запрос к TMDb API
            val response = apiService.getPopularMovies(API_KEY.KEY, page)

            // Возвращаем данные + ключи для предыдущей и следующей страницы
            LoadResult.Page(
                data = response.results,
                prevKey = if (page == 1) null else page - 1,  // Нет предыдущей на первой
                nextKey = if (page < response.totalPages) page + 1 else null  // Конец пагинации
            )
        } catch (e: IOException) {
            LoadResult.Error(e)  // Ошибка сети
        } catch (e: HttpException) {
            LoadResult.Error(e)  // Ошибка сервера
        } catch (e: Exception) {
            LoadResult.Error(e)  // Другие ошибки
        }
    }

    // Определяем, с какой страницы обновлять список
    override fun getRefreshKey(state: PagingState<Int, MovieDto>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}