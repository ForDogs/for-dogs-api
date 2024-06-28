package com.fordogs.order.presentation.request;

import com.fordogs.core.domain.vo.wapper.Price;
import com.fordogs.order.domain.entity.OrderEntity;
import com.fordogs.user.domain.entity.mysql.UserManagementEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "주문 등록 요청")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderRegisterRequest {

    @Schema(description = "주문 총 금액", requiredMode = Schema.RequiredMode.REQUIRED, example = "50.99")
    @NotNull(message = "주문 총 금액을 입력해주세요.")
    private BigDecimal orderTotalPrice;

    @Schema(description = "주문 아이템 정보")
    private List<OrderItemRequest> orderItems;

    public OrderEntity toEntity(UserManagementEntity userManagementEntity) {
        return OrderEntity.builder()
                .buyer(userManagementEntity)
                .totalPrice(Price.builder()
                        .value(this.orderTotalPrice)
                        .build())
                .build();
    }
}
