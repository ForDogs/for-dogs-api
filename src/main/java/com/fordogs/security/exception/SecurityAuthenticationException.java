package com.fordogs.security.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@Getter
public class SecurityAuthenticationException extends AuthenticationException {

    private HttpStatus httpStatus;

    public SecurityAuthenticationException(String message) {
        super(message);
    }

    public SecurityAuthenticationException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public SecurityAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static String getExceptionName() {
        return SecurityAuthenticationException.class.getSimpleName();
    }
}
