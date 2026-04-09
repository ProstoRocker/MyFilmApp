package com.ilyadev.moviesearch.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ilyadev.moviesearch.model.MovieDto
import com.ilyadev.moviesearch.paging.SearchPagingSource
import com.ilyadev.moviesearch.network.MoviesApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * ViewModel для поиска фильмов.
 *
 * Реализует:
 * - Пагинацию результатов через Paging 3
 * - Обновление запроса (через updateQuery)
 */
class SearchViewModel @Inject constructor(
    private val apiService: MoviesApiService
) : ViewModel() {

    // Текущий запрос (управляется из Fragment)
    private var currentQuery = ""

    /**
     * Поток пагинации — используется в UI (RecyclerView).
     * Пересоздаётся при изменении query.
     */
    fun searchResults(query: String): Flow<androidx.paging.PagingData<MovieDto>> {
        currentQuery = query
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 5),
            pagingSourceFactory = { SearchPagingSource(apiService, query) }
        ).flow.cachedIn(viewModelScope)
    }
}