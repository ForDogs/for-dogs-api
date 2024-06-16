package com.fordogs.order.presentation;

import com.fordogs.configuraion.swagger.ApiErrorCode;
import com.fordogs.core.presentation.SuccessResponse;
import com.fordogs.order.application.OrderService;
import com.fordogs.order.error.OrderErrorCode;
import com.fordogs.order.presentation.request.OrderRegisterRequest;
import com.fordogs.order.presentation.response.OrderRegisterResponse;
import com.fordogs.security.exception.error.SecurityErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "Order APIs")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 등록", operationId = "/orders")
    @ApiErrorCode({OrderErrorCode.class, SecurityErrorCode.class})
    @PostMapping
    public ResponseEntity<SuccessResponse<OrderRegisterResponse>> handleCreateOrderRequest(
            @Valid @RequestBody OrderRegisterRequest request) {
        OrderRegisterResponse response = orderService.createOrder(request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }
}
