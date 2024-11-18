package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.CategoryDto;
import com.example.webshopapi.entity.CategoryEntity;
import com.example.webshopapi.error.exception.CategoryNotFoundException;
import com.example.webshopapi.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public void initCategories() {
        if (categoryRepository.count() != 0) {
            return;
        }

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName("Sport accessories");

        CategoryEntity categoryEntity1 = new CategoryEntity();
        categoryEntity1.setName("Sport clothes");

        categoryRepository.saveAll(Arrays.asList(categoryEntity, categoryEntity1));
    }

    @Override
    public List<CategoryDto> retrieveAllCategories() {
        return categoryRepository.findAll().stream().map(this::asDto).toList();
    }

    @Override
    public CategoryDto createNewCategory(String categoryName) {
        CategoryEntity entity = new CategoryEntity();
        entity.setName(categoryName);
        categoryRepository.save(entity);
        return asDto(entity);
    }

    @Override
    public ExecutionResult deleteCategory(String categoryId) {
        categoryRepository.findById(UUID.fromString(categoryId))
                .orElseThrow(() -> new CategoryNotFoundException("Category not found!"));

        categoryRepository.deleteById(UUID.fromString(categoryId));
        return new ExecutionResult("Category removed successfully!");
    }

    private CategoryDto asDto(CategoryEntity categoryEntity) {
        return modelMapper.map(categoryEntity, CategoryDto.class);
    }
}