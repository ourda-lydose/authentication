package id.ac.ui.cs.advprog.authentication;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AuthenticationTest {

    @Test
    public void testAuthenticationWithInvalidCredentialsShouldFail() {
        // Authenticator dan AuthenticationMethod adalah kelas dan interface yang akan kita buat
        AuthenticationMethod method = new SimplePasswordAuthentication(); // Contoh implementasi strategi
        Authenticator authenticator = new Authenticator(method);
        boolean authResult = authenticator.authenticate("user", "invalidPassword");

        assertFalse(authResult, "Authentication should fail with invalid credentials");
    }
}
