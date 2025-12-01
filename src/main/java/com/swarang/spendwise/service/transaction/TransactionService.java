package com.swarang.spendwise.service.transaction;

import com.swarang.spendwise.dto.categorylimit.CategorySummaryResponseDTO;
import com.swarang.spendwise.dto.transaction.TransactionFilterDTO;
import com.swarang.spendwise.dto.transaction.TransactionLimitExceededDTO;
import com.swarang.spendwise.dto.transaction.TransactionRequestDTO;
import com.swarang.spendwise.dto.transaction.TransactionResponseDTO;
import com.swarang.spendwise.model.CategoryType;
import com.swarang.spendwise.model.ProfileUser;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    TransactionLimitExceededDTO createTransaction(ProfileUser profileUser, TransactionRequestDTO transactionRequestDTO);

    TransactionLimitExceededDTO updateTransaction(Long transactionId, TransactionRequestDTO transactionRequestDTO, ProfileUser profileUser);

    void deleteTransaction(Long transactionId, ProfileUser profileUser);

    TransactionResponseDTO getTransactionById(Long transactionId, ProfileUser profileUser);

    Page<TransactionResponseDTO> getAllTransactions(int page, int size, ProfileUser profileUser);

    BigDecimal getTotalIncome(ProfileUser profileUser);

    BigDecimal getTotalExpense(ProfileUser profileUser);

    List<TransactionResponseDTO> getRecentTransactions(ProfileUser profileUser);

    List<CategorySummaryResponseDTO> getCategorySummary(ProfileUser profileUser);

    List<TransactionResponseDTO> filterTransactions(ProfileUser profileUser, TransactionFilterDTO transactionFilterDTO);
}
