package io.github.Eduardo_Port.userhubapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @NotNull
        @NotBlank
        String name,
        String email,
        @NotNull
        @NotBlank
        String password
) {
}
