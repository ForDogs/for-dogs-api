package com.fordogs.core.exception.error;

import com.fordogs.core.exception.DomainException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ValueErrorCode implements BaseErrorCode<DomainException> {

    INVALID_EMAIL_ID(HttpStatus.BAD_REQUEST, "이메일 ID는 영문과 숫자만 사용할 수 있습니다."),
    INVALID_EMAIL_DOMAIN(HttpStatus.BAD_REQUEST, "유효한 이메일 도메인 형식만 사용할 수 있습니다."),
    INVALID_ID_FORMAT(HttpStatus.BAD_REQUEST, "ID는 영문과 숫자만 사용할 수 있습니다."),
    INVALID_NAME_FORMAT(HttpStatus.BAD_REQUEST, "이름은 한글과 영문만 사용할 수 있습니다."),
    INVALID_PASSWORD_LENGTH(HttpStatus.BAD_REQUEST, "비밀번호는 10자리 이상 16자리 이하로 입력해주세요."),
    INVALID_PASSWORD_PATTERN(HttpStatus.BAD_REQUEST, "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다."),
    INVALID_PASSWORD_CONSECUTIVE_CHARS(HttpStatus.BAD_REQUEST, "비밀번호에 연속된 문자열(EX: 123, abc 등)은 사용할 수 없습니다."),
    INVALID_DESCRIPTION_LENGTH(HttpStatus.BAD_REQUEST, "설명은 0자에서 100자 사이어야 합니다."),
    INVALID_DESCRIPTION_PROFANITY(HttpStatus.BAD_REQUEST, "설명에 부적절한 내용이 포함되어 있습니다."),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "가격은 음수가 될 수 없습니다."),

    INVALID_CATEGORY_NAME(HttpStatus.BAD_REQUEST, "유효하지 않은 카테고리 이름입니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, message);
    }
}
