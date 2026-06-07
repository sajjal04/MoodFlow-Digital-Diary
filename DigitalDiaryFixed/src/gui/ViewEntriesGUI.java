package gui;

import manager.JournalManager;
import model.JournalEntry;
import storage.FileHandler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ViewEntriesGUI extends JFrame {

    private final DashboardGUI dashboard;
    private JPanel cardsPanel;
    private JTextField searchField;

    public ViewEntriesGUI(DashboardGUI dashboard) {
        this.dashboard = dashboard;

        setTitle("📋 Journal Entries");
        setSize(900, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout(0, 0));
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

        JLabel title = new JLabel("📋  All Journal Entries");
        title.setFont(Theme.bold(22));
        title.setForeground(Color.WHITE);

        searchField = new JTextField();
        searchField.setFont(Theme.plain(13));
        searchField.setPreferredSize(new Dimension(220, 34));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 160, 230)),
            new EmptyBorder(4, 10, 4, 10)));
        searchField.putClientProperty("JTextField.placeholderText", "Search entries…");
        searchField.addActionListener(e -> refreshCards(searchField.getText().trim()));

        JButton searchBtn = new JButton("🔍 Search");
        searchBtn.setFont(Theme.bold(12));
        searchBtn.setBackground(Theme.BUTTON_SUCCESS);
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setBorderPainted(false);
        searchBtn.addActionListener(e -> refreshCards(searchField.getText().trim()));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        right.add(searchField);
        right.add(searchBtn);

        h.add(title, BorderLayout.WEST);
        h.add(right, BorderLayout.EAST);
        return h;
    }

    private JScrollPane buildBody() {
        cardsPanel = new JPanel();
        cardsPanel.setBackground(Theme.BACKGROUND);
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        cardsPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        refreshCards("");

        JScrollPane scroll = new JScrollPane(cardsPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BACKGROUND);
        return scroll;
    }

    private void refreshCards(String filter) {
        cardsPanel.removeAll();
        List<JournalEntry> entries = JournalManager.getInstance().getEntries();

        if (entries.isEmpty()) {
            JLabel empty = new JLabel("No journal entries found. Start writing! ✍️");
            empty.setFont(Theme.plain(15));
            empty.setForeground(Theme.SECONDARY_TEXT);
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            cardsPanel.add(Box.createVerticalStrut(40));
            cardsPanel.add(empty);
        } else {
            boolean any = false;
            for (int i = entries.size() - 1; i >= 0; i--) {
                JournalEntry e = entries.get(i);
                if (!filter.isEmpty() &&
                    !e.getTitle().toLowerCase().contains(filter.toLowerCase()) &&
                    !e.getEntry().toLowerCase().contains(filter.toLowerCase()) &&
                    !e.getMood().toLowerCase().contains(filter.toLowerCase())) continue;
                cardsPanel.add(buildEntryCard(e, i));
                cardsPanel.add(Box.createVerticalStrut(12));
                any = true;
            }
            if (!any) {
                JLabel none = new JLabel("No entries match \"" + filter + "\".");
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

    private RoundedPanel buildEntryCard(JournalEntry e, int idx) {
        RoundedPanel card = new RoundedPanel(Color.WHITE, 18);
        card.setLayout(new BorderLayout(10, 8));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        // Top row
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel title = new JLabel(e.getTitle());
        title.setFont(Theme.bold(15));
        title.setForeground(Theme.TEXT);

        JLabel mood = new JLabel(Theme.moodEmoji(e.getMood()));
        mood.setFont(Theme.bold(13));
        mood.setOpaque(true);
        mood.setBackground(Theme.moodColor(e.getMood()));
        mood.setBorder(new EmptyBorder(3, 10, 3, 10));
        mood.setForeground(Theme.TEXT);

        JLabel date = new JLabel("📅 " + e.getDate());
        date.setFont(Theme.plain(12));
        date.setForeground(Theme.SECONDARY_TEXT);

        JPanel topLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topLeft.setOpaque(false);
        topLeft.add(title);
        topLeft.add(mood);
        topLeft.add(date);

        top.add(topLeft, BorderLayout.WEST);

        // Delete button
        JButton del = new JButton("🗑 Delete");
        del.setFont(Theme.bold(11));
        del.setBackground(Theme.BUTTON_DANGER);
        del.setForeground(Color.WHITE);
        del.setFocusPainted(false);
        del.setBorderPainted(false);
        del.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        del.addActionListener(ev -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete this journal entry?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                JournalManager.getInstance().deleteEntry(idx);
                try { FileHandler.saveAllJournal(); } catch (Exception ex) { ex.printStackTrace(); }
                refreshCards(searchField.getText().trim());
                if (dashboard != null) dashboard.refreshDashboard();
            }
        });
        top.add(del, BorderLayout.EAST);

        // Body text
        JTextArea body = new JTextArea(e.getEntry());
        body.setFont(Theme.plain(13));
        body.setForeground(Theme.SECONDARY_TEXT);
        body.setEditable(false);
        body.setOpaque(false);
        body.setLineWrap(true);
        body.setWrapStyleWord(true);
        body.setRows(2);

        card.add(top,  BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);
        return card;
    }
}
