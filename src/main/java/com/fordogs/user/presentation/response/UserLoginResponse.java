package com.fordogs.user.presentation.response;

import com.fordogs.user.domain.entity.mysql.UserManagementEntity;
import com.fordogs.user.domain.vo.wrapper.AccessToken;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "로그인 응답")
@Getter
@Setter
@Builder
public class UserLoginResponse {

    @Schema(description = "로그인 회원 ID")
    private String userId;

    @Schema(description = "Access Token")
    private TokenInfo accessToken;

    public static UserLoginResponse toResponse(UserManagementEntity userManagementEntity, AccessToken accessToken) {
        return UserLoginResponse.builder()
                .userId(userManagementEntity.getAccount().getValue())
                .accessToken(TokenInfo.builder()
                        .value(accessToken.getValue())
                        .expiration(accessToken.getExpirationTime())
                        .build())
                .build();
    }
}
