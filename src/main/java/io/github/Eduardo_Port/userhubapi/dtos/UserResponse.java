package io.github.Eduardo_Port.userhubapi.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.net.URI;
import java.sql.Timestamp;
import java.util.UUID;

public record UserResponse(
        UUID idUSer,
        String name,
        String email,
        boolean emailVerified,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
        Timestamp createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
        Timestamp updatedAt,
        URI location
) {
    public UserResponse(UUID idUSer, String name, String email, boolean emailVerified, Timestamp createdAt, Timestamp updatedAt) {
        this(idUSer, name, email, emailVerified, createdAt,updatedAt, null);
    }

    public UserResponse(UUID idUSer, String name, String email, boolean emailVerified, Timestamp createdAt, Timestamp updatedAt, URI location) {
        this.idUSer = idUSer;
        this.name = name;
        this.email = email;
        this.emailVerified = emailVerified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.location = location;
    }
}
