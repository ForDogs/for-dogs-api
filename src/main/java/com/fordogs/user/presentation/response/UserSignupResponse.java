package com.fordogs.user.presentation.response;

import com.fordogs.core.domian.entity.UserManagementEntity;
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

    public static UserSignupResponse toResponse(UserManagementEntity userManagementEntity) {
        return UserSignupResponse.builder()
                .userId(userManagementEntity.getAccount().getValue())
                .userName(userManagementEntity.getName().getValue())
                .build();
    }
}
