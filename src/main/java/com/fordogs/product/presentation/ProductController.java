package com.fordogs.product.presentation;

import com.fordogs.configuraion.swagger.ApiErrorCode;
import com.fordogs.product.application.ProductService;
import com.fordogs.product.error.ProductErrorCode;
import com.fordogs.core.presentation.SuccessResponse;
import com.fordogs.product.presentation.dto.CreateProductDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
}
