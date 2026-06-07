package storage;

import manager.GoalManager;
import model.Goal;

import java.io.*;
import java.nio.file.*;
import java.util.List;

public class GoalFileHandler {

    private static final String FILE_PATH = "data/goals.txt";

    public static void saveAllGoals() throws IOException {
        Files.createDirectories(Paths.get("data"));
        try (BufferedWriter w = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Goal g : GoalManager.getInstance().getGoals()) {
                w.write(g.toFileLine());
                w.newLine();
            }
        }
    }

    public static void loadGoals(GoalManager manager) throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;
        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                Goal g = Goal.fromFileLine(line);
                if (g != null) manager.addGoal(g);
            }
        }
    }
}
