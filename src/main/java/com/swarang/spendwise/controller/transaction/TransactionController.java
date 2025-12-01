package com.swarang.spendwise.controller.transaction;

import com.swarang.spendwise.dto.categorylimit.CategorySummaryResponseDTO;
import com.swarang.spendwise.dto.transaction.TransactionFilterDTO;
import com.swarang.spendwise.dto.transaction.TransactionLimitExceededDTO;
import com.swarang.spendwise.dto.transaction.TransactionRequestDTO;
import com.swarang.spendwise.dto.transaction.TransactionResponseDTO;
import com.swarang.spendwise.model.AuthUser;
import com.swarang.spendwise.model.ProfileUser;
import com.swarang.spendwise.service.auth.TokenService;
import com.swarang.spendwise.service.transaction.TransactionService;
import com.swarang.spendwise.utils.AuthUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Tag(name = "Transactions", description = "Transaction management APIs")
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final TokenService tokenService;
    private final AuthUtils authUtils;

    @PostMapping
    public ResponseEntity<TransactionLimitExceededDTO> create(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
        AuthUser authUser = authUtils.getAuthUser();
        ProfileUser profileUser = tokenService.getProfileCredentials(authUser);

        TransactionLimitExceededDTO response = transactionService.createTransaction(profileUser, transactionRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionLimitExceededDTO> update(@PathVariable("id") Long transactionId,
                                                         @Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {

        AuthUser authUser = authUtils.getAuthUser();
        ProfileUser profileUser = tokenService.getProfileCredentials(authUser);

        TransactionLimitExceededDTO response = transactionService.updateTransaction(transactionId, transactionRequestDTO, profileUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long transactionId) {
        AuthUser authUser = authUtils.getAuthUser();
        ProfileUser profileUser = tokenService.getProfileCredentials(authUser);

        transactionService.deleteTransaction(transactionId, profileUser);
        return ResponseEntity.ok(Map.of("message", "Transaction deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getById(@PathVariable("id") Long transactionId) {
        AuthUser authUser = authUtils.getAuthUser();
        ProfileUser profileUser = tokenService.getProfileCredentials(authUser);

        TransactionResponseDTO response = transactionService.getTransactionById(transactionId, profileUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDTO>> getTransactions(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size) {

        AuthUser authUser = authUtils.getAuthUser();
        ProfileUser profileUser = tokenService.getProfileCredentials(authUser);

        Page<TransactionResponseDTO> response = transactionService.getAllTransactions(page, size, profileUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category-summary")
    public ResponseEntity<List<CategorySummaryResponseDTO>> getCategorySummary() {
        AuthUser authUser = authUtils.getAuthUser();
        ProfileUser profileUser = tokenService.getProfileCredentials(authUser);

        List<CategorySummaryResponseDTO> response = transactionService.getCategorySummary(profileUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TransactionResponseDTO>> filterByType(@ModelAttribute TransactionFilterDTO transactionFilterDTO) {
        AuthUser authUser = authUtils.getAuthUser();
        ProfileUser profileUser = tokenService.getProfileCredentials(authUser);


        List<TransactionResponseDTO> response = transactionService.filterTransactions(profileUser, transactionFilterDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/weekly")
    public ResponseEntity<?> getWeeklyTransactions() {
        AuthUser authUser = authUtils.getAuthUser();
        ProfileUser profileUser = tokenService.getProfileCredentials(authUser);

        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(7);

        TransactionFilterDTO transactionFilterDTO = new TransactionFilterDTO(null, null, start, end);
        return ResponseEntity.ok(transactionService.filterTransactions(profileUser, transactionFilterDTO));
    }

    @GetMapping("/monthly")
    public ResponseEntity<?> getMonthlyTransactions() {
        AuthUser authUser = authUtils.getAuthUser();
        ProfileUser profileUser = tokenService.getProfileCredentials(authUser);

        LocalDate end = LocalDate.now();
        LocalDate start = end.withDayOfMonth(1);

        TransactionFilterDTO transactionFilterDTO = new TransactionFilterDTO(null, null, start, end);
        return ResponseEntity.ok(transactionService.filterTransactions(profileUser, transactionFilterDTO));
    }
}
