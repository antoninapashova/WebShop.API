package com.example.webshopapi.controller;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.CategoryDto;
import com.example.webshopapi.service.CategoryService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;

    @GetMapping("/all-categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> result = categoryService.retrieveAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/add-category/{categoryName}")
    public ResponseEntity<ExecutionResult> createCategory(@PathVariable String categoryName) {
        ExecutionResult newCategory = categoryService.createNewCategory(categoryName);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    @DeleteMapping("/delete-category/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable String categoryId) {
        ExecutionResult result = categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
