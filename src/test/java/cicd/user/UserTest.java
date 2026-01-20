package cicd.user;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.devops.cicd.user.Role;
import com.devops.cicd.user.User;
@DisplayName("Tests unitaires de User")
class UserTest {

    @Test
    @DisplayName("Email doit être trimé à la création")
    void should_trim_email() {
        User user = new User("  alice@test.com  ", "Password123!", Role.USER);
        assertThat(user.getEmail()).isEqualTo("alice@test.com");
    }

    @Test
    @DisplayName("Le mot de passe ne doit pas être modifié (pas de trim)")
    void should_not_trim_password() {
        String pass = "  Pass123!  ";
        User user = new User("a@b.com", pass, Role.USER);
        assertThat(user.getPassword()).isEqualTo(pass);
    }

    @Test
    @DisplayName("Seul l'ADMIN peut accéder à l'interface admin")
    void check_admin_access() {
        assertThat(new User("a@b.com", "P123456!", Role.ADMIN).canAccessAdminArea()).isTrue();
        assertThat(new User("a@b.com", "P123456!", Role.USER).canAccessAdminArea()).isFalse();
    }
}