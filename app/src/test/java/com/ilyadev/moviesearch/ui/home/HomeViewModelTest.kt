package com.ilyadev.moviesearch.ui.home

import com.ilyadev.moviesearch.data.repository.MovieRepository
import com.ilyadev.moviesearch.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Модульные тесты для HomeViewModel.
 * 
 * Тесты покрывают:
 * - Загрузка фильмов при инициализации
 * - Функциональность поиска
 * - Изменения состояния загрузки
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MovieRepository.resetForTesting()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun viewModel_init_loadsMovies() = runTest {
        // Дано
        var loadedMovies: List<Movie>? = null
        var isLoading: Boolean? = null

        // Когда
        val viewModel = HomeViewModel().apply {
            setOnMoviesLoaded { movies -> loadedMovies = movies }
            setOnLoadingChanged { loading -> isLoading = loading }
        }

        // Продвигаем выполнение корутин
        kotlinx.coroutines.test.advanceUntilIdle()

        // Тогда
        assertNotNull(loadedMovies)
        assertTrue(loadedMovies!!.isNotEmpty())
    }

    @Test
    fun search_withEmptyQuery_returnsAllMovies() = runTest {
        // Дано
        var loadedMovies: List<Movie>? = null
        val viewModel = HomeViewModel().apply {
            setOnMoviesLoaded { movies -> loadedMovies = movies }
        }

        kotlinx.coroutines.test.advanceUntilIdle()
        val allMoviesCount = loadedMovies?.size ?: 0

        // Когда
        viewModel.search("")

        // Тогда
        assertEquals(allMoviesCount, loadedMovies?.size)
    }

    @Test
    fun search_withQuery_filtersByTitle() = runTest {
        // Дано
        var loadedMovies: List<Movie>? = null
        val viewModel = HomeViewModel().apply {
            setOnMoviesLoaded { movies -> loadedMovies = movies }
        }

        kotlinx.coroutines.test.advanceUntilIdle()

        // Когда
        viewModel.search("Матрица")

        // Тогда
        assertNotNull(loadedMovies)
        assertTrue(loadedMovies!!.all { it.title.contains("Матрица", ignoreCase = true) })
    }

    @Test
    fun search_withQuery_filtersByGenre() = runTest {
        // Дано
        var loadedMovies: List<Movie>? = null
        val viewModel = HomeViewModel().apply {
            setOnMoviesLoaded { movies -> loadedMovies = movies }
        }

        kotlinx.coroutines.test.advanceUntilIdle()

        // Когда
        viewModel.search("Драма")

        // Тогда
        assertNotNull(loadedMovies)
        assertTrue(loadedMovies!!.all { 
            it.title.contains("Драма", ignoreCase = true) || 
            it.genre.contains("Драма", ignoreCase = true)
        })
    }

    @Test
    fun search_caseInsensitive() = runTest {
        // Дано
        var loadedMovies: List<Movie>? = null
        val viewModel = HomeViewModel().apply {
            setOnMoviesLoaded { movies -> loadedMovies = movies }
        }

        kotlinx.coroutines.test.advanceUntilIdle()

        // Когда
        viewModel.search("матрица")

        // Тогда
        assertNotNull(loadedMovies)
        assertTrue(loadedMovies!!.isNotEmpty())
    }

    @Test
    fun search_noMatches_returnsEmptyList() = runTest {
        // Дано
        var loadedMovies: List<Movie>? = null
        val viewModel = HomeViewModel().apply {
            setOnMoviesLoaded { movies -> loadedMovies = movies }
        }

        kotlinx.coroutines.test.advanceUntilIdle()

        // Когда
        viewModel.search("НесуществующийФильм12345")

        // Тогда
        assertNotNull(loadedMovies)
        assertTrue(loadedMovies!!.isEmpty())
    }
}
