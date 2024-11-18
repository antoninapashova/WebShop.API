package com.example.webshopapi.service.auth;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.requestObjects.SignupRequest;

public interface AuthService {
    ExecutionResult createUser(SignupRequest signupRequest);
    void initAccounts();
    String loadUserRole(String username);
}
