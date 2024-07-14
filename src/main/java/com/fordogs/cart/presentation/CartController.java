package com.fordogs.cart.presentation;

import com.fordogs.cart.application.CartQueryService;
import com.fordogs.cart.application.CartService;
import com.fordogs.cart.error.CartErrorCode;
import com.fordogs.cart.presentation.request.CartCreateRequest;
import com.fordogs.cart.presentation.response.CartSearchResponse;
import com.fordogs.configuraion.swagger.ApiErrorCode;
import com.fordogs.core.presentation.SuccessResponse;
import com.fordogs.security.exception.error.SecurityErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Cart", description = "Cart APIs")
@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartQueryService cartQueryService;

    @Operation(summary = "장바구니 등록", operationId = "/carts")
    @ApiErrorCode({CartErrorCode.class, SecurityErrorCode.class})
    @PostMapping
    public ResponseEntity<SuccessResponse<Object>> handleCreateCartRequest(
            @Valid @RequestBody CartCreateRequest request) {
        cartService.createCart(request);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "장바구니 전체 조회", operationId = "/carts")
    @ApiErrorCode({CartErrorCode.class, SecurityErrorCode.class})
    @GetMapping
    public ResponseEntity<SuccessResponse<List<CartSearchResponse>>> handleGetAllCartsRequest() {
        List<CartSearchResponse> response = cartQueryService.getAllCartsByUserId();

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }
}
