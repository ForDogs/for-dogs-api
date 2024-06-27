package com.fordogs.user.error;

import com.fordogs.core.exception.DomainException;
import com.fordogs.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RefreshTokenErrorCode implements BaseErrorCode<DomainException> {

    TOKEN_ISSUER_MISMATCH(HttpStatus.BAD_REQUEST, "액세스 토큰과 리프레쉬 토큰의 발급자가 일치하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "존재하지 않는 리프레쉬 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "리프레쉬 토큰의 유효 기간이 만료되었습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, message);
    }
}
