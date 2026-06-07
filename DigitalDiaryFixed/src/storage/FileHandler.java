package storage;

import manager.JournalManager;
import model.JournalEntry;

import java.io.*;
import java.nio.file.*;
import java.util.List;

public class FileHandler {

    private static final String FILE_PATH = "data/journal.txt";

    public static void saveAllJournal() throws IOException {
        Files.createDirectories(Paths.get("data"));
        try (BufferedWriter w = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (JournalEntry e : JournalManager.getInstance().getEntries()) {
                w.write(e.toFileLine());
                w.newLine();
            }
        }
    }

    public static void loadJournal(JournalManager manager) throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;
        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                JournalEntry e = JournalEntry.fromFileLine(line);
                if (e != null) manager.addEntry(e);
            }
        }
    }
}
