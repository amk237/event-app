package com.example.event_app.validation;

import java.util.Collections;
import java.util.List;

/**
 * Domain-specific exception used when user input fails validation.
 */
public class UserValidationException extends RuntimeException {

    private final List<String> errors;

    public UserValidationException(List<String> errors) {
        super(String.join("; ", errors));
        this.errors = Collections.unmodifiableList(errors);
    }

    public List<String> getErrors() {
        return errors;
    }
}
