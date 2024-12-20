package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.CategoryDto;
import com.example.webshopapi.entity.CategoryEntity;
import com.example.webshopapi.repository.CategoryRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    ModelMapper modelMapper;

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
    public ExecutionResult createNewCategory(String categoryName) {
        boolean isExists = categoryRepository.existsByName(categoryName);
        if(isExists) throw new EntityExistsException("Category with name " + categoryName + " already exists!");

        CategoryEntity entity = new CategoryEntity();
        entity.setName(categoryName);
        categoryRepository.save(entity);
        return new ExecutionResult("Category added successfully!");
    }

    @Override
    public ExecutionResult deleteCategory(String categoryId) {
        categoryRepository.findById(UUID.fromString(categoryId))
                .orElseThrow(() -> new EntityNotFoundException("Category not found!"));

        categoryRepository.deleteById(UUID.fromString(categoryId));
        return new ExecutionResult("Category removed successfully!");
    }

    private CategoryDto asDto(CategoryEntity categoryEntity) {
        return modelMapper.map(categoryEntity, CategoryDto.class);
    }
}