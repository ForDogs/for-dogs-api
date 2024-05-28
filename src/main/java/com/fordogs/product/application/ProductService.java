package com.fordogs.product.application;

import com.fordogs.core.domian.entity.ProductEntity;
import com.fordogs.core.domian.entity.UserManagementEntity;
import com.fordogs.core.domian.vo.Id;
import com.fordogs.core.exception.error.ProductServiceErrorCode;
import com.fordogs.core.infrastructure.ProductRepository;
import com.fordogs.core.infrastructure.aws.s3.S3ImageUploader;
import com.fordogs.core.infrastructure.aws.s3.dto.ImageUploadResponse;
import com.fordogs.core.util.HttpServletUtil;
import com.fordogs.core.util.constants.RequestAttributesConstants;
import com.fordogs.product.presentation.dto.ProductCreateDto;
import com.fordogs.product.presentation.dto.ProductDetailDto;
import com.fordogs.product.presentation.dto.ProductImageFileUploadDto;
import com.fordogs.product.presentation.dto.ProductListDto;
import com.fordogs.user.application.UserManagementService;
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
    public ProductCreateDto.Response createProduct(ProductCreateDto.Request request) {
        UUID userId = (UUID) HttpServletUtil.getRequestAttribute(RequestAttributesConstants.USER_ID);
        UserManagementEntity userManagementEntity = userManagementService.findById(userId);
        if (productRepository.existsByName(request.getProductName())) {
            throw ProductServiceErrorCode.PRODUCT_ALREADY_EXISTS.toException();
        }
        ProductEntity saveProductEntity = productRepository.save(request.toEntity(userManagementEntity));

        return ProductCreateDto.Response.toResponse(saveProductEntity);
    }

    public Page<ProductListDto.Response> findProducts(String sellerId, Pageable pageable) {
        Page<ProductEntity> productEntities = (sellerId != null)
                ? productRepository.findBySellerAccountAndSellerEnabledTrueAndEnabledTrue(Id.builder().value(sellerId).build(), pageable)
                : productRepository.findAllBySellerEnabledTrueAndEnabledTrue(pageable);

        return productEntities.map(ProductListDto.Response::toResponse);
    }

    public ProductDetailDto.Response findProductDetails(String productId) {
        ProductEntity productEntity = productRepository.findByIdAndSellerEnabledTrueAndEnabledTrue(UUID.fromString(productId))
                .orElseThrow(ProductServiceErrorCode.PRODUCT_NOT_FOUND::toException);

        return ProductDetailDto.Response.toResponse(productEntity);
    }

    public ProductImageFileUploadDto.Response uploadProductImages(MultipartFile[] imageFiles) {
        List<ImageUploadResponse> imageUploadResponseList = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            ImageUploadResponse imageUploadResponse = s3ImageUploader.uploadImage(imageFile);
            imageUploadResponseList.add(imageUploadResponse);
        }

        return ProductImageFileUploadDto.Response.toResponse(imageUploadResponseList);
    }
}
