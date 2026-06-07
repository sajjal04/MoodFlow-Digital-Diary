package test;

import manager.GoalManager;
import model.Goal;

public class GoalManagerTest {

    public static void main(String[] args) {

        GoalManager mgr = GoalManager.getInstance();

        // Test 1: Add goal
        Goal g = new Goal("Learn Java", "Education", "31-12-2026", 40);
        mgr.addGoal(g);
        assert mgr.getTotalGoals() == 1 : "FAIL: total goals should be 1";
        System.out.println("PASS: addGoal");

        // Test 2: Average progress
        assert mgr.getAverageProgress() == 40.0 : "FAIL: average progress";
        System.out.println("PASS: getAverageProgress");

        // Test 3: Complete goal
        mgr.completeGoal(0);
        assert mgr.getGoals().get(0).isCompleted() : "FAIL: goal should be completed";
        assert mgr.getGoals().get(0).getProgress() == 100 : "FAIL: progress should be 100";
        System.out.println("PASS: completeGoal");

        // Test 4: Completed count
        assert mgr.getCompletedGoals() == 1 : "FAIL: completed goals should be 1";
        System.out.println("PASS: getCompletedGoals");

        // Test 5: Delete goal
        mgr.deleteGoal(0);
        assert mgr.getTotalGoals() == 0 : "FAIL: total goals should be 0 after delete";
        System.out.println("PASS: deleteGoal");

        System.out.println("All GoalManagerTests PASSED.");
    }
}
