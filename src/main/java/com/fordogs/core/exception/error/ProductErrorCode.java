package com.fordogs.core.exception.error;

import com.fordogs.core.exception.DomainException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductErrorCode implements BaseErrorCode<DomainException> {

    PRODUCT_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "같은 이름의 상품이 이미 존재합니다."),
    PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "아이디와 일치하는 상품을 찾을 수 없습니다."),

    INVALID_DESCRIPTION_LENGTH(HttpStatus.BAD_REQUEST, "설명은 0자에서 100자 사이어야 합니다."),
    PROFANITY_IN_DESCRIPTION(HttpStatus.BAD_REQUEST, "설명에 욕설이 포함되어 있습니다."),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "가격은 음수가 될 수 없습니다."),
    INVALID_CATEGORY_NAME(HttpStatus.BAD_REQUEST, "유효하지 않은 카테고리 이름입니다."),

    USER_NOT_FOUND(UserManagementErrorCode.USER_NOT_FOUND.getHttpStatus(), UserManagementErrorCode.USER_NOT_FOUND.getMessage());

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, message);
    }
}
