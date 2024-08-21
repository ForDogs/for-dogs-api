package com.fordogs.product.error;

import com.fordogs.core.exception.DomainException;
import com.fordogs.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductErrorCode implements BaseErrorCode<DomainException> {

    PRODUCT_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "같은 이름의 상품이 이미 존재합니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "아이디와 일치하는 상품을 찾을 수 없습니다."),
    PRODUCT_DISABLED(HttpStatus.BAD_REQUEST, "해당 상품은 이미 비활성화된 상품입니다."),
    PRODUCT_QUANTITY_INVALID(HttpStatus.BAD_REQUEST, "상품 수량은 1개부터 등록 가능합니다."),
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "상품 재고가 부족합니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, message);
    }
}
