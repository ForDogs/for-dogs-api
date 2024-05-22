package com.fordogs.product.application;

import com.fordogs.core.domian.entity.ProductEntity;
import com.fordogs.core.domian.entity.UserEntity;
import com.fordogs.core.infrastructure.ProductRepository;
import com.fordogs.core.infrastructure.UserRepository;
import com.fordogs.core.util.HttpServletUtil;
import com.fordogs.product.error.ProductErrorCode;
import com.fordogs.product.presentation.dto.CreateProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    public static final String USER_ID_REQUEST_ATTRIBUTE = "userId";

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateProductDto.Response createProduct(CreateProductDto.Request request) {
        UUID userId = UUID.fromString((String) HttpServletUtil.getRequestAttribute(USER_ID_REQUEST_ATTRIBUTE));
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(ProductErrorCode.TOKEN_USER_NOT_FOUND::toException);
        if (productRepository.existsByName(request.getProductName())) {
            throw ProductErrorCode.PRODUCT_ALREADY_EXISTS.toException();
        }
        ProductEntity saveProductEntity = productRepository.save(request.toEntity(userEntity));

        return CreateProductDto.Response.toResponse(saveProductEntity);
    }
}
