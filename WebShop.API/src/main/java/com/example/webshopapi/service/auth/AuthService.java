package com.example.webshopapi.service.auth;

import com.example.webshopapi.dto.SignupRequest;
import com.example.webshopapi.dto.UserDto;

public interface AuthService {
    UserDto createUser(SignupRequest signupRequest);
    boolean hasUserWithEmail(String email);
}
