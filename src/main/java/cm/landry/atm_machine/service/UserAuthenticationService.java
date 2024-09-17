package cm.landry.atm_machine.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cm.landry.atm_machine.entity.User;
import cm.landry.atm_machine.repository.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * Service class for handling user authentication.
 */
@Service
@RequiredArgsConstructor
public class UserAuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads a user by email for authentication purposes.
     *
     * @param email the email of the user.
     * @return the UserDetails object for the user.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getAuthorities()
        );
    }
}
