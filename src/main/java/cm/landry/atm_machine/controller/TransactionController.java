package cm.landry.atm_machine.controller;

import cm.landry.atm_machine.entity.Account;
import cm.landry.atm_machine.entity.Transaction;
import cm.landry.atm_machine.entity.TransactionType;
import cm.landry.atm_machine.exception.InsufficientFundsException;
import cm.landry.atm_machine.request.DepositRequest;
import cm.landry.atm_machine.request.WithdrawRequest;
import cm.landry.atm_machine.service.AccountService;
import cm.landry.atm_machine.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controller class for managing transactions.
 */
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;

    /**
     * Handles deposits into accounts.
     *
     * @param request the deposit request containing account ID, amount,
     *                description, and optional user ID.
     * @return a ResponseEntity containing the created transaction.
     */
    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@Valid @RequestBody DepositRequest request) {
        // Convertir userId en Long, qui peut être null
        Long userId = request.getUserId(); // Note : Assurez-vous que la méthode getter est correcte

        // Appel de la méthode deposit avec accountId, amount, description et userId
        Transaction transaction = transactionService.deposit(
                request.getAccountId(),
                request.getAmount(),
                request.getDescription(),
                userId);

        return ResponseEntity.ok(transaction);
    }

    /**
     * Handles withdrawals from accounts.
     *
     * @param request the withdrawal request containing account ID, amount,
     *                description, and optional user ID.
     * @return a ResponseEntity containing the created transaction or an error
     *         response in case of insufficient funds.
     */
    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@Valid @RequestBody WithdrawRequest request) {
        try {
            Transaction transaction = transactionService.withdraw(
                    request.getAccountId(),
                    request.getAmount(),
                    request.getDescription(),
                    request.getUserId());

            return ResponseEntity.ok(transaction);
        } catch (InsufficientFundsException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Retrieves all transactions for a specified account.
     *
     * @param accountId the ID of the account.
     * @return a ResponseEntity containing a list of transactions for the account.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Transaction>> getAllTransactions(@RequestParam Long accountId) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }

    /**
     * Retrieves the last 20 transactions for a specified account.
     *
     * @param accountId the ID of the account.
     * @return a ResponseEntity containing a list of the last 20 transactions for
     *         the account.
     */
    @GetMapping("/last20")
    public ResponseEntity<List<Transaction>> getLastTwentyTransactions(@RequestParam Long accountId) {
        List<Transaction> transactions = transactionService.getLastTwentyTransactions(accountId);
        return ResponseEntity.ok(transactions);
    }

    /**
     * Retrieves transactions filtered by account, date range, and optionally by
     * type.
     *
     * @param accountId the ID of the account.
     * @param startDate the start date of the range.
     * @param endDate   the end date of the range.
     * @param type      optional transaction type for filtering.
     * @return a list of transactions matching the criteria.
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountAndDateBetweenAndType(
            @RequestParam Long accountId,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam(required = false) TransactionType type) {

        Account account = accountService.getAccountById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));

        List<Transaction> transactions;
        if (type != null) {
            transactions = transactionService.getTransactionsByAccountAndDateBetweenAndType(account, startDate, endDate,
                    type);
        } else {
            transactions = transactionService.getTransactionsByAccountAndDateBetween(account, startDate, endDate);
        }

        return ResponseEntity.ok(transactions);
    }
}
