package model;

public class JournalEntry {

    private String date;
    private String title;
    private String mood;
    private String entry;

    public JournalEntry(String date, String title, String mood, String entry) {
        this.date  = date;
        this.title = title;
        this.mood  = mood;
        this.entry = entry;
    }

    public String getDate()  { return date;  }
    public String getTitle() { return title; }
    public String getMood()  { return mood;  }
    public String getEntry() { return entry; }

    /** Pipe-delimited line used for file persistence (no newlines in fields). */
    public String toFileLine() {
        // Escape any pipe chars in user text so parsing stays safe
        String safeTitle = title.replace("|", "\\|");
        String safeMood  = mood .replace("|", "\\|");
        String safeEntry = entry.replace("|", "\\|").replace("\n", "\\n");
        return date + "|" + safeTitle + "|" + safeMood + "|" + safeEntry;
    }

    /** Build from a file line produced by toFileLine(). */
    public static JournalEntry fromFileLine(String line) {
        String[] parts = line.split("(?<!\\\\)\\|", 4);
        if (parts.length < 4) return null;
        String date  = parts[0].trim();
        String title = parts[1].trim().replace("\\|", "|");
        String mood  = parts[2].trim().replace("\\|", "|");
        String entry = parts[3].trim().replace("\\|", "|").replace("\\n", "\n");
        return new JournalEntry(date, title, mood, entry);
    }

    @Override
    public String toString() {
        return "Date:  " + date  + "\n"
             + "Title: " + title + "\n"
             + "Mood:  " + mood  + "\n"
             + "Entry: " + entry + "\n"
             + "─────────────────────────────\n";
    }
}
