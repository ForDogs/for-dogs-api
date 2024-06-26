package com.fordogs.core.exception.error;

import com.fordogs.core.exception.DomainException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements BaseErrorCode<DomainException> {

    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "인증되지 않은 접근으로 요청을 처리할 수 없습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 요청에 대한 권한이 없어 정상적으로 처리할 수 없습니다."),
    MISSING_REQUEST_COOKIE(HttpStatus.BAD_REQUEST, "필수 요청 쿠키가 누락되었습니다."),
    MISSING_REQUEST_HEADER(HttpStatus.BAD_REQUEST, "필수 요청 헤더가 누락되었습니다."),
    MAX_UPLOAD_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "최대 허용 파일 크기는 {0}, 요청 전체 크기는 {1}로 제한되어 있습니다."),
    INVALID_REQUEST_PARAMETERS(HttpStatus.BAD_REQUEST, "해당 요청 값을 다시 확인해주세요:"),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "가격은 음수가 될 수 없습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, message);
    }
}
