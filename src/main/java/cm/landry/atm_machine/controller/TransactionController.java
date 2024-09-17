package cm.landry.atm_machine.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cm.landry.atm_machine.entity.Transaction;
import cm.landry.atm_machine.entity.TransactionType;
import cm.landry.atm_machine.exception.InsufficientFundsException;
import cm.landry.atm_machine.request.DepositRequest;
import cm.landry.atm_machine.request.TransferRequest;
import cm.landry.atm_machine.request.WithdrawRequest;
import cm.landry.atm_machine.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@Valid @RequestBody DepositRequest request) {
        Long userId = request.getUserId();
        Transaction transaction = transactionService.deposit(
                request.getAccountId(),
                request.getAmount(),
                request.getDescription(),
                userId);
        return ResponseEntity.ok(transaction);
    }

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

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@Valid @RequestBody TransferRequest request) {
        try {
            transactionService.transferFunds(request.getFromAccountId(), request.getToAccountId(), request.getAmount(), request.getDescription(), request.getUserId());
            return ResponseEntity.ok("Transfer successful");
        } catch (InsufficientFundsException e) {
            return ResponseEntity.badRequest().body("Insufficient funds for this transfer.");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Transaction>> getAllTransactions(@RequestParam Long accountId) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/last20")
    public ResponseEntity<List<Transaction>> getLastTwentyTransactions(@RequestParam Long accountId) {
        List<Transaction> transactions = transactionService.getLastTwentyTransactions(accountId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountAndDateBetweenAndType(
            @RequestParam Long accountId,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam(required = false) TransactionType type) {

        List<Transaction> transactions;
        if (type != null) {
            transactions = transactionService.getTransactionsByAccountAndDateBetweenAndType(
                    accountId, startDate, endDate, type);
        } else {
            transactions = transactionService.getTransactionsByAccountAndDateBetween(accountId, startDate, endDate);
        }

        return ResponseEntity.ok(transactions);
    }
}
