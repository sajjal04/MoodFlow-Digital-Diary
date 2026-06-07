package gui;

import exceptions.EmptyFieldException;
import exceptions.InvalidDateException;
import manager.JournalManager;
import model.JournalEntry;
import storage.FileHandler;
import validation.Validation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class AddEntryGUI extends JFrame {

    private final DashboardGUI dashboard;
    private JTextField txtDate, txtTitle;
    private JComboBox<String> moodBox;
    private JTextArea txtEntry;
    private JLabel moodPreview;

    public AddEntryGUI(DashboardGUI dashboard) {
        this.dashboard = dashboard;

        setTitle("✍️ Add Journal Entry");
        setSize(720, 660);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout(0, 0));
        main.setBackground(Theme.BACKGROUND);

        main.add(buildHeader(), BorderLayout.NORTH);
        main.add(buildForm(),   BorderLayout.CENTER);

        add(main);
        setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(Theme.HEADER);
        h.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("How are you feeling today?");
        title.setFont(Theme.bold(24));
        title.setForeground(Color.WHITE);
        h.add(title, BorderLayout.WEST);
        return h;
    }

    private JPanel buildForm() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(Theme.BACKGROUND);
        wrap.setBorder(new EmptyBorder(25, 30, 25, 30));

        RoundedPanel card = new RoundedPanel(Color.WHITE, 22);
        card.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 15, 10, 15);
        g.fill   = GridBagConstraints.HORIZONTAL;

        // ── Date ──────────────────────────────────────
        g.gridx = 0; g.gridy = 0; g.weightx = 0;
        card.add(makeLabel("📅  Date (DD-MM-YYYY)"), g);
        g.gridx = 1; g.weightx = 1;
        LocalDate today = LocalDate.now();
        txtDate = styledField(String.format("%02d-%02d-%d",
            today.getDayOfMonth(), today.getMonthValue(), today.getYear()));
        card.add(txtDate, g);

        // ── Title ─────────────────────────────────────
        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        card.add(makeLabel("📝  Entry Title"), g);
        g.gridx = 1; g.weightx = 1;
        txtTitle = styledField("");
        card.add(txtTitle, g);

        // ── Mood ──────────────────────────────────────
        g.gridx = 0; g.gridy = 2; g.weightx = 0;
        card.add(makeLabel("💭  Mood"), g);
        g.gridx = 1;
        JPanel moodRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        moodRow.setOpaque(false);
        moodBox = new JComboBox<>(new String[]{"Happy","Excited","Calm","Neutral","Sad","Stressed"});
        moodBox.setFont(Theme.plain(14));
        moodBox.setPreferredSize(new Dimension(160, 34));
        moodPreview = new JLabel("😊 Happy");
        moodPreview.setFont(Theme.bold(14));
        moodPreview.setOpaque(true);
        moodPreview.setBorder(new EmptyBorder(4, 10, 4, 10));
        moodPreview.setBackground(Theme.moodColor("Happy"));
        moodBox.addActionListener(e -> {
            String m = (String) moodBox.getSelectedItem();
            moodPreview.setText(Theme.moodEmoji(m));
            moodPreview.setBackground(Theme.moodColor(m));
        });
        moodRow.add(moodBox);
        moodRow.add(moodPreview);
        card.add(moodRow, g);

        // ── Entry ─────────────────────────────────────
        g.gridx = 0; g.gridy = 3; g.weightx = 0; g.anchor = GridBagConstraints.NORTHWEST;
        card.add(makeLabel("📖  Journal Entry"), g);
        g.gridx = 1; g.weightx = 1; g.weighty = 1; g.fill = GridBagConstraints.BOTH;
        txtEntry = new JTextArea(9, 30);
        txtEntry.setFont(Theme.plain(14));
        txtEntry.setLineWrap(true);
        txtEntry.setWrapStyleWord(true);
        txtEntry.setBorder(new EmptyBorder(8, 10, 8, 10));
        JScrollPane scroll = new JScrollPane(txtEntry);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 190, 230), 1));
        card.add(scroll, g);

        // ── Button ────────────────────────────────────
        g.gridx = 1; g.gridy = 4; g.weighty = 0; g.fill = GridBagConstraints.HORIZONTAL;
        JButton save = styledButton("💾  Save Entry", Theme.BUTTON);
        save.addActionListener(e -> saveEntry());
        card.add(save, g);

        wrap.add(card, BorderLayout.CENTER);
        return wrap;
    }

    private void saveEntry() {
        try {
            String date  = txtDate.getText().trim();
            String title = txtTitle.getText().trim();
            String mood  = (String) moodBox.getSelectedItem();
            String entry = txtEntry.getText().trim();

            Validation.validateEntry(date, title, entry);

            JournalEntry je = new JournalEntry(date, title, mood, entry);
            JournalManager.getInstance().addEntry(je);
            FileHandler.saveAllJournal();

            JOptionPane.showMessageDialog(this,
                "✅  Journal entry saved successfully!", "Saved",
                JOptionPane.INFORMATION_MESSAGE);

            txtTitle.setText("");
            txtEntry.setText("");
            if (dashboard != null) dashboard.refreshDashboard();

        } catch (EmptyFieldException | InvalidDateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error saving entry: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.bold(13));
        l.setForeground(Theme.TEXT);
        return l;
    }

    private JTextField styledField(String def) {
        JTextField f = new JTextField(def);
        f.setFont(Theme.plain(14));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 190, 230)),
            new EmptyBorder(6, 10, 6, 10)));
        return f;
    }

    private JButton styledButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(Theme.bold(14));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(0, 42));
        return b;
    }
}
