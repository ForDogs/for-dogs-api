package com.fordogs.user.presentation.response;

import com.fordogs.user.domain.entity.UserManagementEntity;
import com.fordogs.user.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "회원 정보 조회 응답")
@Getter
@Setter
@Builder
public class UserDetailsResponse {

    @Schema(description = "회원 ID")
    private String userId;

    @Schema(description = "회원명")
    private String userName;

    @Schema(description = "회원 이메일")
    private String userEmail;

    @Schema(description = "회원 역할")
    private Role userRole;

    public static UserDetailsResponse toResponse(UserManagementEntity userManagementEntity) {
        return UserDetailsResponse.builder()
                .userId(userManagementEntity.getAccount().getValue())
                .userName(userManagementEntity.getName().getValue())
                .userEmail(userManagementEntity.getEmail().formattedEmail())
                .userRole(userManagementEntity.getRole())
                .build();
    }
}
