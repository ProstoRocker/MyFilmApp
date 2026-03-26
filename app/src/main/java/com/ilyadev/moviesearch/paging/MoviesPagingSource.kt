package com.ilyadev.moviesearch.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ilyadev.moviesearch.model.MovieDto
import com.ilyadev.moviesearch.network.RetrofitClient
import com.ilyadev.moviesearch.API_KEY
import retrofit2.HttpException
import java.io.IOException

/**
 * Источник данных для Paging 3.
 * Загружает фильмы постранично из TMDb API.
 */
class MoviesPagingSource : PagingSource<Int, MovieDto>() {

    // Начальная страница
    private companion object {
        const val STARTING_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> {
        return try {
            // Определяем номер страницы: либо из запроса, либо начинаем с первой
            val page = params.key ?: STARTING_PAGE_INDEX

            // Выполняем сетевой запрос через Retrofit
            val response = RetrofitClient.apiService.getPopularMovies(
                apiKey = API_KEY.KEY,  // Ваш ключ API
                page = page           // Номер страницы
            )

            // Проверяем, есть ли результаты
            if (response.results.isEmpty()) {
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }

            // Возвращаем страницу с данными
            LoadResult.Page(
                data = response.results,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page < response.totalPages) page + 1 else null
            )

        } catch (exception: IOException) {
            // Ошибка сети (нет интернета)
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            // Ошибка от сервера (401, 404 и т.д.)
            LoadResult.Error(exception)
        } catch (exception: Exception) {
            // Любая другая ошибка
            LoadResult.Error(exception)
        }
    }

    /**
     * Используется при обновлении списка (refresh).
     * Определяет, с какой страницы начать загрузку после ошибки или перезапуска.
     */
    override fun getRefreshKey(state: PagingState<Int, MovieDto>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}
