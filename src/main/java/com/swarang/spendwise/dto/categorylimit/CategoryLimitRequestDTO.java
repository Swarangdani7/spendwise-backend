package com.swarang.spendwise.dto.categorylimit;

import com.swarang.spendwise.model.CategoryType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record CategoryLimitRequestDTO(

        @NotNull(message = "Category is required")
        CategoryType categoryType,

        @NotNull
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        Double limitAmount
) { }
