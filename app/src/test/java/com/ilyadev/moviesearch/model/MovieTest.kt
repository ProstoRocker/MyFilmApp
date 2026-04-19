package com.ilyadev.moviesearch.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Модульные тесты для Movie data class.
 * 
 * Тесты покрывают:
 * - Свойства data class
 * - Методы equals() и hashCode()
 * - Функциональность copy()
 */
class MovieTest {

    @Test
    fun movieCreation_withAllFields() {
        // Когда
        val movie = Movie(
            id = 1,
            title = "Test Movie",
            year = "2023",
            rating = 8.5,
            genre = "Action",
            posterResId = 101,
            backdropResId = 102,
            description = "A test movie description"
        )

        // Тогда
        assertEquals(1, movie.id)
        assertEquals("Test Movie", movie.title)
        assertEquals("2023", movie.year)
        assertEquals(8.5, movie.rating, 0.01)
        assertEquals("Action", movie.genre)
        assertEquals(101, movie.posterResId)
        assertEquals(102, movie.backdropResId)
        assertEquals("A test movie description", movie.description)
    }

    @Test
    fun movie_equals_sameValues() {
        // Дано
        val movie1 = createTestMovie()
        val movie2 = createTestMovie()

        // Тогда
        assertEquals(movie1, movie2)
        assertEquals(movie1.hashCode(), movie2.hashCode())
    }

    @Test
    fun movie_notEquals_differentId() {
        // Дано
        val movie1 = createTestMovie()
        val movie2 = movie1.copy(id = 2)

        // Тогда
        assertNotEquals(movie1, movie2)
    }

    @Test
    fun movie_notEquals_differentTitle() {
        // Дано
        val movie1 = createTestMovie()
        val movie2 = movie1.copy(title = "Different Title")

        // Тогда
        assertNotEquals(movie1, movie2)
    }

    @Test
    fun movie_copy_preservesMostFields() {
        // Дано
        val original = createTestMovie()

        // Когда
        val modified = original.copy(title = "Modified Title", rating = 9.0)

        // Тогда
        assertEquals(original.id, modified.id)
        assertEquals("Modified Title", modified.title)
        assertEquals(9.0, modified.rating, 0.01)
        assertEquals(original.year, modified.year)
        assertEquals(original.genre, modified.genre)
        assertEquals(original.posterResId, modified.posterResId)
        assertEquals(original.backdropResId, modified.backdropResId)
        assertEquals(original.description, modified.description)
    }

    @Test
    fun movie_toString_containsTitle() {
        // Дано
        val movie = createTestMovie()

        // Когда
        val toString = movie.toString()

        // Тогда
        assertTrue(toString.contains("Test Movie"))
    }

    private fun createTestMovie(): Movie {
        return Movie(
            id = 1,
            title = "Test Movie",
            year = "2023",
            rating = 8.5,
            genre = "Action",
            posterResId = 101,
            backdropResId = 102,
            description = "A test movie description"
        )
    }
}
