package cm.landry.atm_machine.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.landry.atm_machine.entity.Account;
import cm.landry.atm_machine.entity.Transaction;
import cm.landry.atm_machine.entity.TransactionType;
import cm.landry.atm_machine.entity.User;
import cm.landry.atm_machine.exception.InsufficientFundsException;
import cm.landry.atm_machine.repository.AccountRepository;
import cm.landry.atm_machine.repository.TransactionRepository;
import cm.landry.atm_machine.repository.UserRepository;
import cm.landry.atm_machine.service.TransactionService;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, 
                                  AccountRepository accountRepository, 
                                  UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Transaction deposit(Long accountId, BigDecimal amount, String description, Long userId) {
        Account account = findAccountById(accountId);
        User user = findUserById(userId);

        Transaction transaction = createTransaction(account, TransactionType.DEPOSIT, amount, description, user);
        account.setBalance(account.getBalance().add(amount));

        return saveTransaction(account, transaction);
    }

    @Override
    public Transaction withdraw(Long accountId, BigDecimal amount, String description, Long userId) throws InsufficientFundsException {
        Account account = findAccountById(accountId);
        User user = findUserById(userId);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for this withdrawal.");
        }

        Transaction transaction = createTransaction(account, TransactionType.WITHDRAWAL, amount.negate(), description, user);
        account.setBalance(account.getBalance().subtract(amount));

        return saveTransaction(account, transaction);
    }

    @Override
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        Account account = findAccountById(accountId);
        return transactionRepository.findByAccountOrderByDateDesc(account);
    }

    @Override
    public List<Transaction> getTransactionsByPeriod(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
        Account account = findAccountById(accountId);
        return transactionRepository.findByAccountAndDateBetween(account, startDate, endDate);
    }

    @Override
    public List<Transaction> getTransactionsByAccountAndDateBetweenAndType(Long accountId, LocalDateTime startDate, LocalDateTime endDate, TransactionType type) {
        Account account = findAccountById(accountId);
        return transactionRepository.findByAccountAndDateBetweenAndTypeOrderByDateDesc(account, startDate, endDate, type);
    }

    @Override
    public List<Transaction> getTransactionsByAccountAndDateBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
        Account account = findAccountById(accountId);
        return transactionRepository.findByAccountAndDateBetweenWithSorting(account, startDate, endDate);
    }

    @Override
    public List<Transaction> getLastTwentyTransactions(Long accountId) {
        Account account = findAccountById(accountId);
        return transactionRepository.findTop20ByAccountOrderByDateDesc(account);
    }

    private Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    private User findUserById(Long userId) {
        return userId != null ? userRepository.findById(userId).orElse(null) : null;
    }

    private Transaction createTransaction(Account account, TransactionType type, BigDecimal amount, String description, User user) {
        return Transaction.builder()
                .account(account)
                .type(type)
                .amount(amount)
                .date(LocalDateTime.now())
                .description(description)
                .user(user)
                .build();
    }

    private Transaction saveTransaction(Account account, Transaction transaction) {
        accountRepository.save(account);
        return transactionRepository.save(transaction);
    }

    @Override
    public boolean transferFunds(Long fromAccountId, Long toAccountId, BigDecimal amount, String description, Long userId) throws InsufficientFundsException {
        Account fromAccount = findAccountById(fromAccountId);
        Account toAccount = findAccountById(toAccountId);
        User user = findUserById(userId);

        // Vérifiez les fonds suffisants
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for this transfer.");
        }

        // Créez les transactions
        Transaction withdrawalTransaction = createTransaction(fromAccount, TransactionType.WITHDRAWAL, amount.negate(), description, user);
        Transaction depositTransaction = createTransaction(toAccount, TransactionType.DEPOSIT, amount, description, user);

        // Effectuez les opérations
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        // Enregistrez les transactions et les comptes
        saveTransaction(fromAccount, withdrawalTransaction);
        saveTransaction(toAccount, depositTransaction);

        return true;
    }
}
