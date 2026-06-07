package gui;

import manager.GoalManager;
import manager.JournalManager;
import model.Goal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class StatisticsGUI extends JFrame {

    public StatisticsGUI() {

        setTitle("📈 Statistics Dashboard");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Theme.BACKGROUND);
        main.add(buildHeader(),  BorderLayout.NORTH);
        main.add(buildBody(),    BorderLayout.CENTER);

        add(main);
        setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(Theme.HEADER);
        h.setBorder(new EmptyBorder(20, 30, 20, 30));
        JLabel lbl = new JLabel("📈  Statistics Dashboard");
        lbl.setFont(Theme.bold(22));
        lbl.setForeground(Color.WHITE);
        h.add(lbl, BorderLayout.WEST);
        return h;
    }

    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout(20, 20));
        body.setBackground(Theme.BACKGROUND);
        body.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Top stat cards
        body.add(buildStatCards(), BorderLayout.NORTH);

        // Lower: mood breakdown + goal status
        JPanel lower = new JPanel(new GridLayout(1, 2, 20, 0));
        lower.setOpaque(false);
        lower.add(buildMoodPanel());
        lower.add(buildGoalStatusPanel());
        body.add(lower, BorderLayout.CENTER);

        return body;
    }

    private JPanel buildStatCards() {
        JPanel p = new JPanel(new GridLayout(1, 4, 15, 0));
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(0, 105));

        int entries   = JournalManager.getInstance().getTotalEntries();
        int goals     = GoalManager.getInstance().getTotalGoals();
        int completed = GoalManager.getInstance().getCompletedGoals();
        double avg    = GoalManager.getInstance().getAverageProgress();

        p.add(statCard("📒 Total Entries",    String.valueOf(entries),              Theme.ENTRY_CARD));
        p.add(statCard("🎯 Total Goals",       String.valueOf(goals),                Theme.GOAL_CARD));
        p.add(statCard("✅ Completed Goals",   completed + " / " + goals,           Theme.STREAK_CARD));
        p.add(statCard("📊 Avg Progress",      String.format("%.1f%%", avg),        Theme.SCORE_CARD));
        return p;
    }

    private RoundedPanel statCard(String title, String value, Color color) {
        RoundedPanel c = new RoundedPanel(color, 20);
        c.setLayout(new BorderLayout());
        c.setBorder(new EmptyBorder(12, 16, 12, 16));
        JLabel t = new JLabel(title); t.setFont(Theme.bold(12)); t.setForeground(Theme.TEXT);
        JLabel v = new JLabel(value); v.setFont(Theme.bold(24)); v.setForeground(Theme.TEXT);
        c.add(t, BorderLayout.NORTH);
        c.add(v, BorderLayout.CENTER);
        return c;
    }

    // ─── Mood Breakdown ───────────────────────────────────────────────────────
    private RoundedPanel buildMoodPanel() {
        RoundedPanel p = new RoundedPanel(Color.WHITE, 22);
        p.setLayout(new BorderLayout(0, 10));
        p.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel title = new JLabel("😊  Mood Breakdown");
        title.setFont(Theme.bold(17));
        title.setForeground(Theme.TEXT);

        JPanel rows = new JPanel();
        rows.setOpaque(false);
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));

        String[] moods = {"Happy","Excited","Calm","Neutral","Sad","Stressed"};
        int total = JournalManager.getInstance().getTotalEntries();
        if (total == 0) {
            rows.add(new JLabel("No entries yet.") {{ setFont(Theme.plain(13)); setForeground(Theme.SECONDARY_TEXT); }});
        } else {
            for (String m : moods) {
                int count = JournalManager.getInstance().countByMood(m);
                int pct   = (int) ((count / (double) total) * 100);
                rows.add(buildMoodRow(m, count, pct));
                rows.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scroll = new JScrollPane(rows);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        p.add(title,  BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildMoodRow(String mood, int count, int pct) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        JLabel lbl = new JLabel(Theme.moodEmoji(mood));
        lbl.setFont(Theme.plain(13));
        lbl.setForeground(Theme.TEXT);
        lbl.setPreferredSize(new Dimension(110, 24));

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(pct);
        bar.setString(count + " (" + pct + "%)");
        bar.setStringPainted(true);
        bar.setFont(Theme.plain(11));
        bar.setForeground(Theme.moodColor(mood));
        bar.setBackground(new Color(235, 230, 248));

        row.add(lbl, BorderLayout.WEST);
        row.add(bar, BorderLayout.CENTER);
        return row;
    }

    // ─── Goal Status Panel ────────────────────────────────────────────────────
    private RoundedPanel buildGoalStatusPanel() {
        RoundedPanel p = new RoundedPanel(Color.WHITE, 22);
        p.setLayout(new BorderLayout(0, 10));
        p.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel title = new JLabel("🎯  Goals by Category");
        title.setFont(Theme.bold(17));
        title.setForeground(Theme.TEXT);

        JPanel rows = new JPanel();
        rows.setOpaque(false);
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));

        List<Goal> goals = GoalManager.getInstance().getGoals();
        if (goals.isEmpty()) {
            rows.add(new JLabel("No goals yet.") {{ setFont(Theme.plain(13)); setForeground(Theme.SECONDARY_TEXT); }});
        } else {
            // Group by category
            Map<String, int[]> catMap = new LinkedHashMap<>();
            for (Goal g : goals) {
                catMap.computeIfAbsent(g.getCategory(), k -> new int[]{0, 0});
                catMap.get(g.getCategory())[0]++;
                catMap.get(g.getCategory())[1] += g.getProgress();
            }
            for (Map.Entry<String, int[]> e : catMap.entrySet()) {
                int count  = e.getValue()[0];
                int avgPct = e.getValue()[1] / count;
                JPanel row = new JPanel(new BorderLayout(8, 0));
                row.setOpaque(false);
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

                JLabel lbl = new JLabel(e.getKey());
                lbl.setFont(Theme.plain(12));
                lbl.setForeground(Theme.TEXT);
                lbl.setPreferredSize(new Dimension(130, 24));

                JProgressBar bar = new JProgressBar(0, 100);
                bar.setValue(avgPct);
                bar.setString(count + " goals · " + avgPct + "% avg");
                bar.setStringPainted(true);
                bar.setFont(Theme.plain(11));
                bar.setForeground(Theme.BUTTON);
                bar.setBackground(new Color(235, 230, 248));

                row.add(lbl, BorderLayout.WEST);
                row.add(bar, BorderLayout.CENTER);
                rows.add(row);
                rows.add(Box.createVerticalStrut(10));
            }
        }

        // Productivity score
        int entries = JournalManager.getInstance().getTotalEntries();
        double avgP = GoalManager.getInstance().getAverageProgress();
        double score = Math.min(100, (entries * 5) + (avgP * 0.5));
        JPanel scoreRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        scoreRow.setOpaque(false);
        JLabel scoreLbl = new JLabel("⭐ Productivity Score:  " + String.format("%.1f", score) + " / 100");
        scoreLbl.setFont(Theme.bold(14));
        scoreLbl.setForeground(Theme.BUTTON);
        scoreRow.add(scoreLbl);

        JScrollPane scroll = new JScrollPane(rows);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        p.add(title,    BorderLayout.NORTH);
        p.add(scroll,   BorderLayout.CENTER);
        p.add(scoreRow, BorderLayout.SOUTH);
        return p;
    }
}
