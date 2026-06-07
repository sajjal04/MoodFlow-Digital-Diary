package test;

import exceptions.EmptyFieldException;
import exceptions.InvalidDateException;
import validation.GoalValidation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 unit tests for {@link GoalValidation#validateGoal(String, String)}.
 */
@DisplayName("GoalValidation — goal input validation")
class GoalValidationTest {

    @Test
    @DisplayName("Valid goal with a deadline passes")
    void validGoalWithDeadline() {
        assertDoesNotThrow(() -> GoalValidation.validateGoal("Run 5k", "31-12-2026"));
    }

    @Test
    @DisplayName("Valid goal with an empty (optional) deadline passes")
    void validGoalNoDeadline() {
        assertDoesNotThrow(() -> GoalValidation.validateGoal("Read more", ""));
        assertDoesNotThrow(() -> GoalValidation.validateGoal("Read more", "   "));
    }

    @Test
    @DisplayName("Empty goal name throws EmptyFieldException")
    void emptyGoalName() {
        EmptyFieldException ex = assertThrows(EmptyFieldException.class,
            () -> GoalValidation.validateGoal("  ", "31-12-2026"));
        assertTrue(ex.getMessage().toLowerCase().contains("goal"));
    }

    @Test
    @DisplayName("Malformed deadline throws InvalidDateException")
    void badDeadlineFormat() {
        assertThrows(InvalidDateException.class,
            () -> GoalValidation.validateGoal("Goal", "2026/12/31"));
        assertThrows(InvalidDateException.class,
            () -> GoalValidation.validateGoal("Goal", "31 Dec 2026"));
    }
}
