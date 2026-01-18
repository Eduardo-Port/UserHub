package io.github.Eduardo_Port.userhubapi.service;

import io.github.Eduardo_Port.userhubapi.dtos.UserRequest;
import io.github.Eduardo_Port.userhubapi.exceptions.EmailAlreadyUsed;
import io.github.Eduardo_Port.userhubapi.model.Status;
import io.github.Eduardo_Port.userhubapi.model.User;
import io.github.Eduardo_Port.userhubapi.repository.UserHubRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class UserHubServiceTest {
    @Mock
    private UserHubRepository userHubRepository;

    @InjectMocks
    private UserHubService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getUsers() {
    }

    @Test
    @DisplayName("Should create User succesfuly when email is unique")
    void createUserCase1() {
        UserRequest user = new UserRequest("Eduardo", "edu.friv10@gmail.com", "123456789");
        when(userHubRepository.findByEmail(user.email())).thenReturn(Optional.empty());
        when(userHubRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        User createdUser = service.createUser(user);

        assertNotNull(createdUser);
        assertEquals(user.name(), createdUser.getName());
        assertEquals(createdUser.getEmail(), user.email());
        assertEquals(Status.ACTIVE, createdUser.getStatus());
        assertNotNull(createdUser.getCreatedAt());
        assertNotNull(createdUser.getUpdatedAt());
    }

    @Test
    @DisplayName("Should throw EmailAlreadyUsed exception when email already exists")
    void createUserCase2() {
        UserRequest user = new UserRequest("Eduardo", "edu.friv10@gmail.com", "123456789");
        when(userHubRepository.findByEmail(user.email())).thenReturn(Optional.of(new User()));
        assertThrows(EmailAlreadyUsed.class, () -> service.createUser(user));

        verify(userHubRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should change status to INACTIVE if user exists and is ACTIVE")
    void deleteByIdCase1() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setIdUser(id);
        user.setStatus(Status.ACTIVE);
        when(userHubRepository.findById(id)).thenReturn(Optional.of(user));
        service.deleteById(id);
        assertEquals(Status.INACTIVE, user.getStatus());
       // verify(userHubRepository, times(1)).save(any(User.class));
    }
    @Test
    @DisplayName("Should throw EntityNotFoundException when User does not exists")
    void deleteByIdCase2() {
        UUID id = UUID.randomUUID();
        when(userHubRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.deleteById(id));
        verify(userHubRepository, never()).save(any(User.class));
    }

    @Test
    void reactivateUser() {

    }

    @Test
    void update() {
    }
}