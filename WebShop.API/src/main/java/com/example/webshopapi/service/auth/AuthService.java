package com.example.webshopapi.service.auth;

import com.example.webshopapi.config.result.TypedResult;
import com.example.webshopapi.dto.requestObjects.SignupRequest;
import com.example.webshopapi.dto.UserDto;

public interface AuthService {
    UserDto createUser(SignupRequest signupRequest);
    boolean hasUserWithEmail(String email);
    void initAccounts();
    TypedResult<String> loadUserRole(String username);
}
