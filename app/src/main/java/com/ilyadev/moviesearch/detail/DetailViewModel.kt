package com.ilyadev.moviesearch.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.ilyadev.moviesearch.model.MovieDto
import com.ilyadev.moviesearch.network.MoviesApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * ViewModel для экрана деталей фильма.
 *
 * Отвечает за:
 * - Загрузку фильма по ID
 * - Управление состоянием: загрузка, ошибка, данные
 * - Хранение временного состояния "избранное"
 *
 * Использует RxJava для асинхронных сетевых запросов.
 */
class DetailViewModel(private val apiService: MoviesApiService) : ViewModel() {

    private val _movie = MutableLiveData<MovieDto?>()
    val movie: MutableLiveData<MovieDto?> = _movie

    private val _isLoading = MutableLiveData<Boolean>().apply { value = false }
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?> = _error

    private val disposables = CompositeDisposable()
    private val favorites = mutableSetOf<Int>()
    private val watchLater = mutableSetOf<Int>()

    override fun onCleared() {
        disposables.clear() // Очистка подписок при уничтожении
        super.onCleared()
    }

    /**
     * Загружает фильм по ID через API.
     */
    fun loadMovie(id: Int) {
        _isLoading.value = true
        disposables.add(
            apiService.getMovieDetails(id, "API_KEY.KEY")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { _isLoading.value = false }
                .subscribeBy(
                    onSuccess = { movie -> _movie.value = movie },
                    onError = { e -> _error.value = e.message }
                )
        )
    }

    /**
     * Проверяет, находится ли фильм в избранном.
     */
    fun isFavorite(id: Int): Boolean = favorites.contains(id)

    /**
     * Добавляет фильм в избранное.
     */
    fun addToFavorites(movie: MovieDto) {
        favorites.add(movie.id)
    }

    /**
     * Удаляет фильм из избранного.
     */
    fun removeFromFavorites(movie: MovieDto) {
        favorites.remove(movie.id)
    }

    /**
     * Добавляет в список "Посмотреть позже" (в этом проекте используется локально).
     */
    fun addWatchLater(movie: MovieDto) {
        watchLater.add(movie.id)
    }
}