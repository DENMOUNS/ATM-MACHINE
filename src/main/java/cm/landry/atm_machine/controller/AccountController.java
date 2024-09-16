package cm.landry.atm_machine.controller;

import cm.landry.atm_machine.dto.AccountSummary;
import cm.landry.atm_machine.entity.Account;
import cm.landry.atm_machine.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST controller for managing accounts.
 */
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * Retrieves all accounts.
     *
     * @return a ResponseEntity containing the list of all accounts.
     */
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    /**
     * Retrieves an account by its ID.
     *
     * @param id the ID of the account.
     * @return a ResponseEntity containing the account if found, otherwise not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves an account by its account number.
     *
     * @param accountNumber the account number.
     * @return a ResponseEntity containing the account if found, otherwise not found.
     */
    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<Account> getAccountByAccountNumber(@PathVariable String accountNumber) {
        return accountService.getAccountByAccountNumber(accountNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new account for a specific user.
     *
     * @param account the account to create.
     * @param userId  the ID of the user associated with the account.
     * @return a ResponseEntity containing the AccountSummary of the created account.
     */
    @PostMapping("/user/{userId}")
    public ResponseEntity<AccountSummary> createAccount(@RequestBody Account account, @PathVariable Long userId) {
        AccountSummary summary = accountService.createAccount(account, userId);
        return ResponseEntity.ok(summary);
    }

    /**
     * Updates the balance of an account.
     *
     * @param id      the ID of the account.
     * @param balance the new balance to set.
     * @return a ResponseEntity containing the updated account if found, otherwise not found.
     */
    @PutMapping("/{id}/balance")
    public ResponseEntity<Account> updateBalance(@PathVariable Long id, @RequestBody BigDecimal balance) {
        return accountService.updateBalance(id, balance)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
