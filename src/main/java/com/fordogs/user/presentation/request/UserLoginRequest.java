package com.fordogs.user.presentation.request;

import com.fordogs.core.domian.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "로그인 요청")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLoginRequest {

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
