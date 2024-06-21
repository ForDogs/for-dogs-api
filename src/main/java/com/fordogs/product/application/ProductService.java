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
import com.fordogs.user.application.UserManagementService;
import com.fordogs.user.domain.entity.UserManagementEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final S3ImageUploader s3ImageUploader;
    private final UserManagementService userManagementService;

    @Transactional
    public ProductRegisterResponse createProduct(ProductRegisterRequest request) {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        UserManagementEntity userManagementEntity = userManagementService.findById(userId);

        checkProductNameDuplicate(request.getProductName());
        ProductEntity savedProductEntity = productRepository.save(request.toEntity(userManagementEntity));

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

    public ProductEntity findActiveProductWithActiveUserById(UUID productId) {
        return productRepository.findByIdAndEnabledTrueAndUserEnabledTrue(productId)
                .orElseThrow(ProductErrorCode.PRODUCT_NOT_FOUND::toException);
    }

    private void checkProductNameDuplicate(String productName) {
        if (productRepository.existsByName(productName)) {
            throw ProductErrorCode.PRODUCT_ALREADY_EXISTS.toException();
        }
    }
}
