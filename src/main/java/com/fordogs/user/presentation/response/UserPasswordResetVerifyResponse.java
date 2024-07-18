package com.fordogs.user.presentation.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "비밀번호 초기화 확인 응답")
@Getter
@Setter
@Builder
public class UserPasswordResetVerifyResponse {

    @Schema(description = "임시 비밀번호")
    private String temporaryPassword;
}
