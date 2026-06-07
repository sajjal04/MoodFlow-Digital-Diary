package model;

public class Goal {

    private String goalName;
    private String category;
    private String deadline;   // DD-MM-YYYY or ""
    private int    progress;
    private boolean completed;

    public Goal(String goalName, String category, String deadline, int progress) {
        this.goalName  = goalName;
        this.category  = category;
        this.deadline  = deadline;
        this.progress  = progress;
        this.completed = (progress == 100);
    }

    public void markComplete() {
        progress  = 100;
        completed = true;
    }

    public String  getGoalName()  { return goalName;  }
    public String  getCategory()  { return category;  }
    public String  getDeadline()  { return deadline;  }
    public int     getProgress()  { return progress;  }
    public boolean isCompleted()  { return completed; }

    public void setProgress(int p) {
        this.progress  = p;
        this.completed = (p == 100);
    }

    /** Pipe-delimited line for file persistence. */
    public String toFileLine() {
        String safeName = goalName.replace("|", "\\|");
        String safeCat  = category.replace("|", "\\|");
        return safeName + "|" + safeCat + "|" + deadline + "|" + progress + "|" + completed;
    }

    public static Goal fromFileLine(String line) {
        String[] parts = line.split("(?<!\\\\)\\|", 5);
        if (parts.length < 5) return null;
        String name      = parts[0].trim().replace("\\|", "|");
        String cat       = parts[1].trim().replace("\\|", "|");
        String deadline  = parts[2].trim();
        int    progress  = Integer.parseInt(parts[3].trim());
        boolean completed = Boolean.parseBoolean(parts[4].trim());
        Goal g = new Goal(name, cat, deadline, progress);
        if (completed) g.markComplete();
        return g;
    }

    @Override
    public String toString() {
        return goalName + " [" + category + "] - " + progress + "%"
             + (completed ? " ✓ DONE" : "")
             + (!deadline.isEmpty() ? " | Due: " + deadline : "");
    }
}
