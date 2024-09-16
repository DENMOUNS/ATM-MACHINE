package cm.landry.atm_machine.service.auth;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
