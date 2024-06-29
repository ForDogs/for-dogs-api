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

    INVALID_DESCRIPTION_LENGTH(HttpStatus.BAD_REQUEST, "설명은 0자에서 100자 사이어야 합니다."),
    INVALID_DESCRIPTION_PROFANITY(HttpStatus.BAD_REQUEST, "설명에 부적절한 내용이 포함되어 있습니다."),
    INVALID_CATEGORY_NAME(HttpStatus.BAD_REQUEST, "유효하지 않은 카테고리 이름입니다."),
    INVALID_QUANTITY_DECREASE(HttpStatus.BAD_REQUEST, "차감할 수량은 0보다 큰 값이어야 합니다."),
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "상품 재고가 부족합니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, message);
    }
}
