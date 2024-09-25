package com.example.webshopapi.repository;

import com.example.webshopapi.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, UUID> {
    boolean existsByUserID(UUID userId);
    CartEntity findByUserID(UUID userId);
}
