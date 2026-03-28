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
            initialLoadSize = 40,     // ← Загружает больше при старте
            maxSize = 200,             // ← Лимит в памяти
            enablePlaceholders = false // ← Без "пустых" ячеек
        ),
        pagingSourceFactory = { MoviesPagingSource() }
    ).flow.cachedIn(viewModelScope) // ← Сохраняет данные при повороте экрана
}