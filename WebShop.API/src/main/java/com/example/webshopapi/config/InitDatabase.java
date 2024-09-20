package com.example.webshopapi.config;

import com.example.webshopapi.service.auth.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitDatabase implements CommandLineRunner {
    private final AuthService authService;

    public InitDatabase(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void run(String... args) {
        authService.initAdmin();
    }
}