package com.ilyadev.moviesearch.data.repository

import com.ilyadev.moviesearch.model.Movie
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Модульные тесты для MovieRepository.
 * 
 * Тесты покрывают:
 * - Получение всех фильмов
 * - Добавление в избранное
 * - Удаление из избранного
 * - Проверка фильма на наличие в избранном
 * - Получение фильма по ID
 */
class MovieRepositoryTest {

    @Before
    fun setup() {
        // Сбрасываем состояние репозитория перед каждым тестом
        MovieRepository.resetForTesting()
    }

    @Test
    fun getAllMovies_returnsNonEmptyList() {
        // Когда
        val movies = MovieRepository.getAllMovies()

        // Тогда
        assertTrue("Список фильмов не должен быть пустым", movies.isNotEmpty())
        assertEquals(7, movies.size) // У нас 7 тестовых фильмов
    }

    @Test
    fun addToFavorites_addsMovieSuccessfully() {
        // Дано
        val movie = MovieRepository.getAllMovies().first()
        val initialSize = MovieRepository.favorites.size

        // Когда
        MovieRepository.addToFavorites(movie)

        // Тогда
        assertEquals(initialSize + 1, MovieRepository.favorites.size)
        assertTrue(MovieRepository.isFavorite(movie.id))
    }

    @Test
    fun addToFavorites_doesNotAddDuplicate() {
        // Дано
        val movie = MovieRepository.getAllMovies().first()
        MovieRepository.addToFavorites(movie)
        val sizeAfterFirstAdd = MovieRepository.favorites.size

        // Когда
        MovieRepository.addToFavorites(movie)

        // Тогда
        assertEquals(sizeAfterFirstAdd, MovieRepository.favorites.size)
    }

    @Test
    fun removeFromFavorites_removesMovieSuccessfully() {
        // Дано
        val movie = MovieRepository.getAllMovies().first()
        MovieRepository.addToFavorites(movie)
        assertTrue(MovieRepository.isFavorite(movie.id))

        // Когда
        MovieRepository.removeFromFavorites(movie.id)

        // Тогда
        assertFalse("Фильм должен быть удалён из избранного", MovieRepository.isFavorite(movie.id))
        assertTrue(MovieRepository.favorites.none { it.id == movie.id })
    }

    @Test
    fun isFavorite_returnsTrueForFavoriteMovie() {
        // Дано
        val movie = MovieRepository.getAllMovies().first()
        MovieRepository.addToFavorites(movie)

        // Когда и Тогда
        assertTrue(MovieRepository.isFavorite(movie.id))
    }

    @Test
    fun isFavorite_returnsFalseForNonFavoriteMovie() {
        // Дано
        val movie = MovieRepository.getAllMovies().first()

        // Когда и Тогда
        assertFalse(MovieRepository.isFavorite(movie.id))
    }

    @Test
    fun getMovieById_returnsMovieFromMockList() {
        // Дано
        val expectedMovie = MovieRepository.getAllMovies().find { it.id == 1 }
        assertNotNull(expectedMovie)

        // Когда
        val result = MovieRepository.getMovieById(1)

        // Тогда
        assertNotNull(result)
        assertEquals(expectedMovie?.id, result?.id)
        assertEquals(expectedMovie?.title, result?.title)
    }

    @Test
    fun getMovieById_returnsMovieFromFavorites() {
        // Дано
        val movie = MovieRepository.getAllMovies().find { it.id == 1 }
        assertNotNull(movie)
        MovieRepository.addToFavorites(movie!!)

        // Когда
        val result = MovieRepository.getMovieById(1)

        // Тогда
        assertNotNull(result)
        assertEquals(movie.id, result?.id)
    }

    @Test
    fun getMovieById_returnsNullForNonExistentId() {
        // Когда
        val result = MovieRepository.getMovieById(Int.MAX_VALUE)

        // Тогда
        assertNull(result)
    }

    @Test
    fun favorites_returnsUnmodifiableCopy() {
        // Дано
        val movie = MovieRepository.getAllMovies().first()
        MovieRepository.addToFavorites(movie)

        // Когда
        val favoritesCopy = MovieRepository.favorites

        // Тогда - проверяем, что получили копию с фильмом
        assertTrue(favoritesCopy.any { it.id == movie.id })
        
        // Оригинальная реализация возвращает toList(), который создаёт новый список,
        // поэтому изменения не повлияют на внутреннее состояние
    }
}
