package com.swarang.spendwise.dto.dashboard;

import com.swarang.spendwise.dto.transaction.TransactionResponseDTO;

import java.util.List;

public record DashboardResponseDTO(
        Double totalIncome,
        Double totalExpense,
        Double balance,
        List<TransactionResponseDTO> recentTransactions
) { }
