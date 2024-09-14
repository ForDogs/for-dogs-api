package com.fordogs.user.error;

import com.fordogs.core.exception.DomainException;
import com.fordogs.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserValidationErrorCode implements BaseErrorCode<DomainException> {

    INVALID_EMAIL_ID(HttpStatus.BAD_REQUEST, "이메일 ID는 영문과 숫자만 사용할 수 있습니다."),
    INVALID_EMAIL_DOMAIN(HttpStatus.BAD_REQUEST, "유효한 이메일 도메인 형식만 사용할 수 있습니다."),
    INVALID_EMAIL_ADDRESS(HttpStatus.BAD_REQUEST, "이메일 주소가 잘못되었습니다."),
    INVALID_ID_FORMAT(HttpStatus.BAD_REQUEST, "ID는 영문과 숫자만 사용할 수 있습니다."),
    INVALID_NAME_FORMAT(HttpStatus.BAD_REQUEST, "이름은 한글 또는 영문 및 다음의 특수문자(-, _, ., /)만 사용할 수 있습니다."),
    INVALID_PASSWORD_LENGTH(HttpStatus.BAD_REQUEST, "비밀번호는 10자리 이상 16자리 이하로 입력해주세요."),
    INVALID_PASSWORD_PATTERN(HttpStatus.BAD_REQUEST, "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다."),
    INVALID_PASSWORD_CONSECUTIVE_CHARS(HttpStatus.BAD_REQUEST, "비밀번호에 연속된 문자열(EX: 123, abc 등)은 사용할 수 없습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, message);
    }
}
