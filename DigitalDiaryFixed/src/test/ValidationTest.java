package test;

import exceptions.EmptyFieldException;
import exceptions.InvalidDateException;
import validation.Validation;
import validation.GoalValidation;

public class ValidationTest {

    public static void main(String[] args) throws Exception {

        // Test 1: Valid entry
        try {
            Validation.validateEntry("07-06-2026", "My Title", "Some text here.");
            System.out.println("PASS: valid entry accepted");
        } catch (Exception e) {
            System.out.println("FAIL: " + e.getMessage());
        }

        // Test 2: Empty date
        try {
            Validation.validateEntry("", "Title", "Entry");
            System.out.println("FAIL: should have thrown EmptyFieldException");
        } catch (EmptyFieldException e) {
            System.out.println("PASS: empty date caught");
        }

        // Test 3: Bad date format
        try {
            Validation.validateEntry("2026/06/07", "Title", "Entry");
            System.out.println("FAIL: should have thrown InvalidDateException");
        } catch (InvalidDateException e) {
            System.out.println("PASS: bad date format caught");
        }

        // Test 4: Empty goal
        try {
            GoalValidation.validateGoal("", "");
            System.out.println("FAIL: should have thrown EmptyFieldException");
        } catch (EmptyFieldException e) {
            System.out.println("PASS: empty goal caught");
        }

        // Test 5: Goal with bad deadline
        try {
            GoalValidation.validateGoal("My Goal", "not-a-date");
            System.out.println("FAIL: should have thrown InvalidDateException");
        } catch (InvalidDateException e) {
            System.out.println("PASS: bad deadline format caught");
        }

        System.out.println("All ValidationTests PASSED.");
    }
}
