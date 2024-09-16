package cm.landry.atm_machine.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AccountSummary {
    private Long id;
    private String accountNumber;
    private String accountName;
    private String pin;
    private BigDecimal balance;
    private UserSummary user;
}