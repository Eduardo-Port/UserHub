package io.github.Eduardo_Port.userhubapi.repository;

import io.github.Eduardo_Port.userhubapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserHubRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    @Query(value = "UPDATE tbUsers SET status = INACTIVE WHERE iduser = ?1", nativeQuery = true)
    @Modifying
    void disactiveUser(UUID id);

    @Query(value = "UPDATE tbUsers SET status = INACTIVE WHERE iduser = ?1", nativeQuery = true)
    @Modifying
    void reactivateUser(String email);
}
