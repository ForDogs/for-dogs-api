package com.fordogs.security.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtException extends AuthenticationException {

    public JwtException(String message) {
        super(message);
    }

    public JwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
