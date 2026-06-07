package test;

import manager.GoalManager;
import model.Goal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive JUnit 5 unit tests for {@link GoalManager}.
 *
 * <p>{@code GoalManager} is a singleton, so {@link #reset()} clears all goals
 * before each test to guarantee isolation between test cases.</p>
 */
@DisplayName("GoalManager — goal tracking business logic")
class GoalManagerTest {

    private GoalManager mgr;

    @BeforeEach
    void reset() {
        mgr = GoalManager.getInstance();
        while (mgr.getTotalGoals() > 0) {
            mgr.deleteGoal(0);
        }
    }

    @Test
    @DisplayName("getInstance() always returns the same singleton instance")
    void singletonIdentity() {
        assertSame(GoalManager.getInstance(), GoalManager.getInstance());
    }

    @Test
    @DisplayName("addGoal() appends and increments the total count")
    void addGoal() {
        mgr.addGoal(new Goal("Learn Java", "Education", "31-12-2026", 40));
        assertEquals(1, mgr.getTotalGoals());
        assertEquals("Learn Java", mgr.getGoal(0).getGoalName());
    }

    @Test
    @DisplayName("getAverageProgress() averages progress; 0 when empty")
    void averageProgress() {
        assertEquals(0.0, mgr.getAverageProgress(), 1e-9);
        mgr.addGoal(new Goal("A", "Education", "", 40));
        mgr.addGoal(new Goal("B", "Career", "", 60));
        assertEquals(50.0, mgr.getAverageProgress(), 1e-9);
    }

    @Test
    @DisplayName("completeGoal() sets progress to 100 and marks completed")
    void completeGoal() {
        mgr.addGoal(new Goal("A", "Education", "", 30));
        mgr.completeGoal(0);
        assertTrue(mgr.getGoal(0).isCompleted());
        assertEquals(100, mgr.getGoal(0).getProgress());
    }

    @Test
    @DisplayName("updateProgress() updates value and completion flag at 100")
    void updateProgress() {
        mgr.addGoal(new Goal("A", "Education", "", 10));
        mgr.updateProgress(0, 55);
        assertEquals(55, mgr.getGoal(0).getProgress());
        assertFalse(mgr.getGoal(0).isCompleted());
        mgr.updateProgress(0, 100);
        assertTrue(mgr.getGoal(0).isCompleted());
    }

    @Test
    @DisplayName("getCompletedGoals() counts only completed goals")
    void completedCount() {
        mgr.addGoal(new Goal("A", "Education", "", 100));
        mgr.addGoal(new Goal("B", "Career", "", 50));
        assertEquals(1, mgr.getCompletedGoals());
    }

    @Test
    @DisplayName("updateGoal() replaces in place without changing the count")
    void updateGoal() {
        mgr.addGoal(new Goal("Old", "Education", "31-12-2026", 40));
        mgr.updateGoal(0, new Goal("Master Java", "Career", "30-06-2027", 75));
        assertEquals(1, mgr.getTotalGoals());
        assertEquals("Master Java", mgr.getGoal(0).getGoalName());
        assertEquals("Career", mgr.getGoal(0).getCategory());
        assertEquals(75, mgr.getGoal(0).getProgress());
    }

    @Test
    @DisplayName("updateGoal() ignores out-of-range indices and null goals")
    void updateGoalGuards() {
        mgr.addGoal(new Goal("Keep", "Education", "", 20));
        mgr.updateGoal(50, new Goal("Nope", "Other", "", 0));
        mgr.updateGoal(0, null);
        assertEquals("Keep", mgr.getGoal(0).getGoalName());
    }

    @Test
    @DisplayName("deleteGoal() removes the goal; bad index is a no-op")
    void deleteGoal() {
        mgr.addGoal(new Goal("A", "Education", "", 10));
        mgr.deleteGoal(9);
        assertEquals(1, mgr.getTotalGoals());
        mgr.deleteGoal(0);
        assertEquals(0, mgr.getTotalGoals());
    }

    @Test
    @DisplayName("getGoal() returns null for out-of-range indices")
    void getGoalOutOfRange() {
        assertNull(mgr.getGoal(0));
        assertNull(mgr.getGoal(-3));
    }
}
