package com.fordogs.user.error;

import com.fordogs.core.exception.DomainException;
import com.fordogs.core.exception.error.BaseErrorCode;
import com.fordogs.core.exception.error.GlobalErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserManagementErrorCode implements BaseErrorCode<DomainException> {

    DUPLICATE_USER_ID(HttpStatus.BAD_REQUEST, "이미 사용 중인 회원 ID입니다."),
    LOGIN_PASSWORD_FAILED(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    USER_ROLE_MISMATCH(HttpStatus.BAD_REQUEST, "회원 역할이 일치하지 않습니다."),

    USER_NOT_FOUND(GlobalErrorCode.USER_DISABLED.getHttpStatus(), GlobalErrorCode.USER_NOT_FOUND.getMessage()),
    USER_DISABLED(GlobalErrorCode.USER_DISABLED.getHttpStatus(), GlobalErrorCode.USER_DISABLED.getMessage());

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, message);
    }
}
