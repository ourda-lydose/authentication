package id.ac.ui.cs.advprog.authentication.method;

public interface AuthenticationMethod {
    boolean authenticate(String username, String password);
}
