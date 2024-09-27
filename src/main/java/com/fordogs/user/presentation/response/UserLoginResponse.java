package com.fordogs.user.presentation.response;

import com.fordogs.user.domain.entity.UserEntity;
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

    @Schema(description = "로그인 공급자")
    private String provider;

    @Schema(description = "Access Token")
    private TokenInfo accessToken;

    public static UserLoginResponse toResponse(UserEntity userEntity, AccessToken accessToken) {
        return UserLoginResponse.builder()
                .userId(userEntity.getAccount().getValue())
                .provider(userEntity.getProvider().name())
                .accessToken(TokenInfo.toResponse(accessToken.getValue(), accessToken.getMetadata().getExpirationTime()))
                .build();
    }
}
