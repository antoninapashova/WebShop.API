package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.config.result.FailureType;
import com.example.webshopapi.config.result.TypedResult;
import com.example.webshopapi.dto.CategoryDto;
import com.example.webshopapi.entity.CategoryEntity;
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
    public TypedResult<List<CategoryDto>> retrieveAllCategories() {
        List<CategoryDto> categories = categoryRepository.findAll().stream().map(this::asDto).toList();
        if (categories.isEmpty()) {
            new TypedResult<>(FailureType.UNKNOWN, "No categories provided!");
        }

        return new TypedResult<>(categories);
    }

    @Override
    public TypedResult<CategoryDto> createNewCategory(String categoryName) {
        CategoryEntity entity = new CategoryEntity();
        entity.setName(categoryName);
        CategoryEntity newCategory = categoryRepository.save(entity);

        return new TypedResult<>(asDto(newCategory));
    }

    @Override
    public ExecutionResult deleteCategory(String categoryId) {
        boolean isExists = categoryRepository.existsById(UUID.fromString(categoryId));
        if (!isExists) {
            return new ExecutionResult(FailureType.NOT_FOUND, "Category not found!");
        }

        categoryRepository.deleteById(UUID.fromString(categoryId));
        return new ExecutionResult("Category removed successfully!");
    }

    private CategoryDto asDto(CategoryEntity categoryEntity) {
        return modelMapper.map(categoryEntity, CategoryDto.class);
    }
}