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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        boolean productExists = productRepository.existsByName(request.getProductName());
        productEntity.checkIfProductExists(productExists);

        ProductEntity savedProductEntity = productRepository.save(productEntity);

        return ProductRegisterResponse.toResponse(savedProductEntity);
    }

    public Page<ProductSearchResponse> searchProducts(String sellerId, Category category, Pageable pageable) {
        return productRepository.findAll(ProductSpecification.withSellerAndCategory(sellerId, category), pageable)
                .map(ProductSearchResponse::toResponse);
    }

    public ProductDetailsResponse findProductDetails(String productId) {
        ProductEntity productEntity = productRepository.findProductWithEnabledSellerAndProduct(UUID.fromString(productId))
                .orElseThrow(ProductErrorCode.PRODUCT_NOT_FOUND::toException);

        return ProductDetailsResponse.toResponse(productEntity);
    }

    @Transactional
    public ProductUpdateResponse updateProduct(String productId, ProductUpdateRequest request) {
        ProductEntity productEntity = productRepository.findProductWithEnabledSellerAndProduct(UUID.fromString(productId))
                .orElseThrow(ProductErrorCode.PRODUCT_NOT_FOUND::toException);

        boolean productExists = productRepository.existsByName(request.getProductName());
        productEntity.checkIfProductExists(productExists);

        UUID userId = (UUID) HttpServletUtil.getRequestAttribute(HttpRequestConstants.REQUEST_ATTRIBUTE_USER_ID);
        productEntity.getSeller().checkUserId(userId);

        productEntity.update(request.getProductName(), request.getProductPrice(), request.getProductQuantity(),
                request.getProductDescription(), request.getProductImages(), request.getProductCategory());

        return ProductUpdateResponse.toResponse(productEntity);
    }

    public ProductImageUploadResponse uploadProductImages(MultipartFile[] imageFiles) {
        List<ImageUploadInfo> imageUploadInfoList = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            ImageUploadInfo imageUploadInfo = s3ImageUploader.uploadImage(imageFile);
            imageUploadInfoList.add(imageUploadInfo);
        }

        return ProductImageUploadResponse.toResponse(imageUploadInfoList);
    }

    public void deleteProductImages(String[] imageUrls) {
        for (String imageUrl : imageUrls) {
            s3ImageUploader.deleteImage(imageUrl);
        }
    }
}
