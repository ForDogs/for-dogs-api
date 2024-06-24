package com.fordogs.user.presentation.response;

import com.fordogs.core.util.TimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(description = "토큰 정보")
@Getter
@Setter
@Builder
public class TokenInfo {

    @Schema(description = "토큰 값")
    private String value;

    @Schema(description = "토큰 만료 시간")
    private LocalDateTime expirationTime;

    public static TokenInfo toResponse(String token, Long expirationTimeInSeconds) {
        return TokenInfo.builder()
                .value(token)
                .expirationTime(TimeUtil.toLocalDateTime(expirationTimeInSeconds))
                .build();
    }
}
