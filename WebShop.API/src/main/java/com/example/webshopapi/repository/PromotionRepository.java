package com.example.webshopapi.repository;

import com.example.webshopapi.entity.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface PromotionRepository extends JpaRepository<PromotionEntity, UUID> {

    @Modifying
    @Query(value = "UPDATE promotions SET is_active = false WHERE end_date <= :now AND is_active = true", nativeQuery = true)
    void deactivateExpiredPromotions(@Param("now") LocalDateTime now);
}
