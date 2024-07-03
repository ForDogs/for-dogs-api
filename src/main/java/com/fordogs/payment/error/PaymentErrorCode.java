package com.fordogs.payment.error;

import com.fordogs.core.exception.DomainException;
import com.fordogs.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorCode implements BaseErrorCode<DomainException> {

    PAYMENT_AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, "주문 금액과 결제 금액이 불일치하여 위/변조 시도가 의심되어 결제가 취소되었습니다."),
    VIRTUAL_ACCOUNT_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "가상 계좌 결제는 허용되지 않습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, message);
    }
}
