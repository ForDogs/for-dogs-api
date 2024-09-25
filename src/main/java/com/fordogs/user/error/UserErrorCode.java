package com.fordogs.user.error;

import com.fordogs.core.exception.DomainException;
import com.fordogs.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements BaseErrorCode<DomainException> {

    DUPLICATE_USER_ID(HttpStatus.CONFLICT, "이미 사용 중인 회원 ID입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 회원 이메일입니다."),
    LOGIN_PASSWORD_FAILED(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    USER_ROLE_MISMATCH(HttpStatus.FORBIDDEN, "회원 역할이 일치하지 않습니다."),
    USER_PROVIDER_MISMATCH(HttpStatus.FORBIDDEN, "소셜 로그인 공급자가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "일치하는 회원을 찾을 수 없습니다."),
    USER_DISABLED(HttpStatus.FORBIDDEN, "탈퇴한 회원은 이용할 수 없습니다."),
    AUTH_CODE_EXPIRED(HttpStatus.UNAUTHORIZED, "인증 코드가 만료되었습니다."),
    SOCIAL_LOGIN_REQUIRED(HttpStatus.FORBIDDEN, "소셜 로그인을 통해서만 로그인할 수 있습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, message);
    }
}
