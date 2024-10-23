package com.example.webshopapi.config;

import com.example.webshopapi.service.CategoryService;
import com.example.webshopapi.service.auth.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitDatabase implements CommandLineRunner {
    private final AuthService authService;
    private final CategoryService categoryService;

    public InitDatabase(AuthService authService, CategoryService categoryService) {
        this.authService = authService;
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... args) {
        authService.initAccounts();
        categoryService.initCategories();
    }
}