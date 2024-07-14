package com.fordogs.cart.infrastructure;

import com.fordogs.cart.domain.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CartRepository extends JpaRepository<CartEntity, UUID>  {
    @Query("SELECT c FROM cart c JOIN FETCH c.product WHERE c.user.id = :userId AND c.canceled = false")
    List<CartEntity> findActiveCartsByUserId(@Param("userId") UUID userId);
}
