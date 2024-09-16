package cm.landry.atm_machine.service;

import cm.landry.atm_machine.dto.AccountSummary;
import cm.landry.atm_machine.dto.UserSummary;
import cm.landry.atm_machine.entity.Account;
import cm.landry.atm_machine.entity.User;
import cm.landry.atm_machine.repository.AccountRepository;
import cm.landry.atm_machine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Service class for handling account-related operations.
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    /**
     * Retrieves all accounts from the repository.
     *
     * @return a list of all accounts.
     */
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    /**
     * Retrieves an account by its ID.
     *
     * @param id the ID of the account.
     * @return an Optional containing the account if found, otherwise empty.
     */
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    /**
     * Finds an account by its ID and throws an exception if not found.
     *
     * @param accountId the ID of the account.
     * @return the account if found.
     * @throws IllegalArgumentException if the account is not found.
     */
    public Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
    }

    /**
     * Retrieves an account by its account number.
     *
     * @param accountNumber the account number.
     * @return an Optional containing the account if found, otherwise empty.
     */
    public Optional<Account> getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    /**
     * Creates a new account and associates it with a user.
     *
     * @param account the account to create.
     * @param userId  the ID of the user to associate with the account.
     * @return a summary of the created account.
     * @throws IllegalArgumentException if the user is not found.
     */
    @Transactional
    public AccountSummary createAccount(Account account, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        account.setUser(user);
        account.setAccountNumber(generateUniqueAccountNumber());
        
        Account savedAccount = accountRepository.save(account);
        
        return createAccountSummary(savedAccount, user);
    }

    /**
     * Updates the balance of an account.
     *
     * @param accountId  the ID of the account.
     * @param newBalance the new balance to set.
     * @return an Optional containing the updated account if found, otherwise empty.
     */
    public Optional<Account> updateBalance(Long accountId, BigDecimal newBalance) {
        return accountRepository.findById(accountId)
                .map(account -> {
                    Account updatedAccount = account.withBalance(newBalance); // Use method chaining
                    return accountRepository.save(updatedAccount);
                });
    }

    /**
     * Generates a unique account number.
     *
     * @return a unique account number.
     */
    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = generateAccountNumber();
        } while (accountRepository.findByAccountNumber(accountNumber).isPresent());
        return accountNumber;
    }

    /**
     * Generates a random account number.
     *
     * @return a random account number.
     */
    public String generateAccountNumber() {
        Random random = new Random();
        return random.ints(11, 0, 10)
                .mapToObj(String::valueOf)
                .reduce(String::concat)
                .orElseThrow();
    }

    /**
     * Creates an AccountSummary from an Account and User.
     *
     * @param account the account to summarize.
     * @param user    the user associated with the account.
     * @return an AccountSummary object.
     */
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
}
