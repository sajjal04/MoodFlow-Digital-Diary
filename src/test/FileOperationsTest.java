package test;

import manager.GoalManager;
import manager.JournalManager;
import model.Goal;
import model.JournalEntry;
import storage.FileHandler;
import storage.GoalFileHandler;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 unit tests for the persistence layer ({@link FileHandler} and
 * {@link GoalFileHandler}).
 *
 * <p>These handlers read/write fixed files under {@code data/}. To avoid
 * clobbering any real user data, existing files are backed up before the
 * suite runs and restored afterwards.</p>
 */
@DisplayName("File operations — FileHandler & GoalFileHandler persistence")
class FileOperationsTest {

    private static final Path JOURNAL = Paths.get("data/journal.txt");
    private static final Path GOALS   = Paths.get("data/goals.txt");
    private static byte[] journalBackup;
    private static byte[] goalsBackup;

    @BeforeAll
    static void backup() throws IOException {
        journalBackup = Files.exists(JOURNAL) ? Files.readAllBytes(JOURNAL) : null;
        goalsBackup   = Files.exists(GOALS)   ? Files.readAllBytes(GOALS)   : null;
    }

    @AfterAll
    static void restore() throws IOException {
        restoreFile(JOURNAL, journalBackup);
        restoreFile(GOALS, goalsBackup);
    }

    private static void restoreFile(Path p, byte[] data) throws IOException {
        if (data != null) {
            Files.createDirectories(p.getParent());
            Files.write(p, data);
        } else {
            Files.deleteIfExists(p);
        }
    }

    @BeforeEach
    void clearManagers() {
        JournalManager jm = JournalManager.getInstance();
        while (jm.getTotalEntries() > 0) jm.deleteEntry(0);
        GoalManager gm = GoalManager.getInstance();
        while (gm.getTotalGoals() > 0) gm.deleteGoal(0);
    }

    @Test
    @DisplayName("Journal entries persist and reload with identical content")
    void journalSaveLoad() throws IOException {
        JournalManager jm = JournalManager.getInstance();
        jm.addEntry(new JournalEntry("07-06-2026", "First", "Happy", "Body one"));
        jm.addEntry(new JournalEntry("08-06-2026", "Second", "Sad", "Body two"));
        FileHandler.saveAllJournal();

        // Reset and reload from disk.
        while (jm.getTotalEntries() > 0) jm.deleteEntry(0);
        FileHandler.loadJournal(jm);

        assertEquals(2, jm.getTotalEntries());
        assertEquals("First", jm.getEntry(0).getTitle());
        assertEquals("Sad", jm.getEntry(1).getMood());
        assertEquals("Body two", jm.getEntry(1).getEntry());
    }

    @Test
    @DisplayName("Saving an empty journal produces an empty (loadable) file")
    void journalSaveEmpty() throws IOException {
        FileHandler.saveAllJournal();          // nothing in manager
        JournalManager jm = JournalManager.getInstance();
        FileHandler.loadJournal(jm);
        assertEquals(0, jm.getTotalEntries());
    }

    @Test
    @DisplayName("Goals persist and reload, preserving completion state")
    void goalSaveLoad() throws IOException {
        GoalManager gm = GoalManager.getInstance();
        gm.addGoal(new Goal("Learn Java", "Education", "31-12-2026", 40));
        gm.addGoal(new Goal("Finish Project", "Career", "", 100)); // completed
        GoalFileHandler.saveAllGoals();

        while (gm.getTotalGoals() > 0) gm.deleteGoal(0);
        GoalFileHandler.loadGoals(gm);

        assertEquals(2, gm.getTotalGoals());
        assertEquals("Learn Java", gm.getGoal(0).getGoalName());
        assertEquals(40, gm.getGoal(0).getProgress());
        assertTrue(gm.getGoal(1).isCompleted());
    }

    @Test
    @DisplayName("Loading when no file exists leaves the manager empty")
    void loadMissingFile() throws IOException {
        Files.deleteIfExists(JOURNAL);
        JournalManager jm = JournalManager.getInstance();
        FileHandler.loadJournal(jm);            // should be a safe no-op
        assertEquals(0, jm.getTotalEntries());
    }
}
