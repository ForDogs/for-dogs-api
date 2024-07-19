package com.fordogs.user.presentation.request;

import com.fordogs.user.domain.entity.mysql.UserEntity;
import com.fordogs.user.domain.enums.Role;
import com.fordogs.user.domain.vo.Email;
import com.fordogs.user.domain.vo.wrapper.Account;
import com.fordogs.user.domain.vo.wrapper.Name;
import com.fordogs.user.domain.vo.wrapper.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Schema(description = "회원 가입 요청")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSignupRequest {

    @Schema(description = "회원 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "hong1234")
    @NotBlank(message = "회원 ID를 입력해주세요.")
    private String userId;

    @Schema(description = "회원명", requiredMode = Schema.RequiredMode.REQUIRED, example = "홍길동")
    @NotBlank(message = "회원명을 입력해주세요.")
    private String userName;

    @Schema(description = "회원 생년월일", requiredMode = Schema.RequiredMode.REQUIRED, example = "1990-01-01")
    @NotNull(message = "생년월일을 입력해주세요.")
    private LocalDate userBirthDate;

    @Schema(description = "회원 이메일 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "honggildong")
    @NotBlank(message = "이메일 ID를 입력해주세요.")
    private String userEmailId;

    @Schema(description = "회원 이메일 도메인", requiredMode = Schema.RequiredMode.REQUIRED, example = "gmail.com")
    @NotBlank(message = "이메일 도메인을 입력해주세요.")
    private String userEmailDomain;

    @Schema(description = "회원 비밀번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "P@ssw0rd13!")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String userPassword;

    @Schema(description = "회원 역할", defaultValue = "BUYER", example = "SELLER")
    private Role userRole;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .account(Account.builder()
                        .value(this.userId)
                        .build())
                .name(Name.builder()
                        .value(this.userName)
                        .build())
                .birthDate(this.userBirthDate)
                .email(Email.builder()
                        .id(this.userEmailId)
                        .domain(this.userEmailDomain)
                        .build())
                .password(Password.builder()
                        .value(this.userPassword)
                        .build())
                .role(this.userRole)
                .build();
    }
}
