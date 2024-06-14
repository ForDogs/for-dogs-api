package com.fordogs.product.application;

import com.fordogs.core.util.HttpServletUtil;
import com.fordogs.core.util.constants.HttpRequestConstants;
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
    private final UserManagementService userManagementService;
    private final S3ImageUploader s3ImageUploader;

    @Transactional
    public ProductRegisterResponse addProduct(ProductRegisterRequest request) {
        UUID userId = (UUID) HttpServletUtil.getRequestAttribute(HttpRequestConstants.REQUEST_ATTRIBUTE_USER_ID);
        UserManagementEntity userManagementEntity = userManagementService.findById(userId);
        ProductEntity productEntity = request.toEntity(userManagementEntity);

        checkProductNameDuplicate(request.getProductName());

        ProductEntity savedProductEntity = productRepository.save(productEntity);

        return ProductRegisterResponse.toResponse(savedProductEntity);
    }

    public Page<ProductSearchResponse> searchProducts(String sellerId, Category category, Pageable pageable) {
        return productRepository.findAll(ProductSpecification.withSellerAndCategory(sellerId, category), pageable)
                .map(ProductSearchResponse::toResponse);
    }

    public ProductDetailsResponse findProductDetails(String productId) {
        ProductEntity productEntity = findEnabledProductById(productId);

        return ProductDetailsResponse.toResponse(productEntity);
    }

    @Transactional
    public ProductUpdateResponse updateProduct(String productId, ProductUpdateRequest request) {
        ProductEntity productEntity = findEnabledProductById(productId);
        checkProductNameDuplicate(request.getProductName());
        productEntity.update(request.getProductName(), request.getProductPrice(), request.getProductQuantity(),
                request.getProductDescription(), request.getProductImages(), request.getProductCategory());

        return ProductUpdateResponse.toResponse(productEntity);
    }

    @Transactional
    public void deactivateProduct(String productId) {
        ProductEntity productEntity = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(ProductErrorCode.PRODUCT_NOT_FOUND::toException);
        productEntity.validateProductIsEnabled();
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

    private ProductEntity findEnabledProductById(String productId) {
        return productRepository.findProductWithEnabledSellerAndProduct(UUID.fromString(productId))
                .orElseThrow(ProductErrorCode.PRODUCT_NOT_FOUND::toException);
    }

    private void checkProductNameDuplicate(String productName) {
        if (productRepository.existsByName(productName)) {
            throw ProductErrorCode.PRODUCT_ALREADY_EXISTS.toException();
        }
    }
}
