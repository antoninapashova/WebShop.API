package com.example.webshopapi.controller;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.config.result.TypedResult;
import com.example.webshopapi.dto.CategoryDto;
import com.example.webshopapi.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all-categories")
    public ResponseEntity<?> getAllCategories() {
        TypedResult<List<CategoryDto>> result = categoryService.retrieveAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/add-category/{categoryName}")
    public ResponseEntity<?> createCategory(@PathVariable String categoryName) {
        TypedResult<CategoryDto> newCategory = categoryService.createNewCategory(categoryName);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory.getData());
    }

    @DeleteMapping("/delete-category/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable String categoryId) {
        ExecutionResult result = categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
