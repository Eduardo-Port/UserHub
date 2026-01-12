package io.github.Eduardo_Port.userhubapi.service;

import io.github.Eduardo_Port.userhubapi.dtos.UserRequest;
import io.github.Eduardo_Port.userhubapi.exceptions.EmailAlreadyUsed;
import io.github.Eduardo_Port.userhubapi.model.User;
import io.github.Eduardo_Port.userhubapi.repository.UserHubRepository;
import io.github.Eduardo_Port.userhubapi.repository.specs.UserSpecs;
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

    public Optional<User> getUserById(UUID id) {
        return userHubRepository.findById(id);
    }

    public User createUser(UserRequest user) {
        User userCreated = new User();
        userCreated.setName(user.name());
        userCreated.setEmail(user.email());
        userCreated.setPasswordHash(hashingPassword(user.password()));
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        ZoneId fusoHorario = ZoneId.of("America/Sao_Paulo");
//        ZonedDateTime zdt = ZonedDateTime.now(fusoHorario);
//
//        String tempo = zdt.format(formatter);
        userCreated.setCreatedAt(Timestamp.from(Instant.now()));
        userCreated.setUpdatedAt(Timestamp.from(Instant.now()));
        if(emailIsUnique(user.email())) {
            userHubRepository.save(userCreated);
            return userCreated;
        } else {
            throw new EmailAlreadyUsed();
        }
    }

    private String hashingPassword(String password) {
        return BCrypt.hashpw(password, salt);
    }

    public void deleteByEmail(UUID id) {
        userHubRepository.disactiveUser(id);
    }

    public void reactivateUser(String email) {
        userHubRepository.reactivateUser(email);
    }

    public User update(UUID id, @Valid UserRequest dto) {
        User user = getUserById(id).orElseThrow();
        user.setName(dto.name());
        user.setPasswordHash(hashingPassword(dto.password()));
        user.setUpdatedAt(Timestamp.from(Instant.now()));
        userHubRepository.save(user);
        return user;
    }

    public boolean emailIsUnique(String email) {
        Optional<User> user = userHubRepository.findByEmail(email);
        return user.isEmpty();
    }
}
