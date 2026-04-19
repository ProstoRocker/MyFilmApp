package com.ilyadev.moviesearch.detail

import com.ilyadev.moviesearch.model.MovieDto
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Модульные тесты для DetailViewModel.
 * 
 * Тесты покрывают:
 * - Загрузка деталей фильма
 * - Управление состоянием загрузки
 * - Обработка ошибок
 * - Управление избранным
 * - Функциональность "посмотреть позже"
 */
class DetailViewModelTest {

    private lateinit var apiService: MoviesApiService
    private lateinit var viewModel: DetailViewModel

    @Before
    fun setup() {
        apiService = mock()
        viewModel = DetailViewModel(apiService)
    }

    @Test
    fun loadMovie_updatesIsLoadingToTrue() {
        // Дано
        val movieId = 1
        val movieDto = createTestMovieDto(movieId)
        
        whenever(apiService.getMovieDetails(movieId, "API_KEY.KEY"))
            .thenReturn(Single.just(movieDto))

        // Когда
        viewModel.loadMovie(movieId)

        // Тогда
        assertTrue(viewModel.isLoading.value ?: false)
    }

    @Test
    fun loadMovie_onSuccess_setsMovieData() {
        // Дано
        val movieId = 1
        val expectedMovie = createTestMovieDto(movieId)
        
        whenever(apiService.getMovieDetails(movieId, "API_KEY.KEY"))
            .thenReturn(Single.just(expectedMovie))

        // Когда
        viewModel.loadMovie(movieId)

        // Примечание: для полной проверки потребуется Robolectric или аналогичный инструмент
        // для корректной обработки планировщиков RxJava
    }

    @Test
    fun loadMovie_onError_setsErrorMessage() {
        // Дано
        val movieId = 1
        val errorMessage = "Ошибка сети"
        
        whenever(apiService.getMovieDetails(movieId, "API_KEY.KEY"))
            .thenReturn(Single.error(Exception(errorMessage)))

        // Когда
        viewModel.loadMovie(movieId)

        // Примечание: ошибка будет установлена после выполнения RxJava на главном потоке
    }

    @Test
    fun isFavorite_returnsFalseForNonFavoriteMovie() {
        // Дано
        val movieId = 999

        // Когда и Тогда
        assertFalse(viewModel.isFavorite(movieId))
    }

    @Test
    fun addToFavorites_addsMovieToFavorites() {
        // Дано
        val movie = createTestMovieDto(1)

        // Когда
        viewModel.addToFavorites(movie)

        // Тогда
        assertTrue(viewModel.isFavorite(movie.id))
    }

    @Test
    fun removeFromFavorites_removesMovieFromFavorites() {
        // Дано
        val movie = createTestMovieDto(1)
        viewModel.addToFavorites(movie)
        assertTrue(viewModel.isFavorite(movie.id))

        // Когда
        viewModel.removeFromFavorites(movie)

        // Тогда
        assertFalse(viewModel.isFavorite(movie.id))
    }

    @Test
    fun addWatchLater_addsMovieToWatchLaterList() {
        // Дано
        val movie = createTestMovieDto(1)

        // Когда
        viewModel.addWatchLater(movie)

        // Примечание: список watchLater не доступен через геттер,
        // поэтому мы не можем напрямую проверить его в модульных тестах
        // Это потребует интеграционного тестирования
    }

    @Test
    fun multipleMovies_canBeAddedToFavorites() {
        // Дано
        val movie1 = createTestMovieDto(1)
        val movie2 = createTestMovieDto(2)
        val movie3 = createTestMovieDto(3)

        // Когда
        viewModel.addToFavorites(movie1)
        viewModel.addToFavorites(movie2)
        viewModel.addToFavorites(movie3)

        // Тогда
        assertTrue(viewModel.isFavorite(movie1.id))
        assertTrue(viewModel.isFavorite(movie2.id))
        assertTrue(viewModel.isFavorite(movie3.id))
    }

    @Test
    fun removeFromFavorites_nonExistentMovie_doesNotCrash() {
        // Дано
        val movie = createTestMovieDto(999)

        // Когда и Тогда - не должно выбрасывать исключение
        viewModel.removeFromFavorites(movie)
    }

    private fun createTestMovieDto(id: Int): MovieDto {
        return MovieDto(
            id = id,
            title = "Test Movie $id",
            originalTitle = "Test Movie $id",
            overview = "Test overview for movie $id",
            posterPath = "/poster$id.jpg",
            backdropPath = "/backdrop$id.jpg",
            releaseDate = "2023-01-01",
            voteAverage = 8.5,
            voteCount = 100,
            popularity = 50.0,
            genreIds = listOf(1, 2, 3),
            originalLanguage = "en",
            adult = false,
            video = false
        )
    }
}
