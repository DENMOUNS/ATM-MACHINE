package cm.landry.atm_machine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cm.landry.atm_machine.dto.LoginResponse;
import cm.landry.atm_machine.dto.UserSummary;
import cm.landry.atm_machine.entity.User;
import cm.landry.atm_machine.service.UserService;
import cm.landry.atm_machine.service.auth.AuthRequest;
import cm.landry.atm_machine.service.auth.AuthResponse;
import cm.landry.atm_machine.util.JwtUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            // Hacher le mot de passe avant de l'enregistrer
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            User newUser = userService.createUser(user);
            return ResponseEntity.ok(newUser); // Inscription réussie
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error registering user: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            // Authentification avec l'email et le mot de passe
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            // Récupérer l'utilisateur à partir de l'email
            User user = userService.getUserByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Générer le token JWT avec l'email de l'utilisateur
            String jwt = jwtUtil.generateToken(user.getEmail());

            // Créer un UserSummary pour l'utilisateur connecté
            UserSummary userSummary = new UserSummary(user.getId(), user.getName(), user.getEmail());

            // Renvoyer la réponse avec le token JWT et les détails de l'utilisateur
            return ResponseEntity.ok(new LoginResponse(jwt, userSummary));

        } catch (AuthenticationException e) {
            // Gérer les erreurs d'authentification
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            // Gérer toute autre exception potentielle
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

}
