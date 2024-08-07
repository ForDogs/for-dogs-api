package com.fordogs.cart.error;

import com.fordogs.core.exception.DomainException;
import com.fordogs.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CartErrorCode implements BaseErrorCode<DomainException> {

    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "장바구니에 담으려는 상품의 재고가 충분하지 않습니다."),
    CART_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "아이디와 일치하는 장바구니 상품을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, message);
    }
}
