package com.ilyadev.moviesearch.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilyadev.moviesearch.data.repository.MovieRepository
import com.ilyadev.moviesearch.model.Movie
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private var _onMoviesLoaded: ((List<Movie>) -> Unit)? = null
    private var _onLoadingChanged: ((Boolean) -> Unit)? = null

    fun setOnMoviesLoaded(callback: (List<Movie>) -> Unit) {
        _onMoviesLoaded = callback
    }

    fun setOnLoadingChanged(callback: (Boolean) -> Unit) {
        _onLoadingChanged = callback
    }

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            _onLoadingChanged?.invoke(true)
            kotlinx.coroutines.delay(500)
            val movies = MovieRepository.getAllMovies()
            _onMoviesLoaded?.invoke(movies)
            _onLoadingChanged?.invoke(false)
        }
    }

    fun search(query: String) {
        val filtered = if (query.isEmpty()) {
            MovieRepository.getAllMovies()
        } else {
            MovieRepository.getAllMovies().filter { movie ->
                movie.title.contains(query, ignoreCase = true) ||
                        movie.genre.contains(query, ignoreCase = true)
            }
        }
        _onMoviesLoaded?.invoke(filtered)
    }
}