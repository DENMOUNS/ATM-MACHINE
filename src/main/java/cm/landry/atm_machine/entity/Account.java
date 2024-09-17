package cm.landry.atm_machine.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a bank account in the system.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(nullable = false, name = "account_number")
    private String accountNumber;

    @Column(nullable = false, name = "account_name")
    private String accountName;

    @Enumerated(EnumType.STRING)
    private AccountType accountType = AccountType.COURANT; 

    @Size(min = 4, max = 6, message = "PIN must be between 4 and 6 digits")
    @Pattern(regexp = "\\d{4,6}", message = "PIN must be a number between 4 and 6 digits")
    private String pin;

    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    /**
     * Creates a new instance of Account with an updated balance.
     *
     * @param newBalance The new balance to set.
     * @return A new Account instance with the updated balance.
     */
    public Account withBalance(BigDecimal newBalance) {
        return this.toBuilder().balance(newBalance).build();
    }
}
