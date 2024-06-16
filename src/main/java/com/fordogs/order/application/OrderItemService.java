package com.fordogs.order.application;

import com.fordogs.order.domain.entity.OrderEntity;
import com.fordogs.order.infrastructure.OrderItemRepository;
import com.fordogs.order.presentation.request.OrderItemInfo;
import com.fordogs.product.application.ProductService;
import com.fordogs.product.domain.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;

    @Transactional
    public void createOrderItem(OrderItemInfo[] orderItems, OrderEntity orderEntity) {
        for (OrderItemInfo orderItem : orderItems) {
            ProductEntity productEntity = productService.findEnabledProductById(orderItem.getOrderProductId());
            productEntity.decreaseQuantity(orderItem.getOrderQuantity());
            orderItemRepository.save(orderItem.toEntity(orderEntity, productEntity));
        }
    }
}
