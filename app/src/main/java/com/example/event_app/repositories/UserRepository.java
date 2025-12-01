package com.example.event_app.repositories;

import com.example.event_app.models.User;

import java.util.Optional;

/**
 * Repository abstraction for persisting and retrieving users.
 */
public interface UserRepository {

    Optional<User> findById(String userId);

    Optional<User> findByDeviceId(String deviceId);

    User save(User user);
}
