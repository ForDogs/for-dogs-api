package com.fordogs.product.application;

import com.fordogs.product.domain.entity.ProductEntity;
import com.fordogs.product.error.ProductErrorCode;
import com.fordogs.product.infrastructure.ProductRepository;
import com.fordogs.product.presentation.request.ProductRegisterRequest;
import com.fordogs.product.presentation.request.ProductUpdateRequest;
import com.fordogs.product.presentation.response.ProductRegisterResponse;
import com.fordogs.product.presentation.response.ProductUpdateResponse;
import com.fordogs.user.application.UserQueryService;
import com.fordogs.user.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductQueryService productQueryService;
    private final UserQueryService userQueryService;

    public ProductRegisterResponse createProduct(ProductRegisterRequest request) {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        UserEntity userEntity = userQueryService.findById(userId);

        if (request.getProductQuantity() <= 0) {
            throw ProductErrorCode.PRODUCT_QUANTITY_INVALID.toException();
        }
        if (productRepository.existsByName(request.getProductName())) {
            throw ProductErrorCode.PRODUCT_ALREADY_EXISTS.toException();
        }

        ProductEntity savedProductEntity = productRepository.save(request.toEntity(userEntity));

        return ProductRegisterResponse.toResponse(savedProductEntity);
    }

    public ProductUpdateResponse updateProduct(UUID productId, ProductUpdateRequest request) {
        ProductEntity productEntity = productQueryService.findActiveProductWithActiveUserById(productId);
        productEntity.update(request.getProductName(), request.getProductPrice(), request.getProductQuantity(),
                request.getProductDescription(), request.getProductImages(), request.getProductCategory());

        return ProductUpdateResponse.toResponse(productEntity);
    }

    public void deactivateProduct(UUID productId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(ProductErrorCode.PRODUCT_NOT_FOUND::toException);
        productEntity.disable();
    }
}
