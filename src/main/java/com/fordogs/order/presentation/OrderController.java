package com.fordogs.order.presentation;

import com.fordogs.configuraion.swagger.ApiErrorCode;
import com.fordogs.core.presentation.SuccessResponse;
import com.fordogs.order.application.OrderService;
import com.fordogs.order.error.OrderErrorCode;
import com.fordogs.order.presentation.request.OrderRegisterRequest;
import com.fordogs.order.presentation.response.OrderRegisterResponse;
import com.fordogs.order.presentation.response.OrderSearchBuyerResponse;
import com.fordogs.order.presentation.response.OrderSearchSellerResponse;
import com.fordogs.security.exception.error.SecurityErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @Operation(summary = "주문 내역 검색", operationId = "/orders/buyer")
    @ApiErrorCode({OrderErrorCode.class, SecurityErrorCode.class})
    @GetMapping("/buyer")
    public ResponseEntity<SuccessResponse<OrderSearchBuyerResponse[]>> handleSearchBuyerOrdersRequest(
            @Parameter(example = "2024-06-23") @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(example = "2024-06-30") @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        OrderSearchBuyerResponse[] response = orderService.searchBuyerOrders(startDate, endDate);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }

    @Operation(summary = "판매 내역 검색", operationId = "/orders/seller")
    @ApiErrorCode({OrderErrorCode.class, SecurityErrorCode.class})
    @GetMapping("/seller")
    public ResponseEntity<SuccessResponse<OrderSearchSellerResponse[]>> handleSearchSellerOrdersRequest(
            @Parameter(example = "2024-06-23") @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(example = "2024-06-30") @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        OrderSearchSellerResponse[] response = orderService.searchSellerOrders(startDate, endDate);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }
}
