package com.ilyadev.moviesearch.ui.home

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ilyadev.moviesearch.db.AppDatabase
import com.ilyadev.moviesearch.db.MovieDao
import com.ilyadev.moviesearch.model.MovieDto
import com.ilyadev.moviesearch.network.MoviesApiService
import com.ilyadev.moviesearch.paging.*
import com.ilyadev.moviesearch.utils.CacheManager
import com.ilyadev.moviesearch.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class PagingHomeViewModel @Inject constructor(
    private val apiService: MoviesApiService,
    private val context: Context
) : ViewModel(), SharedPreferences.OnSharedPreferenceChangeListener {

    // ====== 🔹 UI-состояния (LiveData)
    private val _isLoading = MutableLiveData<Boolean>().apply { value = true }
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = SingleLiveEvent<String>()
    val errorMessage: LiveData<String> = _errorMessage

    // ====== 🔹 Состояние категории
    private lateinit var sharedPrefs: SharedPreferences
    private val _currentCategory = MutableStateFlow("popular")
    val currentCategory: StateFlow<String> = _currentCategory

    // ====== 🔹 Room + Кэш
    private lateinit var movieDao: MovieDao
    private lateinit var cacheManager: CacheManager

    // ====== 🔹 Поток пагинации
    val movies: Flow<PagingData<MovieDto>> get() = createPager().flow.cachedIn(viewModelScope)

    init {
        setupSharedPreferences()
        setupRoom()
        setupCacheManager()
    }

    private fun setupSharedPreferences() {
        sharedPrefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        sharedPrefs.registerOnSharedPreferenceChangeListener(this)

        val savedCategory = sharedPrefs.getString("pref_default_category", "popular") ?: "popular"
        _currentCategory.value = savedCategory
    }

    private fun setupRoom() {
        val database = AppDatabase.getInstance(context)
        movieDao = database.movieDao() // Присваиваем
    }

    private fun setupCacheManager() {
        cacheManager = CacheManager(context) // Создаём менеджер кэша
    }

    private fun createPager(): Pager<Int, MovieDto> {
        return if (cacheManager.isCacheExpired()) {
            // ⏳ Кэш устарел → очищаем БД и грузим из сети
            viewModelScope.launch(Dispatchers.IO) {
                movieDao.deleteAll() // Очистка
            }
            buildNetworkPager()
        } else {
            // Кэш актуален → грузим из БД
            buildCachedPager()
        }
    }

    private fun buildNetworkPager(): Pager<Int, MovieDto> {
        return when (_currentCategory.value) {
            "top_rated" -> Pager(config = pagingConfig) { TopRatedPagingSource(apiService, movieDao) }
            "now_playing" -> Pager(config = pagingConfig) { NowPlayingPagingSource(apiService, movieDao) }
            else -> Pager(config = pagingConfig) { MoviesPagingSource(apiService, movieDao) }
        }
    }

    private fun buildCachedPager(): Pager<Int, MovieDto> {
        return Pager(pagingConfig) {
            movieDao.getAllMoviesAsPagingSource() //  Грузим из кэша
        }
    }

    // ==============================
    // 📦 Работа с БД (Room)
    // ==============================

    fun insertMovie(movie: MovieDto) {
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.insert(movie)
        }
    }

    fun toggleFavorite(movie: MovieDto) {
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.update(movie.copy(isFavorite = !movie.isFavorite))
        }
    }

    fun deleteMovie(movie: MovieDto) {
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.delete(movie)
        }
    }

    fun getFavorites(): Flow<List<MovieDto>> = movieDao.getFavorites()

    fun searchMovies(query: String): Flow<List<MovieDto>> = movieDao.searchMovies(query)

    fun getMoviesWithMinRating(minRating: Double): Flow<List<MovieDto>> =
        movieDao.getMoviesWithMinRating(minRating)

    fun getMoviesByGenre(genreId: Int): Flow<List<MovieDto>> =
        movieDao.getAllMovies().map { movies ->
            movies.filter { it.genreIds.contains(genreId) }
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