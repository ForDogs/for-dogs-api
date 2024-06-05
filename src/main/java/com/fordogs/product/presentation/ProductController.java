package com.fordogs.product.presentation;

import com.fordogs.configuraion.swagger.ApiErrorCode;
import com.fordogs.product.domain.enums.Category;
import com.fordogs.core.presentation.SuccessResponse;
import com.fordogs.product.application.ProductService;
import com.fordogs.product.error.ProductErrorCode;
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

@Tag(name = "Product", description = "Product APIs")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 등록", operationId = "/products/register")
    @ApiErrorCode({ProductErrorCode.class, SecurityErrorCode.class})
    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<ProductRegisterResponse>> handleAddProductRequest(
            @Valid @RequestBody ProductRegisterRequest request) {
        ProductRegisterResponse response = productService.addProduct(request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }

    @Operation(summary = "상품 전체 검색 및 필터링", operationId = "/products/search")
    @ApiErrorCode(ProductErrorCode.class)
    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<Page<ProductSearchResponse>>> handleSearchProductsRequest(
            @Parameter(name = "seller", description = "판매자 ID", example = "hong1234") @RequestParam(required = false, value = "seller") String sellerId,
            @Parameter(name = "category", description = "상품 카테고리") @RequestParam(required = false, value = "category") Category category,
            @ParameterObject @PageableDefault Pageable pageable) {
        Page<ProductSearchResponse> response = productService.searchProducts(sellerId, category, pageable);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }

    @Operation(summary = "상품 상세 검색", operationId = "/products/{productId}/details")
    @ApiErrorCode(ProductErrorCode.class)
    @GetMapping("/{productId}/details")
    public ResponseEntity<SuccessResponse<ProductDetailsResponse>> handleFindProductDetailsRequest(
            @Schema(name = "productId", description = "상품 ID", example = "caa62dd1-1a87-11ef-b72d-9713d59057a1") @PathVariable(name = "productId") String productId) {
        ProductDetailsResponse response = productService.findProductDetails(productId);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }

    @Operation(summary = "상품 수정", operationId = "/products/{productId}/update")
    @ApiErrorCode(ProductErrorCode.class)
    @PatchMapping("/{productId}/update")
    public ResponseEntity<SuccessResponse<ProductUpdateResponse>> handleUpdateProductRequest(
            @Schema(name = "productId", description = "상품 ID", example = "caa62dd1-1a87-11ef-b72d-9713d59057a1") @PathVariable(name = "productId") String productId,
            @RequestBody @Valid ProductUpdateRequest request) {
        ProductUpdateResponse response = productService.updateProduct(productId, request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }

    @Operation(summary = "상품 이미지 파일 업로드", operationId = "/products/images/upload", description = "업로드 가능 이미지 확장자: jpg, jpeg, png, gif")
    @ApiErrorCode({S3ErrorCode.class, SecurityErrorCode.class})
    @PostMapping(value = "/images/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<ProductImageUploadResponse>> handleUploadProductImagesRequest(
            @RequestPart(value = "imageFiles") MultipartFile[] imageFiles) {
        ProductImageUploadResponse response = productService.uploadProductImages(imageFiles);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }

    @Operation(summary = "상품 이미지 파일 삭제", operationId = "/products/images")
    @ApiErrorCode({S3ErrorCode.class, SecurityErrorCode.class})
    @DeleteMapping(value = "/images")
    public ResponseEntity<SuccessResponse<Object>> handleDeleteProductImagesRequest(
            @Parameter(name = "imageUrls", description = "상품 이미지 URL", required = true) @RequestParam("imageUrls") String[] imageUrls) {
        productService.deleteProductImages(imageUrls);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
