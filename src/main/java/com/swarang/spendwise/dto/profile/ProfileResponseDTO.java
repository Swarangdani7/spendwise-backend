package com.swarang.spendwise.dto.profile;

public record ProfileResponseDTO(
        Long profileId,
        Long authId,
        String firstName,
        String lastName
) { }
