package manager;

import model.Goal;
import java.util.ArrayList;

public class GoalManager {

    private static GoalManager instance;
    private ArrayList<Goal> goals = new ArrayList<>();

    private GoalManager() {}

    public static GoalManager getInstance() {
        if (instance == null) instance = new GoalManager();
        return instance;
    }

    public void addGoal(Goal goal) { goals.add(goal); }

    public void deleteGoal(int index) {
        if (index >= 0 && index < goals.size()) goals.remove(index);
    }

    public void completeGoal(int index) {
        if (index >= 0 && index < goals.size()) goals.get(index).markComplete();
    }

    public void updateProgress(int index, int progress) {
        if (index >= 0 && index < goals.size()) goals.get(index).setProgress(progress);
    }

    public ArrayList<Goal> getGoals() { return goals; }

    public int getTotalGoals() { return goals.size(); }

    public int getCompletedGoals() {
        int count = 0;
        for (Goal g : goals) if (g.isCompleted()) count++;
        return count;
    }

    public double getAverageProgress() {
        if (goals.isEmpty()) return 0;
        int total = 0;
        for (Goal g : goals) total += g.getProgress();
        return (double) total / goals.size();
    }
}
