package id.ac.ui.cs.advprog.authentication.method;

public class SimplePasswordAuthentication implements AuthenticationMethod {

    @Override
    public boolean authenticate(String username, String password) {
        // Sementara, kita akan membuat ini selalu gagal untuk menunjukkan Red Phase
        return false;
    }
}
