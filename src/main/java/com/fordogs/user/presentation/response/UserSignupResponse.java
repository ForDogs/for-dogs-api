package com.fordogs.user.presentation.response;

import com.fordogs.user.domain.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "회원 가입 응답")
@Getter
@Setter
@Builder
public class UserSignupResponse {

    @Schema(description = "생성된 회원 ID")
    private String userId;

    @Schema(description = "생성된 회원명")
    private String userName;

    public static UserSignupResponse toResponse(UserEntity userEntity) {
        return UserSignupResponse.builder()
                .userId(userEntity.getAccount().getValue())
                .userName(userEntity.getName().getValue())
                .build();
    }
}
