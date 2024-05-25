package com.fordogs.product.application;

import com.fordogs.core.domian.entity.ProductEntity;
import com.fordogs.core.domian.entity.UserEntity;
import com.fordogs.core.domian.vo.Id;
import com.fordogs.core.exception.error.ProductServiceErrorCode;
import com.fordogs.core.infrastructure.ProductRepository;
import com.fordogs.core.infrastructure.UserRepository;
import com.fordogs.core.util.HttpServletUtil;
import com.fordogs.core.util.constants.RequestAttributesConstants;
import com.fordogs.product.presentation.dto.ProductCreateDto;
import com.fordogs.product.presentation.dto.ProductDetailDto;
import com.fordogs.product.presentation.dto.ProductListDto;
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
    public ProductCreateDto.Response createProduct(ProductCreateDto.Request request) {
        UUID userId = (UUID) HttpServletUtil.getRequestAttribute(RequestAttributesConstants.USER_ID);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(ProductServiceErrorCode.USER_NOT_FOUND::toException);
        if (productRepository.existsByName(request.getProductName())) {
            throw ProductServiceErrorCode.PRODUCT_ALREADY_EXISTS.toException();
        }
        ProductEntity saveProductEntity = productRepository.save(request.toEntity(userEntity));

        return ProductCreateDto.Response.toResponse(saveProductEntity);
    }

    public Page<ProductListDto.Response> findProducts(String sellerId, Pageable pageable) {
        Page<ProductEntity> productEntities = (sellerId != null)
                ? productRepository.findBySellerAccountAndEnabledTrue(Id.builder().value(sellerId).build(), pageable)
                : productRepository.findAllByEnabledTrue(pageable);

        return productEntities.map(ProductListDto.Response::toResponse);
    }

    public ProductDetailDto.Response findOneProduct(String productId) {
        ProductEntity productEntity = productRepository.findByIdAndEnabledTrue(UUID.fromString(productId))
                .orElseThrow(ProductServiceErrorCode.PRODUCT_NOT_FOUND::toException);

        return ProductDetailDto.Response.toResponse(productEntity);
    }
}
