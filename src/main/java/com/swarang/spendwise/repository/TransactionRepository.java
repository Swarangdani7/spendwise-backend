package com.swarang.spendwise.repository;

import com.swarang.spendwise.dto.categorylimit.CategorySummaryResponseDTO;
import com.swarang.spendwise.model.CategoryType;
import com.swarang.spendwise.model.Transaction;
import com.swarang.spendwise.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    Optional<Transaction> findByIdAndProfileUserId(Long transactionId, Long profileUserId);

    Page<Transaction> findAllByProfileUserId(Long profileUserId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.profileUser.id = :profileUserId AND t.transactionType = :type")
    BigDecimal sumAmountByProfileUserAndType(
            @Param("profileUserId") Long profileUserId,
            @Param("type") TransactionType type
    );

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.profileUser.id = :profileUserId
            AND t.categoryType = :type
            AND t.transactionType = com.swarang.spendwise.model.TransactionType.EXPENSE
            AND t.transactionDate BETWEEN :start AND :end
            """)
    BigDecimal getMonthlyCategoryExpense(
            @Param("profileUserId") Long profileUserId,
            @Param("type") CategoryType type,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    List<Transaction> findTop5ByProfileUserIdOrderByTransactionDateDesc(Long profileUserId);

    @Query("""
            SELECT new com.swarang.spendwise.dto.categorylimit.CategorySummaryResponseDTO(
                t.categoryType,
                SUM(t.amount),
                0.0
            )
            FROM Transaction t
            WHERE t.profileUser.id = :profileUserId
            AND t.transactionType = com.swarang.spendwise.model.TransactionType.EXPENSE
            GROUP BY t.categoryType
            ORDER BY t.categoryType
            """)
    List<CategorySummaryResponseDTO> getExpenseSummaryByCategory(@Param("profileUserId") Long profileUserId);
}
