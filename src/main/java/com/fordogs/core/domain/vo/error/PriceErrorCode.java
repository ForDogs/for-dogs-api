package com.fordogs.core.domain.vo.error;

import com.fordogs.core.exception.DomainException;
import com.fordogs.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PriceErrorCode implements BaseErrorCode<DomainException> {

    INVALID_PRICE_NEGATIVE(HttpStatus.BAD_REQUEST, "가격은 음수일 수 없습니다."),
    INVALID_PRICE_EXCEEDS_MAX(HttpStatus.BAD_REQUEST, "가격은 최대값을 초과할 수 없습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, message);
    }
}
