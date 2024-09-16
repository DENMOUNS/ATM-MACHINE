package cm.landry.atm_machine.entity;

import java.util.Collection;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a user in the system, implementing Spring Security's UserDetails.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password; // Password field for authentication

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Account> accounts;

    private boolean blocked;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convert role to GrantedAuthority for Spring Security
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email; // Used as the identifier for authentication
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Default behavior: Account is not expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return !blocked; // Account is locked if 'blocked' is true
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Default behavior: Credentials are not expired
    }

    @Override
    public boolean isEnabled() {
        return true; // Default behavior: Account is enabled
    }
}
