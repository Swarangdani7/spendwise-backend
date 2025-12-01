package com.swarang.spendwise.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequestDTO(
        @NotBlank(message = "First name required")
        String firstName,

        @NotBlank(message = "Last name required")
        String lastName,

        @NotBlank(message = "Email field required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password field required")
        @Size(min = 8, message = "Password must contain at least 8 characters")
        String password
) { }
