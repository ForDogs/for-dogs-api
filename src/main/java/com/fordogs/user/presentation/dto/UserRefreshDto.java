package com.fordogs.user.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fordogs.core.domian.entity.UserManagementEntity;
import com.fordogs.core.domian.vo.wapper.AccessToken;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserRefreshDto {

    @Schema(description = "액세스 토큰 재발급 응답")
    @Getter
    @Setter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {

        @Schema(description = "회원 ID")
        private String userId;

        @Schema(description = "Access Token")
        private TokenDto accessToken;

        public static UserRefreshDto.Response toResponse(UserManagementEntity userManagementEntity, AccessToken accessToken) {
            return UserRefreshDto.Response.builder()
                    .userId(userManagementEntity.getAccount().getValue())
                    .accessToken(TokenDto.builder()
                            .value(accessToken.getValue())
                            .expiration(accessToken.getExpiration())
                            .build())
                    .build();
        }
    }
}
