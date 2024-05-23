package com.fordogs.security.exception.error;

import com.fordogs.core.exception.error.BaseErrorCode;
import com.fordogs.security.exception.SecurityAuthenticationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SecurityErrorCode implements BaseErrorCode<SecurityAuthenticationException> {

    INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "토큰 서명이 올바르지 않습니다."),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 구조가 올바르지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 유효 기간이 만료되었습니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 토큰 형식입니다."),
    INVALID_CLAIMS(HttpStatus.UNAUTHORIZED, "토큰 클레임이 비어있거나 유효하지 않습니다."),

    USER_DISABLED(HttpStatus.UNAUTHORIZED, "탈퇴한 회원은 이용할 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "사용자 정보를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public SecurityAuthenticationException toException() {
        return new SecurityAuthenticationException(httpStatus, message);
    }
}
