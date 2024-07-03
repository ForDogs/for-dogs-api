package com.fordogs.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExternalApiException extends RuntimeException {

    private HttpStatus httpStatus;

    public ExternalApiException(String message) {
        super(message);
    }

    public ExternalApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
