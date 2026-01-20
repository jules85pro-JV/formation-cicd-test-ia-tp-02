package cicd.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.devops.cicd.user.Role;
import com.devops.cicd.user.User;
import com.devops.cicd.user.UserService;

class UserTest {

    @Nested
    @DisplayName("Validation de l'Email")
    class EmailValidation {
        @Test
        void should_trim_email_on_creation() {
            User user = new User("  alice@test.com  ", "Password123!", Role.USER);
            assertThat(user.getEmail()).isEqualTo("alice@test.com");
        }

        @ParameterizedTest
        @ValueSource(strings = { "", "   ", "alice", "alice@", "@test.com", "alice@test", "alice@@test.com" })
        void should_throw_exception_for_invalid_emails(String invalidEmail) {
            assertThatThrownBy(() -> new User(invalidEmail, "Password123!", Role.USER))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("email must be valid");
        }
    }

    @Nested
    @DisplayName("Validation du Mot de passe")
    class PasswordValidation {
        @Test
        void should_accept_strong_password() {
            // Note: PasswordPolicy.isStrong doit être simulé ou implémenté
            User user = new User("alice@test.com", "Strong123!", Role.USER);
            assertThat(user.getPassword()).isEqualTo("Strong123!");
        }

        @Test
        void should_throw_exception_for_weak_password() {
            assertThatThrownBy(() -> new User("alice@test.com", "weak", Role.USER))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("password must be strong");
        }
    }

    @Nested
    @DisplayName("Autorisations (RBAC)")
    class Authorization {
        @Test
        void admin_should_have_access_to_admin_area() {
            User admin = new User("admin@test.com", "Admin123!", Role.ADMIN);
            assertThat(admin.canAccessAdminArea()).isTrue();
        }

        @Test
        void user_should_not_have_access_to_admin_area() {
            User user = new User("user@test.com", "User1234!", Role.USER);
            assertThat(user.canAccessAdminArea()).isFalse();
        }
    }

    @Nested
    @DisplayName("Cas limites et Rôles")
    class EdgeCases {

        @Test
        void should_throw_exception_when_email_is_null() {
            assertThatThrownBy(() -> new User(null, "Strong123!", Role.USER))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("email must be valid");
        }

        @Test
        void should_throw_exception_when_role_is_null() {
            assertThatThrownBy(() -> new User("alice@test.com", "Strong123!", null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("role must not be null");
        }

        @Test
        void should_not_modify_password_with_spaces() {
            // Le password ne doit PAS être trimé selon la spec
            String passWithSpace = "  Strong123!  ";
            User user = new User("alice@test.com", passWithSpace, Role.USER);
            assertThat(user.getPassword()).isEqualTo(passWithSpace);
        }
    }

    @Nested
    @DisplayName("Tests Fonctionnels - UserService")
    class UserServiceTest {
        private final UserService service = new UserService();

        @Test 
        void should_register_successfully() {
            User user = service.register("bob@test.com", "Secure123!", Role.ADMIN);
            assertThat(user).isNotNull();
            assertThat(user.getEmail()).isEqualTo("bob@test.com");
        }

        @Test
        void should_fail_registration_on_invalid_data() {
            // Test de la propagation d'erreur
            assertThatThrownBy(() -> service.register("invalid", "123", Role.USER))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}