package com.devops.cicd.user;

import com.devops.cicd.PasswordPolicy;

public class User {

    private final String email;
    private final String password;
    private final Role role;

    public User(String email, String password, Role role) throws IllegalArgumentException {
        // TODO: appliquer toutes les règles de validation de la spec
        // - email: obligatoire, trim, format simple
        // - password: obligatoire, strong (PasswordPolicy.isStrong)
        // - role: obligatoire (non null)
        //
        // En cas d'erreur: IllegalArgumentException avec un message explicite
        // ("email must be valid", "password must be strong", "role must not be null")

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("email must be valid");
        }
        String trimmedEmail = email.trim();
        if (!EmailValidator.isValid(trimmedEmail)) {
            throw new IllegalArgumentException("email must be valid");
        }
        this.email = trimmedEmail;
        if (password == null || !PasswordPolicy.isStrong(password)) {
            throw new IllegalArgumentException("password must be strong");
        }
        this.password = password;

        if (role == null) {
            throw new IllegalArgumentException("role must not be null");
        }
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public boolean canAccessAdminArea() {
        return this.role == Role.ADMIN;
    }

    // BONUS: vous pouvez ajouter equals/hashCode/toString si utile (non obligatoire)
}
