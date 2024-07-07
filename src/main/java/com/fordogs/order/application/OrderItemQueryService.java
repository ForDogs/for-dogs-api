package com.fordogs.order.application;

import com.fordogs.order.domain.entity.OrderItemEntity;
import com.fordogs.order.infrastructure.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderItemQueryService {

    private final OrderItemRepository orderItemRepository;

    public List<OrderItemEntity> getOrderItemsWithProductsByOrderId(UUID orderId) {
        return orderItemRepository.findOrderItemsWithProductsByOrderId(orderId);
    }
}
