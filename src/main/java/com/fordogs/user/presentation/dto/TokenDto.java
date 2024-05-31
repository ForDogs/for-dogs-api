package com.fordogs.user.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(description = "토큰 응답")
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenDto {

    @Schema(description = "토큰 값")
    private String value;

    @Schema(description = "토큰 만료 시간")
    private LocalDateTime expiration;
}
