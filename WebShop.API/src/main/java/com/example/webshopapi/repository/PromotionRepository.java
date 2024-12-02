package com.example.webshopapi.repository;

import com.example.webshopapi.entity.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PromotionRepository extends JpaRepository<PromotionEntity, UUID> {
}
