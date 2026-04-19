package com.ilyadev.moviesearch.data.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Модульные тесты для Reminder data class.
 * 
 * Тесты покрывают:
 * - Свойства data class
 * - Методы equals() и hashCode()
 * - Функциональность copy()
 * - Значения по умолчанию
 */
class ReminderTest {

    @Test
    fun reminderCreation_withAllFields() {
        // Когда
        val reminder = Reminder(
            id = 1,
            movieId = 100,
            movieTitle = "Test Movie",
            reminderTimeMillis = 1234567890L,
            isScheduled = true
        )

        // Тогда
        assertEquals(1, reminder.id)
        assertEquals(100, reminder.movieId)
        assertEquals("Test Movie", reminder.movieTitle)
        assertEquals(1234567890L, reminder.reminderTimeMillis)
        assertTrue(reminder.isScheduled)
    }

    @Test
    fun reminderCreation_defaultIsScheduledTrue() {
        // Когда
        val reminder = Reminder(
            id = 1,
            movieId = 100,
            movieTitle = "Test Movie",
            reminderTimeMillis = 1234567890L
        )

        // Тогда
        assertTrue(reminder.isScheduled)
    }

    @Test
    fun reminder_equals_sameValues() {
        // Дано
        val reminder1 = createTestReminder()
        val reminder2 = createTestReminder()

        // Тогда
        assertEquals(reminder1, reminder2)
        assertEquals(reminder1.hashCode(), reminder2.hashCode())
    }

    @Test
    fun reminder_notEquals_differentId() {
        // Дано
        val reminder1 = createTestReminder()
        val reminder2 = reminder1.copy(id = 2)

        // Тогда
        assertNotEquals(reminder1, reminder2)
    }

    @Test
    fun reminder_notEquals_differentMovieId() {
        // Дано
        val reminder1 = createTestReminder()
        val reminder2 = reminder1.copy(movieId = 200)

        // Тогда
        assertNotEquals(reminder1, reminder2)
    }

    @Test
    fun reminder_notEquals_differentTitle() {
        // Дано
        val reminder1 = createTestReminder()
        val reminder2 = reminder1.copy(movieTitle = "Different Movie")

        // Тогда
        assertNotEquals(reminder1, reminder2)
    }

    @Test
    fun reminder_copy_preservesMostFields() {
        // Дано
        val original = createTestReminder()

        // Когда
        val modified = original.copy(movieTitle = "Modified Title", isScheduled = false)

        // Тогда
        assertEquals(original.id, modified.id)
        assertEquals(original.movieId, modified.movieId)
        assertEquals("Modified Title", modified.movieTitle)
        assertEquals(original.reminderTimeMillis, modified.reminderTimeMillis)
        assertFalse(modified.isScheduled)
    }

    @Test
    fun reminder_toString_containsTitle() {
        // Дано
        val reminder = createTestReminder()

        // Когда
        val toString = reminder.toString()

        // Тогда
        assertTrue(toString.contains("Test Movie"))
    }

    private fun createTestReminder(): Reminder {
        return Reminder(
            id = 1,
            movieId = 100,
            movieTitle = "Test Movie",
            reminderTimeMillis = 1234567890L,
            isScheduled = true
        )
    }
}
