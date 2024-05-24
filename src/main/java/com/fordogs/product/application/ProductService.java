package com.fordogs.product.application;

import com.fordogs.core.domian.entity.ProductEntity;
import com.fordogs.core.domian.entity.UserEntity;
import com.fordogs.core.domian.vo.Id;
import com.fordogs.core.exception.error.JwtErrorCode;
import com.fordogs.core.infrastructure.ProductRepository;
import com.fordogs.core.infrastructure.UserRepository;
import com.fordogs.core.util.HttpServletUtil;
import com.fordogs.product.error.ProductErrorCode;
import com.fordogs.product.presentation.dto.CreateProductDto;
import com.fordogs.product.presentation.dto.ReadProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateProductDto.Response createProduct(CreateProductDto.Request request) {
        UUID userId = HttpServletUtil.extractUserId();
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(JwtErrorCode.TOKEN_USER_NOT_FOUND::toException);
        if (productRepository.existsByName(request.getProductName())) {
            throw ProductErrorCode.PRODUCT_ALREADY_EXISTS.toException();
        }
        ProductEntity saveProductEntity = productRepository.save(request.toEntity(userEntity));

        return CreateProductDto.Response.toResponse(saveProductEntity);
    }

    public Page<ReadProductDto.Response> findProducts(String sellerId, Pageable pageable) {
        Page<ProductEntity> products = (sellerId != null)
                ? productRepository.findBySellerUserIdentifier(Id.builder().value(sellerId).build(), pageable)
                : productRepository.findAll(pageable);

        return products.map(ReadProductDto.Response::toResponse);
    }
}
