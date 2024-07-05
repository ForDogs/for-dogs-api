package com.fordogs.order.presentation.request;

import com.fordogs.core.domain.vo.wapper.Price;
import com.fordogs.order.domain.entity.OrderEntity;
import com.fordogs.user.domain.entity.mysql.UserManagementEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "주문 등록 요청")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderRegisterRequest {

    @Schema(description = "주문 아이템 정보")
    private List<OrderItemRequest> orderItems;

    public OrderEntity toEntity(UserManagementEntity userManagementEntity) {
        OrderEntity orderEntity = OrderEntity.builder()
                .buyer(userManagementEntity)
                .totalPrice(Price.builder().value(0).build())
                .build();

        if (orderItems != null && !orderItems.isEmpty()) {
            List<Integer> unitPrices = orderItems.stream()
                    .map(OrderItemRequest::getOrderUnitPrice)
                    .collect(Collectors.toList());

            List<Integer> quantities = orderItems.stream()
                    .map(OrderItemRequest::getOrderQuantity)
                    .collect(Collectors.toList());

            orderEntity.calculateTotalPrice(unitPrices, quantities);
        }

        return orderEntity;
    }
}
