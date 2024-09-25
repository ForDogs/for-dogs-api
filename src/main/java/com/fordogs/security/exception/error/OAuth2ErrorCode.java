package com.fordogs.security.exception.error;

import com.fordogs.core.exception.error.BaseErrorCode;
import com.fordogs.security.exception.SecurityAuthenticationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OAuth2ErrorCode implements BaseErrorCode<SecurityAuthenticationException> {

    ILLEGAL_REGISTRATION_ID(HttpStatus.BAD_REQUEST, "REGISTRATION ID가 존재하지 않거나 올바르지 않습니다."),
    INVALID_REDIRECT_URL(HttpStatus.BAD_REQUEST, "리디렉션 URI가 존재하지 않거나 형식이 올바르지 않습니다."),
    UNAUTHORIZED_REDIRECT_URL(HttpStatus.FORBIDDEN, "리디렉션 URI의 도메인이 허용되지 않습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public SecurityAuthenticationException toException() {
        return new SecurityAuthenticationException(httpStatus, message);
    }
}
