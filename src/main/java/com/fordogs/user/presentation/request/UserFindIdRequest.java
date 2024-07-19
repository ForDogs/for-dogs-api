package com.fordogs.user.presentation.request;

import com.fordogs.user.domain.vo.Email;
import com.fordogs.user.domain.vo.wrapper.Name;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "회원 생년월일", requiredMode = Schema.RequiredMode.REQUIRED, example = "1900-01-01")
    @NotNull(message = "생년월일을 입력해주세요.")
    private LocalDate userBirthDate;

    @Schema(description = "회원 이메일 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "honggildong")
    @NotBlank(message = "이메일 ID를 입력해주세요.")
    private String userEmailId;

    @Schema(description = "회원 이메일 도메인", requiredMode = Schema.RequiredMode.REQUIRED, example = "gmail.com")
    @NotBlank(message = "이메일 도메인을 입력해주세요.")
    private String userEmailDomain;

    public Name toName() {
        return Name.builder()
                .value(userName)
                .build();
    }

    public Email toEmail() {
        return Email.builder()
                .id(userEmailId)
                .domain(userEmailDomain)
                .build();
    }
}
