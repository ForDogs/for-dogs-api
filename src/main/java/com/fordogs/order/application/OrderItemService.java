package com.fordogs.order.application;

import com.fordogs.order.domain.entity.OrderEntity;
import com.fordogs.order.domain.entity.OrderItemEntity;
import com.fordogs.order.infrastructure.OrderItemRepository;
import com.fordogs.order.presentation.request.OrderItemRequest;
import com.fordogs.product.application.ProductQueryService;
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
@Transactional
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductQueryService productQueryService;

    public List<OrderItemEntity> createOrderItems(List<OrderItemRequest> orderItemRequests, OrderEntity orderEntity) {
        Set<UUID> orderProductIds = orderItemRequests.stream()
                .map(OrderItemRequest::getOrderProductId)
                .collect(Collectors.toSet());

        List<ProductEntity> products = productQueryService.findActiveProductsWithActiveUserByIds(orderProductIds);

        Map<UUID, ProductEntity> productCache = products.stream()
                .collect(Collectors.toMap(ProductEntity::getId, product -> product));

        return orderItemRequests.stream()
                .map(orderItem -> {
                    UUID productId = orderItem.getOrderProductId();
                    ProductEntity productEntity = productCache.get(productId);
                    productEntity.decreaseQuantity(orderItem.getOrderQuantity());

                    return orderItem.toEntity(orderEntity, productEntity);
                })
                .collect(Collectors.toList());
    }

    public void saveAll(List<OrderItemEntity> orderItemEntities) {
        orderItemRepository.saveAll(orderItemEntities);
    }
}
