package com.example.webshopapi.repository;

import com.example.webshopapi.entity.PromotionProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PromotionProductRepository extends JpaRepository<PromotionProduct, UUID> {
}
