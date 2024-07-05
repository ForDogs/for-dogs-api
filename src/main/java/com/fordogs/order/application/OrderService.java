package com.fordogs.order.application;

import com.fordogs.order.domain.entity.OrderEntity;
import com.fordogs.order.domain.entity.OrderItemEntity;
import com.fordogs.order.domain.eums.OrderStatus;
import com.fordogs.order.error.OrderErrorCode;
import com.fordogs.order.infrastructure.OrderRepository;
import com.fordogs.order.presentation.request.OrderRegisterRequest;
import com.fordogs.order.presentation.request.OrderStatusUpdateRequest;
import com.fordogs.order.presentation.response.OrderRegisterResponse;
import com.fordogs.order.presentation.response.OrderSearchBuyerResponse;
import com.fordogs.order.presentation.response.OrderSearchSellerResponse;
import com.fordogs.user.application.UserManagementService;
import com.fordogs.user.domain.entity.mysql.UserManagementEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final UserManagementService userManagementService;

    @Transactional
    public OrderRegisterResponse createOrder(OrderRegisterRequest request) {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        UserManagementEntity userManagementEntity = userManagementService.findById(userId);

        OrderEntity orderEntity = request.toEntity(userManagementEntity);
        orderRepository.save(orderEntity);

        List<OrderItemEntity> orderItemEntities = orderItemService.createOrderItems(request.getOrderItems(), orderEntity);
        orderEntity.addOrderItems(orderItemEntities);

        return OrderRegisterResponse.toResponse(orderEntity);
    }

    public OrderSearchBuyerResponse[] searchBuyerOrders(LocalDate startDate, LocalDate endDate) {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<OrderEntity> orderEntityList = orderRepository.findOrdersByCreatedAtBetweenAndBuyerId(startDateTime, endDateTime, userId);

        return orderEntityList.parallelStream()
                .map(OrderSearchBuyerResponse::toResponse)
                .toArray(OrderSearchBuyerResponse[]::new);
    }

    public OrderSearchSellerResponse[] searchSellerOrders(LocalDate startDate, LocalDate endDate) {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<OrderEntity> orderEntityList = orderRepository.findOrdersBySellerAndDateRange(startDateTime, endDateTime, userId);

        return orderEntityList.parallelStream()
                .map(OrderSearchSellerResponse::toResponse)
                .toArray(OrderSearchSellerResponse[]::new);
    }

    @Transactional
    public void orderStatusUpdate(UUID orderId, OrderStatusUpdateRequest request) {
        validateOrderStatus(request.getOrderStatus());
        OrderEntity orderEntity = findOrderById(orderId);
        orderEntity.changeOrderStatus(request.getOrderStatus());
    }

    public OrderEntity findOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(OrderErrorCode.ORDER_NOT_FOUND::toException);
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
