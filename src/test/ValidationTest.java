package test;

import exceptions.EmptyFieldException;
import exceptions.InvalidDateException;
import validation.Validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 unit tests for {@link Validation#validateEntry(String, String, String)},
 * covering both the happy path and every failure branch.
 */
@DisplayName("Validation — journal entry input validation")
class ValidationTest {

    @Test
    @DisplayName("A fully valid entry passes without throwing")
    void validEntryAccepted() {
        assertDoesNotThrow(() ->
            Validation.validateEntry("07-06-2026", "My Title", "Some content"));
    }

    @Test
    @DisplayName("Empty date throws EmptyFieldException")
    void emptyDate() {
        EmptyFieldException ex = assertThrows(EmptyFieldException.class,
            () -> Validation.validateEntry("  ", "Title", "Body"));
        assertTrue(ex.getMessage().toLowerCase().contains("date"));
    }

    @Test
    @DisplayName("Empty title throws EmptyFieldException")
    void emptyTitle() {
        assertThrows(EmptyFieldException.class,
            () -> Validation.validateEntry("07-06-2026", "", "Body"));
    }

    @Test
    @DisplayName("Empty entry body throws EmptyFieldException")
    void emptyBody() {
        assertThrows(EmptyFieldException.class,
            () -> Validation.validateEntry("07-06-2026", "Title", "   "));
    }

    @Test
    @DisplayName("Malformed date format throws InvalidDateException")
    void badDateFormat() {
        assertThrows(InvalidDateException.class,
            () -> Validation.validateEntry("2026-06-07", "Title", "Body"));
        assertThrows(InvalidDateException.class,
            () -> Validation.validateEntry("7-6-26", "Title", "Body"));
    }

    @Test
    @DisplayName("Month out of range throws InvalidDateException")
    void invalidMonth() {
        InvalidDateException ex = assertThrows(InvalidDateException.class,
            () -> Validation.validateEntry("07-13-2026", "Title", "Body"));
        assertTrue(ex.getMessage().toLowerCase().contains("month"));
    }

    @Test
    @DisplayName("Day out of range throws InvalidDateException")
    void invalidDay() {
        InvalidDateException ex = assertThrows(InvalidDateException.class,
            () -> Validation.validateEntry("32-06-2026", "Title", "Body"));
        assertTrue(ex.getMessage().toLowerCase().contains("day"));
    }

    @Test
    @DisplayName("Year out of range throws InvalidDateException")
    void invalidYear() {
        InvalidDateException ex = assertThrows(InvalidDateException.class,
            () -> Validation.validateEntry("07-06-1999", "Title", "Body"));
        assertTrue(ex.getMessage().toLowerCase().contains("year"));
    }
}
