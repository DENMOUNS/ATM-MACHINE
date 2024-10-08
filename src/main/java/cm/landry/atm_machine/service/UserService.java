package cm.landry.atm_machine.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cm.landry.atm_machine.entity.User;
import cm.landry.atm_machine.repository.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * Service class for managing users.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Retrieves all users.
     *
     * @return a list of all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id the ID of the user.
     * @return an Optional containing the user if found, otherwise empty.
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Retrieves a user by email.
     *
     * @param email the email of the user.
     * @return an Optional containing the user if found, otherwise empty.
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Creates a new user.
     *
     * @param user the user to create.
     * @return the created user.
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Updates an existing user.
     *
     * @param id          the ID of the user to update.
     * @param userDetails the new details for the user.
     * @return the updated user.
     * @throws RuntimeException if the user is not found.
     */
    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(userDetails.getName());
                    user.setEmail(userDetails.getEmail());
                    return userRepository.save(user);
                }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the ID of the user to delete.
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Blocks a user.
     *
     * @param id the ID of the user to block.
     * @return the blocked user.
     * @throws RuntimeException if the user is not found.
     */
    public User blockUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setBlocked(true);
                    return userRepository.save(user);
                }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Unblocks a user.
     *
     * @param id the ID of the user to unblock.
     * @return the unblocked user.
     * @throws RuntimeException if the user is not found.
     */
    public User unblockUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setBlocked(false);
                    return userRepository.save(user);
                }).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
