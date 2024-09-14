package com.fordogs.core.presentation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fordogs.core.util.HttpServletUtil;
import com.fordogs.core.util.TimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessResponse<T> {

    @Schema(example = "true", defaultValue = "true")
    private boolean ok;

    @Schema(example = "/{url}")
    private String path;

    @Schema(example = "2024-05-02T00:11:24")
    private String timeStamp;

    @Schema
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public static <T> SuccessResponse<T> of(T result) {
        return SuccessResponse.<T>builder()
                .ok(true)
                .path(HttpServletUtil.getRequestUrlAndQuery())
                .timeStamp(TimeUtil.formatLocalDateTime(LocalDateTime.now()))
                .result(result)
                .build();
    }
}
