package io.github.Eduardo_Port.userhubapi.repository;

import io.github.Eduardo_Port.userhubapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserHubRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    void deleteByEmail(String email);
}
