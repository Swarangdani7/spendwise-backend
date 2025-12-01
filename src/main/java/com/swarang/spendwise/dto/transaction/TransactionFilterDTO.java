package com.swarang.spendwise.dto.transaction;

import com.swarang.spendwise.model.CategoryType;
import com.swarang.spendwise.model.TransactionType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record TransactionFilterDTO(
        TransactionType type,
        CategoryType category,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate start,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate end
) {
}
