package com.fordogs.core.infrastructure;

import com.fordogs.core.domian.entity.ProductEntity;
import com.fordogs.core.domian.vo.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    boolean existsByName(String name);

    Page<ProductEntity> findBySellerAccountAndEnabledTrue(Id account, Pageable pageable);

    Page<ProductEntity> findAllByEnabledTrue(Pageable pageable);

    Optional<ProductEntity> findByIdAndEnabledTrue(UUID id);
}
