package com.ilyadev.moviesearch.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ilyadev.moviesearch.model.MovieDto
import com.ilyadev.moviesearch.paging.MoviesPagingSource
import kotlinx.coroutines.flow.Flow

class PagingHomeViewModel : ViewModel() {

    val movies: Flow<PagingData<MovieDto>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            prefetchDistance = 5,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { MoviesPagingSource() }
    ).flow.cachedIn(viewModelScope)
}