package com.example.webshopapi.repository;

import com.example.webshopapi.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    boolean existsByName(String name);
    ProductEntity findProductEntityById(UUID id);
    @Query(value = "SELECT * FROM products WHERE name LIKE %?1%", nativeQuery = true)
    List<ProductEntity> findAllByName(String name);
}