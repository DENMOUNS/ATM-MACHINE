package cm.landry.atm_machine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cm.landry.atm_machine.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Trouver un compte par son numéro de compte.
     *
     * @param accountNumber Le numéro de compte à rechercher.
     * @return Un Optional contenant le compte trouvé ou vide si aucun compte n'est trouvé.
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * Trouver un compte par son ID et l'ID de l'utilisateur associé.
     *
     * @param accountId L'ID du compte à rechercher.
     * @param userId L'ID de l'utilisateur associé au compte.
     * @return Un Optional contenant le compte trouvé ou vide si aucun compte n'est trouvé.
     */
    Optional<Account> findByIdAndUserId(Long accountId, Long userId);
}
