package com.example.event_app.services;

import com.example.event_app.models.User;
import com.example.event_app.validation.UserValidationException;
import com.example.event_app.validation.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
class UserServiceTest {

    private UserService.InMemoryUserRepository repository;
    private UserValidator validator;
    private UserService service;

    @BeforeEach
    void setUp() {
        validator = new UserValidator();
        repository = new UserService.InMemoryUserRepository();
        service = new UserService(repository, validator);
    }

    @Test
    @DisplayName("createUser persists validated dummy profile")
    void createUser_savesValidatedProfile() {
        User result = service.createUser("user-1", "device-001", "Jane Test", "jane@test.com", 28);

        assertEquals("user-1", result.getUserId());
        assertEquals("device-001", result.getDeviceId());
        assertEquals("Jane Test", result.getName());
        assertEquals(28, result.getAge());
        assertEquals(Optional.of(result), repository.findById("user-1"));
    }

    @Test
    @DisplayName("createUser rejects duplicate device IDs with aggregated validation errors")
    void createUser_duplicateDeviceId_rejected() {
        repository.save(new User("existing", "device-001", "Existing User", "existing@test.com"));

        UserValidationException exception = assertThrows(UserValidationException.class, () ->
                service.createUser("user-1", "device-001", "", "bad-email", 5)
        );

        assertTrue(exception.getErrors().contains("deviceId must be unique"));
        assertTrue(exception.getErrors().contains("email must be valid"));
        assertTrue(exception.getErrors().contains("name must be at least 3 characters"));
    }

    @Test
    @DisplayName("toggleNotifications updates persisted flag and timestamp")
    void toggleNotifications_updatesState() {
        User existing = new User("user-1", "device-001", "Jane", "jane@test.com");
        existing.setUpdatedAt(0L);
        repository.save(existing);

        User updated = service.toggleNotifications("user-1", false);

        assertFalse(updated.isNotificationsEnabled());
        assertNotEquals(0L, updated.getUpdatedAt());
    }

    @Test
    @DisplayName("addRole injects role into user record and persists")
    void addRole_persistsRoleChange() {
        User existing = new User("user-1", "device-001", "Jane", "jane@test.com");
        repository.save(existing);

        User updated = service.addRole("user-1", "admin");

        assertTrue(updated.getRoles().contains("admin"));
    }

    @Test
    @DisplayName("addFavorite enforces blank checks before hitting repository")
    void addFavorite_blankEventId_rejectedEarly() {
        assertThrows(IllegalArgumentException.class, () -> service.addFavorite("user-1", " "));
    }

    @Test
    @DisplayName("addFavorite records event ID for the user")
    void addFavorite_persistsFavoriteList() {
        User existing = new User("user-1", "device-001", "Jane", "jane@test.com");
        repository.save(existing);

        User updated = service.addFavorite("user-1", "EVT-42");

        assertTrue(updated.getFavoriteEvents().contains("EVT-42"));
    }
}
