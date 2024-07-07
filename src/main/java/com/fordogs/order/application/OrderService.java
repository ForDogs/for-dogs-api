package com.fordogs.order.application;

import com.fordogs.order.domain.entity.OrderEntity;
import com.fordogs.order.domain.entity.OrderItemEntity;
import com.fordogs.order.domain.eums.OrderStatus;
import com.fordogs.order.error.OrderErrorCode;
import com.fordogs.order.infrastructure.OrderRepository;
import com.fordogs.order.presentation.request.OrderCancelRequest;
import com.fordogs.order.presentation.request.OrderRegisterRequest;
import com.fordogs.order.presentation.request.OrderStatusUpdateRequest;
import com.fordogs.order.presentation.response.OrderRegisterResponse;
import com.fordogs.payment.application.PaymentService;
import com.fordogs.user.application.UserManagementService;
import com.fordogs.user.domain.entity.mysql.UserManagementEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemQueryService orderItemQueryService;
    private final OrderItemService orderItemService;
    private final OrderQueryService orderQueryService;
    private final PaymentService paymentService;
    private final UserManagementService userManagementService;

    public OrderRegisterResponse createOrder(OrderRegisterRequest request) {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        UserManagementEntity userManagementEntity = userManagementService.findById(userId);

        OrderEntity orderEntity = request.toEntity(userManagementEntity);
        orderRepository.save(orderEntity);

        List<OrderItemEntity> orderItemEntities = orderItemService.createOrderItems(request.getOrderItems(), orderEntity);
        orderEntity.addOrderItems(orderItemEntities);

        return OrderRegisterResponse.toResponse(orderEntity);
    }

    public void orderStatusUpdate(UUID orderId, OrderStatusUpdateRequest request) {
        validateOrderStatus(request.getOrderStatus());
        OrderEntity orderEntity = orderQueryService.findOrderById(orderId);
        orderEntity.changeOrderStatus(request.getOrderStatus());
    }

    public void cancelOrder(OrderCancelRequest request) {
        OrderEntity orderEntity = orderQueryService.findOrderById(request.getOrderId());
        orderEntity.changeOrderStatus(OrderStatus.CANCELLED);

        paymentService.cancelPayment(orderEntity, request.getCancelReason());

        List<OrderItemEntity> orderItems = orderItemQueryService.getOrderItemsWithProductsByOrderId(request.getOrderId());
        orderItems.forEach(orderItem ->
                orderItem.getProduct().increaseQuantity(orderItem.getQuantity()));
    }

    private void validateOrderStatus(OrderStatus orderStatus) {
        List<OrderStatus> allowedStatusesForSeller = Arrays.asList(
                OrderStatus.CONFIRMED,
                OrderStatus.AWAITING_SHIPMENT,
                OrderStatus.SHIPPED,
                OrderStatus.DELIVERED
        );
        if (!allowedStatusesForSeller.contains(orderStatus)) {
            throw OrderErrorCode.INVALID_ACTION_FOR_ORDER_STATUS.toException();
        }
    }
}
