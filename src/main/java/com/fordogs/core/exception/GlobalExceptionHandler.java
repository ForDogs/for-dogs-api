package com.fordogs.core.exception;

import com.fordogs.core.exception.error.GlobalErrorCode;
import com.fordogs.core.presentation.ErrorResponse;
import com.fordogs.security.exception.SecurityAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.text.MessageFormat;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String MB = "MB";

    private final MultipartProperties multipartProperties;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse response = ErrorResponse.of(e);
        logErrorWithException(e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
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
        ErrorResponse response = ErrorResponse.of(e, buildErrorMessage(e));
        logErrorWithException(e);

        return ResponseEntity.status(GlobalErrorCode.INVALID_REQUEST_PARAMETERS.getHttpStatus())
                .body(response);
    }

    private String buildErrorMessage(MethodArgumentNotValidException e) {
        String fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getField)
                .collect(Collectors.joining(", "));

        return GlobalErrorCode.INVALID_REQUEST_PARAMETERS.getMessage() + " " + fieldErrors;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        ErrorResponse response = ErrorResponse.of(e, getMaxUploadSizeErrorMessage());
        logErrorWithException(e);

        return ResponseEntity.status(GlobalErrorCode.MAX_UPLOAD_SIZE_EXCEEDED.getHttpStatus())
                .body(response);
    }

    private String getMaxUploadSizeErrorMessage() {
        String maxFileSize = multipartProperties.getMaxFileSize().toMegabytes() + MB;
        String maxRequestSize = multipartProperties.getMaxRequestSize().toMegabytes() + MB;

        return MessageFormat.format(GlobalErrorCode.MAX_UPLOAD_SIZE_EXCEEDED.getMessage(), maxFileSize, maxRequestSize);
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestCookieException(MissingRequestCookieException e) {
        String errorMessage = e.getCookieName() + " " + GlobalErrorCode.MISSING_REQUEST_COOKIE.getMessage();
        ErrorResponse response = ErrorResponse.of(e, errorMessage);
        logErrorWithException(e);

        return ResponseEntity.status(GlobalErrorCode.MISSING_REQUEST_COOKIE.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        String errorMessage = e.getHeaderName() + " " + GlobalErrorCode.MISSING_REQUEST_HEADER.getMessage();
        ErrorResponse response = ErrorResponse.of(e, errorMessage);
        logErrorWithException(e);

        return ResponseEntity.status(GlobalErrorCode.MISSING_REQUEST_HEADER.getHttpStatus())
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
        ErrorResponse response = ErrorResponse.of(e, GlobalErrorCode.ACCESS_DENIED.getMessage());
        logErrorWithException(e);

        return ResponseEntity.status(GlobalErrorCode.ACCESS_DENIED.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AuthenticationException e) {
        ErrorResponse response = ErrorResponse.of(e, GlobalErrorCode.UNAUTHORIZED_ACCESS.getMessage());
        logErrorWithException(e);

        return ResponseEntity.status(GlobalErrorCode.UNAUTHORIZED_ACCESS.getHttpStatus())
                .body(response);
    }

    private void logErrorWithException(Exception exception) {
        log.error("Exception ClassName: {}", exception.getClass().getName());
        log.error("Exception Message: {}", exception.getMessage());
        log.error("Stack Trace:");
        for (StackTraceElement element : exception.getStackTrace()) {
            log.error("  {}", element.toString());
        }
    }
}
