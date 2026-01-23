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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @DisplayName("Should change status to ACTIVE if user exists and is INACTIVE")
    void reactivateUserCase1() {
        String email = "eduardo17.pro16@gmail.com";
        User user = new User();
        //user.setEmail(email);
        user.setStatus(Status.INACTIVE);
        when(userHubRepository.findByEmail(email)).thenReturn(Optional.of(user));
        service.reactivateUser(email);
        assertEquals(Status.ACTIVE, user.getStatus());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when User does not exists")
    void reactivateUserCase2() {
        String email = "eduardo17.pro16@gmail.com";
        when(userHubRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.reactivateUser(email));
    }

    @Test
    @DisplayName("Should update user name or password with the necessity")
    void updateCase1() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UUID id = UUID.randomUUID();
        UserRequest userRequest = new UserRequest("Eduardo", "Oliveira@gmail.com", "1588915000");
        User user1 = new User();
        user1.setEmail("Oliveira@gmail.com");
        when(userHubRepository.findById(id)).thenReturn(Optional.of(user1));
        User user = service.update(id, userRequest);
        assertEquals(user.getEmail(), userRequest.email());
        assertEquals(user.getName(), userRequest.name());
        assertTrue(passwordEncoder.matches( userRequest.password(), user.getPasswordHash()));
    }
    @Test
    @DisplayName("Should throw EntityNotFoundException because user isnt found")
    void updateCase2() {
        UUID id = UUID.randomUUID();
        UserRequest userRequest = new UserRequest("Eduardo", "Oliveira@gmail.com", "1588915000");
        when(userHubRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.update(id, userRequest));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException because the founded email is different ")
    void updateCase3() {
        UUID id = UUID.randomUUID();
        UserRequest userRequest = new UserRequest("Eduardo", "Olivei@gmail.com", "1588915000");
        User user1 = new User();
        user1.setEmail("Oliveira@gmail.com");
        when(userHubRepository.findById(id)).thenReturn(Optional.of(user1));
        assertThrows(EntityNotFoundException.class, () -> service.update(id, userRequest));
    }
}