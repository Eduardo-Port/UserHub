package io.github.Eduardo_Port.userhubapi.controller;

import io.github.Eduardo_Port.userhubapi.dtos.UserRequest;
import io.github.Eduardo_Port.userhubapi.dtos.UserResponse;
import io.github.Eduardo_Port.userhubapi.model.User;
import io.github.Eduardo_Port.userhubapi.service.UserHubService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
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
    public ResponseEntity<Void> createUser(@RequestBody UserRequest user) {
        UUID id = service.createUser(user);
        URI loc = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(loc).build();
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        service.deleteByEmail(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserRequest dto) {
        User user = service.getUserByEmail(dto.email()).orElseThrow();
        UUID id = service.update(user, dto);
        URI loc = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<User> getUserByEmail(@RequestBody String email) {
        User user = service.getUserByEmail(email).orElse(null);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable String id) {
        var idUser = UUID.fromString(id);

        return service.getUserById(idUser)
                .map(user -> {
                    UserResponse response = new UserResponse(user.getName(), user.getEmail(), user.getEmailVerified(), user.getCreatedAt(), user.getUpdatedAt());
                    return ResponseEntity.ok(response);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/list")
    public ResponseEntity<List<UserResponse>> listUsers() {
        List<User> users = service.getUsers();
        List<UserResponse> response = users.stream()
                .map(user -> {
                    UserResponse res = new UserResponse(user.getName(), user.getEmail(), user.getEmailVerified(), user.getCreatedAt(), user.getUpdatedAt());
                    return res;
                    }
                ).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
