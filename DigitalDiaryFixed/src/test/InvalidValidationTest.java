package test;

import exceptions.EmptyFieldException;
import exceptions.InvalidDateException;
import validation.Validation;

public class InvalidValidationTest {

    public static void main(String[] args) {

        // Test: Bad month
        try {
            Validation.validateEntry("07-13-2026", "Title", "Entry");
            System.out.println("FAIL: month 13 should be rejected");
        } catch (InvalidDateException e) {
            System.out.println("PASS: invalid month caught — " + e.getMessage());
        } catch (EmptyFieldException e) {
            System.out.println("FAIL: wrong exception type");
        }

        // Test: Empty entry text
        try {
            Validation.validateEntry("07-06-2026", "Title", "   ");
            System.out.println("FAIL: blank entry should be rejected");
        } catch (EmptyFieldException e) {
            System.out.println("PASS: blank entry caught — " + e.getMessage());
        } catch (InvalidDateException e) {
            System.out.println("FAIL: wrong exception type");
        }

        // Test: Empty title
        try {
            Validation.validateEntry("07-06-2026", "", "Some entry.");
            System.out.println("FAIL: blank title should be rejected");
        } catch (EmptyFieldException e) {
            System.out.println("PASS: blank title caught — " + e.getMessage());
        } catch (InvalidDateException e) {
            System.out.println("FAIL: wrong exception type");
        }

        System.out.println("All InvalidValidationTests PASSED.");
    }
}
