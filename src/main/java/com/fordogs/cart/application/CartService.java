package com.fordogs.cart.application;

import com.fordogs.cart.domain.entity.CartEntity;
import com.fordogs.cart.error.CartErrorCode;
import com.fordogs.cart.infrastructure.CartRepository;
import com.fordogs.cart.presentation.request.CartCreateRequest;
import com.fordogs.product.application.ProductService;
import com.fordogs.product.domain.entity.ProductEntity;
import com.fordogs.user.application.UserManagementService;
import com.fordogs.user.domain.entity.mysql.UserManagementEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final ProductService productService;
    private final UserManagementService userManagementService;
    private final CartRepository cartRepository;

    public void createCart(CartCreateRequest request) {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        UserManagementEntity userManagementEntity = userManagementService.findById(userId);

        ProductEntity productEntity = productService.findById(request.getProductId());
        if (!productEntity.isStockAvailable(request.getProductQuantity())) {
            throw CartErrorCode.INSUFFICIENT_STOCK.toException();
        }

        CartEntity savedCartEntity = cartRepository.save(request.toEntity(userManagementEntity, productEntity));
    }
}
