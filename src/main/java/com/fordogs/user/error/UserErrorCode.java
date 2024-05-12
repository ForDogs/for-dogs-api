package com.fordogs.user.error;

import com.fordogs.core.exception.DomainException;
import com.fordogs.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements BaseErrorCode<DomainException> {

    DUPLICATE_USER_ID(HttpStatus.BAD_REQUEST, "이미 사용 중인 회원 ID입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "아이디와 일치하는 회원을 찾을 수 없습니다."),
    LOGIN_PASSWORD_FAILED(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    USER_DISABLED(HttpStatus.BAD_REQUEST, "비활성화된 회원입니다. 로그인이 불가능합니다."),
    ALREADY_DISABLED(HttpStatus.BAD_REQUEST, "이미 비활성화된 회원입니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, message);
    }
}
