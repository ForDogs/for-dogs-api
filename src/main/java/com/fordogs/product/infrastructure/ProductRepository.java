package com.fordogs.product.infrastructure;

import com.fordogs.core.domian.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {

    boolean existsByName(String name);

    Optional<ProductEntity> findByIdAndSellerEnabledTrueAndEnabledTrue(UUID id);
}
