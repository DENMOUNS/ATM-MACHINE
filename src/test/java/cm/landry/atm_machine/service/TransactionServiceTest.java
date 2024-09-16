package cm.landry.atm_machine.service;

import cm.landry.atm_machine.entity.Account;
import cm.landry.atm_machine.entity.Transaction;
import cm.landry.atm_machine.entity.TransactionType;
import cm.landry.atm_machine.entity.User;
import cm.landry.atm_machine.exception.InsufficientFundsException;
import cm.landry.atm_machine.repository.AccountRepository;
import cm.landry.atm_machine.repository.TransactionRepository;
import cm.landry.atm_machine.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    void testDeposit() {
        Long accountId = 1L;
        BigDecimal amount = new BigDecimal("100.00");
        String description = "Deposit test";
        Long userId = 2L;

        Account account = new Account();
        account.setId(accountId);
        account.setBalance(BigDecimal.ZERO);

        User user = new User();
        user.setId(userId);

        Transaction transaction = Transaction.builder()
                .account(account)
                .type(TransactionType.DEPOSIT)
                .amount(amount)
                .date(LocalDateTime.now())
                .description(description)
                .user(user)
                .build();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionService.deposit(accountId, amount, description, userId);

        assertNotNull(result);
        assertEquals(TransactionType.DEPOSIT, result.getType());
        assertEquals(amount, result.getAmount());
        assertEquals(description, result.getDescription());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testWithdrawWithSufficientFunds() throws InsufficientFundsException {
        Long accountId = 1L;
        BigDecimal amount = new BigDecimal("50.00");
        String description = "Withdraw test";
        Long userId = 2L;

        Account account = new Account();
        account.setId(accountId);
        account.setBalance(new BigDecimal("100.00"));

        User user = new User();
        user.setId(userId);

        Transaction transaction = Transaction.builder()
                .account(account)
                .type(TransactionType.WITHDRAWAL)
                .amount(amount.negate())
                .date(LocalDateTime.now())
                .description(description)
                .user(user)
                .build();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionService.withdraw(accountId, amount, description, userId);

        assertNotNull(result);
        assertEquals(TransactionType.WITHDRAWAL, result.getType());
        assertEquals(amount.negate(), result.getAmount());
        assertEquals(description, result.getDescription());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testWithdrawWithInsufficientFunds() {
        Long accountId = 1L;
        BigDecimal amount = new BigDecimal("150.00");
        String description = "Withdraw test";
        Long userId = 2L;

        Account account = new Account();
        account.setId(accountId);
        account.setBalance(new BigDecimal("100.00"));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        assertThrows(InsufficientFundsException.class, () -> 
            transactionService.withdraw(accountId, amount, description, userId)
        );

        verify(transactionRepository, times(0)).save(any(Transaction.class));
        verify(accountRepository, times(0)).save(any(Account.class));
    }

    @Test
    void testGetTransactionsByAccountId() {
        Long accountId = 1L;
        Account account = new Account();
        account.setId(accountId);

        Transaction transaction = Transaction.builder()
                .account(account)
                .type(TransactionType.DEPOSIT)
                .amount(new BigDecimal("100.00"))
                .date(LocalDateTime.now())
                .description("Deposit test")
                .build();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(transactionRepository.findByAccountOrderByDateDesc(account)).thenReturn(Collections.singletonList(transaction));

        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);

        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(transaction, transactions.get(0));
    }

    @Test
    void testGetTransactionsByPeriod() {
        Long accountId = 1L;
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();
        Account account = new Account();
        account.setId(accountId);

        Transaction transaction = Transaction.builder()
                .account(account)
                .type(TransactionType.DEPOSIT)
                .amount(new BigDecimal("100.00"))
                .date(LocalDateTime.now())
                .description("Deposit test")
                .build();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(transactionRepository.findByAccountAndDateBetween(account, startDate, endDate)).thenReturn(Collections.singletonList(transaction));

        List<Transaction> transactions = transactionService.getTransactionsByPeriod(accountId, startDate, endDate);

        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(transaction, transactions.get(0));
    }

    @Test
    void testGetLastTwentyTransactions() {
        Long accountId = 1L;
        Account account = new Account();
        account.setId(accountId);

        Transaction transaction = Transaction.builder()
                .account(account)
                .type(TransactionType.DEPOSIT)
                .amount(new BigDecimal("100.00"))
                .date(LocalDateTime.now())
                .description("Deposit test")
                .build();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(transactionRepository.findTop20ByAccountOrderByDateDesc(account)).thenReturn(Collections.singletonList(transaction));

        List<Transaction> transactions = transactionService.getLastTwentyTransactions(accountId);

        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(transaction, transactions.get(0));
    }
}
