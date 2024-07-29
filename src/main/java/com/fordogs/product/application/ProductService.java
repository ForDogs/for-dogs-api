package com.fordogs.product.application;

import com.fordogs.product.application.aws.s3.S3ImageUploader;
import com.fordogs.product.application.aws.s3.response.ImageUploadInfo;
import com.fordogs.product.domain.entity.ProductEntity;
import com.fordogs.product.domain.enums.Category;
import com.fordogs.product.domain.specification.ProductSpecification;
import com.fordogs.product.error.ProductErrorCode;
import com.fordogs.product.infrastructure.ProductRepository;
import com.fordogs.product.presentation.request.ProductRegisterRequest;
import com.fordogs.product.presentation.request.ProductUpdateRequest;
import com.fordogs.product.presentation.response.*;
import com.fordogs.user.application.UserQueryService;
import com.fordogs.user.domain.entity.mysql.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final S3ImageUploader s3ImageUploader;
    private final UserQueryService userQueryService;

    @Transactional
    public ProductRegisterResponse createProduct(ProductRegisterRequest request) {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        UserEntity userEntity = userQueryService.findById(userId);

        if (request.getProductQuantity() <= 0) {
            throw ProductErrorCode.PRODUCT_QUANTITY_INVALID.toException();
        }

        checkProductNameDuplicate(request.getProductName());

        ProductEntity savedProductEntity = productRepository.save(request.toEntity(userEntity));

        return ProductRegisterResponse.toResponse(savedProductEntity);
    }

    public Page<ProductSearchResponse> searchProducts(String sellerId, Category category, Pageable pageable) {
        return productRepository.findAll(ProductSpecification.withSellerAndCategory(sellerId, category), pageable)
                .map(ProductSearchResponse::toResponse);
    }

    public ProductDetailsResponse findProductDetails(UUID productId) {
        ProductEntity productEntity = findActiveProductWithActiveUserById(productId);

        return ProductDetailsResponse.toResponse(productEntity);
    }

    @Transactional
    public ProductUpdateResponse updateProduct(UUID productId, ProductUpdateRequest request) {
        ProductEntity productEntity = findActiveProductWithActiveUserById(productId);
        checkProductNameDuplicate(request.getProductName());
        productEntity.update(request.getProductName(), request.getProductPrice(), request.getProductQuantity(),
                request.getProductDescription(), request.getProductImages(), request.getProductCategory());

        return ProductUpdateResponse.toResponse(productEntity);
    }

    @Transactional
    public void deactivateProduct(UUID productId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(ProductErrorCode.PRODUCT_NOT_FOUND::toException);
        productEntity.disable();
    }

    public ProductImageUploadResponse uploadProductImages(MultipartFile[] imageFiles) {
        List<ImageUploadInfo> imageUploadInfoList = Arrays.stream(imageFiles)
                .map(s3ImageUploader::uploadImage)
                .collect(Collectors.toList());

        return ProductImageUploadResponse.toResponse(imageUploadInfoList);
    }

    public void deleteProductImages(String[] imageUrls) {
        Arrays.stream(imageUrls).forEach(s3ImageUploader::deleteImage);
    }

    public List<ProductEntity> findActiveProductsWithActiveUserByIds(Set<UUID> productIds) {
        List<ProductEntity> products = productRepository.findAllByIdAndEnabledTrueAndUserEnabledTrue(productIds);
        if (products.size() != productIds.size() || products.isEmpty()) {
            throw ProductErrorCode.PRODUCT_NOT_FOUND.toException();
        }

        return products;
    }

    public ProductEntity findById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(ProductErrorCode.PRODUCT_NOT_FOUND::toException);
    }

    public List<ProductEntity> findLowStockProducts(int quantity) {
        return productRepository.findByStockQuantityLessThan(quantity);
    }

    private ProductEntity findActiveProductWithActiveUserById(UUID productId) {
        return productRepository.findByIdAndEnabledTrueAndUserEnabledTrue(productId)
                .orElseThrow(ProductErrorCode.PRODUCT_NOT_FOUND::toException);
    }

    private void checkProductNameDuplicate(String productName) {
        if (productRepository.existsByName(productName)) {
            throw ProductErrorCode.PRODUCT_ALREADY_EXISTS.toException();
        }
    }
}
