package com.fordogs.core.exception.error;

import com.fordogs.core.exception.GlobalException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements BaseErrorCode<GlobalException> {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "인증되지 않은 접근으로 요청을 처리할 수 없습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 요청에 대한 권한이 없어 정상적으로 처리할 수 없습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public GlobalException toException() {
        return new GlobalException(httpStatus, message);
    }

    public static GlobalException internalServerException(String message) {
        return new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static GlobalException badRequestException(String message) {
        return new GlobalException(HttpStatus.BAD_REQUEST, message);
    }
}
