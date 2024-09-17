package cm.landry.atm_machine.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import cm.landry.atm_machine.entity.Transaction;
import cm.landry.atm_machine.entity.TransactionType;
import cm.landry.atm_machine.exception.InsufficientFundsException;

public interface TransactionService {
    Transaction deposit(Long accountId, BigDecimal amount, String description, Long userId);
    Transaction withdraw(Long accountId, BigDecimal amount, String description, Long userId) throws InsufficientFundsException;
    List<Transaction> getTransactionsByAccountId(Long accountId);
    List<Transaction> getTransactionsByPeriod(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
    List<Transaction> getTransactionsByAccountAndDateBetweenAndType(Long accountId, LocalDateTime startDate, LocalDateTime endDate, TransactionType type);
    List<Transaction> getTransactionsByAccountAndDateBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
    List<Transaction> getLastTwentyTransactions(Long accountId);
    boolean transferFunds(Long fromAccountId, Long toAccountId, BigDecimal amount, String description, Long userId) throws InsufficientFundsException;
}
