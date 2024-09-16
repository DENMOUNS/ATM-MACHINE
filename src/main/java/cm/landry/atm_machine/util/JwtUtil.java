package cm.landry.atm_machine.util;

import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;  // La clé secrète est injectée depuis les variables d'environnement

    // Extraire le nom d'utilisateur (ou l'email) du token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extraire la date d'expiration du token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extraire une réclamation spécifique du token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extraire toutes les réclamations du token
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // Gestion des exceptions potentielles (token invalide ou expiré)
            return null;
        }
    }

    // Vérifier si le token est expiré
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Générer un nouveau token avec une durée de validité spécifiée (ici 10 heures)
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 heures
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Valider le token en comparant le nom d'utilisateur et en vérifiant l'expiration
    public Boolean validateToken(String token, String username) {
        final String usernameExtracted = extractUsername(token);
        return (usernameExtracted.equals(username) && !isTokenExpired(token));
    }
}
