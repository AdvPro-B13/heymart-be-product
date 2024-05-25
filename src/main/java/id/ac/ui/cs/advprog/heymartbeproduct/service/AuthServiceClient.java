package id.ac.ui.cs.advprog.heymartbeproduct.service;

public interface AuthServiceClient {
    public boolean verifyUserAuthorization(String action, String token);
}
