package com.fordogs.user.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "소셜 로그인 요청")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthCodeLoginRequest {

    @Schema(description = "인증 코드", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "인증 코드를 입력해주세요.")
    private String authCode;
}
