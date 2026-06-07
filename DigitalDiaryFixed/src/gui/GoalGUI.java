package gui;

import exceptions.EmptyFieldException;
import exceptions.InvalidDateException;
import manager.GoalManager;
import model.Goal;
import storage.GoalFileHandler;
import validation.GoalValidation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GoalGUI extends JFrame {

    private final DashboardGUI dashboard;
    private JTextField txtGoal, txtDeadline;
    private JComboBox<String> categoryBox;
    private JSlider progressSlider;
    private JLabel progressLabel;

    public GoalGUI(DashboardGUI dashboard) {
        this.dashboard = dashboard;

        setTitle("🎯 Add New Goal");
        setSize(700, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
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
        JLabel lbl = new JLabel("Set a New Goal 🚀");
        lbl.setFont(Theme.bold(22));
        lbl.setForeground(Color.WHITE);
        h.add(lbl, BorderLayout.WEST);
        return h;
    }

    private JPanel buildForm() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(Theme.BACKGROUND);
        wrap.setBorder(new EmptyBorder(25, 30, 25, 30));

        RoundedPanel card = new RoundedPanel(Color.WHITE, 22);
        card.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets  = new Insets(12, 15, 12, 15);
        g.fill    = GridBagConstraints.HORIZONTAL;
        g.weightx = 0;

        // Goal name
        g.gridx = 0; g.gridy = 0;
        card.add(label("🎯  Goal Name"), g);
        g.gridx = 1; g.weightx = 1;
        txtGoal = field();
        card.add(txtGoal, g);

        // Category
        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        card.add(label("🏷️  Category"), g);
        g.gridx = 1; g.weightx = 1;
        categoryBox = new JComboBox<>(new String[]{
            "Personal", "Health & Fitness", "Education",
            "Career", "Finance", "Relationships", "Hobbies", "Other"
        });
        categoryBox.setFont(Theme.plain(14));
        card.add(categoryBox, g);

        // Deadline
        g.gridx = 0; g.gridy = 2; g.weightx = 0;
        card.add(label("📅  Deadline (DD-MM-YYYY)"), g);
        g.gridx = 1; g.weightx = 1;
        txtDeadline = field();
        txtDeadline.setText("(optional)");
        txtDeadline.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtDeadline.getText().equals("(optional)")) txtDeadline.setText("");
            }
        });
        card.add(txtDeadline, g);

        // Progress slider
        g.gridx = 0; g.gridy = 3; g.weightx = 0;
        card.add(label("📊  Initial Progress"), g);
        g.gridx = 1; g.weightx = 1;
        progressSlider = new JSlider(0, 100, 0);
        progressSlider.setMajorTickSpacing(25);
        progressSlider.setPaintTicks(true);
        progressSlider.setPaintLabels(true);
        progressSlider.setOpaque(false);
        progressLabel = new JLabel("0%");
        progressLabel.setFont(Theme.bold(16));
        progressLabel.setForeground(Theme.BUTTON);
        progressSlider.addChangeListener(e ->
            progressLabel.setText(progressSlider.getValue() + "%"));
        JPanel sliderRow = new JPanel(new BorderLayout(10, 0));
        sliderRow.setOpaque(false);
        sliderRow.add(progressSlider, BorderLayout.CENTER);
        sliderRow.add(progressLabel, BorderLayout.EAST);
        card.add(sliderRow, g);

        // Save button
        g.gridx = 1; g.gridy = 4;
        JButton save = btn("💾  Save Goal", Theme.BUTTON);
        save.addActionListener(e -> saveGoal());
        card.add(save, g);

        wrap.add(card, BorderLayout.CENTER);
        return wrap;
    }

    private void saveGoal() {
        try {
            String name     = txtGoal.getText().trim();
            String category = (String) categoryBox.getSelectedItem();
            String deadline = txtDeadline.getText().trim();
            if (deadline.equals("(optional)")) deadline = "";
            int progress    = progressSlider.getValue();

            GoalValidation.validateGoal(name, deadline);

            Goal goal = new Goal(name, category, deadline, progress);
            GoalManager.getInstance().addGoal(goal);
            GoalFileHandler.saveAllGoals();

            JOptionPane.showMessageDialog(this,
                "✅  Goal saved successfully!", "Saved",
                JOptionPane.INFORMATION_MESSAGE);

            txtGoal.setText("");
            txtDeadline.setText("(optional)");
            progressSlider.setValue(0);
            if (dashboard != null) dashboard.refreshDashboard();

        } catch (EmptyFieldException | InvalidDateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setFont(Theme.bold(13));
        l.setForeground(Theme.TEXT);
        return l;
    }

    private JTextField field() {
        JTextField f = new JTextField();
        f.setFont(Theme.plain(14));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 190, 230)),
            new EmptyBorder(6, 10, 6, 10)));
        return f;
    }

    private JButton btn(String text, Color bg) {
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
