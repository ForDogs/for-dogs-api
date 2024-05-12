package com.fordogs.core.exception;

import com.fordogs.core.presentation.ErrorResponse;
import com.fordogs.security.exception.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final HttpStatus INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;
    private static final HttpStatus UNAUTHORIZED = HttpStatus.UNAUTHORIZED;
    private static final HttpStatus FORBIDDEN = HttpStatus.FORBIDDEN;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse response = ErrorResponse.of(e);
        logErrorWithException(e);

        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException e) {
        ErrorResponse response = ErrorResponse.of(e);
        logErrorWithException(e);

        return ResponseEntity.status(e.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtException e) {
        ErrorResponse response = ErrorResponse.of(e);
        logErrorWithException(e);

        return ResponseEntity.status(e.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentValidException(MethodArgumentNotValidException e) {
        ErrorResponse response = ErrorResponse.of(e);
        logErrorWithException(e);

        return ResponseEntity.status(determineHttpStatus(e))
                .body(response);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        String errorMessage = e.getHeaderName() + " 헤더는 필수 요청 헤더입니다.";
        ErrorResponse response = ErrorResponse.of(e, errorMessage);
        logErrorWithException(e);

        return ResponseEntity.status(BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        String errorMessage = "해당 요청에 대한 권한이 없어 정상적으로 처리할 수 없습니다.";
        ErrorResponse response = ErrorResponse.of(e, errorMessage);
        logErrorWithException(e);

        return ResponseEntity.status(FORBIDDEN)
                .body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AuthenticationException e) {
        String errorMessage = "인증되지 않은 접근으로 요청을 정상적으로 처리할 수 없습니다.";
        ErrorResponse response = ErrorResponse.of(e, errorMessage);
        logErrorWithException(e);

        return ResponseEntity.status(UNAUTHORIZED)
                .body(response);
    }

    private HttpStatus determineHttpStatus(MethodArgumentNotValidException e) {
        ResponseStatus responseStatus = e.getClass().getAnnotation(ResponseStatus.class);

        return responseStatus == null ? BAD_REQUEST : responseStatus.value();
    }

    private void logErrorWithException(Exception exception) {
        log.error("Exception ClassName = " + exception.getClass().getName());
        log.error("Exception Message = " + exception.getMessage());
        for (StackTraceElement element : exception.getStackTrace()) {
            log.error(element.toString());
        }
    }
}
