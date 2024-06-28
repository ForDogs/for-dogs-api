package com.fordogs.order.application;

import com.fordogs.order.domain.entity.OrderEntity;
import com.fordogs.order.domain.entity.OrderItemEntity;
import com.fordogs.order.infrastructure.OrderRepository;
import com.fordogs.order.presentation.request.OrderRegisterRequest;
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

        List<OrderItemEntity> orderItemEntities = orderItemService.createOrderItem(request.getOrderItems(), orderEntity);
        orderEntity.addOrderItem(orderItemEntities);

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
}
