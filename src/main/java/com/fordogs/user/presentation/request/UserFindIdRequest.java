package com.fordogs.user.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Schema(description = "아이디 찾기 요청")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserFindIdRequest {

    @Schema(description = "회원명", requiredMode = Schema.RequiredMode.REQUIRED, example = "홍길동")
    @NotBlank(message = "회원명을 입력해주세요.")
    private String userName;

    @Schema(description = "회원 생년월일", requiredMode = Schema.RequiredMode.REQUIRED, example = "1990-01-01")
    @NotNull(message = "생년월일을 입력해주세요.")
    private LocalDate birthDate;

    @Schema(description = "회원 이메일", requiredMode = Schema.RequiredMode.REQUIRED, example = "hong@example.com")
    @Email(message = "이메일 형식으로 입력해주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String userEmail;
}
