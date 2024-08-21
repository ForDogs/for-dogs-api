package com.fordogs.core.domain.vo.error;

import com.fordogs.core.exception.DomainException;
import com.fordogs.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum QuantityErrorCode implements BaseErrorCode<DomainException> {

    INVALID_QUANTITY_NEGATIVE(HttpStatus.BAD_REQUEST, "수량은 0 이상이어야 합니다."),
    INVALID_QUANTITY_EXCEEDS_MAX(HttpStatus.BAD_REQUEST, "수량은 최대 999개까지 등록 가능합니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, message);
    }
}
