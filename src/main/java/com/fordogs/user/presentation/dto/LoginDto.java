package com.fordogs.user.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fordogs.core.domian.entity.UserEntity;
import com.fordogs.core.domian.enums.Role;
import com.fordogs.core.domian.vo.AccessToken;
import com.fordogs.core.domian.vo.RefreshToken;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class LoginDto {

    @Schema(description = "로그인 요청")
    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Request {

        @Schema(description = "회원 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "hong1234")
        @NotBlank(message = "회원 ID를 입력해주세요.")
        private String userId;

        @Schema(description = "회원 비밀번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "P@ssw0rd13!")
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String userPassword;

        @Schema(description = "회원 역할", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "회원 역할을 입력해주세요.")
        private Role userRole;
    }

    @Schema(description = "로그인 응답")
    @Getter
    @Setter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {

        @Schema(description = "로그인 회원 ID")
        private String userId;

        @Schema(description = "Refresh Token")
        private String refreshToken;

        @Schema(description = "Access Token")
        private String accessToken;

        public static LoginDto.Response toResponse(UserEntity userEntity, RefreshToken refreshToken, AccessToken accessToken) {
            return LoginDto.Response.builder()
                    .userId(userEntity.getUserIdentifier().getValue())
                    .refreshToken(refreshToken.getValue())
                    .accessToken(accessToken.getValue())
                    .build();
        }
    }
}
