package com.fordogs.user.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fordogs.core.domian.entity.UserEntity;
import com.fordogs.core.domian.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class JoinDto {

    @Schema(description = "회원 가입 요청")
    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Request {

        @Schema(description = "회원 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "hong1234")
        @NotBlank(message = "회원 ID를 입력해주세요.")
        private String userId;

        @Schema(description = "회원명", requiredMode = Schema.RequiredMode.REQUIRED, example = "홍길동")
        @NotBlank(message = "회원명을 입력해주세요.")
        private String userName;

        @Schema(description = "회원 이메일 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "honggildong")
        @NotBlank(message = "이메일 ID를 입력해주세요.")
        private String emailId;

        @Schema(description = "회원 이메일 도메인", requiredMode = Schema.RequiredMode.REQUIRED, example = "gmail.com")
        @NotBlank(message = "이메일 도메인을 입력해주세요.")
        private String emailDomain;

        @Schema(description = "회원 비밀번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "P@ssw0rd123!")
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;

        @Schema(description = "회원 역할", defaultValue = "BUYER", allowableValues = {"SELLER", "BUYER"})
        private Role role;

        public UserEntity toEntity() {
            return UserEntity.JoinBuilder()
                    .userIdentifier(this.userId)
                    .name(this.userName)
                    .emailId(this.emailId)
                    .emailDomain(this.emailDomain)
                    .password(this.password)
                    .role(this.role)
                    .build();
        }
    }

    @Schema(description = "회원 가입 응답")
    @Getter
    @Setter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {

        @Schema(description = "생성된 회원 ID")
        private String userId;

        @Schema(description = "생성된 회원명")
        private String userName;

        public static Response toResponse(UserEntity userEntity) {
            return Response.builder()
                    .userId(userEntity.getUserIdentifier().getValue())
                    .userName(userEntity.getName().getValue())
                    .build();
        }
    }
}
