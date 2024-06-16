package com.fordogs.order.application;

import com.fordogs.core.util.HttpServletUtil;
import com.fordogs.core.util.constants.HttpRequestConstants;
import com.fordogs.order.domain.entity.OrderEntity;
import com.fordogs.order.infrastructure.OrderRepository;
import com.fordogs.order.presentation.request.OrderRegisterRequest;
import com.fordogs.order.presentation.response.OrderRegisterResponse;
import com.fordogs.user.application.UserManagementService;
import com.fordogs.user.domain.entity.UserManagementEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        UUID userId = (UUID) HttpServletUtil.getRequestAttribute(HttpRequestConstants.REQUEST_ATTRIBUTE_USER_ID);
        UserManagementEntity userManagementEntity = userManagementService.findById(userId);
        OrderEntity savedOrderEntity = orderRepository.save(request.toEntity(userManagementEntity));
        orderItemService.createOrderItem(request.getOrderItems(), savedOrderEntity);

        return OrderRegisterResponse.toResponse(savedOrderEntity);
    }
}
