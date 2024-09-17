package cm.landry.atm_machine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * DTO representing a summary of a user.
 */
@Data
@AllArgsConstructor
public class UserSummary {
    private Long id;
    private String username;
    private String email;
}