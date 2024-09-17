package cm.landry.atm_machine.service;

import java.util.Random;

import org.springframework.stereotype.Service;

import cm.landry.atm_machine.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

/**
 * Service responsible for generating unique account numbers.
 */
@Service
@RequiredArgsConstructor
public class AccountNumberGeneratorService {

    private final AccountRepository accountRepository;

    /**
     * Generates a unique account number.
     *
     * @return a unique account number.
     */
    public String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = generateAccountNumber();
        } while (accountRepository.findByAccountNumber(accountNumber).isPresent());
        return accountNumber;
    }

    /**
     * Generates a random account number.
     *
     * @return a random account number.
     */
    private String generateAccountNumber() {
        Random random = new Random();
        return random.ints(11, 0, 10)
                .mapToObj(String::valueOf)
                .reduce(String::concat)
                .orElseThrow();
    }
}
