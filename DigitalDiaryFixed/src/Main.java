import gui.DashboardGUI;
import manager.JournalManager;
import manager.GoalManager;
import storage.FileHandler;
import storage.GoalFileHandler;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        // Load persisted data on startup
        try {
            FileHandler.loadJournal(JournalManager.getInstance());
            GoalFileHandler.loadGoals(GoalManager.getInstance());
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> new DashboardGUI());
    }
}
