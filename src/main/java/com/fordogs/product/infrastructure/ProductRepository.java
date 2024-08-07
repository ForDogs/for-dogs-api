package com.fordogs.product.infrastructure;

import com.fordogs.product.domain.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {

    boolean existsByName(String name);

    @Query("SELECT p FROM product p LEFT JOIN FETCH p.seller s WHERE p.id = :productId AND s.enabled = true AND p.enabled = true")
    Optional<ProductEntity> findByIdAndEnabledTrueAndUserEnabledTrue(@Param("productId") UUID id);

    @Query("SELECT p FROM product p LEFT JOIN FETCH p.seller s WHERE p.id IN :productIds AND s.enabled = true AND p.enabled = true")
    List<ProductEntity> findAllByIdAndEnabledTrueAndUserEnabledTrue(@Param("productIds") Set<UUID> productIds);

    @Query("SELECT p FROM product p JOIN FETCH p.seller u WHERE p.quantity.value < :quantity")
    List<ProductEntity> findByStockQuantityLessThan(@Param("quantity") int quantity);
}
