package io.github.Eduardo_Port.userhubapi.dtos;

import jakarta.validation.constraints.*;

public record UserRequest(
        @NotNull
        @NotBlank
        String name,
        @NotNull
        @NotBlank
        @Email
        String email,
        @NotNull
        @NotBlank
        @Size(min = 6, max = 50)
        String password
) {
}
