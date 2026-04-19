package com.ilyadev.moviesearch.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Модульные тесты для MovieResponse data class.
 * 
 * Тесты покрывают:
 * - Свойства data class
 * - Методы equals() и hashCode()
 * - Функциональность copy()
 * - Обработка пустых результатов
 */
class MovieResponseTest {

    @Test
    fun movieResponseCreation_withAllFields() {
        // Дано
        val movies = listOf(
            createTestMovieDto(1),
            createTestMovieDto(2)
        )

        // Когда
        val response = MovieResponse(
            results = movies,
            total_pages = 10,
            page = 1
        )

        // Тогда
        assertEquals(2, response.results.size)
        assertEquals(10, response.total_pages)
        assertEquals(1, response.page)
    }

    @Test
    fun movieResponseCreation_withEmptyResults() {
        // Когда
        val response = MovieResponse(
            results = emptyList(),
            total_pages = 0,
            page = 1
        )

        // Тогда
        assertTrue(response.results.isEmpty())
        assertEquals(0, response.total_pages)
        assertEquals(1, response.page)
    }

    @Test
    fun movieResponse_equals_sameValues() {
        // Дано
        val movies = listOf(createTestMovieDto(1))
        val response1 = MovieResponse(movies, 10, 1)
        val response2 = MovieResponse(movies, 10, 1)

        // Тогда
        assertEquals(response1, response2)
        assertEquals(response1.hashCode(), response2.hashCode())
    }

    @Test
    fun movieResponse_notEquals_differentResults() {
        // Дано
        val response1 = MovieResponse(listOf(createTestMovieDto(1)), 10, 1)
        val response2 = MovieResponse(listOf(createTestMovieDto(2)), 10, 1)

        // Тогда
        assertNotEquals(response1, response2)
    }

    @Test
    fun movieResponse_notEquals_differentTotalPages() {
        // Дано
        val movies = listOf(createTestMovieDto(1))
        val response1 = MovieResponse(movies, 10, 1)
        val response2 = MovieResponse(movies, 20, 1)

        // Тогда
        assertNotEquals(response1, response2)
    }

    @Test
    fun movieResponse_notEquals_differentPage() {
        // Дано
        val movies = listOf(createTestMovieDto(1))
        val response1 = MovieResponse(movies, 10, 1)
        val response2 = MovieResponse(movies, 10, 2)

        // Тогда
        assertNotEquals(response1, response2)
    }

    @Test
    fun movieResponse_copy_preservesMostFields() {
        // Дано
        val original = MovieResponse(listOf(createTestMovieDto(1)), 10, 1)

        // Когда
        val modified = original.copy(page = 5, total_pages = 15)

        // Тогда
        assertEquals(original.results, modified.results)
        assertEquals(15, modified.total_pages)
        assertEquals(5, modified.page)
    }

    @Test
    fun movieResponse_toString_containsInfo() {
        // Дано
        val response = MovieResponse(listOf(createTestMovieDto(1)), 10, 1)

        // Когда
        val toString = response.toString()

        // Тогда
        assertTrue(toString.contains("10")) // total_pages
        assertTrue(toString.contains("page=1")) // информация о странице
    }

    private fun createTestMovieDto(id: Int): MovieDto {
        return MovieDto(
            id = id,
            title = "Movie $id",
            releaseDate = "2023-01-01",
            overview = "Overview for movie $id",
            posterPath = "/poster$id.jpg",
            backdropPath = "/backdrop$id.jpg",
            voteAverage = 8.0,
            voteCount = 100,
            isFavorite = false
        )
    }
}
