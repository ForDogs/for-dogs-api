package com.fordogs.cart.infrastructure;

import com.fordogs.cart.domain.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<CartEntity, UUID>  {
}
