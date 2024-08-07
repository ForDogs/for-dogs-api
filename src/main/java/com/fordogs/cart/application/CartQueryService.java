package com.fordogs.cart.application;

import com.fordogs.cart.domain.entity.CartEntity;
import com.fordogs.cart.error.CartErrorCode;
import com.fordogs.cart.infrastructure.CartRepository;
import com.fordogs.cart.presentation.response.CartSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartQueryService {

    private final CartRepository cartRepository;

    public List<CartSearchResponse> getAllCartsByUserId() {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        List<CartEntity> cartEntityList = cartRepository.findActiveCartsByUserId(userId);

        return cartEntityList.stream()
                .map(CartSearchResponse::toResponse)
                .collect(Collectors.toList());
    }

    public CartEntity findById(UUID cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(CartErrorCode.CART_PRODUCT_NOT_FOUND::toException);
    }
}
