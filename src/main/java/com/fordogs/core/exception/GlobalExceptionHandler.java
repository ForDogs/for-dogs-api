package com.fordogs.core.exception;

import com.fordogs.core.presentation.ErrorResponse;
import com.fordogs.security.exception.SecurityAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MultipartProperties multipartProperties;

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

    @ExceptionHandler(SecurityAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleSecurityAuthenticationException(SecurityAuthenticationException e) {
        ErrorResponse response = ErrorResponse.of(e);
        logErrorWithException(e);

        return ResponseEntity.status(e.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentValidException(MethodArgumentNotValidException e) {
        String errorMessage = buildErrorMessage(e);
        ErrorResponse response = ErrorResponse.of(e, errorMessage);
        logErrorWithException(e);

        return ResponseEntity.status(e.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        String maxFileSize = multipartProperties.getMaxFileSize().toMegabytes() + "MB";
        String maxRequestSize = multipartProperties.getMaxRequestSize().toMegabytes() + "MB";
        String errorMessage = String.format("최대 허용 파일 크기는 %s이며, 요청 전체 크기는 %s로 제한되어 있습니다.", maxFileSize, maxRequestSize);
        ErrorResponse response = ErrorResponse.of(e, errorMessage);
        logErrorWithException(e);

        return ResponseEntity.status(BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestCookieException(MissingRequestCookieException e) {
        String errorMessage = e.getCookieName() + " 쿠키는 필수 요청 쿠키입니다.";
        ErrorResponse response = ErrorResponse.of(e, errorMessage);
        logErrorWithException(e);

        return ResponseEntity.status(BAD_REQUEST)
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

    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<ErrorResponse> handleS3Exception(S3Exception e) {
        ErrorResponse response = ErrorResponse.of(e);
        logErrorWithException(e);

        return ResponseEntity.status(e.statusCode())
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

    private String buildErrorMessage(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> "[" + fieldError.getField() + ": " + fieldError.getDefaultMessage() + "]")
                .collect(Collectors.joining(", "));
    }

    private void logErrorWithException(Exception exception) {
        log.error("Exception ClassName = " + exception.getClass().getName());
        log.error("Exception Message = " + exception.getMessage());
        for (StackTraceElement element : exception.getStackTrace()) {
            log.error(element.toString());
        }
    }
}
