package com.fordogs.security.exception.error;

import com.fordogs.security.exception.JwtException;
import com.fordogs.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtErrorCode implements BaseErrorCode<JwtException> {

    TOKEN_VALIDITY_REMAINING(HttpStatus.BAD_REQUEST, "AccessToken의 유효 기간이 아직 남아 있습니다."),
    MISSING_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "요청된 쿠키에 'REFRESH_TOKEN'이 없습니다."),
    TOKEN_ISSUER_MISMATCH(HttpStatus.BAD_REQUEST, "AccessToken과 RefreshToken의 발급자가 일치하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "쿠키에 저장된 RefreshToken이 유효하지 않습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "RefreshToken의 유효 기간이 만료되었습니다. 다시 로그인해 주세요."),

    INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "토큰 서명이 올바르지 않습니다."),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 구조가 올바르지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 유효 기간이 만료되었습니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 토큰 형식입니다."),
    INVALID_CLAIMS(HttpStatus.UNAUTHORIZED, "토큰 클레임이 비어있거나 유효하지 않습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public JwtException toException() {
        return new JwtException(httpStatus, message);
    }
}
