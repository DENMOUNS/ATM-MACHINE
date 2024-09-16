package cm.landry.atm_machine.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class WithdrawRequest {
    
    @NotNull(message = "Account ID cannot be null")
    private Long accountId;

    @NotNull(message = "Amount cannot be null")
    private BigDecimal amount;

    private String description;

    @NotNull(message = "User ID cannot be null")
    private Long userId; // Assurez-vous que le nom du champ est "userId"
}
