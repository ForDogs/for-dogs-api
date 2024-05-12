package com.fordogs.security.exception.error;

import com.fordogs.core.exception.error.BaseErrorCode;
import com.fordogs.security.exception.JwtException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtErrorCode implements BaseErrorCode<JwtException> {

    TOKEN_VALIDITY_REMAINING(HttpStatus.BAD_REQUEST, "AccessToken의 유효기간이 아직 남아있습니다."),
    MISSING_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "REFRESH_TOKEN이 요청된 쿠키에 존재하지 않습니다."),
    TOKEN_ISSUER_MISMATCH(HttpStatus.BAD_REQUEST, "AccessToken과 RefreshToken의 발급자가 일치하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "쿠키에 저장된 RefreshToken 값이 유효하지 않습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "RefreshToken의 유효기간이 만료되었습니다. 다시 로그인해주세요."),
    INVALID_ACCESS_TOKEN_FORMAT(HttpStatus.INTERNAL_SERVER_ERROR, "AccessToken이 JWT 토큰 형식이 아닙니다."),
    INVALID_REFRESH_TOKEN_FORMAT(HttpStatus.INTERNAL_SERVER_ERROR, "RefreshToken이 JWT 토큰 형식이 아닙니다."),
    NO_SECRET_KEY(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 발행을 위한 SecretKey 값이 존재하지 않습니다."),
    NO_USER_DATA_FOR_ACCESS_TOKEN(HttpStatus.NOT_FOUND, "AccessToken 발행을 위한 회원 데이터가 존재하지 않습니다."),
    NO_USER_DATA_FOR_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "RefreshToken 발행을 위한 회원 데이터가 존재하지 않습니다."),
    INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "토큰의 서명이 올바르지 않습니다."),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰의 구조가 올바르지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰의 유효 기한이 만료되었습니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 토큰 형식입니다."),
    INVALID_CLAIMS(HttpStatus.UNAUTHORIZED, "토큰의 클레임이 비어있거나 유효하지 않습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public JwtException toException() {
        return new JwtException(httpStatus, message);
    }
}
