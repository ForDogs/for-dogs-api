package com.fordogs.core.exception.error;

import com.fordogs.core.exception.DomainException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements BaseErrorCode<DomainException> {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "아이디와 일치하는 회원을 찾을 수 없습니다."),
    USER_DISABLED(HttpStatus.UNAUTHORIZED, "탈퇴한 회원은 이용할 수 없습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, message);
    }
}
