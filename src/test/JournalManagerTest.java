package test;

import manager.JournalManager;
import model.JournalEntry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive JUnit 5 unit tests for {@link JournalManager}.
 *
 * <p>{@code JournalManager} is a singleton, so {@link #reset()} clears all
 * entries before each test to guarantee isolation between test cases.</p>
 */
@DisplayName("JournalManager — journal entry business logic")
class JournalManagerTest {

    private JournalManager mgr;

    @BeforeEach
    void reset() {
        mgr = JournalManager.getInstance();
        // Clear shared singleton state for a clean slate.
        while (mgr.getTotalEntries() > 0) {
            mgr.deleteEntry(0);
        }
    }

    private JournalEntry sample(String title, String mood) {
        return new JournalEntry("07-06-2026", title, mood, "Body text for " + title);
    }

    @Test
    @DisplayName("getInstance() always returns the same singleton instance")
    void singletonIdentity() {
        assertSame(JournalManager.getInstance(), JournalManager.getInstance());
    }

    @Test
    @DisplayName("addEntry() appends and increments the total count")
    void addEntry() {
        mgr.addEntry(sample("Good Day", "Happy"));
        assertEquals(1, mgr.getTotalEntries());
        assertEquals("Good Day", mgr.getEntry(0).getTitle());
    }

    @Test
    @DisplayName("countByMood() is case-insensitive and counts only matches")
    void countByMood() {
        mgr.addEntry(sample("A", "Happy"));
        mgr.addEntry(sample("B", "happy"));   // different case
        mgr.addEntry(sample("C", "Sad"));
        assertEquals(2, mgr.countByMood("Happy"));
        assertEquals(1, mgr.countByMood("sad"));
        assertEquals(0, mgr.countByMood("Excited"));
    }

    @Test
    @DisplayName("topMood() returns the most frequent mood, or N/A when empty")
    void topMood() {
        assertEquals("N/A", mgr.topMood());
        mgr.addEntry(sample("A", "Calm"));
        mgr.addEntry(sample("B", "Calm"));
        mgr.addEntry(sample("C", "Sad"));
        assertEquals("Calm", mgr.topMood());
    }

    @Test
    @DisplayName("calculateStreak() counts distinct entry dates")
    void calculateStreak() {
        assertEquals(0, mgr.calculateStreak());
        mgr.addEntry(new JournalEntry("07-06-2026", "A", "Happy", "x"));
        mgr.addEntry(new JournalEntry("07-06-2026", "B", "Happy", "y")); // same date
        mgr.addEntry(new JournalEntry("08-06-2026", "C", "Sad", "z"));
        assertEquals(2, mgr.calculateStreak());
    }

    @Test
    @DisplayName("updateEntry() replaces in place without changing the count")
    void updateEntry() {
        mgr.addEntry(sample("Old", "Happy"));
        mgr.updateEntry(0, new JournalEntry("08-06-2026", "New", "Excited", "updated"));
        assertEquals(1, mgr.getTotalEntries());
        assertEquals("New", mgr.getEntry(0).getTitle());
        assertEquals("Excited", mgr.getEntry(0).getMood());
    }

    @Test
    @DisplayName("updateEntry() ignores out-of-range indices and null entries")
    void updateEntryGuards() {
        mgr.addEntry(sample("Keep", "Happy"));
        mgr.updateEntry(99, sample("Nope", "Sad"));   // out of range
        mgr.updateEntry(0, null);                       // null ignored
        assertEquals("Keep", mgr.getEntry(0).getTitle());
    }

    @Test
    @DisplayName("deleteEntry() removes the entry; bad index is a no-op")
    void deleteEntry() {
        mgr.addEntry(sample("A", "Happy"));
        mgr.deleteEntry(5);                 // out of range — no effect
        assertEquals(1, mgr.getTotalEntries());
        mgr.deleteEntry(0);
        assertEquals(0, mgr.getTotalEntries());
    }

    @Test
    @DisplayName("getEntry() returns null for out-of-range indices")
    void getEntryOutOfRange() {
        assertNull(mgr.getEntry(0));
        assertNull(mgr.getEntry(-1));
    }
}
