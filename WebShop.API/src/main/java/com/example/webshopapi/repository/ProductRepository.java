package com.example.webshopapi.repository;

import com.example.webshopapi.entity.ProductEntity;
import com.example.webshopapi.projection.Product;
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

    @Query(value = """
            SELECT 
            p.id AS productId,
            p.name as productName,
            p.category_id AS categoryId, 
            p.description AS description, 
            p.price,
            pp.price_in_promotion AS priceInPromotion,
            pp.is_active AS isActive,
            pr.end_date AS endDate,
            (
                SELECT array_agg(im.image_data)
                FROM images im
                WHERE im.product_id = p.id
            ) AS images
            FROM products p 
            LEFT JOIN promotion_product pp 
                ON p.id = pp.product_id 
            LEFT JOIN promotions pr 
                ON pp.promotion_id = pr.id 
            WHERE p.is_deleted = false AND (pp.is_active = true OR pp.is_active IS NULL)
            """, nativeQuery = true)
    List<Product> findAllProducts();
}