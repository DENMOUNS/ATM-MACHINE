package cm.landry.atm_machine.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.landry.atm_machine.dto.AccountSummary;
import cm.landry.atm_machine.dto.UserSummary;
import cm.landry.atm_machine.entity.Account;
import cm.landry.atm_machine.entity.User;
import cm.landry.atm_machine.repository.AccountRepository;
import cm.landry.atm_machine.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService implements AccountOperations {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountNumberGeneratorService accountNumberGeneratorService;

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
    }

    @Transactional
    @Override
    public AccountSummary createAccount(Account account, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        account.setUser(user);
        account.setAccountNumber(accountNumberGeneratorService.generateUniqueAccountNumber());

        Account savedAccount = accountRepository.save(account);

        return createAccountSummary(savedAccount, user);
    }

    @Override
    public Optional<Account> updateBalance(Long accountId, BigDecimal newBalance) {
        return accountRepository.findById(accountId)
                .map(account -> {
                    Account updatedAccount = account.withBalance(newBalance); // Immutability pattern
                    return accountRepository.save(updatedAccount);
                });
    }

    private AccountSummary createAccountSummary(Account account, User user) {
        return AccountSummary.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountName(account.getAccountType().toString()) // Convert AccountType to String
                .pin(account.getPin())
                .balance(account.getBalance())
                .user(new UserSummary(user.getId(), user.getUsername(), user.getEmail()))
                .build();
    }

    @Override
    public void increaseBalance(Account account, BigDecimal amount) {
        Account updatedAccount = account.withBalance(account.getBalance().add(amount));
        accountRepository.save(updatedAccount);
    }

    @Override
    public void decreaseBalance(Account account, BigDecimal amount) {
        Account updatedAccount = account.withBalance(account.getBalance().subtract(amount));
        accountRepository.save(updatedAccount);
    }
}
