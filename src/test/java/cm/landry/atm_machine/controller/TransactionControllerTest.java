package cm.landry.atm_machine.controller;

import cm.landry.atm_machine.entity.Transaction;
import cm.landry.atm_machine.exception.InsufficientFundsException;
import cm.landry.atm_machine.request.DepositRequest;
import cm.landry.atm_machine.request.WithdrawRequest;
import cm.landry.atm_machine.service.AccountService;
import cm.landry.atm_machine.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransactionController transactionController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void deposit_ShouldReturnTransaction() throws Exception {
        // Given
        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setAccountId(1L);
        depositRequest.setAmount(BigDecimal.valueOf(100));
        depositRequest.setDescription("Deposit");
        depositRequest.setUserId(1L);
        
        Transaction transaction = new Transaction();
        when(transactionService.deposit(anyLong(), any(BigDecimal.class), anyString(), anyLong())).thenReturn(transaction);

        // When & Then
        mockMvc.perform(post("/api/transactions/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.amount").value(transaction.getAmount()));
    }

    @Test
    void withdraw_ShouldReturnTransaction() throws Exception {
        // Given
        WithdrawRequest withdrawRequest = new WithdrawRequest();
        withdrawRequest.setAccountId(1L);
        withdrawRequest.setAmount(BigDecimal.valueOf(100));
        withdrawRequest.setDescription("Withdrawal");
        withdrawRequest.setUserId(1L);

        Transaction transaction = new Transaction();
        when(transactionService.withdraw(anyLong(), any(BigDecimal.class), anyString(), anyLong())).thenReturn(transaction);

        // When & Then
        mockMvc.perform(post("/api/transactions/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(withdrawRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.amount").value(transaction.getAmount()));
    }

    @Test
    void withdraw_ShouldReturnBadRequest_WhenInsufficientFunds() throws Exception {
        // Given
        WithdrawRequest withdrawRequest = new WithdrawRequest();
        withdrawRequest.setAccountId(1L);
        withdrawRequest.setAmount(BigDecimal.valueOf(100));
        withdrawRequest.setDescription("Withdrawal");
        withdrawRequest.setUserId(1L);

        when(transactionService.withdraw(anyLong(), any(BigDecimal.class), anyString(), anyLong()))
                .thenThrow(new InsufficientFundsException("Insufficient funds"));

        // When & Then
        mockMvc.perform(post("/api/transactions/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(withdrawRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllTransactions_ShouldReturnTransactions() throws Exception {
        // Given
        Transaction transaction = new Transaction();
        List<Transaction> transactions = Collections.singletonList(transaction);
        when(transactionService.getTransactionsByAccountId(anyLong())).thenReturn(transactions);

        // When & Then
        mockMvc.perform(get("/api/transactions/all")
                .param("accountId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void getLastTwentyTransactions_ShouldReturnTransactions() throws Exception {
        // Given
        Transaction transaction = new Transaction();
        List<Transaction> transactions = Collections.singletonList(transaction);
        when(transactionService.getLastTwentyTransactions(anyLong())).thenReturn(transactions);

        // When & Then
        mockMvc.perform(get("/api/transactions/last20")
                .param("accountId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists());
    }
}
