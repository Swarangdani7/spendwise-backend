package com.swarang.spendwise.dto.transaction;

import com.swarang.spendwise.model.CategoryType;
import com.swarang.spendwise.model.TransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransactionLimitExceededDTO(
        Long transactionId,
        Long profileId,
        String itemName,
        Double amount,
        TransactionType transactionType,
        CategoryType categoryType,
        LocalDate transactionDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean categoryLimitExceeded
) {
}
