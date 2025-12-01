package com.example.event_app.services;

import com.example.event_app.models.User;
import com.example.event_app.repositories.UserRepository;
import com.example.event_app.validation.UserValidationException;
import com.example.event_app.validation.UserValidator;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

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

    /**
     * Convenience factory for unit tests that prefer a lightweight, in-memory
     * store instead of a mocked {@link UserRepository} implementation.
     */
    public static UserService withInMemoryStore(UserValidator validator) {
        return new UserService(new InMemoryUserRepository(), validator);
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

    /**
     * Minimal repository implementation backed by an in-memory map so tests can
     * exercise {@link UserService} without depending on a concrete repository
     * class.
     */
    public static class InMemoryUserRepository implements UserRepository {
        private final Map<String, User> users = new ConcurrentHashMap<>();

        @Override
        public Optional<User> findById(String userId) {
            return Optional.ofNullable(users.get(userId));
        }

        @Override
        public Optional<User> findByDeviceId(String deviceId) {
            return users.values().stream()
                    .filter(user -> deviceId != null && deviceId.equals(user.getDeviceId()))
                    .findFirst();
        }

        @Override
        public User save(User user) {
            users.put(user.getUserId(), user);
            return user;
        }
    }
}
