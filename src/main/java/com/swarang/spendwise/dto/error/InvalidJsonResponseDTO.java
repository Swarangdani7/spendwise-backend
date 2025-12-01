package com.swarang.spendwise.dto.error;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record InvalidJsonResponseDTO(
        LocalDateTime timestamp,
        int status,
        HttpStatus error,
        String message,
        List<Map<String, String>> errors
) { }
