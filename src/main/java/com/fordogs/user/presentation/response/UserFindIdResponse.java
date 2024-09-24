package com.fordogs.user.presentation.response;

import com.fordogs.user.domain.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "아이디 찾기 응답")
@Getter
@Setter
@Builder
public class UserFindIdResponse {

    @Schema(description = "조회된 회원 ID")
    private String userId;

    public static UserFindIdResponse toResponse(UserEntity userEntity) {
        return UserFindIdResponse.builder()
                .userId(userEntity.getAccount().getValue())
                .build();
    }
}
