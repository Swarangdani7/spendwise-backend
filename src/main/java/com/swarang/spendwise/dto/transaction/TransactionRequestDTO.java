package com.swarang.spendwise.dto.transaction;

import com.swarang.spendwise.model.CategoryType;
import com.swarang.spendwise.model.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record TransactionRequestDTO(

        @NotBlank(message = "Item name is required")
        String itemName,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        Double amount,

        @NotNull(message = "Transaction type is required")
        TransactionType transactionType,

        @NotNull(message = "Category is required")
        CategoryType categoryType,

        @NotNull(message = "Transaction date is required")
        @PastOrPresent(message = "Transaction date cannot be in the future")
        LocalDate transactionDate
) { }
