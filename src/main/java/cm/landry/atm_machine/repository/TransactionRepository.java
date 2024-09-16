package cm.landry.atm_machine.repository;

import cm.landry.atm_machine.entity.Account;
import cm.landry.atm_machine.entity.Transaction;
import cm.landry.atm_machine.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Trouver toutes les transactions pour un compte donné.
     *
     * @param account L'objet Account pour lequel trouver les transactions.
     * @return Liste des transactions associées au compte.
     */
    List<Transaction> findByAccount(Account account);

    /**
     * Trouver les transactions d'un compte entre deux dates.
     *
     * @param account L'objet Account pour lequel trouver les transactions.
     * @param startDate La date de début de la période.
     * @param endDate La date de fin de la période.
     * @return Liste des transactions entre les deux dates pour le compte donné.
     */
    List<Transaction> findByAccountAndDateBetween(Account account, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Trouver les transactions par compte, période et type, triées par date décroissante.
     *
     * @param account L'objet Account pour lequel trouver les transactions.
     * @param startDate La date de début de la période.
     * @param endDate La date de fin de la période.
     * @param type Le type de transaction.
     * @return Liste des transactions filtrées par compte, période et type, triées par date décroissante.
     */
    List<Transaction> findByAccountAndDateBetweenAndTypeOrderByDateDesc(
            Account account, LocalDateTime startDate, LocalDateTime endDate, TransactionType type);

    /**
     * Récupérer les 20 dernières transactions d'un compte, triées par date décroissante.
     *
     * @param account L'objet Account pour lequel trouver les transactions.
     * @return Liste des 20 dernières transactions pour le compte donné.
     */
    List<Transaction> findTop20ByAccountOrderByDateDesc(Account account);

    /**
     * Récupérer toutes les transactions d'un compte, triées par date décroissante.
     *
     * @param account L'objet Account pour lequel trouver les transactions.
     * @return Liste des transactions triées par date décroissante.
     */
    List<Transaction> findByAccountOrderByDateDesc(Account account);

    /**
     * Trouver les transactions d'un compte entre deux dates avec tri par date décroissante.
     *
     * @param account L'objet Account pour lequel trouver les transactions.
     * @param startDate La date de début de la période.
     * @param endDate La date de fin de la période.
     * @return Liste des transactions entre les deux dates pour le compte donné, triées par date décroissante.
     */
    @Query("SELECT t FROM Transaction t WHERE t.account = :account AND t.date BETWEEN :startDate AND :endDate ORDER BY t.date DESC")
    List<Transaction> findByAccountAndDateBetweenWithSorting(
            @Param("account") Account account,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
