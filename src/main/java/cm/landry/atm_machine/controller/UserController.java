package cm.landry.atm_machine.controller;

import cm.landry.atm_machine.entity.User;
import cm.landry.atm_machine.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller class for managing users.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Retrieves all users.
     *
     * @return a list of all users.
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id the ID of the user.
     * @return a ResponseEntity containing the user if found, otherwise a 404 status.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves a user by email.
     *
     * @param email the email of the user.
     * @return a ResponseEntity containing the user if found, otherwise a 404 status.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new user.
     *
     * @param user the user to create.
     * @return the created user.
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    /**
     * Updates an existing user.
     *
     * @param id          the ID of the user to update.
     * @param userDetails the new details for the user.
     * @return a ResponseEntity containing the updated user if found, otherwise a 404 status.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, userDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the ID of the user to delete.
     * @return a ResponseEntity with no content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Blocks a user.
     *
     * @param id the ID of the user to block.
     * @return a ResponseEntity containing the blocked user if found, otherwise a 404 status.
     */
    @PutMapping("/{id}/block")
    public ResponseEntity<User> blockUser(@PathVariable Long id) {
        try {
            User user = userService.blockUser(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Unblocks a user.
     *
     * @param id the ID of the user to unblock.
     * @return a ResponseEntity containing the unblocked user if found, otherwise a 404 status.
     */
    @PutMapping("/{id}/unblock")
    public ResponseEntity<User> unblockUser(@PathVariable Long id) {
        try {
            User user = userService.unblockUser(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
