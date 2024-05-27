package com.fordogs.core.exception.error;

import com.fordogs.core.exception.DomainException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserRefreshTokenServiceErrorCode implements BaseErrorCode<DomainException> {

    TOKEN_VALIDITY_REMAINING(HttpStatus.BAD_REQUEST, "액세스 토큰의 유효 기간이 아직 남아 있습니다."),
    MISSING_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "요청된 쿠키에 'REFRESH_TOKEN' 값이 없습니다."),
    TOKEN_ISSUER_MISMATCH(HttpStatus.BAD_REQUEST, "액세스 토큰과 리프레쉬 토큰의 발급자가 일치하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "존재하지 않는 리프레쉬 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "리프레쉬 토큰의 유효 기간이 만료되었습니다. 다시 로그인해 주세요."),

    USER_DISABLED(UserManagementServiceErrorCode.USER_DISABLED.getHttpStatus(), UserManagementServiceErrorCode.USER_DISABLED.getMessage());

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, message);
    }
}
