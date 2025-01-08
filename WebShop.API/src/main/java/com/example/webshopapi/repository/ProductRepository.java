package com.example.webshopapi.repository;

import com.example.webshopapi.entity.ProductEntity;
import com.example.webshopapi.projection.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

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
               p.description AS description,
               p.price,
               c.name AS categoryName,
               pp.price_in_promotion AS priceInPromotion,
               pr.is_active AS isActive,
               pr.end_date AS endDate,
               (
                   SELECT array_agg(im.image_data)
                   FROM images im
                   WHERE im.product_id = p.id
               ) AS images
               FROM products p
               JOIN categories c 
                  on c.id = p.category_id
               LEFT JOIN promotion_product pp
                   ON p.id = pp.product_id
               LEFT JOIN promotions pr
                   ON pp.promotion_id = pr.id
               WHERE p.is_deleted = false
            """, nativeQuery = true)
    List<Product> findAllProducts();

    @Query(value = """
            SELECT 
            p.id,
            p.name,
            p.description, 
            p.price,
            p.quantity,
            p.category_id,
            p.is_deleted
            FROM products p 
            WHERE not p.is_deleted
            AND not EXISTS(SELECT null FROM promotions pr where pr.is_active and pr.id in 
            (SELECT pp.promotion_id FROM promotion_product pp where pp.product_id = p.id))
            """, nativeQuery = true)
    List<ProductEntity> findAllNonPromotionalProducts();

    @Query(value = """
            SELECT pp.price_in_promotion
            FROM promotion_product pp
            JOIN products p ON p.id = :productId 
               AND NOT p.is_deleted AND p.id = pp.product_id
            WHERE EXISTS (SELECT null FROM promotions pr 
                          WHERE pr.id = pp.promotion_id AND pr.is_active)
            """, nativeQuery = true)
    Double findPromotionalPrice(@PathVariable("productId") UUID productId);
}