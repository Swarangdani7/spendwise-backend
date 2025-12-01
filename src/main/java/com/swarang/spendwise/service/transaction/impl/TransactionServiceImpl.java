package com.swarang.spendwise.service.transaction.impl;

import com.swarang.spendwise.dto.categorylimit.CategorySummaryResponseDTO;
import com.swarang.spendwise.dto.transaction.TransactionFilterDTO;
import com.swarang.spendwise.dto.transaction.TransactionLimitExceededDTO;
import com.swarang.spendwise.dto.transaction.TransactionRequestDTO;
import com.swarang.spendwise.dto.transaction.TransactionResponseDTO;
import com.swarang.spendwise.exception.TransactionNotFoundException;
import com.swarang.spendwise.model.*;
import com.swarang.spendwise.repository.TransactionRepository;
import com.swarang.spendwise.service.transaction.TransactionService;
import com.swarang.spendwise.utils.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public TransactionLimitExceededDTO createTransaction(ProfileUser profileUser, TransactionRequestDTO transactionRequestDTO) {
        Transaction transaction = Transaction.builder()
                .itemName(transactionRequestDTO.itemName())
                .amount(transactionRequestDTO.amount())
                .transactionType(transactionRequestDTO.transactionType())
                .categoryType(transactionRequestDTO.categoryType())
                .transactionDate(transactionRequestDTO.transactionDate())
                .updatedAt(LocalDateTime.now())
                .profileUser(profileUser)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        profileUser.getTransactions().add(savedTransaction);

        Boolean categoryLimitExceeded = false;

        if (transactionRequestDTO.transactionType() == TransactionType.EXPENSE && transactionRequestDTO.categoryType() != null) {
            categoryLimitExceeded = isCategoryLimitExceeded(profileUser, transactionRequestDTO.categoryType(), transactionRequestDTO.amount());
        }

        return generateCategoryLimitExceededResponse(savedTransaction, categoryLimitExceeded);
    }

    @Override
    @Transactional
    public TransactionLimitExceededDTO updateTransaction(Long transactionId, TransactionRequestDTO transactionRequestDTO, ProfileUser profileUser) {
        Transaction transaction = transactionRepository.findByIdAndProfileUserId(transactionId, profileUser.getId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + transactionId));

        transaction.setItemName(transactionRequestDTO.itemName());
        transaction.setAmount(transactionRequestDTO.amount());
        transaction.setCategoryType(transactionRequestDTO.categoryType());
        transaction.setTransactionType(transactionRequestDTO.transactionType());
        transaction.setTransactionDate(transactionRequestDTO.transactionDate());
        transaction.setUpdatedAt(LocalDateTime.now());

        Transaction updatedTransaction = transactionRepository.save(transaction);

        Boolean categoryLimitExceeded = false;

        if (transactionRequestDTO.transactionType() == TransactionType.EXPENSE && transactionRequestDTO.categoryType() != null) {
            categoryLimitExceeded = isCategoryLimitExceeded(profileUser, transactionRequestDTO.categoryType(), transactionRequestDTO.amount());
        }

        return generateCategoryLimitExceededResponse(updatedTransaction, categoryLimitExceeded);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long transactionId, ProfileUser profileUser) {
        Transaction transaction = transactionRepository.findByIdAndProfileUserId(transactionId, profileUser.getId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + transactionId));

        transactionRepository.delete(transaction);
    }

    @Override
    @Transactional
    public TransactionResponseDTO getTransactionById(Long transactionId, ProfileUser profileUser) {
        Transaction transaction = transactionRepository.findByIdAndProfileUserId(transactionId, profileUser.getId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + transactionId));

        return generateResponse(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionResponseDTO> getAllTransactions(int page, int size, ProfileUser profileUser) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("transactionDate").descending());
        Page<Transaction> result = transactionRepository.findAllByProfileUserId(profileUser.getId(), pageable);
        return result.map(this::generateResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalIncome(ProfileUser profileUser) {
        BigDecimal sum = transactionRepository.sumAmountByProfileUserAndType(profileUser.getId(), TransactionType.INCOME);
        return sum != null ? sum : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalExpense(ProfileUser profileUser) {
        BigDecimal sum = transactionRepository.sumAmountByProfileUserAndType(profileUser.getId(), TransactionType.EXPENSE);
        return sum != null ? sum : BigDecimal.ZERO;
    }

    @Override
    @Transactional
    public List<TransactionResponseDTO> getRecentTransactions(ProfileUser profileUser) {
        List<Transaction> transactionList = transactionRepository.findTop5ByProfileUserIdOrderByTransactionDateDesc(profileUser.getId());
        return transactionList.stream().map(this::generateResponse).toList();
    }

    @Override
    @Transactional
    public List<TransactionResponseDTO> filterTransactions(ProfileUser profileUser, TransactionFilterDTO transactionFilterDTO) {
        Specification<Transaction> spec = SpecificationUtils.withFilter(profileUser, transactionFilterDTO);
        System.out.println(spec.toString());
        List<Transaction> transactionList = transactionRepository.findAll(spec);

        return transactionList
                .stream()
                .map(this::generateResponse)
                .toList();
    }

    @Override
    @Transactional
    public List<CategorySummaryResponseDTO> getCategorySummary(ProfileUser profileUser) {
        List<CategorySummaryResponseDTO> summaryList = transactionRepository.getExpenseSummaryByCategory(profileUser.getId());

        if (summaryList == null || summaryList.isEmpty()) {
            return List.of();
        }

        double totalExpense = summaryList.stream()
                .map(CategorySummaryResponseDTO::amountSpent)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();

        return summaryList.stream()
                .map(dto -> {
                    double amountSpent = dto.amountSpent() == null ? 0.0 : dto.amountSpent();
                    double percentage = (amountSpent / totalExpense) * 100.0;

                    return new CategorySummaryResponseDTO(
                            dto.categoryType(),
                            amountSpent,
                            (double) Math.round(percentage)
                    );
                })
                .toList();
    }

    private Boolean isCategoryLimitExceeded(ProfileUser profileUser, CategoryType categoryType, Double amount) {
        double threshold = profileUser.getCategoryLimits()
                .stream()
                .filter(categoryLimit -> categoryLimit.getCategoryType().equals(categoryType))
                .map(CategoryLimit::getLimitAmount)
                .findFirst()
                .orElse(0.0);

        Double totalSpent = transactionRepository.getMonthlyCategoryExpense(
                profileUser.getId(),
                categoryType,
                LocalDate.now().withDayOfMonth(1),
                LocalDate.now()
        ).doubleValue();

        double projected = totalSpent + amount;
        // notify user
        return threshold > 0.0 && projected >= threshold;
    }

    private TransactionResponseDTO generateResponse(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getProfileUser().getId(),
                transaction.getItemName(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getCategoryType(),
                transaction.getTransactionDate(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }

    private TransactionLimitExceededDTO generateCategoryLimitExceededResponse(Transaction transaction, Boolean categoryLimitExceeded){
        return new TransactionLimitExceededDTO(
                transaction.getId(),
                transaction.getProfileUser().getId(),
                transaction.getItemName(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getCategoryType(),
                transaction.getTransactionDate(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt(),
                categoryLimitExceeded
        );
    }
}

