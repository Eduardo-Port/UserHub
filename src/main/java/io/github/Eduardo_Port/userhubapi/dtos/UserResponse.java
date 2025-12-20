package io.github.Eduardo_Port.userhubapi.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public record UserResponse(
        String name,
        String email,
        boolean emailVerified,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
        Timestamp createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
        Timestamp updatedAt
) {
    public UserResponse(String name, String email, boolean emailVerified, Timestamp createdAt, Timestamp updatedAt) {
        this.name = name;
        this.email = email;
        this.emailVerified = emailVerified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
