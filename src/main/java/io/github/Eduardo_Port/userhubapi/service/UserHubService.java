package io.github.Eduardo_Port.userhubapi.service;

import io.github.Eduardo_Port.userhubapi.dtos.UserRequest;
import io.github.Eduardo_Port.userhubapi.exceptions.EmailAlreadyUsed;
import io.github.Eduardo_Port.userhubapi.model.Status;
import io.github.Eduardo_Port.userhubapi.model.User;
import io.github.Eduardo_Port.userhubapi.repository.UserHubRepository;
import io.github.Eduardo_Port.userhubapi.repository.specs.UserSpecs;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserHubService {
    private final String salt = BCrypt.gensalt();
    @Autowired
    private UserHubRepository userHubRepository;

    public Optional<User> findUserById(UUID id) {
        return userHubRepository.findById(id);
    }

    public Page<User> getUsers(int page, int size, String name, String email) {
        Specification<User> spec = UserSpecs.withFilter(name, email);
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                "name"
        );

        return userHubRepository.findAll(spec, pageRequest);
    }
    @Transactional
    public User createUser(UserRequest user) {
        if(!emailIsUnique(user.email())) {
            throw new EmailAlreadyUsed();
        }
        User userCreated = new User();
        userCreated.setName(user.name());
        userCreated.setEmail(user.email());
        userCreated.setPasswordHash(hashingPassword(user.password()));
        userCreated.setStatus(Status.ACTIVE);
        userCreated.setCreatedAt(Timestamp.from(Instant.now()));
        userCreated.setUpdatedAt(Timestamp.from(Instant.now()));
       return userHubRepository.save(userCreated);
    }

    private String hashingPassword(String password) {
        return BCrypt.hashpw(password, salt);
    }

    @Transactional
    public void deleteById(UUID id) {
        User user = userHubRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if(user.getStatus().equals(Status.ACTIVE)) {
            user.setStatus(Status.INACTIVE);
        }
    }

    @Transactional
    public void reactivateUser(String email) {
        User user = userHubRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("User not found"));
        user.setStatus(Status.ACTIVE);
    }

    @Transactional
    public User update(UUID id, @Valid UserRequest dto) {
        User user = userHubRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setName(dto.name());
        user.setPasswordHash(hashingPassword(dto.password()));
        return user;
    }

    private boolean emailIsUnique(String email) {
        return userHubRepository.findByEmail(email).isEmpty();
    }
}
