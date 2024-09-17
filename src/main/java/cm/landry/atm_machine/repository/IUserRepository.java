package cm.landry.atm_machine.repository;

import java.util.Optional;

import cm.landry.atm_machine.entity.User;

/**
 * Interface for user repository.
 */
public interface IUserRepository {
    Optional<User> findById(Long id);
}
