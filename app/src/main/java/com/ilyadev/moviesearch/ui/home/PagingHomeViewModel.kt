package com.ilyadev.moviesearch.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ilyadev.moviesearch.model.MovieDto
import com.ilyadev.moviesearch.network.MoviesApiService
import com.ilyadev.moviesearch.paging.MoviesPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PagingHomeViewModel @Inject constructor(
    private val apiService: MoviesApiService
) : ViewModel() {

    val movies: Flow<PagingData<MovieDto>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { MoviesPagingSource(apiService) }
    ).flow.cachedIn(viewModelScope)
}