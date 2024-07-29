package com.fordogs.core.exception.error;

import com.fordogs.core.exception.DomainException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalDomainErrorCode implements BaseErrorCode<DomainException> {

    INVALID_PRICE_NEGATIVE(HttpStatus.BAD_REQUEST, "가격은 음수일 수 없습니다."),
    INVALID_PRICE_EXCEEDS_MAX(HttpStatus.BAD_REQUEST, "가격은 최대값을 초과할 수 없습니다."),
    INVALID_QUANTITY_NEGATIVE(HttpStatus.BAD_REQUEST, "수량은 0 이상이어야 합니다."),
    INVALID_QUANTITY_EXCEEDS_MAX(HttpStatus.BAD_REQUEST, "수량은 최대 999개까지 등록 가능합니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, message);
    }
}
