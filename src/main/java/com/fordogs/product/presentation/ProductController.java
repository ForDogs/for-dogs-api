package com.fordogs.product.presentation;

import com.fordogs.configuraion.swagger.ApiErrorCode;
import com.fordogs.core.exception.error.ProductServiceErrorCode;
import com.fordogs.core.exception.error.S3ErrorCode;
import com.fordogs.core.exception.error.SecurityServiceErrorCode;
import com.fordogs.core.presentation.SuccessResponse;
import com.fordogs.product.application.ProductService;
import com.fordogs.product.presentation.dto.ProductCreateDto;
import com.fordogs.product.presentation.dto.ProductDetailDto;
import com.fordogs.product.presentation.dto.ProductImageFileUploadDto;
import com.fordogs.product.presentation.dto.ProductListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

    @Operation(summary = "상품 등록", operationId = "/products")
    @ApiErrorCode({ProductServiceErrorCode.class, SecurityServiceErrorCode.class})
    @PostMapping
    public ResponseEntity<SuccessResponse<ProductCreateDto.Response>> handleCreateProductRequest(
            @Valid @RequestBody ProductCreateDto.Request request) {
        ProductCreateDto.Response response = productService.createProduct(request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }

    @Operation(summary = "상품 검색 및 필터링", operationId = "/products")
    @ApiErrorCode(ProductServiceErrorCode.class)
    @GetMapping
    public ResponseEntity<SuccessResponse<Page<ProductListDto.Response>>> handleFindProductsRequest(
            @Parameter(name = "seller", example = "hong1234", in = ParameterIn.QUERY) @RequestParam(required = false, value = "seller") String sellerId,
            @ParameterObject @PageableDefault Pageable pageable) {
        Page<ProductListDto.Response> response = productService.findProducts(sellerId, pageable);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }

    @Operation(summary = "상품 상세 검색", operationId = "/products/{productId}")
    @ApiErrorCode(ProductServiceErrorCode.class)
    @GetMapping("/{productId}")
    public ResponseEntity<SuccessResponse<ProductDetailDto.Response>> handleFindProductDetailsRequest(
            @Parameter(name = "상품 ID", required = true, example = "11ef1a87-caa6-2dd1-b72d-9713d59057a1", in = ParameterIn.PATH) @PathVariable(name = "productId") String productId) {
        ProductDetailDto.Response response = productService.findProductDetails(productId);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }

    @Operation(summary = "상품 이미지 파일 업로드", operationId = "/products/images/upload", description = "업로드 가능 이미지 확장자: jpg, jpeg, png, gif")
    @ApiErrorCode({S3ErrorCode.class, SecurityServiceErrorCode.class})
    @PostMapping(value = "/images/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<ProductImageFileUploadDto.Response>> handleUploadProductImagesRequest(
            @RequestPart(value = "imageFiles") MultipartFile[] imageFiles) {
        ProductImageFileUploadDto.Response response = productService.uploadProductImages(imageFiles);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }

    @Operation(summary = "상품 이미지 파일 삭제", operationId = "/products/images")
    @ApiErrorCode({S3ErrorCode.class, SecurityServiceErrorCode.class})
    @DeleteMapping(value = "/images")
    public ResponseEntity<SuccessResponse<Object>> handleDeleteProductImagesRequest(
            @Parameter(name = "상품 이미지 URL", required = true, example = "https://bucket/product.jpg", in = ParameterIn.QUERY) @RequestParam("imageUrls") String[] imageUrls) {
        productService.deleteProductImages(imageUrls);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
