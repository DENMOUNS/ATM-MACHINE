package cm.landry.atm_machine.repository;

import java.util.List;
import java.util.Optional;

import cm.landry.atm_machine.entity.Account;

/**
 * Interface for account repository.
 */
public interface IAccountRepository {

    List<Account> findAll();

    Optional<Account> findById(Long id);

    Optional<Account> findByAccountNumber(String accountNumber);

    Account save(Account account);
}
