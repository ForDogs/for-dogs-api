package com.fordogs.user.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "비밀번호 초기화 확인 요청")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserPasswordResetVerifyRequest {

    @Schema(description = "인증 코드", requiredMode = Schema.RequiredMode.REQUIRED, example = "3H7U")
    @NotBlank(message = "인증 코드를 입력해주세요.")
    private String authCode;
}
