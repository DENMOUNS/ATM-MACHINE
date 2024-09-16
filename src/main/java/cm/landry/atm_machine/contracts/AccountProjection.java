package cm.landry.atm_machine.contracts;

import org.springframework.data.rest.core.config.Projection;

import cm.landry.atm_machine.entity.Account;
import cm.landry.atm_machine.entity.User;


@Projection(name = "accountProjection", types = Account.class)
public interface AccountProjection {
    Long getId();
    String getAccountNumber();
    String getAccountName();
    String getPin();
    double getBalance();
    User getUser();
}


