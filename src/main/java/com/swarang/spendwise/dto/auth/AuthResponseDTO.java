package com.swarang.spendwise.dto.auth;

public record AuthResponseDTO(
        Long authId,
        String accessToken,
        String tokenType
) {
}
