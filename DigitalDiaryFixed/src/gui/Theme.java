package gui;

import java.awt.*;

public class Theme {

    // ─── Background ────────────────────────────────
    public static final Color BACKGROUND   = new Color(245, 242, 255);
    public static final Color SIDEBAR      = new Color(58,  47,  88);
    public static final Color SIDEBAR_HOVER= new Color(80,  65, 120);

    // ─── Header ────────────────────────────────────
    public static final Color HEADER       = new Color(111,  76, 182);
    public static final Color HEADER_LIGHT = new Color(138, 108, 210);

    // ─── Cards ─────────────────────────────────────
    public static final Color ENTRY_CARD   = new Color(219, 202, 255);
    public static final Color GOAL_CARD    = new Color(195, 236, 213);
    public static final Color STREAK_CARD  = new Color(255, 218, 185);
    public static final Color SCORE_CARD   = new Color(255, 198, 220);
    public static final Color WHITE_CARD   = Color.WHITE;

    // ─── Mood colours ──────────────────────────────
    public static final Color MOOD_HAPPY   = new Color(255, 220, 100);
    public static final Color MOOD_EXCITED = new Color(255, 160,  80);
    public static final Color MOOD_CALM    = new Color(120, 200, 230);
    public static final Color MOOD_NEUTRAL = new Color(190, 190, 190);
    public static final Color MOOD_SAD     = new Color(130, 170, 230);
    public static final Color MOOD_STRESSED= new Color(230, 130, 130);

    // ─── Text ──────────────────────────────────────
    public static final Color TEXT         = new Color(40,  30,  60);
    public static final Color TEXT_LIGHT   = new Color(255, 255, 255);
    public static final Color SECONDARY_TEXT = new Color(120, 110, 140);

    // ─── Buttons ───────────────────────────────────
    public static final Color BUTTON       = new Color(111,  76, 182);
    public static final Color BUTTON_HOVER = new Color(138, 108, 210);
    public static final Color BUTTON_DANGER= new Color(220,  60,  60);
    public static final Color BUTTON_SUCCESS=new Color( 50, 168, 100);

    // ─── Sidebar menu ──────────────────────────────
    public static final Color ACTIVE_MENU  = new Color(111,  76, 182);
    public static final Color MENU_TEXT    = new Color(220, 210, 240);

    // ─── Fonts ─────────────────────────────────────
    public static Font font(int style, int size) {
        return new Font("Segoe UI", style, size);
    }

    public static Font bold(int size)  { return font(Font.BOLD,  size); }
    public static Font plain(int size) { return font(Font.PLAIN, size); }

    // ─── Mood emoji helper ─────────────────────────
    public static String moodEmoji(String mood) {
        return switch (mood) {
            case "Happy"    -> "😊 Happy";
            case "Excited"  -> "🤩 Excited";
            case "Calm"     -> "😌 Calm";
            case "Neutral"  -> "😐 Neutral";
            case "Sad"      -> "😢 Sad";
            case "Stressed" -> "😰 Stressed";
            default         -> mood;
        };
    }

    public static Color moodColor(String mood) {
        return switch (mood) {
            case "Happy"    -> MOOD_HAPPY;
            case "Excited"  -> MOOD_EXCITED;
            case "Calm"     -> MOOD_CALM;
            case "Sad"      -> MOOD_SAD;
            case "Stressed" -> MOOD_STRESSED;
            default         -> MOOD_NEUTRAL;
        };
    }
}
