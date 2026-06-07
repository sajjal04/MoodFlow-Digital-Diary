package gui;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {

    private final Color color;
    private final int   radius;
    private final boolean shadow;

    public RoundedPanel(Color color, int radius) {
        this(color, radius, true);
    }

    public RoundedPanel(Color color, int radius, boolean shadow) {
        this.color  = color;
        this.radius = radius;
        this.shadow = shadow;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        if (shadow) {
            // Soft drop shadow
            for (int i = 4; i >= 1; i--) {
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(i, i, getWidth() - i, getHeight() - i, radius, radius);
            }
        }

        g2.setColor(color);
        g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, radius, radius);
        g2.dispose();

        super.paintComponent(g);
    }
}
