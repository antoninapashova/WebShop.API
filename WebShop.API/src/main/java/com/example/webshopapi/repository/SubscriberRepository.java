package com.example.webshopapi.repository;

import com.example.webshopapi.entity.SubscriberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriberRepository extends JpaRepository<SubscriberEntity, UUID> {
     Optional<SubscriberEntity> findByEmail(String email);
}
