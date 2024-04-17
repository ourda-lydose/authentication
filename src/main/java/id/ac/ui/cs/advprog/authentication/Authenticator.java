package id.ac.ui.cs.advprog.authentication;

import id.ac.ui.cs.advprog.authentication.method.AuthenticationMethod;

public class Authenticator {
    private AuthenticationMethod method;

    public Authenticator(AuthenticationMethod method) {
        this.method = method;
    }

    public boolean authenticate(String username, String password) {
        return method.authenticate(username, password);
    }
}
