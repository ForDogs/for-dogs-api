package com.fordogs.core.infrastructure;

import com.fordogs.core.domian.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    boolean existsByName(String name);
}
