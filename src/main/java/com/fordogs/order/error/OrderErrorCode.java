package com.fordogs.order.error;

import com.fordogs.core.exception.DomainException;
import com.fordogs.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode implements BaseErrorCode<DomainException> {

    INVALID_ORDER_STATUS_NAME(HttpStatus.BAD_REQUEST, "유효하지 않은 주문 상태 이름입니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "아이디와 일치하는 주문 내역을 찾을 수 없습니다."),
    ORDER_CANNOT_BE_MODIFIED(HttpStatus.BAD_REQUEST, "주문이 취소된 상태에서는 주문 상태를 변경할 수 없습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, message);
    }
}
