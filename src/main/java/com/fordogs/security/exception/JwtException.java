package com.fordogs.security.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtException extends AuthenticationException {

    private HttpStatus httpStatus;

    public JwtException(String message) {
        super(message);
    }

    public JwtException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public JwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
