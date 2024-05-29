package com.fordogs.product.infrastructure;

import com.fordogs.core.domian.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {

    boolean existsByName(String name);

    @Query("SELECT p FROM product p LEFT JOIN FETCH p.seller s WHERE p.id = :productId AND s.enabled = true AND p.enabled = true")
    Optional<ProductEntity> findProductWithEnabledSellerAndProduct(@Param("productId") UUID id);
}
