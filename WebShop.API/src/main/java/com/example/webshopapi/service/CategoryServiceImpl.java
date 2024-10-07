package com.example.webshopapi.service;

import com.example.webshopapi.entity.CategoryEntity;
import com.example.webshopapi.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

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
}