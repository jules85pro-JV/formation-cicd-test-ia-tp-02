package cicd.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.devops.cicd.user.Role;
import com.devops.cicd.user.User;
import com.devops.cicd.user.UserService;

@DisplayName("Tests d'intégration - Service Utilisateur")
class UserServiceIT {
    
    private final UserService service = new UserService();

    @Test
    @DisplayName("Scénario nominal : Enregistrement réussi")
    void should_register_valid_user() {
        User user = service.register("success@test.com", "Strong123!", Role.USER);
        assertThat(user).isNotNull();
        assertThat(user.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("Scénario d'échec : Propagation d'erreur si l'email est invalide")
    void should_fail_when_email_is_invalid() {
        assertThatThrownBy(() -> service.register("invalid-email", "Strong123!", Role.USER))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("email must be valid");
    }
}