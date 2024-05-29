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
    PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "아이디와 일치하는 상품을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, message);
    }
}
