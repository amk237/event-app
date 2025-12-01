package com.example.event_app.validation;

import com.example.event_app.TestUserFactory;
import com.example.event_app.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    private final UserValidator validator = new UserValidator();

    @Test
    @DisplayName("valid user returns no validation errors")
    void validUser_hasNoErrors() {
        User user = TestUserFactory.createUser("1");

        List<String> errors = validator.validateForCreate(user);

        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("required identifiers are validated")
    void missingIds_reported() {
        User user = new User(null, null, "User", "user@example.com");

        List<String> errors = validator.validateForCreate(user);

        assertTrue(errors.contains("userId is required"));
        assertTrue(errors.contains("deviceId is required"));
    }

    @Test
    @DisplayName("name length boundaries are enforced")
    void nameBoundaries_checked() {
        User tooShort = TestUserFactory.createUser("short");
        tooShort.setName("Al");
        User tooLong = TestUserFactory.createUser("long");
        tooLong.setName("A".repeat(51));
        User withinBounds = TestUserFactory.createUser("ok");
        withinBounds.setName("Ana");

        assertTrue(validator.validateForCreate(tooShort).contains("name must be at least 3 characters"));
        assertTrue(validator.validateForCreate(tooLong).contains("name must be at most 50 characters"));
        assertTrue(validator.validateForCreate(withinBounds).isEmpty());
    }

    @Test
    @DisplayName("email must be present and well-formed")
    void emailFormat_checked() {
        User missing = TestUserFactory.createUser("missing");
        missing.setEmail(null);
        User malformed = TestUserFactory.createUser("bad");
        malformed.setEmail("invalid");
        User valid = TestUserFactory.createUser("good");

        assertTrue(validator.validateForCreate(missing).contains("email is required"));
        assertTrue(validator.validateForCreate(malformed).contains("email must be valid"));
        assertFalse(validator.validateForCreate(valid).contains("email must be valid"));
    }

    @Test
    @DisplayName("age boundaries capture underage and unrealistic values")
    void ageBoundaries_checked() {
        User underAge = TestUserFactory.createUser("young");
        underAge.setAge(12);
        User acceptable = TestUserFactory.createUser("adult");
        acceptable.setAge(13);
        User tooOld = TestUserFactory.createUser("old");
        tooOld.setAge(121);

        assertTrue(validator.validateForCreate(underAge).contains("age must be at least 13"));
        assertTrue(validator.validateForCreate(acceptable).isEmpty());
        assertTrue(validator.validateForCreate(tooOld).contains("age must be realistic (<= 120)"));
    }

    @Test
    @DisplayName("phone number is optional but format checked when provided")
    void phoneNumber_checkedWhenPresent() {
        User invalidPhone = TestUserFactory.createUser("bad-phone");
        invalidPhone.setPhoneNumber("12-34");
        User validPhone = TestUserFactory.createUser("good-phone");
        validPhone.setPhoneNumber("+123456789012");
        User withoutPhone = TestUserFactory.createUser("no-phone");
        withoutPhone.setPhoneNumber("");

        assertTrue(validator.validateForCreate(invalidPhone).contains("phoneNumber must be digits (10-15) and may start with +"));
        assertTrue(validator.validateForCreate(validPhone).isEmpty());
        assertTrue(validator.validateForCreate(withoutPhone).isEmpty());
    }
}
