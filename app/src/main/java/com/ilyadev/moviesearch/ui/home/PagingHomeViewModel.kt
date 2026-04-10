package com.ilyadev.moviesearch.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ilyadev.moviesearch.API_KEY
import com.ilyadev.moviesearch.db.AppDatabase
import com.ilyadev.moviesearch.db.MovieDao
import com.ilyadev.moviesearch.model.MovieDto
import com.ilyadev.moviesearch.network.MoviesApiService
import com.ilyadev.moviesearch.paging.MoviesPagingSource
import com.ilyadev.moviesearch.paging.NowPlayingPagingSource
import com.ilyadev.moviesearch.paging.TopRatedPagingSource
import com.ilyadev.moviesearch.utils.CacheManager
import com.ilyadev.moviesearch.utils.PosterDownloader
import com.ilyadev.moviesearch.utils.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * ViewModel для главного экрана, реализованная на RxJava + Paging 3.
 *
 * Отвечает за:
 * - Загрузку фильмов из сети или кэша (Room)
 * - Управление состоянием UI: загрузка, ошибка
 * - Переключение категорий
 * - Предоставление потока данных для RecyclerView через Paging 3
 */
class PagingHomeViewModel @Inject constructor(
    private val apiService: MoviesApiService,
    private val context: Context
) : ViewModel(), SharedPreferences.OnSharedPreferenceChangeListener {

    // ====== 🔹 UI-состояния (для наблюдения в Fragment) ======

    /** Показывает, идёт ли загрузка данных (прогресс-бар) */
    private val _isLoading = MutableLiveData<Boolean>().apply { value = true }
    val isLoading: LiveData<Boolean> = _isLoading

    /** Ошибка, которую нужно показать однократно (Snackbar) */
    private val _errorMessage = SingleLiveEvent<String>()
    val errorMessage: LiveData<String> = _errorMessage

    /**
     * Публичный метод для отправки сообщений об ошибках.
     * Используется при сетевых ошибках, ошибках БД и т.д.
     */
    fun postErrorMessage(message: String) {
        _errorMessage.value = message
    }

    // ====== 🔹 Управление категорией ======

    /** Текущая выбранная категория (по умолчанию "popular") */
    private val _currentCategory = MutableLiveData<String>().apply { value = "popular" }
    val currentCategory: LiveData<String> = _currentCategory

    // ====== 🔹 Работа с данными ======

    private lateinit var movieDao: MovieDao
    private lateinit var cacheManager: CacheManager

    // Для управления подписками RxJava
    private val disposables = CompositeDisposable()

    init {
        setupSharedPreferences()
        setupRoom()
        setupCacheManager()
        loadMovies()
    }

    // ====== 🔹 Асинхронные операции: сохранение постера ======

    /**
     * Асинхронная загрузка постера фильма и сохранение в галерею.
     *
     * Используется при долгом нажатии на карточку фильма.
     */
    suspend fun downloadPosterSuspend(movie: MovieDto): Result<Uri> {
        return try {
            PosterDownloader.downloadAndSaveToGallery(
                context = context,
                posterUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                title = movie.title
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ====== 🔹 Настройка компонентов ======

    /**
     * Настройка SharedPreferences для хранения пользовательских настроек.
     * Например: выбранная категория по умолчанию.
     */
    private fun setupSharedPreferences() {
        val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        prefs.registerOnSharedPreferenceChangeListener(this)

        val savedCategory = prefs.getString("pref_default_category", "popular") ?: "popular"
        _currentCategory.value = savedCategory
    }

    /**
     * Инициализация Room Database.
     * Получаем DAO для работы с таблицей фильмов.
     */
    private fun setupRoom() {
        val database = AppDatabase.getInstance(context)
        movieDao = database.movieDao()
    }

    /**
     * Инициализация менеджера кэширования.
     * Проверяет, прошло ли более 10 минут с последней загрузки.
     */
    private fun setupCacheManager() {
        cacheManager = CacheManager(context)
    }

    // ==============================
    // 📥 Загрузка фильмов (основной метод)
    // ==============================

    /**
     * Загружает фильмы:
     * - Если кэш устарел — очищаем БД и загружаем из сети
     * - Иначе — показываем данные из локального кэша
     */
    fun loadMovies() {
        _isLoading.value = true

        if (cacheManager.isCacheExpired()) {
            clearAndLoadFromNetwork()
        } else {
            loadFromCache()
        }
    }

    /**
     * Очищает БД и загружает данные из сети.
     */
    private fun clearAndLoadFromNetwork() {
        disposables.add(
            movieDao.deleteAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { fetchFromNetwork() }
                .subscribeBy(
                    onComplete = {},
                    onError = { e ->
                        postErrorMessage("Ошибка очистки кэша: ${e.message}")
                        loadFromCache() // fallback
                    }
                )
        )
    }

    /**
     * Выполняет запрос к API в зависимости от текущей категории.
     * Сохраняет полученные фильмы в БД.
     */
    private fun fetchFromNetwork() {
        val call = when (_currentCategory.value) {
            "top_rated" -> apiService.getTopRatedMovies(API_KEY.KEY)
            "now_playing" -> apiService.getNowPlayingMovies(API_KEY.KEY)
            else -> apiService.getPopularMovies(API_KEY.KEY)
        }

        disposables.add(
            call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { response ->
                    // Сохраняем в БД (все фильмы — не избранные)
                    movieDao.insertAll(response.results.map { it.copy(isFavorite = false) })
                        .subscribeOn(Schedulers.io())
                        .subscribeBy(
                            onComplete = {},
                            onError = { /* игнорируем */ }
                        )
                }
                .subscribeBy(
                    onSuccess = { response ->
                        _isLoading.value = false
                    },
                    onError = { e ->
                        postErrorMessage("Ошибка сети: ${e.message}")
                        loadFromCache() // fallback на кэш
                    }
                )
        )
    }

    /**
     * Загружает фильмы из локальной БД (кэш).
     */
    private fun loadFromCache() {
        disposables.add(
            movieDao.getAllMovies()
                .firstOrError()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = { movies ->
                        _isLoading.value = false
                    },
                    onError = { e ->
                        postErrorMessage("Кэш пуст или ошибка чтения: ${e.message}")
                        _isLoading.value = false
                    }
                )
        )
    }

    // ==============================
    // 🔹 Поток пагинации (для RecyclerView)
    // ==============================

    /**
     * Публичный поток фильмов для отображения в UI через Paging 3.
     * Подключает Network + Database через соответствующий PagingSource.
     */
    val movies: Flow<PagingData<MovieDto>> get() = createPager().flow.cachedIn(viewModelScope)

    private fun createPager(): Pager<Int, MovieDto> {
        val config = pagingConfig
        return when (_currentCategory.value) {
            "top_rated" -> Pager(config = config) {
                TopRatedPagingSource(apiService, movieDao)
            }
            "now_playing" -> Pager(config = config) {
                NowPlayingPagingSource(apiService, movieDao)
            }
            else -> Pager(config = config) {
                MoviesPagingSource(apiService, movieDao)
            }
        }
    }

    companion object {
        private val pagingConfig = PagingConfig(pageSize = 20, prefetchDistance = 5)
    }

    // ==============================
    // 🔁 Обработка изменений в SharedPreferences
    // ==============================

    override fun onSharedPreferenceChanged(sharedPrefs: SharedPreferences?, key: String?) {
        if (key == "pref_default_category") {
            val newCategory = sharedPrefs?.getString(key, "popular") ?: "popular"
            _currentCategory.value = newCategory
        }
    }

    // ==============================
    // 🧹 Очистка ресурсов
    // ==============================

    override fun onCleared() {
        disposables.clear()
        context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
            .unregisterOnSharedPreferenceChangeListener(this)
        super.onCleared()
    }
}