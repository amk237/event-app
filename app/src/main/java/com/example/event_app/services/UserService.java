package com.example.event_app.services;

import com.example.event_app.models.User;
import com.example.event_app.repositories.UserRepository;
import com.example.event_app.validation.UserValidationException;
import com.example.event_app.validation.UserValidator;

import java.util.List;

/**
 * Simple service demonstrating stateful behaviors for {@link User} entities.
 */
public class UserService {

    private final UserRepository repository;
    private final UserValidator validator;

    public UserService(UserRepository repository, UserValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public User createUser(String userId, String deviceId, String name, String email, Integer age) {
        User user = new User(userId, deviceId, name, email);
        user.setAge(age);
        List<String> errors = validator.validateForCreate(user);
        repository.findByDeviceId(deviceId).ifPresent(existing -> {
            errors.add("deviceId must be unique");
        });
        if (!errors.isEmpty()) {
            throw new UserValidationException(errors);
        }
        return repository.save(user);
    }

    public User toggleNotifications(String userId, boolean enabled) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setNotificationsEnabled(enabled);
        user.setUpdatedAt(System.currentTimeMillis());
        return repository.save(user);
    }

    public User addRole(String userId, String role) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.addRole(role);
        user.setUpdatedAt(System.currentTimeMillis());
        return repository.save(user);
    }

    public User addFavorite(String userId, String eventId) {
        if (eventId == null || eventId.isBlank()) {
            throw new IllegalArgumentException("Event ID cannot be blank");
        }
        User user = repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.addFavorite(eventId);
        user.setUpdatedAt(System.currentTimeMillis());
        return repository.save(user);
    }
}
