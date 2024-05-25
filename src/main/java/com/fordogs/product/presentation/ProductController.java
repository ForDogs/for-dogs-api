package com.fordogs.product.presentation;

import com.fordogs.configuraion.swagger.ApiErrorCode;
import com.fordogs.core.exception.error.ProductServiceErrorCode;
import com.fordogs.core.presentation.SuccessResponse;
import com.fordogs.product.application.ProductService;
import com.fordogs.product.presentation.dto.ProductCreateDto;
import com.fordogs.product.presentation.dto.ProductDetailDto;
import com.fordogs.product.presentation.dto.ProductListDto;
import com.fordogs.core.exception.error.SecurityServiceErrorCode;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product", description = "Product APIs")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 등록", operationId = "/products", description = "판매자(SELLER) 회원 전용 API")
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
    public ResponseEntity<SuccessResponse<ProductDetailDto.Response>> handleFindOneProductsRequest(
            @Parameter(name = "상품 ID", required = true, example = "11ef1a87-caa6-2dd1-b72d-9713d59057a1", in = ParameterIn.PATH) @PathVariable(name = "productId") String productId) {
        ProductDetailDto.Response response = productService.findOneProduct(productId);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }
}
