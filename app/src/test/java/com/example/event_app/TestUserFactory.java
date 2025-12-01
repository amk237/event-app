package com.example.event_app;

import com.example.event_app.models.User;

import java.util.List;

/**
 * Simple factory for generating deterministic {@link User} instances in tests.
 */
public final class TestUserFactory {

    private TestUserFactory() {
    }

    public static User createUser(String idSuffix) {
        User user = new User("uid-" + idSuffix, "device-" + idSuffix, "User " + idSuffix, "user" + idSuffix + "@example.com");
        user.setAge(20);
        user.setPhoneNumber("1234567890");
        user.setFcmToken("token-" + idSuffix);
        user.setCreatedAt(1_000L);
        user.setUpdatedAt(1_000L);
        return user;
    }

    public static User createUserWithRoles(String idSuffix, List<String> roles) {
        User user = createUser(idSuffix);
        user.setRoles(roles);
        return user;
    }
}
