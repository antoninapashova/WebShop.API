package com.example.webshopapi.config;

import com.example.webshopapi.service.CategoryService;
import com.example.webshopapi.service.ProductService;
import com.example.webshopapi.service.auth.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitDatabase implements CommandLineRunner {
    private final AuthService authService;
    private final ProductService productService;
    private final CategoryService categoryService;

    public InitDatabase(AuthService authService, ProductService productService, CategoryService categoryService) {
        this.authService = authService;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... args) {
        authService.initAccounts();
        categoryService.initCategories();
        productService.initProducts();
    }
}