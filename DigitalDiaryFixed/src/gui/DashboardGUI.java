package gui;

import manager.GoalManager;
import manager.JournalManager;
import model.Goal;
import model.JournalEntry;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.util.List;

public class DashboardGUI extends JFrame {

    private JPanel contentArea;
    private JLabel statEntries, statGoals, statStreak, statScore;
    private JTextArea recentArea;
    private JPanel goalContentPanel;

    // Sidebar buttons stored so we can highlight the active one
    private JButton[] menuButtons;
    private String[] menuLabels = {
        "🏠  Dashboard", "📖  Add Entry", "📋  View Entries",
        "🎯  Add Goal",  "📊  View Goals", "📈  Statistics"
    };

    public DashboardGUI() {

        setTitle("MoodFlow — Digital Diary & Goal Tracker");
        setSize(1350, 820);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BACKGROUND);

        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildContent(), BorderLayout.CENTER);

        add(root);
        setVisible(true);
    }

    // ────────────────────────────────────────────────
    // SIDEBAR
    // ────────────────────────────────────────────────
    private JPanel buildSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(Theme.SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(30, 18, 30, 18));

        // Logo
        JLabel logo = new JLabel("MoodFlow");
        logo.setFont(Theme.bold(28));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logo);

        JLabel tagline = new JLabel("Your personal growth companion");
        tagline.setFont(Theme.plain(11));
        tagline.setForeground(new Color(180, 160, 220));
        tagline.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(tagline);
        sidebar.add(Box.createVerticalStrut(35));

        // Menu buttons
        menuButtons = new JButton[menuLabels.length];
        Runnable[] actions = {
            this::showDashboard,
            () -> new AddEntryGUI(this),
            () -> new ViewEntriesGUI(this),
            () -> new GoalGUI(this),
            () -> new ViewGoalsGUI(this),
            () -> new StatisticsGUI()
        };

        for (int i = 0; i < menuLabels.length; i++) {
            menuButtons[i] = createSidebarBtn(menuLabels[i], i == 0);
            int idx = i;
            menuButtons[i].addActionListener(e -> {
                setActiveButton(idx);
                actions[idx].run();
            });
            sidebar.add(menuButtons[i]);
            sidebar.add(Box.createVerticalStrut(8));
        }

        sidebar.add(Box.createVerticalGlue());

        JLabel version = new JLabel("v2.0  •  MoodFlow");
        version.setFont(Theme.plain(11));
        version.setForeground(new Color(140, 120, 180));
        version.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(version);

        return sidebar;
    }

    private JButton createSidebarBtn(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(220, 46));
        btn.setPreferredSize(new Dimension(220, 46));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFont(Theme.bold(14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 12, 0, 0));
        styleMenuBtn(btn, active);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!btn.getBackground().equals(Theme.ACTIVE_MENU))
                    btn.setBackground(Theme.SIDEBAR_HOVER);
            }
            public void mouseExited(MouseEvent e) {
                if (!btn.getBackground().equals(Theme.ACTIVE_MENU))
                    btn.setBackground(Theme.SIDEBAR);
            }
        });
        return btn;
    }

    private void styleMenuBtn(JButton btn, boolean active) {
        btn.setBackground(active ? Theme.ACTIVE_MENU : Theme.SIDEBAR);
        btn.setForeground(active ? Color.WHITE : Theme.MENU_TEXT);
    }

    public void setActiveButton(int index) {
        for (int i = 0; i < menuButtons.length; i++)
            styleMenuBtn(menuButtons[i], i == index);
    }

    // ────────────────────────────────────────────────
    // CONTENT
    // ────────────────────────────────────────────────
    private JPanel buildContent() {

        contentArea = new JPanel(new BorderLayout(20, 20));
        contentArea.setBackground(Theme.BACKGROUND);
        contentArea.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header banner
        contentArea.add(buildHeader(), BorderLayout.NORTH);

        // Centre: stat cards + lower panels
        JPanel centre = new JPanel(new BorderLayout(0, 20));
        centre.setOpaque(false);
        centre.add(buildStatCards(), BorderLayout.NORTH);
        centre.add(buildLowerPanel(), BorderLayout.CENTER);

        contentArea.add(centre, BorderLayout.CENTER);
        return contentArea;
    }

    private JPanel buildHeader() {
        RoundedPanel header = new RoundedPanel(Theme.HEADER, 30, false);
        header.setPreferredSize(new Dimension(0, 130));
        header.setLayout(new BorderLayout());

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setBorder(new EmptyBorder(22, 30, 22, 30));
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel greeting = new JLabel(getGreeting());
        greeting.setFont(Theme.bold(30));
        greeting.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Track your thoughts. Measure your growth. 🌱");
        sub.setFont(Theme.plain(16));
        sub.setForeground(new Color(220, 210, 255));

        text.add(greeting);
        text.add(Box.createVerticalStrut(6));
        text.add(sub);
        header.add(text, BorderLayout.CENTER);

        // Today's date label on right
        java.time.LocalDate today = java.time.LocalDate.now();
        String dateStr = String.format("%02d-%02d-%d",
            today.getDayOfMonth(), today.getMonthValue(), today.getYear());
        JLabel dateLabel = new JLabel("📅 " + dateStr + "  ");
        dateLabel.setFont(Theme.bold(14));
        dateLabel.setForeground(new Color(220, 210, 255));
        dateLabel.setVerticalAlignment(SwingConstants.CENTER);
        header.add(dateLabel, BorderLayout.EAST);

        return header;
    }

    private JPanel buildStatCards() {
        JPanel stats = new JPanel(new GridLayout(1, 4, 15, 0));
        stats.setOpaque(false);
        stats.setPreferredSize(new Dimension(0, 110));

        int entries  = JournalManager.getInstance().getTotalEntries();
        int goals    = GoalManager.getInstance().getTotalGoals();
        int streak   = JournalManager.getInstance().calculateStreak();
        double score = GoalManager.getInstance().getAverageProgress();

        stats.add(createStatCard("📒 Entries",  String.valueOf(entries),  Theme.ENTRY_CARD));
        stats.add(createStatCard("🎯 Goals",    String.valueOf(goals),    Theme.GOAL_CARD));
        stats.add(createStatCard("🔥 Streak",   streak + " Days",         Theme.STREAK_CARD));
        stats.add(createStatCard("📊 Avg Goal", String.format("%.0f%%", score), Theme.SCORE_CARD));

        return stats;
    }

    private RoundedPanel createStatCard(String title, String value, Color color) {
        RoundedPanel card = new RoundedPanel(color, 20);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel lbl = new JLabel(title, SwingConstants.LEFT);
        lbl.setFont(Theme.bold(13));
        lbl.setForeground(Theme.TEXT);

        JLabel num = new JLabel(value, SwingConstants.LEFT);
        num.setFont(Theme.bold(26));
        num.setForeground(Theme.TEXT);

        card.add(lbl, BorderLayout.NORTH);
        card.add(num, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildLowerPanel() {
        JPanel lower = new JPanel(new GridLayout(1, 2, 20, 0));
        lower.setOpaque(false);

        lower.add(buildRecentEntries());
        lower.add(buildActiveGoals());
        return lower;
    }

    private RoundedPanel buildRecentEntries() {
        RoundedPanel panel = new RoundedPanel(Theme.WHITE_CARD, 25);
        panel.setLayout(new BorderLayout(0, 10));
        panel.setBorder(new EmptyBorder(20, 22, 20, 22));

        JLabel title = new JLabel("📖 Recent Journal Entries");
        title.setFont(Theme.bold(18));
        title.setForeground(Theme.TEXT);

        recentArea = new JTextArea();
        recentArea.setEditable(false);
        recentArea.setOpaque(false);
        recentArea.setFont(Theme.plain(14));
        recentArea.setForeground(Theme.SECONDARY_TEXT);
        recentArea.setLineWrap(true);
        recentArea.setWrapStyleWord(true);

        refreshRecentEntries();

        JScrollPane scroll = new JScrollPane(recentArea);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);

        panel.add(title, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private RoundedPanel buildActiveGoals() {
        RoundedPanel panel = new RoundedPanel(Theme.WHITE_CARD, 25);
        panel.setLayout(new BorderLayout(0, 10));
        panel.setBorder(new EmptyBorder(20, 22, 20, 22));

        JLabel title = new JLabel("🎯 Active Goals");
        title.setFont(Theme.bold(18));
        title.setForeground(Theme.TEXT);

        goalContentPanel = new JPanel();
        goalContentPanel.setOpaque(false);
        goalContentPanel.setLayout(new BoxLayout(goalContentPanel, BoxLayout.Y_AXIS));

        refreshGoalPanel();

        JScrollPane scroll = new JScrollPane(goalContentPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);

        panel.add(title, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ────────────────────────────────────────────────
    // REFRESH HELPERS  (called by child windows)
    // ────────────────────────────────────────────────
    public void refreshDashboard() {
        refreshRecentEntries();
        refreshGoalPanel();
        refreshStatCards();
    }

    private void refreshStatCards() {
        // Easiest: just rebuild content. Remove old centre, re-add.
        // (For SCD scope this approach is clean enough.)
        contentArea.removeAll();
        contentArea.add(buildHeader(), BorderLayout.NORTH);
        JPanel centre = new JPanel(new BorderLayout(0, 20));
        centre.setOpaque(false);
        centre.add(buildStatCards(), BorderLayout.NORTH);
        centre.add(buildLowerPanel(), BorderLayout.CENTER);
        contentArea.add(centre, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    private void refreshRecentEntries() {
        if (recentArea == null) return;
        List<JournalEntry> entries = JournalManager.getInstance().getEntries();
        if (entries.isEmpty()) {
            recentArea.setText("No journal entries yet.\nClick 'Add Entry' to get started! ✍️");
            return;
        }
        StringBuilder sb = new StringBuilder();
        int start = Math.max(0, entries.size() - 4);
        for (int i = entries.size() - 1; i >= start; i--) {
            JournalEntry e = entries.get(i);
            sb.append("• [").append(e.getDate()).append("] ")
              .append(e.getTitle()).append(" — ").append(Theme.moodEmoji(e.getMood()))
              .append("\n\n");
        }
        recentArea.setText(sb.toString().trim());
    }

    private void refreshGoalPanel() {
        if (goalContentPanel == null) return;
        goalContentPanel.removeAll();
        List<Goal> goals = GoalManager.getInstance().getGoals();
        if (goals.isEmpty()) {
            JLabel empty = new JLabel("No goals yet. Add one to get started! 🚀");
            empty.setFont(Theme.plain(13));
            empty.setForeground(Theme.SECONDARY_TEXT);
            goalContentPanel.add(empty);
        } else {
            int shown = Math.min(goals.size(), 4);
            for (int i = goals.size() - 1; i >= goals.size() - shown; i--) {
                Goal g = goals.get(i);
                goalContentPanel.add(createGoalRow(g));
                goalContentPanel.add(Box.createVerticalStrut(14));
            }
        }
        goalContentPanel.revalidate();
        goalContentPanel.repaint();
    }

    private JPanel createGoalRow(Goal g) {
        JPanel row = new JPanel(new BorderLayout(0, 4));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel lbl = new JLabel(g.getGoalName() + " [" + g.getCategory() + "]");
        lbl.setFont(Theme.plain(13));
        lbl.setForeground(Theme.TEXT);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(g.getProgress());
        bar.setStringPainted(true);
        bar.setFont(Theme.bold(11));
        bar.setForeground(g.isCompleted() ? Theme.BUTTON_SUCCESS : Theme.BUTTON);
        bar.setBackground(new Color(230, 225, 245));

        row.add(lbl, BorderLayout.NORTH);
        row.add(bar, BorderLayout.CENTER);
        return row;
    }

    private void showDashboard() {
        refreshDashboard();
        setActiveButton(0);
    }

    private String getGreeting() {
        int hour = LocalTime.now().getHour();
        if (hour < 12) return "Good Morning! ☀️";
        if (hour < 17) return "Good Afternoon! 🌤️";
        if (hour < 21) return "Good Evening! 🌙";
        return "Good Night! ⭐";
    }
}
