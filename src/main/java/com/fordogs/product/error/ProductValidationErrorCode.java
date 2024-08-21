package com.fordogs.product.error;

import com.fordogs.core.exception.DomainException;
import com.fordogs.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductValidationErrorCode implements BaseErrorCode<DomainException> {

    INVALID_DESCRIPTION_LENGTH(HttpStatus.BAD_REQUEST, "설명은 0자에서 100자 사이어야 합니다."),
    INVALID_DESCRIPTION_PROFANITY(HttpStatus.BAD_REQUEST, "설명에 부적절한 내용이 포함되어 있습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, message);
    }
}
