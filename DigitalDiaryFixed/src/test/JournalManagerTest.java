package test;

import manager.JournalManager;
import model.JournalEntry;

public class JournalManagerTest {

    public static void main(String[] args) {

        JournalManager mgr = JournalManager.getInstance();

        // Test 1: Add entry
        JournalEntry e1 = new JournalEntry("07-06-2026", "Good Day", "Happy", "Had a productive day!");
        mgr.addEntry(e1);
        assert mgr.getTotalEntries() == 1 : "FAIL: total entries should be 1";
        System.out.println("PASS: addEntry");

        // Test 2: Mood count
        assert mgr.countByMood("Happy") == 1 : "FAIL: mood count should be 1";
        System.out.println("PASS: countByMood");

        // Test 3: Top mood
        assert "Happy".equals(mgr.topMood()) : "FAIL: top mood should be Happy";
        System.out.println("PASS: topMood");

        // Test 4: Delete entry
        mgr.deleteEntry(0);
        assert mgr.getTotalEntries() == 0 : "FAIL: total entries should be 0 after delete";
        System.out.println("PASS: deleteEntry");

        System.out.println("All JournalManagerTests PASSED.");
    }
}
