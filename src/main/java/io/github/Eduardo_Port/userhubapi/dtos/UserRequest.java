package io.github.Eduardo_Port.userhubapi.dtos;

import jakarta.validation.constraints.*;

public record UserRequest(
        @NotNull
        @NotBlank
        @Pattern(regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ\\s]+$",
                message = "O nome deve conter apenas letras e espaços.")
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
