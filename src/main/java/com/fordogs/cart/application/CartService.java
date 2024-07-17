package com.fordogs.cart.application;

import com.fordogs.cart.domain.entity.CartEntity;
import com.fordogs.cart.error.CartErrorCode;
import com.fordogs.cart.infrastructure.CartRepository;
import com.fordogs.cart.presentation.request.CartCreateRequest;
import com.fordogs.core.domain.vo.wapper.Quantity;
import com.fordogs.product.application.ProductService;
import com.fordogs.product.domain.entity.ProductEntity;
import com.fordogs.user.application.UserQueryService;
import com.fordogs.user.domain.entity.mysql.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartQueryService cartQueryService;
    private final ProductService productService;
    private final UserQueryService userQueryService;
    private final CartRepository cartRepository;

    public void createCart(CartCreateRequest request) {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        UserEntity userEntity = userQueryService.findById(userId);

        ProductEntity productEntity = productService.findById(request.getProductId());
        if (productEntity.getQuantity().getValue() < request.getProductQuantity()) {
            throw CartErrorCode.INSUFFICIENT_STOCK.toException();
        }

        cartRepository.save(request.toEntity(userEntity, productEntity));
    }

    public void updateCartQuantity(UUID cartId, Integer quantity) {
        CartEntity cartEntity = cartQueryService.findById(cartId);
        cartEntity.updateQuantity(Quantity.builder().value(quantity).build());
    }

    public void deleteCart(UUID cartId) {
        CartEntity cartEntity = cartQueryService.findById(cartId);
        cartEntity.cancel();
    }
}
