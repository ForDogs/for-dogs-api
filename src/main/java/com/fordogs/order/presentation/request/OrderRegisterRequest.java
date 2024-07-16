package com.fordogs.order.presentation.request;

import com.fordogs.order.domain.entity.OrderEntity;
import com.fordogs.user.domain.entity.mysql.UserManagementEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Schema(description = "주문 등록 요청")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderRegisterRequest {

    @Schema(description = "주문 아이템 정보")
    private List<OrderItemRequest> orderItems;

    public OrderEntity toEntity(UserManagementEntity userManagementEntity) {
        return OrderEntity.builder()
                .buyer(userManagementEntity)
                .build();
    }
}
