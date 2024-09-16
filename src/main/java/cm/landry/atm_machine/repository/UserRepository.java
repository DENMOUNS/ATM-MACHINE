package cm.landry.atm_machine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cm.landry.atm_machine.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Trouver un utilisateur par son adresse e-mail.
     *
     * @param email L'adresse e-mail de l'utilisateur à rechercher.
     * @return Un Optional contenant l'utilisateur trouvé ou vide si aucun utilisateur n'est trouvé.
     */
    Optional<User> findByEmail(String email);
}
