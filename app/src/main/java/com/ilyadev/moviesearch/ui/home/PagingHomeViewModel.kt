package com.ilyadev.moviesearch.ui.home

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ilyadev.moviesearch.model.MovieDto
import com.ilyadev.moviesearch.network.MoviesApiService
import com.ilyadev.moviesearch.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class PagingHomeViewModel @Inject constructor(
    private val apiService: MoviesApiService,
    private val context: Context
) : ViewModel(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var sharedPrefs: SharedPreferences
    private val _currentCategory = MutableStateFlow("popular")
    val currentCategory: StateFlow<String> = _currentCategory

    // 🔥 Теперь каждый вызов создаёт новый поток
    val movies: Flow<PagingData<MovieDto>> get() = createPager().flow.cachedIn(viewModelScope)

    init {
        setupSharedPreferences()
    }

    private fun setupSharedPreferences() {
        sharedPrefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        sharedPrefs.registerOnSharedPreferenceChangeListener(this)

        val savedCategory = sharedPrefs.getString("pref_default_category", "popular") ?: "popular"
        _currentCategory.value = savedCategory
    }

    private fun createPager(): Pager<Int, MovieDto> {
        return when (_currentCategory.value) {
            "top_rated" -> Pager(config = pagingConfig) { TopRatedPagingSource(apiService) }
            "now_playing" -> Pager(config = pagingConfig) { NowPlayingPagingSource(apiService) }
            else -> Pager(config = pagingConfig) { MoviesPagingSource(apiService) }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "pref_default_category") {
            val newCategory = sharedPreferences?.getString(key, "popular") ?: "popular"
            _currentCategory.value = newCategory
        }
    }

    override fun onCleared() {
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(this)
        super.onCleared()
    }

    companion object {
        private val pagingConfig = PagingConfig(pageSize = 20, prefetchDistance = 5)
    }
}