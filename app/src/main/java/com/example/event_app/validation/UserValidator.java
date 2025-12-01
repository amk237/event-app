package com.example.event_app.validation;

import com.example.event_app.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Validates {@link User} instances using simple business rules intended for
 * state-based unit testing.
 */
public class UserValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\n]+@[^@\n]+\\.[^@\n]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$");

    public List<String> validateForCreate(User user) {
        List<String> errors = new ArrayList<>();

        if (isBlank(user.getUserId())) {
            errors.add("userId is required");
        }
        if (isBlank(user.getDeviceId())) {
            errors.add("deviceId is required");
        }
        if (isBlank(user.getName())) {
            errors.add("name is required");
        } else {
            int length = user.getName().length();
            if (length < 3) {
                errors.add("name must be at least 3 characters");
            } else if (length > 50) {
                errors.add("name must be at most 50 characters");
            }
        }

        if (isBlank(user.getEmail())) {
            errors.add("email is required");
        } else if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            errors.add("email must be valid");
        }

        if (user.getAge() != null) {
            if (user.getAge() < 13) {
                errors.add("age must be at least 13");
            } else if (user.getAge() > 120) {
                errors.add("age must be realistic (<= 120)");
            }
        }

        if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()
                && !PHONE_PATTERN.matcher(user.getPhoneNumber()).matches()) {
            errors.add("phoneNumber must be digits (10-15) and may start with +");
        }

        return errors;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
