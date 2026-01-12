package io.github.Eduardo_Port.userhubapi.controller;

import io.github.Eduardo_Port.userhubapi.dtos.UserRequest;
import io.github.Eduardo_Port.userhubapi.dtos.UserResponse;
import io.github.Eduardo_Port.userhubapi.model.User;
import io.github.Eduardo_Port.userhubapi.service.UserHubService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class UserHubController {
    @Autowired
    private UserHubService service;
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userData) {
        User user = service.createUser(userData);
        URI loc = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getIdUser()).toUri();
        UserResponse response = new UserResponse(user.getIdUser(), user.getName(), user.getEmail(), user.getEmailVerified(), user.getCreatedAt(), user.getUpdatedAt(), loc);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        service.deleteByEmail(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{email}")
    public ResponseEntity<Void> reactivateUser(@PathVariable String email) {
        service.reactivateUser(email);
        return ResponseEntity.ok().build();
        }

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserRequest dto) {
        User user = service.getUserByEmail(dto.email()).orElseThrow();
        UUID id = service.update(user, dto);
        URI loc = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        UserResponse response = new UserResponse(user.getIdUser(), user.getName(), user.getEmail(), user.getEmailVerified(), user.getCreatedAt(), user.getUpdatedAt(), loc);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable String id) {
        var idUser = UUID.fromString(id);

        return service.getUserById(idUser)
                .map(user -> {
                    UserResponse response = new UserResponse(user.getIdUser(), user.getName(), user.getEmail(), user.getEmailVerified(), user.getCreatedAt(), user.getUpdatedAt());
                    return ResponseEntity.ok(response);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/list")
    public ResponseEntity<List<UserResponse>> listUsers() {
        List<User> users = service.getUsers();
        List<UserResponse> response = users.stream()
                .map(user -> new UserResponse(user.getIdUser(), user.getName(), user.getEmail(), user.getEmailVerified(), user.getCreatedAt(), user.getUpdatedAt()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
