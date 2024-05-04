package com.fordogs.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DomainException extends RuntimeException {

    private HttpStatus httpStatus;

    public DomainException(String message) {
        super(message);
    }

    public DomainException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
