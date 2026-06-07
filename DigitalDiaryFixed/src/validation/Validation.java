package validation;

import exceptions.EmptyFieldException;
import exceptions.InvalidDateException;

public class Validation {

    public static void validateEntry(String date, String title, String entry)
            throws EmptyFieldException, InvalidDateException {

        if (date == null || date.trim().isEmpty())
            throw new EmptyFieldException("Date cannot be empty.");

        if (title == null || title.trim().isEmpty())
            throw new EmptyFieldException("Title cannot be empty.");

        if (entry == null || entry.trim().isEmpty())
            throw new EmptyFieldException("Journal entry cannot be empty.");

        if (!date.matches("\\d{2}-\\d{2}-\\d{4}"))
            throw new InvalidDateException("Date format must be DD-MM-YYYY (e.g. 07-06-2026).");

        String[] parts = date.split("-");
        int day   = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year  = Integer.parseInt(parts[2]);

        if (month < 1 || month > 12)
            throw new InvalidDateException("Month must be between 01 and 12.");
        if (day < 1 || day > 31)
            throw new InvalidDateException("Day must be between 01 and 31.");
        if (year < 2000 || year > 2100)
            throw new InvalidDateException("Year must be between 2000 and 2100.");
    }
}
