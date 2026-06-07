package gui;

import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;


public final class IconFactory {

    public static final int DEFAULT_SIZE = 16;

    private IconFactory() { /* utility class — no instances */ }

    // ──────────────────────────────────────────────────────────────────
    //  Public factory methods (one per action)
    // ──────────────────────────────────────────────────────────────────

    public static ImageIcon edit()      { return edit(DEFAULT_SIZE, Color.WHITE); }
    public static ImageIcon delete()    { return delete(DEFAULT_SIZE, Color.WHITE); }
    public static ImageIcon search()    { return search(DEFAULT_SIZE, Color.WHITE); }
    public static ImageIcon add()       { return add(DEFAULT_SIZE, Color.WHITE); }
    public static ImageIcon save()      { return save(DEFAULT_SIZE, Color.WHITE); }
    public static ImageIcon update()    { return update(DEFAULT_SIZE, Color.WHITE); }
    public static ImageIcon back()      { return back(DEFAULT_SIZE, Color.WHITE); }
    public static ImageIcon complete()  { return complete(DEFAULT_SIZE, Color.WHITE); }

    // Navigation / dashboard icons (tinted for the light sidebar by callers)
    public static ImageIcon home()      { return home(DEFAULT_SIZE, Color.WHITE); }
    public static ImageIcon book()      { return book(DEFAULT_SIZE, Color.WHITE); }
    public static ImageIcon list()      { return list(DEFAULT_SIZE, Color.WHITE); }
    public static ImageIcon target()    { return target(DEFAULT_SIZE, Color.WHITE); }
    public static ImageIcon chart()     { return chart(DEFAULT_SIZE, Color.WHITE); }
    public static ImageIcon stats()     { return stats(DEFAULT_SIZE, Color.WHITE); }

    // ──────────────────────────────────────────────────────────────────
    //  Drawing implementations
    // ──────────────────────────────────────────────────────────────────

    /** Pencil icon for "Edit". */
    public static ImageIcon edit(int size, Color color) {
        return draw(size, color, (g, s, st) -> {
            // Draw a clean diagonal pencil: body rectangle + nib triangle,
            // built in a local "pencil" coordinate space then rotated 45°.
            Graphics2D g2 = (Graphics2D) g.create();
            g2.translate(s * 0.5, s * 0.5);
            g2.rotate(Math.toRadians(45));
            double len = s * 0.66;        // total pencil length
            double w   = s * 0.20;        // pencil width
            double nib = s * 0.16;        // nib (tip) length
            double half = w / 2;
            double bodyStart = -len / 2;          // eraser end
            double bodyEnd   =  len / 2 - nib;    // where nib begins
            double tipX      =  len / 2;          // sharp point

            // pencil body outline
            g2.draw(new Rectangle2D.Double(bodyStart, -half, bodyEnd - bodyStart, w));
            // metal band near the eraser
            g2.draw(new Line2D.Double(bodyStart + len * 0.22, -half,
                                      bodyStart + len * 0.22,  half));
            // nib (filled triangle)
            Path2D tip = new Path2D.Double();
            tip.moveTo(bodyEnd, -half);
            tip.lineTo(tipX, 0);
            tip.lineTo(bodyEnd, half);
            tip.closePath();
            g2.fill(tip);
            g2.dispose();
        });
    }

    /** Trash-can icon for "Delete". */
    public static ImageIcon delete(int size, Color color) {
        return draw(size, color, (g, s, st) -> {
            g.setStroke(st);
            double left = s * 0.26, right = s * 0.74;
            double top = s * 0.30, bottom = s * 0.82;
            // lid
            g.draw(new Line2D.Double(s * 0.20, top, s * 0.80, top));
            // handle
            g.draw(new Line2D.Double(s * 0.40, top, s * 0.40, s * 0.20));
            g.draw(new Line2D.Double(s * 0.60, top, s * 0.60, s * 0.20));
            g.draw(new Line2D.Double(s * 0.40, s * 0.20, s * 0.60, s * 0.20));
            // can body
            g.draw(new Line2D.Double(left, top, left + s * 0.04, bottom));
            g.draw(new Line2D.Double(right, top, right - s * 0.04, bottom));
            g.draw(new Line2D.Double(left + s * 0.04, bottom, right - s * 0.04, bottom));
            // vertical ribs
            g.draw(new Line2D.Double(s * 0.42, top + s * 0.08, s * 0.42, bottom - s * 0.06));
            g.draw(new Line2D.Double(s * 0.58, top + s * 0.08, s * 0.58, bottom - s * 0.06));
        });
    }

    /** Magnifying-glass icon for "Search". */
    public static ImageIcon search(int size, Color color) {
        return draw(size, color, (g, s, st) -> {
            g.setStroke(st);
            double d = s * 0.46;
            double x = s * 0.18, y = s * 0.18;
            g.draw(new Ellipse2D.Double(x, y, d, d));
            // handle
            g.draw(new Line2D.Double(x + d * 0.85, y + d * 0.85, s * 0.84, s * 0.84));
        });
    }

    /** Plus icon for "Add". */
    public static ImageIcon add(int size, Color color) {
        return draw(size, color, (g, s, st) -> {
            g.setStroke(st);
            g.draw(new Line2D.Double(s * 0.5, s * 0.18, s * 0.5, s * 0.82));
            g.draw(new Line2D.Double(s * 0.18, s * 0.5, s * 0.82, s * 0.5));
        });
    }

    /** Floppy-disk icon for "Save". */
    public static ImageIcon save(int size, Color color) {
        return draw(size, color, (g, s, st) -> {
            g.setStroke(st);
            double m = s * 0.18;
            // outer body with clipped top-right corner
            Path2D body = new Path2D.Double();
            body.moveTo(m, m);
            body.lineTo(s - m - s * 0.12, m);
            body.lineTo(s - m, m + s * 0.12);
            body.lineTo(s - m, s - m);
            body.lineTo(m, s - m);
            body.closePath();
            g.draw(body);
            // inner label (bottom)
            g.draw(new Rectangle2D.Double(m + s * 0.10, s * 0.52, s * 0.44, s - m - s * 0.52));
            // top shutter slot
            g.fill(new Rectangle2D.Double(s * 0.52, m + s * 0.03, s * 0.12, s * 0.16));
        });
    }

    /** Circular refresh arrows for "Update". */
    public static ImageIcon update(int size, Color color) {
        return draw(size, color, (g, s, st) -> {
            g.setStroke(st);
            double m = s * 0.20;
            double d = s - 2 * m;
            // two open arcs forming a refresh loop
            g.draw(new Arc2D.Double(m, m, d, d, 60, 200, Arc2D.OPEN));
            g.draw(new Arc2D.Double(m, m, d, d, 240, 200, Arc2D.OPEN));
            // arrow heads
            double cx = s * 0.5, cy = s * 0.5, r = d / 2;
            arrowHead(g, cx + r * Math.cos(Math.toRadians(60)),
                         cy - r * Math.sin(Math.toRadians(60)), s * 0.16, -30);
            arrowHead(g, cx + r * Math.cos(Math.toRadians(240)),
                         cy - r * Math.sin(Math.toRadians(240)), s * 0.16, 150);
        });
    }

    /** Left-pointing arrow for "Back". */
    public static ImageIcon back(int size, Color color) {
        return draw(size, color, (g, s, st) -> {
            g.setStroke(st);
            g.draw(new Line2D.Double(s * 0.78, s * 0.5, s * 0.26, s * 0.5));
            g.draw(new Line2D.Double(s * 0.26, s * 0.5, s * 0.46, s * 0.30));
            g.draw(new Line2D.Double(s * 0.26, s * 0.5, s * 0.46, s * 0.70));
        });
    }

    /** Check-mark icon for "Complete". */
    public static ImageIcon complete(int size, Color color) {
        return draw(size, color, (g, s, st) -> {
            g.setStroke(st);
            g.draw(new Line2D.Double(s * 0.20, s * 0.52, s * 0.42, s * 0.74));
            g.draw(new Line2D.Double(s * 0.42, s * 0.74, s * 0.80, s * 0.28));
        });
    }

    /** House icon for "Dashboard". */
    public static ImageIcon home(int size, Color color) {
        return draw(size, color, (g, s, st) -> {
            g.setStroke(st);
            // roof
            g.draw(new Line2D.Double(s * 0.5, s * 0.18, s * 0.16, s * 0.50));
            g.draw(new Line2D.Double(s * 0.5, s * 0.18, s * 0.84, s * 0.50));
            // walls
            g.draw(new Line2D.Double(s * 0.26, s * 0.46, s * 0.26, s * 0.82));
            g.draw(new Line2D.Double(s * 0.74, s * 0.46, s * 0.74, s * 0.82));
            g.draw(new Line2D.Double(s * 0.26, s * 0.82, s * 0.74, s * 0.82));
            // door
            g.draw(new Rectangle2D.Double(s * 0.44, s * 0.58, s * 0.14, s * 0.24));
        });
    }

    /** Open-book icon for "Add Entry". */
    public static ImageIcon book(int size, Color color) {
        return draw(size, color, (g, s, st) -> {
            g.setStroke(st);
            // spine
            g.draw(new Line2D.Double(s * 0.5, s * 0.26, s * 0.5, s * 0.80));
            // left page
            g.draw(new QuadCurve2D.Double(s * 0.5, s * 0.26, s * 0.30, s * 0.20, s * 0.16, s * 0.30));
            g.draw(new Line2D.Double(s * 0.16, s * 0.30, s * 0.16, s * 0.74));
            g.draw(new QuadCurve2D.Double(s * 0.5, s * 0.80, s * 0.30, s * 0.72, s * 0.16, s * 0.74));
            // right page
            g.draw(new QuadCurve2D.Double(s * 0.5, s * 0.26, s * 0.70, s * 0.20, s * 0.84, s * 0.30));
            g.draw(new Line2D.Double(s * 0.84, s * 0.30, s * 0.84, s * 0.74));
            g.draw(new QuadCurve2D.Double(s * 0.5, s * 0.80, s * 0.70, s * 0.72, s * 0.84, s * 0.74));
        });
    }

    /** Lined-list icon for "View Entries". */
    public static ImageIcon list(int size, Color color) {
        return draw(size, color, (g, s, st) -> {
            g.setStroke(st);
            for (int i = 0; i < 3; i++) {
                double y = s * (0.30 + i * 0.20);
                // bullet
                g.fill(new Ellipse2D.Double(s * 0.20, y - s * 0.05, s * 0.10, s * 0.10));
                // line
                g.draw(new Line2D.Double(s * 0.40, y, s * 0.80, y));
            }
        });
    }

    /** Concentric target icon for "Add Goal". */
    public static ImageIcon target(int size, Color color) {
        return draw(size, color, (g, s, st) -> {
            g.setStroke(st);
            double[] r = { 0.34, 0.20 };
            for (double rr : r) {
                double d = s * rr * 2;
                g.draw(new Ellipse2D.Double(s * 0.5 - s * rr, s * 0.5 - s * rr, d, d));
            }
            g.fill(new Ellipse2D.Double(s * 0.5 - s * 0.06, s * 0.5 - s * 0.06, s * 0.12, s * 0.12));
        });
    }

    /** Bar-chart icon for "View Goals". */
    public static ImageIcon chart(int size, Color color) {
        return draw(size, color, (g, s, st) -> {
            g.setStroke(st);
            // axes
            g.draw(new Line2D.Double(s * 0.20, s * 0.18, s * 0.20, s * 0.80));
            g.draw(new Line2D.Double(s * 0.20, s * 0.80, s * 0.82, s * 0.80));
            // bars
            g.fill(new Rectangle2D.Double(s * 0.30, s * 0.56, s * 0.12, s * 0.24));
            g.fill(new Rectangle2D.Double(s * 0.48, s * 0.42, s * 0.12, s * 0.38));
            g.fill(new Rectangle2D.Double(s * 0.66, s * 0.30, s * 0.12, s * 0.50));
        });
    }

    /** Trend-line icon for "Statistics". */
    public static ImageIcon stats(int size, Color color) {
        return draw(size, color, (g, s, st) -> {
            g.setStroke(st);
            // axes
            g.draw(new Line2D.Double(s * 0.18, s * 0.18, s * 0.18, s * 0.82));
            g.draw(new Line2D.Double(s * 0.18, s * 0.82, s * 0.84, s * 0.82));
            // rising line
            Path2D line = new Path2D.Double();
            line.moveTo(s * 0.24, s * 0.68);
            line.lineTo(s * 0.42, s * 0.52);
            line.lineTo(s * 0.58, s * 0.60);
            line.lineTo(s * 0.80, s * 0.30);
            g.draw(line);
            // arrow head at the end
            arrowHead(g, s * 0.80, s * 0.30, s * 0.16, -54);
        });
    }

    // ──────────────────────────────────────────────────────────────────
    //  Internal helpers
    // ──────────────────────────────────────────────────────────────────

    /** Functional drawing callback. */
    @FunctionalInterface
    private interface Drawer {
        void paint(Graphics2D g, double size, BasicStroke stroke);
    }


    private static ImageIcon draw(int size, Color color, Drawer drawer) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setColor(color);
        float strokeW = Math.max(1.4f, size * 0.09f);
        BasicStroke stroke = new BasicStroke(strokeW, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g.setStroke(stroke);
        drawer.paint(g, size, stroke);
        g.dispose();
        return new ImageIcon(img);
    }

    /** Draws a small "V" arrow head centred at (x,y) rotated by angleDeg. */
    private static void arrowHead(Graphics2D g, double x, double y, double len, double angleDeg) {
        double a = Math.toRadians(angleDeg);
        for (double spread : new double[]{ a + Math.toRadians(35), a - Math.toRadians(35) }) {
            double ex = x - len * Math.cos(spread);
            double ey = y - len * Math.sin(spread);
            g.draw(new Line2D.Double(x, y, ex, ey));
        }
    }
}
