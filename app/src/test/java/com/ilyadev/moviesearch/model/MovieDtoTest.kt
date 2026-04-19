package com.ilyadev.moviesearch.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Модульные тесты для MovieDto data class.
 * 
 * Тесты покрывают:
 * - Свойства data class
 * - Методы equals() и hashCode()
 * - Функциональность copy()
 * - Значения по умолчанию
 * - Обработка null для опциональных полей
 */
class MovieDtoTest {

    @Test
    fun movieDtoCreation_withAllFields() {
        // Когда
        val movieDto = MovieDto(
            id = 1,
            title = "Test Movie",
            releaseDate = "2023-01-01",
            overview = "A test movie overview",
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            voteAverage = 8.5,
            voteCount = 1000,
            isFavorite = true
        )

        // Тогда
        assertEquals(1, movieDto.id)
        assertEquals("Test Movie", movieDto.title)
        assertEquals("2023-01-01", movieDto.releaseDate)
        assertEquals("A test movie overview", movieDto.overview)
        assertEquals("/poster.jpg", movieDto.posterPath)
        assertEquals("/backdrop.jpg", movieDto.backdropPath)
        assertEquals(8.5, movieDto.voteAverage, 0.01)
        assertEquals(1000, movieDto.voteCount)
        assertTrue(movieDto.isFavorite)
    }

    @Test
    fun movieDtoCreation_defaultIsFavoriteFalse() {
        // Когда
        val movieDto = MovieDto(
            id = 1,
            title = "Test Movie",
            releaseDate = "2023-01-01",
            overview = null,
            posterPath = null,
            backdropPath = null,
            voteAverage = 8.5,
            voteCount = 1000
        )

        // Тогда
        assertFalse(movieDto.isFavorite)
    }

    @Test
    fun movieDtoCreation_allowsNullOverview() {
        // Когда
        val movieDto = MovieDto(
            id = 1,
            title = "Test Movie",
            releaseDate = "2023-01-01",
            overview = null,
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            voteAverage = 8.5,
            voteCount = 1000
        )

        // Тогда
        assertNull(movieDto.overview)
    }

    @Test
    fun movieDtoCreation_allowsNullPosterPath() {
        // Когда
        val movieDto = MovieDto(
            id = 1,
            title = "Test Movie",
            releaseDate = "2023-01-01",
            overview = "Overview",
            posterPath = null,
            backdropPath = "/backdrop.jpg",
            voteAverage = 8.5,
            voteCount = 1000
        )

        // Тогда
        assertNull(movieDto.posterPath)
    }

    @Test
    fun movieDtoCreation_allowsNullBackdropPath() {
        // Когда
        val movieDto = MovieDto(
            id = 1,
            title = "Test Movie",
            releaseDate = "2023-01-01",
            overview = "Overview",
            posterPath = "/poster.jpg",
            backdropPath = null,
            voteAverage = 8.5,
            voteCount = 1000
        )

        // Тогда
        assertNull(movieDto.backdropPath)
    }

    @Test
    fun movieDto_equals_sameValues() {
        // Дано
        val dto1 = createTestMovieDto()
        val dto2 = createTestMovieDto()

        // Тогда
        assertEquals(dto1, dto2)
        assertEquals(dto1.hashCode(), dto2.hashCode())
    }

    @Test
    fun movieDto_notEquals_differentId() {
        // Дано
        val dto1 = createTestMovieDto()
        val dto2 = dto1.copy(id = 2)

        // Тогда
        assertNotEquals(dto1, dto2)
    }

    @Test
    fun movieDto_notEquals_differentTitle() {
        // Дано
        val dto1 = createTestMovieDto()
        val dto2 = dto1.copy(title = "Different Title")

        // Тогда
        assertNotEquals(dto1, dto2)
    }

    @Test
    fun movieDto_notEquals_differentIsFavorite() {
        // Дано
        val dto1 = createTestMovieDto()
        val dto2 = dto1.copy(isFavorite = true)

        // Тогда
        assertNotEquals(dto1, dto2)
    }

    @Test
    fun movieDto_copy_preservesMostFields() {
        // Дано
        val original = createTestMovieDto()

        // Когда
        val modified = original.copy(title = "Modified", voteAverage = 9.0, isFavorite = true)

        // Тогда
        assertEquals(original.id, modified.id)
        assertEquals("Modified", modified.title)
        assertEquals(9.0, modified.voteAverage, 0.01)
        assertTrue(modified.isFavorite)
        assertEquals(original.releaseDate, modified.releaseDate)
        assertEquals(original.overview, modified.overview)
        assertEquals(original.posterPath, modified.posterPath)
        assertEquals(original.backdropPath, modified.backdropPath)
        assertEquals(original.voteCount, modified.voteCount)
    }

    @Test
    fun movieDto_toString_containsTitle() {
        // Дано
        val dto = createTestMovieDto()

        // Когда
        val toString = dto.toString()

        // Тогда
        assertTrue(toString.contains("Test Movie"))
    }

    private fun createTestMovieDto(): MovieDto {
        return MovieDto(
            id = 1,
            title = "Test Movie",
            releaseDate = "2023-01-01",
            overview = "A test movie overview",
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            voteAverage = 8.5,
            voteCount = 1000,
            isFavorite = false
        )
    }
}
