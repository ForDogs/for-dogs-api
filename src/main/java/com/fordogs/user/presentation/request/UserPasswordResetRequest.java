package com.fordogs.user.presentation.request;

import com.fordogs.user.domain.vo.Email;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "비밀번호 초기화 요청")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserPasswordResetRequest {

    @Schema(description = "회원 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "hong1234")
    @NotBlank(message = "회원 ID를 입력해주세요.")
    private String userId;

    @Schema(description = "회원 이메일 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "honggildong")
    @NotBlank(message = "이메일 ID를 입력해주세요.")
    private String userEmailId;

    @Schema(description = "회원 이메일 도메인", requiredMode = Schema.RequiredMode.REQUIRED, example = "gmail.com")
    @NotBlank(message = "이메일 도메인을 입력해주세요.")
    private String userEmailDomain;

    public Email toEmail() {
        return Email.builder()
                .id(userEmailId)
                .domain(userEmailDomain)
                .build();
    }
}
