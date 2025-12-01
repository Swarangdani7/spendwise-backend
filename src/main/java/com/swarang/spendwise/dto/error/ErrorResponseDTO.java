package com.swarang.spendwise.dto.error;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        HttpStatus error,
        String message
) { }
