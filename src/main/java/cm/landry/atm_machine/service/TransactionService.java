package cm.landry.atm_machine.service;

import cm.landry.atm_machine.entity.Account;
import cm.landry.atm_machine.entity.Transaction;
import cm.landry.atm_machine.entity.TransactionType;
import cm.landry.atm_machine.entity.User;
import cm.landry.atm_machine.exception.InsufficientFundsException;
import cm.landry.atm_machine.repository.AccountRepository;
import cm.landry.atm_machine.repository.TransactionRepository;
import cm.landry.atm_machine.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing transactions.
 */
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository,
            AccountRepository accountRepository,
            UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    /**
     * Deposits an amount into the specified account.
     *
     * @param accountId   the ID of the account to deposit into.
     * @param amount      the amount to deposit.
     * @param description the description of the transaction.
     * @param userId      optional ID of the user making the deposit.
     * @return the created transaction.
     */
    @Transactional
    public Transaction deposit(Long accountId, BigDecimal amount, String description, Long userId) {
        // Récupérer l'objet Account correspondant à accountId
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        // Récupérer l'utilisateur s'il est présent
        User user = (userId != null) ? userRepository.findById(userId).orElse(null) : null;

        LocalDateTime now = LocalDateTime.now();

        // Créer la transaction
        Transaction transaction = Transaction.builder()
                .account(account)
                .type(TransactionType.DEPOSIT)
                .amount(amount)
                .date(now)
                .description(description)
                .user(user) // Assigner l'utilisateur s'il existe
                .build();

        // Sauvegarder la transaction et mettre à jour le solde du compte
        Transaction savedTransaction = transactionRepository.save(transaction);
        accountRepository.save(account.withBalance(account.getBalance().add(amount)));

        return savedTransaction;
    }

    /**
     * Withdraws an amount from the specified account.
     *
     * @param accountId   the ID of the account to withdraw from.
     * @param amount      the amount to withdraw.
     * @param description the description of the transaction.
     * @param userId      optional ID of the user making the withdrawal.
     * @return the created transaction.
     * @throws InsufficientFundsException if the account does not have enough funds.
     */
    @Transactional
    public Transaction withdraw(Long accountId, BigDecimal amount, String description, Long userId)
            throws InsufficientFundsException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        User user = userRepository.findById(userId)
                .orElse(null); // L'utilisateur peut être null

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for this withdrawal.");
        }

        Transaction transaction = Transaction.builder()
                .account(account)
                .type(TransactionType.WITHDRAWAL)
                .amount(amount.negate())
                .date(LocalDateTime.now())
                .description(description)
                .user(user)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        return savedTransaction;
    }

    /**
     * Retrieves all transactions for a specified account.
     *
     * @param accountId the ID of the account.
     * @return a list of transactions for the account.
     */
    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        return transactionRepository.findByAccountOrderByDateDesc(account);
    }

    /**
     * Retrieves transactions for a specified account within a date range.
     *
     * @param accountId the ID of the account.
     * @param startDate the start date of the range.
     * @param endDate   the end date of the range.
     * @return a list of transactions for the account within the date range.
     */
    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByPeriod(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        return transactionRepository.findByAccountAndDateBetween(account, startDate, endDate);
    }

    /**
     * Retrieves transactions for a specified account within a date range and of a
     * specific type.
     *
     * @param account   the account.
     * @param startDate the start date of the range.
     * @param endDate   the end date of the range.
     * @param type      the type of transaction.
     * @return a list of transactions matching the criteria.
     */
    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByAccountAndDateBetweenAndType(Account account,
            LocalDateTime startDate,
            LocalDateTime endDate,
            TransactionType type) {
        return transactionRepository.findByAccountAndDateBetweenAndTypeOrderByDateDesc(account, startDate, endDate,
                type);
    }

    /**
     * Retrieves the last 20 transactions for a specified account.
     *
     * @param accountId the ID of the account.
     * @return a list of the last 20 transactions for the account.
     */
    @Transactional(readOnly = true)
    public List<Transaction> getLastTwentyTransactions(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        return transactionRepository.findTop20ByAccountOrderByDateDesc(account);
    }

    /**
     * Retrieves transactions for a specified account within a date range.
     *
     * @param account   the account.
     * @param startDate the start date of the range.
     * @param endDate   the end date of the range.
     * @return a list of transactions for the account within the date range.
     */
    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByAccountAndDateBetween(Account account,
            LocalDateTime startDate,
            LocalDateTime endDate) {
        return transactionRepository.findByAccountAndDateBetween(account, startDate, endDate);
    }
}
