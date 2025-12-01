package com.swarang.spendwise.service.dashboard.impl;

import com.swarang.spendwise.dto.dashboard.DashboardResponseDTO;
import com.swarang.spendwise.dto.transaction.TransactionResponseDTO;
import com.swarang.spendwise.model.ProfileUser;
import com.swarang.spendwise.service.dashboard.DashboardService;
import com.swarang.spendwise.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final TransactionService transactionService;

    @Override
    @Transactional
    public DashboardResponseDTO getDashboardDetails(ProfileUser profileUser) {

        Double totalIncome = transactionService.getTotalIncome(profileUser).doubleValue();
        Double totalExpense = transactionService.getTotalExpense(profileUser).doubleValue();
        Double balance = totalIncome - totalExpense;
        List<TransactionResponseDTO> recentTransactions = transactionService.getRecentTransactions(profileUser);

        DashboardResponseDTO response = new DashboardResponseDTO(
                totalIncome,
                totalExpense,
                balance,
                recentTransactions
        );
        return response;
    }
}
