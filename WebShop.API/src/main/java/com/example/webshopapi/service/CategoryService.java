package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    void initCategories();
    List<CategoryDto> retrieveAllCategories();
    CategoryDto createNewCategory(String categoryName);
    ExecutionResult deleteCategory(String categoryId);
}