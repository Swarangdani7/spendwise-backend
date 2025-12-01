package com.swarang.spendwise.dto.categorylimit;

import com.swarang.spendwise.model.CategoryType;

public record CategorySummaryResponseDTO(
        CategoryType categoryType,
        Double amountSpent,
        Double percentage
) { }
