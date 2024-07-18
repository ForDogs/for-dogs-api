package com.fordogs.user.error;

import com.fordogs.core.exception.DomainException;
import com.fordogs.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PasswordResetErrorCode implements BaseErrorCode<DomainException> {

    USER_NOT_FOUND(UserErrorCode.USER_NOT_FOUND.getHttpStatus(), UserErrorCode.USER_NOT_FOUND.getMessage()),
    AUTH_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "인증 코드를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, message);
    }
}
