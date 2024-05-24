package com.fordogs.user.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fordogs.core.domian.entity.UserEntity;
import com.fordogs.core.domian.vo.AccessToken;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class RefreshTokenDto {

    @Schema(description = "액세스 토큰 재발급 응답")
    @Getter
    @Setter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {

        @Schema(description = "회원 ID")
        private String userId;

        @Schema(description = "Access Token")
        private String accessToken;

        public static RefreshTokenDto.Response toResponse(UserEntity userEntity, AccessToken accessToken) {
            return RefreshTokenDto.Response.builder()
                    .userId(userEntity.getAccount().getValue())
                    .accessToken(accessToken.getValue())
                    .build();
        }
    }
}
