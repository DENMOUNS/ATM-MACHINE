package cm.landry.atm_machine.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import cm.landry.atm_machine.dto.AccountSummary;
import cm.landry.atm_machine.entity.Account;

public interface AccountOperations {
    List<Account> getAllAccounts();
    Optional<Account> getAccountById(Long id);
    Account findAccountById(Long accountId);
    AccountSummary createAccount(Account account, Long userId);
    Optional<Account> updateBalance(Long accountId, BigDecimal newBalance);

    void increaseBalance(Account account, BigDecimal amount);
    void decreaseBalance(Account account, BigDecimal amount);
}
