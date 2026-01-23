package io.github.Eduardo_Port.userhubapi.controller;

import io.github.Eduardo_Port.userhubapi.dtos.UserRequest;
import io.github.Eduardo_Port.userhubapi.dtos.UserResponse;
import io.github.Eduardo_Port.userhubapi.model.User;
import io.github.Eduardo_Port.userhubapi.service.UserHubService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import java.util.UUID;

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
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{email}")
    public ResponseEntity<Void> reactivateUser(@PathVariable String email) {
        service.reactivateUser(email);
        return ResponseEntity.ok().build();
        }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserRequest dto, @PathVariable UUID id) {
        User user = service.update(id, dto);
        URI loc = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        UserResponse response = new UserResponse(user.getIdUser(), user.getName(), user.getEmail(), user.getEmailVerified(), user.getCreatedAt(), user.getUpdatedAt(), loc);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable String id) {
        var idUser = UUID.fromString(id);

        return service.findUserById(idUser)
                .map(user -> {
                    UserResponse response = new UserResponse(user);
                    return ResponseEntity.ok(response);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/list")
    public ResponseEntity<Page<UserResponse>> listUsers(
            @RequestParam(
                required = false,
                defaultValue = "0") int page,
            @RequestParam(
                required = false,
                defaultValue = "3") int size,
            @RequestParam(
                    required = false
            ) String name,
            @RequestParam(required = false) String email) {
        Page<User> users = service.getUsers(page, size, name, email);
        Page<UserResponse> res = users.map(user -> {
            URI loc = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getIdUser()).toUri();
            return new UserResponse(user.getIdUser(), user.getName(), user.getEmail(), user.getEmailVerified(), user.getCreatedAt(), user.getUpdatedAt(), loc);
        });
        return ResponseEntity.ok(res);
    }
}
