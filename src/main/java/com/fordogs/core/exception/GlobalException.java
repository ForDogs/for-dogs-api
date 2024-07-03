package com.fordogs.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends RuntimeException {

    private HttpStatus httpStatus;

    public GlobalException(String message) {
        super(message);
    }

    public GlobalException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
