package com.fordogs.user.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "비밀번호 변경 요청")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserPasswordChangeRequest {

    @Schema(description = "현재 비밀번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "P@ssw0rd97*")
    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String currentPassword;

    @Schema(description = "새로운 비밀번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "P@ssw0rd13!")
    @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
    private String newPassword;
}
