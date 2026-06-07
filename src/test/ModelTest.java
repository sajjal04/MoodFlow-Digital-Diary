package test;

import model.Goal;
import model.JournalEntry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 unit tests for the {@link JournalEntry} and {@link Goal} model
 * classes, focusing on the file-persistence serialization round-trips
 * ({@code toFileLine} / {@code fromFileLine}) and completion logic.
 */
@DisplayName("Models — JournalEntry & Goal serialization and logic")
class ModelTest {

    // ── JournalEntry ────────────────────────────────────────────────

    @Test
    @DisplayName("JournalEntry round-trips through toFileLine/fromFileLine")
    void journalRoundTrip() {
        JournalEntry e = new JournalEntry("07-06-2026", "My Title", "Happy", "Hello world");
        JournalEntry back = JournalEntry.fromFileLine(e.toFileLine());
        assertNotNull(back);
        assertEquals(e.getDate(), back.getDate());
        assertEquals(e.getTitle(), back.getTitle());
        assertEquals(e.getMood(), back.getMood());
        assertEquals(e.getEntry(), back.getEntry());
    }

    @Test
    @DisplayName("JournalEntry preserves pipe and newline characters in fields")
    void journalEscaping() {
        JournalEntry e = new JournalEntry("07-06-2026", "A|B", "Happy", "line1\nline2 | end");
        JournalEntry back = JournalEntry.fromFileLine(e.toFileLine());
        assertNotNull(back);
        assertEquals("A|B", back.getTitle());
        assertEquals("line1\nline2 | end", back.getEntry());
    }

    @Test
    @DisplayName("JournalEntry.fromFileLine returns null for malformed lines")
    void journalMalformed() {
        assertNull(JournalEntry.fromFileLine("only|three|fields"));
    }

    // ── Goal ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Goal round-trips through toFileLine/fromFileLine")
    void goalRoundTrip() {
        Goal g = new Goal("Learn Java", "Education", "31-12-2026", 40);
        Goal back = Goal.fromFileLine(g.toFileLine());
        assertNotNull(back);
        assertEquals("Learn Java", back.getGoalName());
        assertEquals("Education", back.getCategory());
        assertEquals("31-12-2026", back.getDeadline());
        assertEquals(40, back.getProgress());
        assertFalse(back.isCompleted());
    }

    @Test
    @DisplayName("Goal constructed at 100% is automatically completed")
    void goalAutoComplete() {
        Goal g = new Goal("Done", "Other", "", 100);
        assertTrue(g.isCompleted());
    }

    @Test
    @DisplayName("markComplete() forces progress to 100 and completed flag")
    void goalMarkComplete() {
        Goal g = new Goal("WIP", "Other", "", 30);
        assertFalse(g.isCompleted());
        g.markComplete();
        assertTrue(g.isCompleted());
        assertEquals(100, g.getProgress());
    }

    @Test
    @DisplayName("setProgress() toggles completion exactly at 100")
    void goalSetProgress() {
        Goal g = new Goal("WIP", "Other", "", 0);
        g.setProgress(99);
        assertFalse(g.isCompleted());
        g.setProgress(100);
        assertTrue(g.isCompleted());
    }

    @Test
    @DisplayName("Completed Goal survives a serialization round-trip")
    void goalCompletedRoundTrip() {
        Goal g = new Goal("Finished", "Career", "", 100);
        Goal back = Goal.fromFileLine(g.toFileLine());
        assertNotNull(back);
        assertTrue(back.isCompleted());
        assertEquals(100, back.getProgress());
    }

    @Test
    @DisplayName("Goal.fromFileLine returns null for malformed lines")
    void goalMalformed() {
        assertNull(Goal.fromFileLine("too|few|parts"));
    }
}
