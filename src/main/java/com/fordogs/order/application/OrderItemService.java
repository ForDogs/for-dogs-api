package com.fordogs.order.application;

import com.fordogs.order.domain.entity.OrderEntity;
import com.fordogs.order.domain.entity.OrderItemEntity;
import com.fordogs.order.infrastructure.OrderItemRepository;
import com.fordogs.order.presentation.request.OrderItemRequest;
import com.fordogs.product.application.ProductService;
import com.fordogs.product.domain.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;

    public List<OrderItemEntity> createOrderItem(List<OrderItemRequest> orderItems, OrderEntity orderEntity) {
        Set<UUID> productIds = orderItems.stream()
                .map(OrderItemRequest::getOrderProductId)
                .collect(Collectors.toSet());

        List<ProductEntity> products = productService.findActiveProductsWithActiveUserByIds(productIds);

        Map<UUID, ProductEntity> productCache = products.stream()
                .collect(Collectors.toMap(ProductEntity::getId, product -> product));

        List<OrderItemEntity> orderItemEntities = orderItems.stream()
                .map(orderItem -> {
                    UUID productId = orderItem.getOrderProductId();
                    ProductEntity productEntity = productCache.get(productId);
                    productEntity.decreaseQuantity(orderItem.getOrderQuantity());

                    return orderItem.toEntity(orderEntity, productEntity);
                })
                .collect(Collectors.toList());

        return orderItemRepository.saveAll(orderItemEntities);
    }
}
