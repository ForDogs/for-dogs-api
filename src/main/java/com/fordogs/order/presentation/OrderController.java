package com.fordogs.order.presentation;

import com.fordogs.configuraion.swagger.ApiErrorCode;
import com.fordogs.core.domain.vo.error.PriceErrorCode;
import com.fordogs.core.presentation.SuccessResponse;
import com.fordogs.order.application.OrderQueryService;
import com.fordogs.order.application.OrderService;
import com.fordogs.order.error.OrderErrorCode;
import com.fordogs.order.presentation.request.OrderCancelRequest;
import com.fordogs.order.presentation.request.OrderRegisterRequest;
import com.fordogs.order.presentation.request.OrderStatusUpdateRequest;
import com.fordogs.order.presentation.response.OrderRegisterResponse;
import com.fordogs.order.presentation.response.OrderSearchBuyerResponse;
import com.fordogs.order.presentation.response.OrderSearchSellerResponse;
import com.fordogs.security.exception.error.SecurityErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@Tag(name = "Order", description = "Order APIs")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderQueryService orderQueryService;

    @Operation(summary = "주문 등록", operationId = "/orders")
    @ApiErrorCode({OrderErrorCode.class, PriceErrorCode.class, SecurityErrorCode.class})
    @PostMapping
    public ResponseEntity<SuccessResponse<OrderRegisterResponse>> handleCreateOrderRequest(
            @Valid @RequestBody OrderRegisterRequest request) {
        OrderRegisterResponse response = orderService.createOrder(request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }

    @Operation(
            summary = "주문 취소",
            operationId = "/orders/{orderId}/cancel",
            description = "주문 ID를 기반으로 특정 주문을 취소 후 취소된 주문의 결제도 함께 취소합니다."
    )
    @ApiErrorCode({OrderErrorCode.class, SecurityErrorCode.class})
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<SuccessResponse<Object>> handleCancelOrderRequest(
            @Schema(name = "orderId", description = "주문 ID", example = "11ef3c60-149c-2901-9ff0-7decf5c16a75") @PathVariable("orderId") UUID orderId,
            @Valid @RequestBody OrderCancelRequest request) {
        orderService.cancelOrder(orderId, request);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "주문 상태 변경",
            operationId = "/orders/{orderId}/status",
            description = "주문 상태는 '구매 확인', '배송 대기 중', '배송 중', '배송 완료' 상태로만 변경할 수 있습니다."
    )
    @ApiErrorCode({OrderErrorCode.class, SecurityErrorCode.class})
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<SuccessResponse<Object>> handleOrderStatusUpdateRequest(
            @Schema(name = "orderId", description = "주문 ID", example = "11ef3c60-149c-2901-9ff0-7decf5c16a75") @PathVariable("orderId") UUID orderId,
            @Valid @RequestBody OrderStatusUpdateRequest request) {
        orderService.orderStatusUpdate(orderId, request);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "주문 내역 조회", operationId = "/orders/buyer")
    @ApiErrorCode({OrderErrorCode.class, SecurityErrorCode.class})
    @GetMapping("/buyer")
    public ResponseEntity<SuccessResponse<OrderSearchBuyerResponse[]>> handleSearchBuyerOrdersRequest(
            @Parameter(example = "2024-06-23") @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(example = "2024-06-30") @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        OrderSearchBuyerResponse[] response = orderQueryService.searchBuyerOrders(startDate, endDate);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }

    @Operation(summary = "판매 내역 조회", operationId = "/orders/seller")
    @ApiErrorCode({OrderErrorCode.class, SecurityErrorCode.class})
    @GetMapping("/seller")
    public ResponseEntity<SuccessResponse<OrderSearchSellerResponse[]>> handleSearchSellerOrdersRequest(
            @Parameter(example = "2024-06-23") @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(example = "2024-06-30") @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        OrderSearchSellerResponse[] response = orderQueryService.searchSellerOrders(startDate, endDate);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }
}
