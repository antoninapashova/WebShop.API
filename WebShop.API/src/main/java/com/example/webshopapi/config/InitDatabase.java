package com.example.webshopapi.config;

import com.example.webshopapi.service.ProductService;
import com.example.webshopapi.service.auth.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitDatabase implements CommandLineRunner {
    private final AuthService authService;
    private final ProductService productService;

    public InitDatabase(AuthService authService, ProductService productService) {
        this.authService = authService;
        this.productService = productService;
    }

    @Override
    public void run(String... args) {
        authService.initAdmin();
        productService.initProducts();
    }
}