package com.example.event_app.models;

import com.example.event_app.TestUserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserModelTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = TestUserFactory.createUser("base");
    }

    @Test
    @DisplayName("constructor sets defaults and setters update state")
    void constructorAndSetters_updateState() {
        assertAll(
                () -> assertTrue(user.isNotificationsEnabled()),
                () -> assertNotNull(user.getRoles()),
                () -> assertNotNull(user.getFavoriteEvents())
        );

        user.setPhoneNumber("0987654321");
        user.setNotificationsEnabled(false);
        user.setFavoriteEvents(List.of("event-1"));

        assertAll(
                () -> assertEquals("0987654321", user.getPhoneNumber()),
                () -> assertFalse(user.isNotificationsEnabled()),
                () -> assertEquals(List.of("event-1"), user.getFavoriteEvents())
        );
    }

    @Test
    @DisplayName("addRole adds unique roles and reports membership")
    void addRole_managesRoles() {
        user.addRole("entrant");
        user.addRole("entrant");
        user.addRole("organizer");

        assertAll(
                () -> assertTrue(user.hasRole("entrant")),
                () -> assertTrue(user.hasRole("organizer")),
                () -> assertEquals(2, user.getRoles().size())
        );
    }

    @Test
    @DisplayName("favorite helpers avoid duplicates and remove entries")
    void favorites_manageLifecycle() {
        user.addFavorite("event-1");
        user.addFavorite("event-2");
        user.addFavorite("event-1");
        assertEquals(2, user.getFavoriteEvents().size());
        assertTrue(user.isFavorite("event-1"));

        user.removeFavorite("event-1");
        assertFalse(user.isFavorite("event-1"));
        assertEquals(List.of("event-2"), user.getFavoriteEvents());
    }

    @Test
    @DisplayName("equals and hashCode require matching user state")
    void equalsAndHashCode_matchOnSameState() {
        User same = TestUserFactory.createUser("base");
        same.setRoles(user.getRoles());
        same.setFavoriteEvents(user.getFavoriteEvents());

        User different = TestUserFactory.createUser("other");

        assertEquals(user, same);
        assertEquals(user.hashCode(), same.hashCode());
        assertNotEquals(user, different);
    }

    @Test
    @DisplayName("toString contains identifiers for debugging")
    void toString_includesKeyFields() {
        String asString = user.toString();
        assertAll(
                () -> assertTrue(asString.contains("uid-base")),
                () -> assertTrue(asString.contains("device-base")),
                () -> assertTrue(asString.contains("User base"))
        );
    }
}
