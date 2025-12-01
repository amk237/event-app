package com.example.event_app.services;

import com.example.event_app.TestUserFactory;
import com.example.event_app.models.User;
import com.example.event_app.repositories.UserRepository;
import com.example.event_app.validation.UserValidationException;
import com.example.event_app.validation.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    private UserValidator validator;

    @InjectMocks
    private UserService service;

    @BeforeEach
    void initValidator() {
        validator = new UserValidator();
        service = new UserService(repository, validator);
    }

    @Test
    @DisplayName("createUser saves validated user data")
    void createUser_persistsValidUser() {
        when(repository.findByDeviceId("device-1")).thenReturn(Optional.empty());
        User saved = TestUserFactory.createUser("1");
        when(repository.save(any(User.class))).thenReturn(saved);

        User result = service.createUser("uid-1", "device-1", "User 1", "user1@example.com", 20);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(repository).save(captor.capture());
        User persisted = captor.getValue();

        assertAll(
                () -> assertEquals("uid-1", persisted.getUserId()),
                () -> assertEquals("device-1", persisted.getDeviceId()),
                () -> assertEquals("User 1", persisted.getName()),
                () -> assertEquals(20, persisted.getAge()),
                () -> assertEquals(saved, result)
        );
    }

    @Test
    @DisplayName("createUser rejects duplicate device IDs")
    void createUser_duplicateDeviceId_throws() {
        when(repository.findByDeviceId("device-dup")).thenReturn(Optional.of(TestUserFactory.createUser("dup")));

        UserValidationException exception = assertThrows(UserValidationException.class, () ->
                service.createUser("uid-dup", "device-dup", "User", "user@example.com", 22)
        );

        assertTrue(exception.getErrors().contains("deviceId must be unique"));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("toggleNotifications updates flag and persists change")
    void toggleNotifications_updatesState() {
        User existing = TestUserFactory.createUser("notify");
        when(repository.findById("uid-notify")).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        User updated = service.toggleNotifications("uid-notify", false);

        assertFalse(updated.isNotificationsEnabled());
        verify(repository).save(existing);
    }

    @Test
    @DisplayName("addRole adds missing role and saves")
    void addRole_persistsRoleChange() {
        User existing = TestUserFactory.createUserWithRoles("role", List.of("entrant"));
        when(repository.findById("uid-role")).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        User updated = service.addRole("uid-role", "organizer");

        assertTrue(updated.hasRole("organizer"));
        verify(repository).save(existing);
    }

    @Test
    @DisplayName("addFavorite validates input and missing user")
    void addFavorite_handlesInvalidInputAndMissingUser() {
        assertThrows(IllegalArgumentException.class, () -> service.addFavorite("uid-x", " "));

        when(repository.findById("uid-missing")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.addFavorite("uid-missing", "event-1"));
    }
}
