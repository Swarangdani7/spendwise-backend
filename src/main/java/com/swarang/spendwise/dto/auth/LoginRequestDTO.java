package com.swarang.spendwise.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "Email field required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password field required")
        String password
) { }
