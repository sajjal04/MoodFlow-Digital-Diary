package test;

import exceptions.EmptyFieldException;
import exceptions.InvalidDateException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 unit tests for the custom checked exceptions used by the app.
 *
 * <p>Verifies that each exception is a checked {@link Exception}, preserves
 * the message passed to its constructor, and can be thrown and caught.</p>
 */
@DisplayName("Custom exceptions — EmptyFieldException & InvalidDateException")
class ExceptionTest {

    @Test
    @DisplayName("EmptyFieldException preserves its message")
    void emptyFieldMessage() {
        EmptyFieldException ex = new EmptyFieldException("field is empty");
        assertEquals("field is empty", ex.getMessage());
    }

    @Test
    @DisplayName("InvalidDateException preserves its message")
    void invalidDateMessage() {
        InvalidDateException ex = new InvalidDateException("bad date");
        assertEquals("bad date", ex.getMessage());
    }

    @Test
    @DisplayName("Both exceptions are checked exceptions (extend Exception)")
    void areCheckedExceptions() {
        assertTrue(Exception.class.isAssignableFrom(EmptyFieldException.class));
        assertTrue(Exception.class.isAssignableFrom(InvalidDateException.class));
        assertFalse(RuntimeException.class.isAssignableFrom(EmptyFieldException.class));
        assertFalse(RuntimeException.class.isAssignableFrom(InvalidDateException.class));
    }

    @Test
    @DisplayName("EmptyFieldException can be thrown and caught")
    void throwAndCatchEmptyField() {
        EmptyFieldException caught = assertThrows(EmptyFieldException.class, () -> {
            throw new EmptyFieldException("boom");
        });
        assertEquals("boom", caught.getMessage());
    }

    @Test
    @DisplayName("InvalidDateException can be thrown and caught")
    void throwAndCatchInvalidDate() {
        InvalidDateException caught = assertThrows(InvalidDateException.class, () -> {
            throw new InvalidDateException("nope");
        });
        assertEquals("nope", caught.getMessage());
    }
}
