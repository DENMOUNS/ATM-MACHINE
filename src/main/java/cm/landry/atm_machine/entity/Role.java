package cm.landry.atm_machine.entity;

/**
 * Enumération représentant les rôles d'utilisateur dans le système.
 */
public enum Role {
    USER("User"),
    ADMIN("Administrator");

    private final String roleName;

    // Constructeur privé pour initialiser le nom du rôle
    Role(String roleName) {
        this.roleName = roleName;
    }

    /**
     * Obtient le nom du rôle en tant que chaîne de caractères.
     *
     * @return le nom du rôle
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Obtient le rôle correspondant au nom spécifié.
     *
     * @param roleName le nom du rôle
     * @return l'instance Role correspondante
     * @throws IllegalArgumentException si aucun rôle ne correspond au nom donné
     */
    public static Role fromRoleName(String roleName) {
        for (Role role : Role.values()) {
            if (role.getRoleName().equalsIgnoreCase(roleName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role name: " + roleName);
    }
}
