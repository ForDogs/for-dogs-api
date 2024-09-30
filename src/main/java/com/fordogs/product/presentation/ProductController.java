package com.fordogs.product.presentation;

import com.fordogs.configuraion.swagger.ApiErrorCode;
import com.fordogs.core.domain.vo.error.PriceErrorCode;
import com.fordogs.core.domain.vo.error.QuantityErrorCode;
import com.fordogs.product.application.ProductQueryService;
import com.fordogs.product.domain.enums.Category;
import com.fordogs.core.presentation.SuccessResponse;
import com.fordogs.product.application.ProductService;
import com.fordogs.product.error.ProductErrorCode;
import com.fordogs.product.error.ProductValidationErrorCode;
import com.fordogs.product.error.S3ErrorCode;
import com.fordogs.product.presentation.request.ProductRegisterRequest;
import com.fordogs.product.presentation.request.ProductUpdateRequest;
import com.fordogs.product.presentation.response.*;
import com.fordogs.security.exception.error.SecurityErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "Product", description = "Product APIs")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductQueryService productQueryService;

    @Operation(summary = "상품 등록", operationId = "/products")
    @ApiErrorCode({ProductErrorCode.class, ProductValidationErrorCode.class, PriceErrorCode.class, QuantityErrorCode.class, SecurityErrorCode.class})
    @PostMapping
    public ResponseEntity<SuccessResponse<ProductRegisterResponse>> handleCreateProductRequest(
            @Valid @RequestBody ProductRegisterRequest request) {
        ProductRegisterResponse response = productService.createProduct(request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }

    @Operation(summary = "상품 전체 조회 및 필터링", operationId = "/products")
    @ApiErrorCode(ProductErrorCode.class)
    @GetMapping
    public ResponseEntity<SuccessResponse<Page<ProductSearchResponse>>> handleSearchProductsRequest(
            @Parameter(name = "seller", description = "판매자 ID", example = "hong1234") @RequestParam(required = false, value = "seller") String sellerId,
            @Parameter(name = "category", description = "상품 카테고리") @RequestParam(required = false, value = "category") Category category,
            @Parameter(name = "name", description = "상품 이름") @RequestParam(required = false, value = "name") String productName,
            @ParameterObject @PageableDefault Pageable pageable) {
        Page<ProductSearchResponse> response = productQueryService.searchProducts(sellerId, category, productName, pageable);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }

    @Operation(summary = "상품 상세 조회", operationId = "/products/{productId}")
    @ApiErrorCode(ProductErrorCode.class)
    @GetMapping("/{productId}")
    public ResponseEntity<SuccessResponse<ProductDetailsResponse>> handleFindProductDetailsRequest(
            @Schema(name = "productId", description = "상품 ID", example = "caa62dd1-1a87-11ef-b72d-9713d59057a1") @PathVariable(name = "productId") UUID productId) {
        ProductDetailsResponse response = productQueryService.findProductDetails(productId);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }

    @Operation(summary = "상품 수정", operationId = "/products/{productId}")
    @ApiErrorCode({ProductErrorCode.class, ProductValidationErrorCode.class, PriceErrorCode.class, QuantityErrorCode.class, SecurityErrorCode.class})
    @PatchMapping("/{productId}")
    public ResponseEntity<SuccessResponse<ProductUpdateResponse>> handleUpdateProductRequest(
            @Schema(name = "productId", description = "상품 ID", example = "caa62dd1-1a87-11ef-b72d-9713d59057a1") @PathVariable(name = "productId") UUID productId,
            @RequestBody @Valid ProductUpdateRequest request) {
        ProductUpdateResponse response = productService.updateProduct(productId, request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }

    @Operation(summary = "상품 비활성화", operationId = "/products/{productId}")
    @ApiErrorCode({ProductErrorCode.class, SecurityErrorCode.class})
    @DeleteMapping("/{productId}")
    public ResponseEntity<SuccessResponse<Object>> handleDeactivateProductRequest(
            @Schema(name = "productId", description = "상품 ID", example = "caa62dd1-1a87-11ef-b72d-9713d59057a1") @PathVariable(name = "productId") UUID productId) {
        productService.deactivateProduct(productId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "상품 이미지 파일 업로드", operationId = "/products/images", description = "업로드 가능 이미지 확장자: jpg, jpeg, png, gif")
    @ApiErrorCode({S3ErrorCode.class, SecurityErrorCode.class})
    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<ProductImageUploadResponse>> handleUploadProductImagesRequest(
            @RequestPart(value = "imageFiles") MultipartFile[] imageFiles) {
        ProductImageUploadResponse response = productQueryService.uploadProductImages(imageFiles);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }

    @Operation(summary = "상품 이미지 파일 삭제", operationId = "/products/images")
    @ApiErrorCode({S3ErrorCode.class, SecurityErrorCode.class})
    @DeleteMapping(value = "/images")
    public ResponseEntity<SuccessResponse<Object>> handleDeleteProductImagesRequest(
            @Parameter(name = "imageUrls", description = "상품 이미지 URL", required = true) @RequestParam("imageUrls") String[] imageUrls) {
        productQueryService.deleteProductImages(imageUrls);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
