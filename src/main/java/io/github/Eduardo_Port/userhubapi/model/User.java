package io.github.Eduardo_Port.userhubapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "tb_users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_user")
    private UUID idUser;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    @Column(name = "email_verified")
    private boolean emailVerified;
    @Enumerated(EnumType.STRING)
    @Column
    private Status status;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "updated_at")
    @LastModifiedDate
    private Timestamp updatedAt;

    public boolean getEmailVerified() {
        return this.emailVerified;
    }
}

