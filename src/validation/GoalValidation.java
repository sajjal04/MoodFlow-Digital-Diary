package validation;

import exceptions.EmptyFieldException;
import exceptions.InvalidDateException;

public class GoalValidation {

    public static void validateGoal(String goalName, String deadline)
            throws EmptyFieldException, InvalidDateException {

        if (goalName == null || goalName.trim().isEmpty())
            throw new EmptyFieldException("Goal name cannot be empty.");

        if (deadline != null && !deadline.trim().isEmpty()) {
            if (!deadline.matches("\\d{2}-\\d{2}-\\d{4}"))
                throw new InvalidDateException("Deadline format must be DD-MM-YYYY.");
        }
    }
}
