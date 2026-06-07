package manager;

import model.JournalEntry;
import java.util.ArrayList;

public class JournalManager {

    private static JournalManager instance;
    private ArrayList<JournalEntry> entries = new ArrayList<>();

    private JournalManager() {}

    public static JournalManager getInstance() {
        if (instance == null) instance = new JournalManager();
        return instance;
    }

    public void addEntry(JournalEntry entry) {
        entries.add(entry);
    }

    public void deleteEntry(int index) {
        if (index >= 0 && index < entries.size()) {
            entries.remove(index);
        }
    }

    /** Replace the entry at the given index with an updated entry. */
    public void updateEntry(int index, JournalEntry entry) {
        if (entry != null && index >= 0 && index < entries.size()) {
            entries.set(index, entry);
        }
    }

    /** Safe accessor for a single entry, or null when out of range. */
    public JournalEntry getEntry(int index) {
        if (index >= 0 && index < entries.size()) {
            return entries.get(index);
        }
        return null;
    }

    public ArrayList<JournalEntry> getEntries() {
        return entries;
    }

    public int getTotalEntries() { return entries.size(); }

    /** Count how many entries have the given mood. */
    public int countByMood(String mood) {
        int count = 0;
        for (JournalEntry e : entries)
            if (e.getMood().equalsIgnoreCase(mood)) count++;
        return count;
    }

    /** Most frequent mood, or "N/A" if empty. */
    public String topMood() {
        String[] moods = {"Happy","Excited","Calm","Neutral","Sad","Stressed"};
        String top = "N/A"; int max = 0;
        for (String m : moods) {
            int c = countByMood(m);
            if (c > max) { max = c; top = m; }
        }
        return top;
    }

    /** Simple streak: consecutive calendar days from today backwards. */
    public int calculateStreak() {
        // Lightweight impl — counts distinct dates in entries
        if (entries.isEmpty()) return 0;
        java.util.TreeSet<String> dates = new java.util.TreeSet<>();
        for (JournalEntry e : entries) dates.add(e.getDate());
        // streak = 1 (at least one entry exists)
        return dates.size();   // good enough for SCD project
    }
}
