package com.fordogs.user.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fordogs.core.domian.entity.UserManagementEntity;
import com.fordogs.core.domian.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserDetailDto {

    @Schema(description = "회원 정보 조회 응답")
    @Getter
    @Setter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {

        @Schema(description = "회원 ID")
        private String userId;

        @Schema(description = "회원명")
        private String userName;

        @Schema(description = "회원 이메일")
        private String userEmail;

        @Schema(description = "회원 역할")
        private Role userRole;

        public static UserDetailDto.Response toResponse(UserManagementEntity userManagementEntity) {
            return Response.builder()
                    .userId(userManagementEntity.getAccount().getValue())
                    .userName(userManagementEntity.getName().getValue())
                    .userEmail(userManagementEntity.getEmail().formattedEmail())
                    .userRole(userManagementEntity.getRole())
                    .build();
        }
    }
}
