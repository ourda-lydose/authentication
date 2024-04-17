package id.ac.ui.cs.advprog.authentication.method;

public class PlainTextPasswordAuthentication implements AuthenticationMethod {
    private static final String KNOWN_USERNAME = "knownUsername";
    private static final String KNOWN_PASSWORD = "knownPassword";

    @Override
    public boolean authenticate(String username, String password) {
        return KNOWN_USERNAME.equals(username) && KNOWN_PASSWORD.equals(password);
    }
}
