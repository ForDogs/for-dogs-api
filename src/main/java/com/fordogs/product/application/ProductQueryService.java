package com.fordogs.product.application;

import com.fordogs.product.application.aws.s3.S3ImageUploader;
import com.fordogs.product.application.aws.s3.response.ImageUploadInfo;
import com.fordogs.product.domain.entity.ProductEntity;
import com.fordogs.product.domain.enums.Category;
import com.fordogs.product.domain.specification.ProductSpecification;
import com.fordogs.product.error.ProductErrorCode;
import com.fordogs.product.infrastructure.ProductRepository;
import com.fordogs.product.presentation.response.ProductDetailsResponse;
import com.fordogs.product.presentation.response.ProductImageUploadResponse;
import com.fordogs.product.presentation.response.ProductSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class ProductQueryService {

    private final ProductRepository productRepository;
    private final S3ImageUploader s3ImageUploader;

    public Page<ProductSearchResponse> searchProducts(String sellerId, Category category, String productName, Pageable pageable) {
        return productRepository.findAll(ProductSpecification.withSellerAndCategory(sellerId, category, productName), pageable)
                .map(ProductSearchResponse::toResponse);
    }

    public ProductDetailsResponse findProductDetails(UUID productId) {
        ProductEntity productEntity = findActiveProductWithActiveUserById(productId);

        return ProductDetailsResponse.toResponse(productEntity);
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

    public ProductEntity findActiveProductWithActiveUserById(UUID productId) {
        return productRepository.findByIdAndEnabledTrueAndUserEnabledTrue(productId)
                .orElseThrow(ProductErrorCode.PRODUCT_NOT_FOUND::toException);
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
}
