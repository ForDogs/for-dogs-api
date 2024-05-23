package com.fordogs.product.presentation;

import com.fordogs.configuraion.swagger.ApiErrorCode;
import com.fordogs.core.presentation.SuccessResponse;
import com.fordogs.product.application.ProductService;
import com.fordogs.product.error.ProductErrorCode;
import com.fordogs.product.presentation.dto.CreateProductDto;
import com.fordogs.product.presentation.dto.ReadProductDto;
import com.fordogs.security.exception.error.JwtErrorCode;
import io.swagger.v3.oas.annotations.Operation;
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
    @ApiErrorCode(ProductErrorCode.class)
    @PostMapping
    public ResponseEntity<SuccessResponse<CreateProductDto.Response>> handleCreateProductRequest(
            @Valid @RequestBody CreateProductDto.Request request) {
        CreateProductDto.Response response = productService.createProduct(request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }

    @Operation(summary = "상품 검색 및 필터링", operationId = "/products", description = "AccessToken을 통해 회원이 등록한 전체 상품을 조회할 수 있습니다.")
    @ApiErrorCode(JwtErrorCode.class)
    @GetMapping
    public ResponseEntity<SuccessResponse<Page<ReadProductDto.Response>>> handleFindProductsRequest(
            @ParameterObject @PageableDefault Pageable pageable) {
        Page<ReadProductDto.Response> response = productService.findProducts(pageable);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }
}
