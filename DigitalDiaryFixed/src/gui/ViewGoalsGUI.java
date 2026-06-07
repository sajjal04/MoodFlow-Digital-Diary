package gui;

import manager.GoalManager;
import model.Goal;
import storage.GoalFileHandler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ViewGoalsGUI extends JFrame {

    private final DashboardGUI dashboard;
    private JPanel cardsPanel;
    private JComboBox<String> filterBox;

    public ViewGoalsGUI(DashboardGUI dashboard) {
        this.dashboard = dashboard;

        setTitle("📊 All Goals");
        setSize(900, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Theme.BACKGROUND);
        main.add(buildHeader(), BorderLayout.NORTH);
        main.add(buildBody(),   BorderLayout.CENTER);

        add(main);
        setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout(15, 0));
        h.setBackground(Theme.HEADER);
        h.setBorder(new EmptyBorder(18, 28, 18, 28));

        JLabel title = new JLabel("📊  All Goals");
        title.setFont(Theme.bold(22));
        title.setForeground(Color.WHITE);

        filterBox = new JComboBox<>(new String[]{"All", "Active", "Completed"});
        filterBox.setFont(Theme.plain(13));
        filterBox.addActionListener(e -> refreshCards());

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        right.add(new JLabel("Filter: ") {{ setForeground(Color.WHITE); setFont(Theme.bold(13)); }});
        right.add(filterBox);

        h.add(title, BorderLayout.WEST);
        h.add(right, BorderLayout.EAST);
        return h;
    }

    private JScrollPane buildBody() {
        cardsPanel = new JPanel();
        cardsPanel.setBackground(Theme.BACKGROUND);
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        cardsPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        refreshCards();

        JScrollPane scroll = new JScrollPane(cardsPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BACKGROUND);
        return scroll;
    }

    private void refreshCards() {
        cardsPanel.removeAll();
        String filter = (String) filterBox.getSelectedItem();
        List<Goal> goals = GoalManager.getInstance().getGoals();

        if (goals.isEmpty()) {
            JLabel empty = new JLabel("No goals yet. Add one to get started! 🚀");
            empty.setFont(Theme.plain(15));
            empty.setForeground(Theme.SECONDARY_TEXT);
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            cardsPanel.add(Box.createVerticalStrut(40));
            cardsPanel.add(empty);
        } else {
            boolean any = false;
            for (int i = goals.size() - 1; i >= 0; i--) {
                Goal g = goals.get(i);
                if ("Active".equals(filter)    && g.isCompleted()) continue;
                if ("Completed".equals(filter) && !g.isCompleted()) continue;
                cardsPanel.add(buildGoalCard(g, i));
                cardsPanel.add(Box.createVerticalStrut(12));
                any = true;
            }
            if (!any) {
                JLabel none = new JLabel("No goals in this category.");
                none.setFont(Theme.plain(14));
                none.setForeground(Theme.SECONDARY_TEXT);
                none.setAlignmentX(Component.CENTER_ALIGNMENT);
                cardsPanel.add(Box.createVerticalStrut(30));
                cardsPanel.add(none);
            }
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private RoundedPanel buildGoalCard(Goal g, int idx) {
        Color cardColor = g.isCompleted() ? new Color(225, 250, 235) : Color.WHITE;
        RoundedPanel card = new RoundedPanel(cardColor, 18);
        card.setLayout(new BorderLayout(10, 8));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Top row
        JPanel top = new JPanel(new BorderLayout(10, 0));
        top.setOpaque(false);

        JLabel name = new JLabel((g.isCompleted() ? "✅ " : "🎯 ") + g.getGoalName());
        name.setFont(Theme.bold(15));
        name.setForeground(Theme.TEXT);

        JLabel cat = new JLabel("  [" + g.getCategory() + "]");
        cat.setFont(Theme.plain(12));
        cat.setForeground(Theme.SECONDARY_TEXT);

        JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        nameRow.setOpaque(false);
        nameRow.add(name);
        nameRow.add(cat);

        top.add(nameRow, BorderLayout.WEST);

        // Action buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        btnRow.setOpaque(false);

        if (!g.isCompleted()) {
            JButton complete = actionBtn("✔ Complete", Theme.BUTTON_SUCCESS);
            complete.addActionListener(e -> {
                GoalManager.getInstance().completeGoal(idx);
                trySave(); refreshCards();
                if (dashboard != null) dashboard.refreshDashboard();
            });
            btnRow.add(complete);
        }

        JButton delete = actionBtn("🗑 Delete", Theme.BUTTON_DANGER);
        delete.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this, "Delete this goal?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                GoalManager.getInstance().deleteGoal(idx);
                trySave(); refreshCards();
                if (dashboard != null) dashboard.refreshDashboard();
            }
        });
        btnRow.add(delete);
        top.add(btnRow, BorderLayout.EAST);

        // Progress row
        JPanel progRow = new JPanel(new BorderLayout(10, 0));
        progRow.setOpaque(false);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(g.getProgress());
        bar.setStringPainted(true);
        bar.setString(g.getProgress() + "%");
        bar.setFont(Theme.bold(11));
        bar.setForeground(g.isCompleted() ? Theme.BUTTON_SUCCESS : Theme.BUTTON);
        bar.setBackground(new Color(230, 225, 245));

        String deadline = g.getDeadline().isEmpty() ? "" : "  📅 Due: " + g.getDeadline();
        JLabel dlLabel = new JLabel(deadline);
        dlLabel.setFont(Theme.plain(12));
        dlLabel.setForeground(Theme.SECONDARY_TEXT);

        progRow.add(bar, BorderLayout.CENTER);
        progRow.add(dlLabel, BorderLayout.EAST);

        card.add(top,     BorderLayout.NORTH);
        card.add(progRow, BorderLayout.CENTER);
        return card;
    }

    private JButton actionBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(Theme.bold(11));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void trySave() {
        try { GoalFileHandler.saveAllGoals(); } catch (Exception ex) { ex.printStackTrace(); }
    }
}
