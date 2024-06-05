package com.fordogs.core.presentation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fordogs.core.exception.error.BaseErrorCode;
import com.fordogs.core.util.HttpServletUtil;
import com.fordogs.core.util.TimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    @Schema(example = "false", defaultValue = "false")
    private boolean ok;

    @Schema(example = "/{url}")
    private String path;

    @Schema(example = "2024-05-30T00:11:24")
    private String timeStamp;

    @Schema
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Error error;

    public static ErrorResponse of(Exception exception) {
        return ErrorResponse.builder()
                .ok(false)
                .path(HttpServletUtil.getRequestUrlAndQuery())
                .timeStamp(TimeUtil.formatLocalDateTime(LocalDateTime.now()))
                .error(Error.builder()
                        .message(exception.getMessage())
                        .stack(convertStackTraceToStringArray(exception, 10))
                        .build())
                .build();
    }

    public static ErrorResponse of(Exception exception, String message) {
        return ErrorResponse.builder()
                .ok(false)
                .path(HttpServletUtil.getRequestUrlAndQuery())
                .timeStamp(TimeUtil.formatLocalDateTime(LocalDateTime.now()))
                .error(Error.builder()
                        .message(message)
                        .stack(convertStackTraceToStringArray(exception, 10))
                        .build())
                .build();
    }

    public static ErrorResponse of(String path, BaseErrorCode baseErrorCode) {
        return ErrorResponse.builder()
                .ok(false)
                .path(path)
                .timeStamp(TimeUtil.formatLocalDateTime(LocalDateTime.now()))
                .error(Error.builder()
                        .message(baseErrorCode.getMessage())
                        .stack(convertStackTraceToStringArray(baseErrorCode.toException(), 10))
                        .build())
                .build();
    }

    private static String[] convertStackTraceToStringArray(Throwable throwable, int depth) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int length = Math.min(stackTrace.length, depth);

        String[] result = new String[length];
        for (int i = 0; i < length; i++) {
            result[i] = formatStackTraceElement(stackTrace[i]);
        }

        return result;
    }

    private static String formatStackTraceElement(StackTraceElement stackTraceElement) {
        String declaringClass = stackTraceElement.getClassName();
        String methodName = stackTraceElement.getMethodName();
        String fileName = stackTraceElement.getFileName();

        return declaringClass + "." + methodName + "(" + fileName + ")";
    }

    @Getter
    @Schema
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Error {

        @Schema(example = "존재하지 않는 값입니다.")
        private final String message;

        @Schema(type = "array", example = "[\"org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java)\"]")
        private final String[] stack;

        @Builder
        private Error(String message, String[] stack) {
            this.message = message;
            this.stack = stack;
        }
    }
}
