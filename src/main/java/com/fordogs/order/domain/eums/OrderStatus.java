package com.fordogs.order.domain.eums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fordogs.core.exception.error.GlobalErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    AWAITING_PAYMENT("결제 대기 중"),
    PAID("결제 완료"),
    PAYMENT_FAILED("결제 오류"),
    CONFIRMED("구매 확인"),
    AWAITING_SHIPMENT("배송 대기 중"),
    SHIPPED("배송 중"),
    DELIVERED("배송 완료"),
    CANCELLED("주문 취소");

    private final String description;

    @JsonCreator
    public static OrderStatus fromValue(String value) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.name().equalsIgnoreCase(value)) {
                return orderStatus;
            }
        }

        throw GlobalErrorCode.badRequestException("유효하지 않은 주문 상태 이름입니다.");
    }
}
