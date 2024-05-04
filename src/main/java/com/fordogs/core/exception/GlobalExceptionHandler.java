package com.fordogs.core.exception;

import com.fordogs.core.presentation.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException e) {
        ErrorResponse response = ErrorResponse.of(e);

        return ResponseEntity.status(e.getHttpStatus())
            .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentValidException(MethodArgumentNotValidException e) {
        ErrorResponse response = ErrorResponse.of(e);

        return ResponseEntity.status(determineHttpStatus(e))
            .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse response = ErrorResponse.of(e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
    }

    private HttpStatus determineHttpStatus(MethodArgumentNotValidException e) {
        ResponseStatus responseStatus = e.getClass().getAnnotation(ResponseStatus.class);

        return responseStatus == null ? HttpStatus.BAD_REQUEST : responseStatus.value();
    }
}
