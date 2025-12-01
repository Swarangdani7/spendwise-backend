package com.swarang.spendwise.dto.categorylimit;

import com.swarang.spendwise.model.CategoryType;

import java.time.LocalDateTime;

public record CategoryLimitResponseDTO(
        Long id,
        Long profileId,
        CategoryType categoryType,
        Double limitAmount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) { }
